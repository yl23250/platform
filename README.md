# 生物技术平台 (BioBt Platform)

## 项目简介

生物技术平台是一个基于微服务架构的企业级SaaS平台，专为生物技术行业设计，提供完整的客户管理、项目管理、合同管理、订单管理等业务功能。

## 技术架构

### 后端技术栈
- **Java 21** - 编程语言
- **Spring Boot 3.5** - 应用框架
- **Spring Cloud Alibaba** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis Plus** - ORM框架
- **MySQL 8.0** - 主数据库
- **Redis 7.x** - 缓存数据库
- **RocketMQ** - 消息队列
- **Nacos** - 注册中心和配置中心
- **Elasticsearch** - 搜索引擎
- **MinIO** - 对象存储

### 前端技术栈
- **Vue 3.x** - 前端框架
- **TypeScript** - 编程语言
- **Element Plus** - UI组件库
- **Vite** - 构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理

### 基础设施
- **Docker** - 容器化
- **Kubernetes** - 容器编排
- **Prometheus** - 监控系统
- **Grafana** - 可视化监控
- **Jaeger** - 链路追踪
- **ELK Stack** - 日志分析

## 项目结构

```
bioBt-platform/
├── README.md                    # 项目说明文档
├── docker-compose.yml           # Docker编排文件
├── project-structure.md         # 项目结构设计文档
├── architecture-design.md       # 架构设计文档
├── prd.md                      # 产品需求文档
├── docs/                       # 文档目录
│   ├── api/                    # API文档
│   └── deployment/             # 部署文档
├── scripts/                    # 脚本目录
│   ├── build.sh               # 构建脚本
│   ├── deploy.sh              # 部署脚本
│   └── init-db.sql            # 数据库初始化脚本
├── common/                     # 公共模块
│   ├── common-core/           # 核心工具模块
│   ├── common-security/       # 安全模块
│   ├── common-database/       # 数据库模块
│   ├── common-redis/          # Redis模块
│   └── common-web/            # Web模块
├── gateway/                    # 网关服务
│   └── api-gateway/           # API网关
├── services/                   # 业务服务
│   ├── user-service/          # 用户服务
│   ├── tenant-service/        # 租户服务
│   ├── customer-service/      # 客户服务
│   ├── project-service/       # 项目服务
│   ├── contract-service/      # 合同服务
│   ├── order-service/         # 订单服务
│   ├── payment-service/       # 支付服务
│   └── report-service/        # 报表服务
├── web/                       # 前端应用
│   ├── admin-web/             # 管理后台
│   ├── tenant-web/            # 租户端
│   └── mobile-app/            # 移动端
├── infrastructure/            # 基础设施配置
│   ├── mysql/                 # MySQL配置
│   ├── redis/                 # Redis配置
│   ├── elasticsearch/         # ES配置
│   ├── rocketmq/             # RocketMQ配置
│   └── nacos/                # Nacos配置
└── kubernetes/               # K8s部署配置
    ├── namespace.yaml
    ├── configmap.yaml
    └── services/
```

## 核心功能模块

### PaaS层功能
- **R&D建模** - 研发流程建模和管理
- **流程引擎** - 业务流程自动化
- **消息引擎** - 消息通知和推送
- **任务引擎** - 任务调度和执行
- **日志引擎** - 系统日志管理
- **用户管理** - 用户账号管理
- **角色管理** - 角色权限管理
- **资源管理** - 系统资源管理
- **授权管理** - 访问权限控制
- **统一认证** - 单点登录认证
- **多组织管理** - 多租户组织架构

### 平台侧功能
- **租户管理** - 租户注册和管理
- **订阅管理** - 服务订阅和计费

### 服务商侧功能
- **客户管理** - 客户信息管理
- **项目管理** - 项目全生命周期管理
- **价格管理** - 产品定价管理
- **合同管理** - 合同签署和管理

### 客户侧功能
- **项目查询** - 项目进度查询
- **合同查询** - 合同信息查询
- **报表查询** - 数据报表查询
- **订单管理** - 订单下单和管理
- **支付管理** - 在线支付功能

