#!/bin/bash

# BioBt Platform 构建脚本
# 用于构建整个平台的所有服务

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
PROFILE="dev"
SKIP_TESTS="false"
BUILD_DOCKER="false"
PUSH_DOCKER="false"
CLEAN="false"
SERVICES="all"
VERSION="1.0.0-SNAPSHOT"

# 帮助信息
show_help() {
    cat << EOF
BioBt Platform 构建脚本

用法: $0 [选项]

选项:
    -p, --profile PROFILE       指定构建环境 (dev|test|prod) [默认: dev]
    -s, --skip-tests            跳过测试
    -d, --docker                构建Docker镜像
    -u, --push                  推送Docker镜像到仓库
    -c, --clean                 清理构建缓存
    --services SERVICES         指定要构建的服务 (all|common|gateway|user|customer|project|contract|order|payment|report) [默认: all]
    --version VERSION           指定版本号 [默认: 1.0.0-SNAPSHOT]
    -h, --help                  显示帮助信息

示例:
    $0                          # 构建所有服务 (开发环境)
    $0 -p prod -d               # 构建生产环境并创建Docker镜像
    $0 --services user,customer # 只构建用户服务和客户服务
    $0 -c                       # 清理构建缓存
    $0 -s -d -u                 # 跳过测试，构建并推送Docker镜像

EOF
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -p|--profile)
            PROFILE="$2"
            shift 2
            ;;
        -s|--skip-tests)
            SKIP_TESTS="true"
            shift
            ;;
        -d|--docker)
            BUILD_DOCKER="true"
            shift
            ;;
        -u|--push)
            PUSH_DOCKER="true"
            BUILD_DOCKER="true"
            shift
            ;;
        -c|--clean)
            CLEAN="true"
            shift
            ;;
        --services)
            SERVICES="$2"
            shift 2
            ;;
        --version)
            VERSION="$2"
            shift 2
            ;;
        -h|--help)
            show_help
            exit 0
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
    log_info "检查构建环境..."
    
    # 检查Java
    if ! command -v java &> /dev/null; then
        log_error "Java未安装或不在PATH中"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [[ "$JAVA_VERSION" -lt 17 ]]; then
        log_error "需要Java 17或更高版本，当前版本: $JAVA_VERSION"
        exit 1
    fi
    
    # 检查Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven未安装或不在PATH中"
        exit 1
    fi
    
    # 检查Docker（如果需要构建镜像）
    if [[ "$BUILD_DOCKER" == "true" ]]; then
        if ! command -v docker &> /dev/null; then
            log_error "Docker未安装或不在PATH中"
            exit 1
        fi
        
        if ! docker info &> /dev/null; then
            log_error "Docker守护进程未运行"
            exit 1
        fi
    fi
    
    log_success "环境检查通过"
}

# 清理构建缓存
clean_build() {
    if [[ "$CLEAN" == "true" ]]; then
        log_info "清理构建缓存..."
        cd "$PROJECT_ROOT"
        mvn clean -q
        
        # 清理Docker镜像（可选）
        if [[ "$BUILD_DOCKER" == "true" ]]; then
            log_info "清理Docker镜像..."
            docker system prune -f --filter "label=com.biobt.platform=true" || true
        fi
        
        log_success "清理完成"
    fi
}

# 构建公共模块
build_common_modules() {
    log_info "构建公共模块..."
    cd "$PROJECT_ROOT"
    
    local maven_opts="-P$PROFILE"
    if [[ "$SKIP_TESTS" == "true" ]]; then
        maven_opts="$maven_opts -DskipTests"
    fi
    
    # 构建公共模块
    local common_modules=(
        "common/common-core"
        "common/common-security"
        "common/common-database"
        "common/common-redis"
        "common/common-web"
        "common/common-mq"
        "common/common-elasticsearch"
        "common/common-oss"
    )
    
    for module in "${common_modules[@]}"; do
        if [[ -d "$module" ]]; then
            log_info "构建模块: $module"
            cd "$PROJECT_ROOT/$module"
            mvn clean install $maven_opts -q
            log_success "模块构建完成: $module"
        else
            log_warning "模块不存在，跳过: $module"
        fi
    done
    
    cd "$PROJECT_ROOT"
    log_success "公共模块构建完成"
}

