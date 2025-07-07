#!/bin/bash

# BioBt Platform 部署脚本
# 用于部署平台到不同环境（开发、测试、生产）

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# 默认参数
ENVIRONMENT="dev"
NAMESPACE="biobt-platform"
DEPLOY_TYPE="kubernetes"
SERVICES="all"
VERSION="1.0.0-SNAPSHOT"
DRY_RUN="false"
WAIT_READY="true"
ROLLBACK="false"
CONFIG_UPDATE="false"

# 帮助信息
show_help() {
    cat << EOF
BioBt Platform 部署脚本

用法: $0 [选项] ENVIRONMENT

环境:
    dev                         开发环境
    test                        测试环境
    prod                        生产环境

选项:
    -t, --type TYPE             部署类型 (kubernetes|docker-compose) [默认: kubernetes]
    -n, --namespace NAMESPACE   Kubernetes命名空间 [默认: biobt-platform]
    -s, --services SERVICES     指定要部署的服务 (all|gateway|user|customer|project|contract|order|payment|report) [默认: all]
    -v, --version VERSION       指定版本号 [默认: 1.0.0-SNAPSHOT]
    --dry-run                   只显示将要执行的操作，不实际部署
    --no-wait                   不等待服务就绪
    --rollback                  回滚到上一个版本
    --config-update             更新配置文件
    -h, --help                  显示帮助信息

示例:
    $0 dev                      # 部署到开发环境
    $0 prod -s user,customer    # 只部署用户服务和客户服务到生产环境
    $0 test --dry-run           # 预览测试环境部署
    $0 prod --rollback          # 回滚生产环境
    $0 dev --config-update      # 更新开发环境配置

EOF
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -t|--type)
            DEPLOY_TYPE="$2"
            shift 2
            ;;
        -n|--namespace)
            NAMESPACE="$2"
            shift 2
            ;;
        -s|--services)
            SERVICES="$2"
            shift 2
            ;;
        -v|--version)
            VERSION="$2"
            shift 2
            ;;
        --dry-run)
            DRY_RUN="true"
            shift
            ;;
        --no-wait)
            WAIT_READY="false"
            shift
            ;;
        --rollback)
            ROLLBACK="true"
            shift
            ;;
        --config-update)
            CONFIG_UPDATE="true"
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        dev|test|prod)
            ENVIRONMENT="$1"
            shift
            ;;
        *)
            log_error "未知参数: $1"
            show_help
            exit 1
            ;;
    esac
done

# 验证环境
check_environment() {
    log_info "检查部署环境..."
    
    case $DEPLOY_TYPE in
        kubernetes)
            # 检查kubectl
            if ! command -v kubectl &> /dev/null; then
                log_error "kubectl未安装或不在PATH中"
                exit 1
            fi
            
            # 检查集群连接
            if ! kubectl cluster-info &> /dev/null; then
                log_error "无法连接到Kubernetes集群"
                exit 1
            fi
            
            # 检查命名空间
            if ! kubectl get namespace "$NAMESPACE" &> /dev/null; then
                log_warning "命名空间 $NAMESPACE 不存在，将自动创建"
            fi
            ;;
        docker-compose)
            # 检查docker-compose
            if ! command -v docker-compose &> /dev/null; then
                log_error "docker-compose未安装或不在PATH中"
                exit 1
            fi
            
            # 检查Docker
            if ! docker info &> /dev/null; then
                log_error "Docker守护进程未运行"
                exit 1
            fi
            ;;
        *)
            log_error "不支持的部署类型: $DEPLOY_TYPE"
            exit 1
            ;;
    esac
    
    log_success "环境检查通过"
}

# 创建Kubernetes命名空间
create_namespace() {
    if [[ "$DEPLOY_TYPE" == "kubernetes" ]]; then
        if ! kubectl get namespace "$NAMESPACE" &> /dev/null; then
            log_info "创建命名空间: $NAMESPACE"
            if [[ "$DRY_RUN" == "false" ]]; then
                kubectl create namespace "$NAMESPACE"
                kubectl label namespace "$NAMESPACE" name="$NAMESPACE"
            else
                log_info "[DRY RUN] kubectl create namespace $NAMESPACE"
            fi
        fi
    fi
}