## 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- Docker 20+
- Docker Compose 2+
- Maven 3.8+

### 本地开发环境搭建

1. **克隆项目**
```bash
git clone https://github.com/your-org/biobt-platform.git
cd biobt-platform
```

2. **启动基础设施**
```bash
# 启动MySQL、Redis、Nacos等基础服务
docker-compose up -d mysql-master redis-node-1 nacos elasticsearch

# 等待服务启动完成
docker-compose logs -f nacos
```

3. **初始化数据库**
```bash
# 执行数据库初始化脚本
mysql -h localhost -P 3306 -u root -proot123 < scripts/init-db.sql
```

4. **构建公共模块**
```bash
cd common
mvn clean install -DskipTests
cd ..
```

5. **启动网关服务**
```bash
cd gateway/api-gateway
mvn spring-boot:run
```

6. **启动业务服务**
```bash
# 启动用户服务
cd services/user-service
mvn spring-boot:run

# 启动其他服务...
```

7. **启动前端应用**
```bash
cd web/admin-web
npm install
npm run dev
```

### 生产环境部署

1. **构建Docker镜像**
```bash
./scripts/build.sh
```

2. **部署到Kubernetes**
```bash
./scripts/deploy.sh prod
```

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化配置
- 编写完整的单元测试
- 添加详细的API文档注释

### 分支管理
- `main` - 主分支，用于生产环境
- `develop` - 开发分支，用于集成测试
- `feature/*` - 功能分支，用于新功能开发
- `hotfix/*` - 热修复分支，用于紧急修复

### 提交规范
```
type(scope): subject

body

footer
```

类型说明：
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

## API文档

### 接口地址
- 开发环境：http://localhost:8080
- 测试环境：https://test-api.biobt.com
- 生产环境：https://api.biobt.com

### Swagger文档
- 用户服务：http://localhost:8081/swagger-ui.html
- 客户服务：http://localhost:8082/swagger-ui.html
- 项目服务：http://localhost:8083/swagger-ui.html

## 监控和运维

### 监控地址
- Prometheus：http://localhost:9090
- Grafana：http://localhost:3000 (admin/admin123)
- Jaeger：http://localhost:16686
- Kibana：http://localhost:5601

### 日志查看
```bash
# 查看服务日志
docker-compose logs -f user-service

# 查看所有服务日志
docker-compose logs -f
```

### 健康检查
```bash
# 检查服务状态
curl http://localhost:8080/actuator/health

# 检查服务信息
curl http://localhost:8080/actuator/info
```

## 测试

### 单元测试
```bash
# 运行所有单元测试
mvn test

# 运行指定服务的测试
cd services/user-service
mvn test
```

### 集成测试
```bash
# 运行集成测试
mvn verify -P integration-test
```

### 性能测试
```bash
# 使用JMeter进行性能测试
jmeter -n -t tests/performance/user-service.jmx -l results.jtl
```

## 常见问题

### Q: 服务启动失败怎么办？
A: 检查以下几点：
1. 确保基础服务（MySQL、Redis、Nacos）已启动
2. 检查端口是否被占用
3. 查看服务日志排查具体错误

### Q: 如何添加新的微服务？
A: 参考现有服务结构：
1. 在services目录下创建新服务
2. 添加Maven依赖和配置
3. 实现业务逻辑和API接口
4. 添加服务注册和发现配置

### Q: 如何进行数据库迁移？
A: 使用Flyway进行数据库版本管理：
1. 在resources/db/migration目录添加SQL脚本
2. 按照V{version}__{description}.sql格式命名
3. 重启服务自动执行迁移

## 贡献指南

1. Fork项目到个人仓库
2. 创建功能分支
3. 提交代码变更
4. 创建Pull Request
5. 等待代码审查

## 许可证

本项目采用MIT许可证，详见[LICENSE](LICENSE)文件。

## 联系我们

- 项目负责人：张三 (zhangsan@biobt.com)
- 技术支持：李四 (lisi@biobt.com)
- 官方网站：https://www.biobt.com
- 技术文档：https://docs.biobt.com

---

**注意**：本项目仍在开发中，部分功能可能不完整。如有问题请及时反馈。