# 构建服务
build_service() {
    local service_name="$1"
    local service_path="$2"
    
    if [[ ! -d "$service_path" ]]; then
        log_warning "服务不存在，跳过: $service_name"
        return
    fi
    
    log_info "构建服务: $service_name"
    cd "$service_path"
    
    local maven_opts="-P$PROFILE"
    if [[ "$SKIP_TESTS" == "true" ]]; then
        maven_opts="$maven_opts -DskipTests"
    fi
    
    # 构建服务
    mvn clean package $maven_opts -q
    
    # 构建Docker镜像
    if [[ "$BUILD_DOCKER" == "true" ]]; then
        log_info "构建Docker镜像: $service_name"
        
        # 检查Dockerfile是否存在
        if [[ -f "Dockerfile" ]]; then
            docker build -t "biobt/$service_name:$VERSION" -t "biobt/$service_name:latest" .
            
            # 推送镜像
            if [[ "$PUSH_DOCKER" == "true" ]]; then
                log_info "推送Docker镜像: $service_name"
                docker push "biobt/$service_name:$VERSION"
                docker push "biobt/$service_name:latest"
            fi
        else
            log_warning "Dockerfile不存在，跳过Docker构建: $service_name"
        fi
    fi
    
    cd "$PROJECT_ROOT"
    log_success "服务构建完成: $service_name"
}

# 构建所有服务
build_services() {
    log_info "构建业务服务..."
    
    # 定义服务列表
    declare -A service_map=(
        ["gateway"]="gateway/api-gateway"
        ["user"]="services/user-service"
        ["tenant"]="services/tenant-service"
        ["customer"]="services/customer-service"
        ["project"]="services/project-service"
        ["contract"]="services/contract-service"
        ["order"]="services/order-service"
        ["payment"]="services/payment-service"
        ["report"]="services/report-service"
        ["notification"]="services/notification-service"
        ["file"]="services/file-service"
    )
    
    # 解析要构建的服务
    if [[ "$SERVICES" == "all" ]]; then
        # 构建所有服务
        for service_name in "${!service_map[@]}"; do
            build_service "$service_name" "${service_map[$service_name]}"
        done
    else
        # 构建指定服务
        IFS=',' read -ra SERVICE_ARRAY <<< "$SERVICES"
        for service_name in "${SERVICE_ARRAY[@]}"; do
            service_name=$(echo "$service_name" | xargs) # 去除空格
            if [[ -n "${service_map[$service_name]}" ]]; then
                build_service "$service_name" "${service_map[$service_name]}"
            else
                log_error "未知服务: $service_name"
                log_info "可用服务: ${!service_map[*]}"
                exit 1
            fi
        done
    fi
    
    log_success "业务服务构建完成"
}

# 生成构建报告
generate_report() {
    log_info "生成构建报告..."
    
    local report_file="$PROJECT_ROOT/build-report-$(date +%Y%m%d-%H%M%S).txt"
    
    cat > "$report_file" << EOF
BioBt Platform 构建报告
========================

构建时间: $(date)
构建环境: $PROFILE
构建版本: $VERSION
跳过测试: $SKIP_TESTS
构建Docker: $BUILD_DOCKER
推送Docker: $PUSH_DOCKER
构建服务: $SERVICES

构建结果:
EOF
    
    # 检查构建产物
    if [[ "$SERVICES" == "all" || "$SERVICES" == *"user"* ]]; then
        if [[ -f "$PROJECT_ROOT/services/user-service/target/user-service.jar" ]]; then
            echo "✓ user-service.jar" >> "$report_file"
        else
            echo "✗ user-service.jar" >> "$report_file"
        fi
    fi
    
    # 检查Docker镜像
    if [[ "$BUILD_DOCKER" == "true" ]]; then
        echo "" >> "$report_file"
        echo "Docker镜像:" >> "$report_file"
        docker images biobt/* --format "table {{.Repository}}:{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}" >> "$report_file" 2>/dev/null || true
    fi
    
    log_success "构建报告已生成: $report_file"
}

# 主函数
main() {
    log_info "开始构建 BioBt Platform"
    log_info "构建环境: $PROFILE"
    log_info "构建版本: $VERSION"
    log_info "构建服务: $SERVICES"
    
    # 记录开始时间
    local start_time=$(date +%s)
    
    # 执行构建步骤
    check_environment
    clean_build
    
    # 根据服务选择构建公共模块
    if [[ "$SERVICES" == "all" || "$SERVICES" == "common" ]]; then
        build_common_modules
    fi
    
    # 构建业务服务
    if [[ "$SERVICES" != "common" ]]; then
        build_services
    fi
    
    # 生成报告
    generate_report
    
    # 计算构建时间
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))
    
    log_success "构建完成！耗时: ${duration}秒"
}

# 错误处理
trap 'log_error "构建失败！"; exit 1' ERR

# 执行主函数
main "$@"