# 更新配置
update_config() {
    if [[ "$CONFIG_UPDATE" == "true" || "$ROLLBACK" == "false" ]]; then
        log_info "更新配置文件..."
        
        case $DEPLOY_TYPE in
            kubernetes)
                local config_dir="$PROJECT_ROOT/kubernetes"
                if [[ -d "$config_dir" ]]; then
                    # 应用ConfigMap
                    if [[ -f "$config_dir/configmap.yaml" ]]; then
                        log_info "应用ConfigMap配置"
                        if [[ "$DRY_RUN" == "false" ]]; then
                            kubectl apply -f "$config_dir/configmap.yaml" -n "$NAMESPACE"
                        else
                            log_info "[DRY RUN] kubectl apply -f $config_dir/configmap.yaml -n $NAMESPACE"
                        fi
                    fi
                    
                    # 应用Secret
                    if [[ -f "$config_dir/secret.yaml" ]]; then
                        log_info "应用Secret配置"
                        if [[ "$DRY_RUN" == "false" ]]; then
                            kubectl apply -f "$config_dir/secret.yaml" -n "$NAMESPACE"
                        else
                            log_info "[DRY RUN] kubectl apply -f $config_dir/secret.yaml -n $NAMESPACE"
                        fi
                    fi
                fi
                ;;
            docker-compose)
                log_info "Docker Compose模式下配置通过环境变量管理"
                ;;
        esac
    fi
}

# 部署服务到Kubernetes
deploy_to_kubernetes() {
    local service_name="$1"
    local service_config="$PROJECT_ROOT/kubernetes/services/$service_name.yaml"
    
    if [[ ! -f "$service_config" ]]; then
        log_warning "服务配置文件不存在，跳过: $service_config"
        return
    fi
    
    log_info "部署服务到Kubernetes: $service_name"
    
    if [[ "$ROLLBACK" == "true" ]]; then
        # 回滚操作
        log_info "回滚服务: $service_name"
        if [[ "$DRY_RUN" == "false" ]]; then
            kubectl rollout undo deployment/$service_name -n "$NAMESPACE" || true
        else
            log_info "[DRY RUN] kubectl rollout undo deployment/$service_name -n $NAMESPACE"
        fi
    else
        # 正常部署
        # 替换配置文件中的变量
        local temp_config="/tmp/$service_name-$ENVIRONMENT.yaml"
        sed -e "s/{{ENVIRONMENT}}/$ENVIRONMENT/g" \
            -e "s/{{VERSION}}/$VERSION/g" \
            -e "s/{{NAMESPACE}}/$NAMESPACE/g" \
            "$service_config" > "$temp_config"
        
        if [[ "$DRY_RUN" == "false" ]]; then
            kubectl apply -f "$temp_config" -n "$NAMESPACE"
            rm -f "$temp_config"
        else
            log_info "[DRY RUN] kubectl apply -f $temp_config -n $NAMESPACE"
            cat "$temp_config"
            rm -f "$temp_config"
        fi
    fi
    
    # 等待服务就绪
    if [[ "$WAIT_READY" == "true" && "$DRY_RUN" == "false" && "$ROLLBACK" == "false" ]]; then
        log_info "等待服务就绪: $service_name"
        kubectl wait --for=condition=available --timeout=300s deployment/$service_name -n "$NAMESPACE" || {
            log_error "服务部署超时: $service_name"
            kubectl describe deployment/$service_name -n "$NAMESPACE"
            kubectl logs -l app=$service_name -n "$NAMESPACE" --tail=50
            return 1
        }
    fi
}

# 部署服务到Docker Compose
deploy_to_docker_compose() {
    log_info "使用Docker Compose部署服务..."
    
    cd "$PROJECT_ROOT"
    
    # 设置环境变量
    export ENVIRONMENT="$ENVIRONMENT"
    export VERSION="$VERSION"
    
    if [[ "$ROLLBACK" == "true" ]]; then
        log_error "Docker Compose模式不支持回滚操作"
        exit 1
    fi
    
    local compose_file="docker-compose.yml"
    if [[ -f "docker-compose.$ENVIRONMENT.yml" ]]; then
        compose_file="docker-compose.$ENVIRONMENT.yml"
    fi
    
    if [[ "$DRY_RUN" == "false" ]]; then
        # 停止现有服务
        docker-compose -f "$compose_file" down || true
        
        # 拉取最新镜像
        docker-compose -f "$compose_file" pull
        
        # 启动服务
        docker-compose -f "$compose_file" up -d
        
        # 等待服务就绪
        if [[ "$WAIT_READY" == "true" ]]; then
            log_info "等待服务就绪..."
            sleep 30
            docker-compose -f "$compose_file" ps
        fi
    else
        log_info "[DRY RUN] docker-compose -f $compose_file up -d"
        docker-compose -f "$compose_file" config
    fi
}

# 部署服务
deploy_services() {
    log_info "开始部署服务..."
    
    # 定义服务列表
    local all_services=(
        "api-gateway"
        "user-service"
        "tenant-service"
        "customer-service"
        "project-service"
        "contract-service"
        "order-service"
        "payment-service"
        "report-service"
        "notification-service"
        "file-service"
    )
    
    case $DEPLOY_TYPE in
        kubernetes)
            # 解析要部署的服务
            if [[ "$SERVICES" == "all" ]]; then
                for service in "${all_services[@]}"; do
                    deploy_to_kubernetes "$service"
                done
            else
                IFS=',' read -ra SERVICE_ARRAY <<< "$SERVICES"
                for service_name in "${SERVICE_ARRAY[@]}"; do
                    service_name=$(echo "$service_name" | xargs) # 去除空格
                    # 转换服务名称格式
                    case $service_name in
                        gateway) service_name="api-gateway" ;;
                        user) service_name="user-service" ;;
                        tenant) service_name="tenant-service" ;;
                        customer) service_name="customer-service" ;;
                        project) service_name="project-service" ;;
                        contract) service_name="contract-service" ;;
                        order) service_name="order-service" ;;
                        payment) service_name="payment-service" ;;
                        report) service_name="report-service" ;;
                        notification) service_name="notification-service" ;;
                        file) service_name="file-service" ;;
                    esac
                    deploy_to_kubernetes "$service_name"
                done
            fi
            ;;
        docker-compose)
            deploy_to_docker_compose
            ;;
    esac
    
    log_success "服务部署完成"
}

# 验证部署
verify_deployment() {
    if [[ "$DRY_RUN" == "true" ]]; then
        return
    fi
    
    log_info "验证部署状态..."
    
    case $DEPLOY_TYPE in
        kubernetes)
            # 检查Pod状态
            log_info "检查Pod状态:"
            kubectl get pods -n "$NAMESPACE" -o wide
            
            # 检查服务状态
            log_info "检查服务状态:"
            kubectl get services -n "$NAMESPACE"
            
            # 检查Ingress状态
            if kubectl get ingress -n "$NAMESPACE" &> /dev/null; then
                log_info "检查Ingress状态:"
                kubectl get ingress -n "$NAMESPACE"
            fi
            ;;
        docker-compose)
            log_info "检查容器状态:"
            cd "$PROJECT_ROOT"
            docker-compose ps
            ;;
    esac
}

# 生成部署报告
generate_report() {
    log_info "生成部署报告..."
    
    local report_file="$PROJECT_ROOT/deploy-report-$ENVIRONMENT-$(date +%Y%m%d-%H%M%S).txt"
    
    cat > "$report_file" << EOF
BioBt Platform 部署报告
========================

部署时间: $(date)
部署环境: $ENVIRONMENT
部署类型: $DEPLOY_TYPE
命名空间: $NAMESPACE
部署版本: $VERSION
部署服务: $SERVICES
回滚操作: $ROLLBACK
配置更新: $CONFIG_UPDATE

部署结果:
EOF
    
    if [[ "$DRY_RUN" == "false" ]]; then
        case $DEPLOY_TYPE in
            kubernetes)
                echo "" >> "$report_file"
                echo "Pod状态:" >> "$report_file"
                kubectl get pods -n "$NAMESPACE" >> "$report_file" 2>/dev/null || true
                
                echo "" >> "$report_file"
                echo "服务状态:" >> "$report_file"
                kubectl get services -n "$NAMESPACE" >> "$report_file" 2>/dev/null || true
                ;;
            docker-compose)
                echo "" >> "$report_file"
                echo "容器状态:" >> "$report_file"
                cd "$PROJECT_ROOT"
                docker-compose ps >> "$report_file" 2>/dev/null || true
                ;;
        esac
    else
        echo "[DRY RUN] 未实际执行部署" >> "$report_file"
    fi
    
    log_success "部署报告已生成: $report_file"
}

# 主函数
main() {
    log_info "开始部署 BioBt Platform"
    log_info "部署环境: $ENVIRONMENT"
    log_info "部署类型: $DEPLOY_TYPE"
    log_info "部署版本: $VERSION"
    log_info "部署服务: $SERVICES"
    
    if [[ "$DRY_RUN" == "true" ]]; then
        log_warning "这是一次预演，不会实际执行部署操作"
    fi
    
    # 记录开始时间
    local start_time=$(date +%s)
    
    # 执行部署步骤
    check_environment
    create_namespace
    update_config
    deploy_services
    verify_deployment
    generate_report
    
    # 计算部署时间
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))
    
    if [[ "$DRY_RUN" == "true" ]]; then
        log_success "预演完成！耗时: ${duration}秒"
    else
        log_success "部署完成！耗时: ${duration}秒"
    fi
}

# 错误处理
trap 'log_error "部署失败！"; exit 1' ERR

# 执行主函数
main "$@"