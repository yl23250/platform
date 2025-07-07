# 生物技术平台分层架构设计

## 架构概述

本文档详细描述了生物技术平台的分层架构设计，采用经典的四层架构模式：表现层、业务层、数据访问层和基础设施层。每一层都有明确的职责边界和标准化的接口规范。

## 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        表现层 (Presentation Layer)           │
├─────────────────────────────────────────────────────────────┤
│  Web前端应用    │  移动端应用    │  API网关    │  管理后台     │
├─────────────────────────────────────────────────────────────┤
│                        业务层 (Business Layer)               │
├─────────────────────────────────────────────────────────────┤
│  用户服务  │  租户服务  │  客户服务  │  项目服务  │  合同服务    │
│  价格服务  │  订单服务  │  支付服务  │  报告服务  │  流程服务    │
├─────────────────────────────────────────────────────────────┤
│                      数据访问层 (Data Access Layer)          │
├─────────────────────────────────────────────────────────────┤
│  数据库访问  │  缓存访问  │  搜索引擎  │  文件存储  │  消息队列   │
├─────────────────────────────────────────────────────────────┤
│                      基础设施层 (Infrastructure Layer)       │
├─────────────────────────────────────────────────────────────┤
│  MySQL集群  │  Redis集群  │  Elasticsearch  │  MinIO  │  RocketMQ │
└─────────────────────────────────────────────────────────────┘
```

# 第一层：表现层 (Presentation Layer)

## 层级职责
- 用户界面展示和交互
- 请求路由和负载均衡
- 身份认证和授权验证
- 请求参数验证和响应格式化
- 跨域处理和安全防护

## 核心组件

### 1. API网关 (Spring Cloud Gateway)
```yaml
# 配置示例
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=2
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
```

**功能特性：**
- 统一入口管理
- 路由转发和负载均衡
- 限流熔断保护
- 请求日志记录
- 跨域CORS处理

### 2. Web前端应用 (Vue 3.x)
```typescript
// 项目结构
src/
├── components/          # 通用组件
│   ├── common/          # 基础组件
│   ├── business/        # 业务组件
│   └── layout/          # 布局组件
├── views/               # 页面视图
│   ├── user/            # 用户管理
│   ├── tenant/          # 租户管理
│   ├── customer/        # 客户管理
│   ├── project/         # 项目管理
│   ├── contract/        # 合同管理
│   └── order/           # 订单管理
├── store/               # 状态管理
├── router/              # 路由配置
├── api/                 # API接口
└── utils/               # 工具函数
```

**技术栈：**
- Vue 3.x + TypeScript
- Element Plus UI组件库
- Pinia状态管理
- Vue Router路由管理
- Axios HTTP客户端

### 3. 移动端应用
```typescript
// 技术选型
- 跨平台方案：uni-app / React Native
- 原生开发：iOS (Swift) / Android (Kotlin)
- 混合开发：Ionic + Vue
```

### 4. 管理后台
```typescript
// 功能模块
- 系统管理：用户、角色、权限、组织
- 租户管理：租户信息、订阅管理
- 运营管理：数据统计、监控告警
- 配置管理：系统参数、业务配置
```

## 安全机制

### 1. 身份认证
```java
@Component
public class JwtAuthenticationFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // JWT Token验证逻辑
        String token = extractToken(exchange.getRequest());
        if (validateToken(token)) {
            return chain.filter(exchange);
        }
        return unauthorized(exchange.getResponse());
    }
}
```

### 2. 权限控制
```java
@PreAuthorize("hasPermission('customer', 'read')")
@GetMapping("/customers")
public ResponseEntity<List<Customer>> getCustomers() {
    // 业务逻辑
}
```

# 第二层：业务层 (Business Layer)

## 层级职责
- 业务逻辑处理和规则验证
- 服务编排和事务管理
- 数据转换和业务计算
- 外部系统集成
- 业务事件发布和处理

## 微服务架构设计

### 1. 用户认证服务 (user-service)
```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }
}
```

**核心功能：**
- 用户注册登录
- 密码管理
- 用户信息维护
- 第三方登录集成

### 2. 租户管理服务 (tenant-service)
```java
@Service
@Transactional
public class TenantService {
    
    public TenantResponse createTenant(CreateTenantRequest request) {
        // 1. 创建租户基础信息
        Tenant tenant = tenantRepository.save(buildTenant(request));
        
        // 2. 初始化租户数据库
        tenantDatabaseService.initTenantDatabase(tenant.getId());
        
        // 3. 创建默认管理员账号
        userService.createTenantAdmin(tenant.getId(), request.getAdminInfo());
        
        // 4. 发布租户创建事件
        eventPublisher.publishEvent(new TenantCreatedEvent(tenant));
        
        return TenantResponse.from(tenant);
    }
}
```

**核心功能：**
- 租户生命周期管理
- 多租户数据隔离
- 租户配置管理
- 订阅计费管理

### 3. 客户管理服务 (customer-service)
```java
@Service
public class CustomerService {
    
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        // 业务规则验证
        validateCustomerData(request);
        
        // 客户分级计算
        CustomerLevel level = calculateCustomerLevel(request);
        
        // 保存客户信息
        Customer customer = customerRepository.save(
            Customer.builder()
                .name(request.getName())
                .level(level)
                .tenantId(getCurrentTenantId())
                .build()
        );
        
        return CustomerResponse.from(customer);
    }
}
```

**核心功能：**
- 客户档案管理
- 客户分级管理
- 地址簿管理
- 客户关系维护

### 4. 项目管理服务 (project-service)
```java
@Service
public class ProjectService {
    
    public ProjectResponse createProject(CreateProjectRequest request) {
        // 项目编码生成
        String projectCode = projectCodeGenerator.generate();
        
        // 项目价格计算
        BigDecimal price = priceCalculator.calculate(request);
        
        // 保存项目信息
        Project project = projectRepository.save(
            Project.builder()
                .code(projectCode)
                .name(request.getName())
                .price(price)
                .tenantId(getCurrentTenantId())
                .build()
        );
        
        return ProjectResponse.from(project);
    }
}
```

**核心功能：**
- 项目库管理
- 项目配置管理
- 项目价格管理
- 项目分类管理

### 5. 合同管理服务 (contract-service)
```java
@Service
@Transactional
public class ContractService {
    
    public ContractResponse createContract(CreateContractRequest request) {
        // 合同编号生成
        String contractNo = contractNoGenerator.generate();
        
        // 合同条款验证
        validateContractTerms(request);
        
        // 价格计算
        ContractPrice price = contractPriceCalculator.calculate(request);
        
        // 保存合同信息
        Contract contract = contractRepository.save(
            Contract.builder()
                .contractNo(contractNo)
                .customerId(request.getCustomerId())
                .price(price)
                .status(ContractStatus.DRAFT)
                .tenantId(getCurrentTenantId())
                .build()
        );
        
        // 启动审批流程
        workflowService.startContractApprovalProcess(contract.getId());
        
        return ContractResponse.from(contract);
    }
}
```

**核心功能：**
- 合同全生命周期管理
- 合同审批流程
- 合同价格管理
- 合同履约管理

## 服务间通信

### 1. 同步调用 (OpenFeign)
```java
@FeignClient(name = "customer-service")
public interface CustomerServiceClient {
    
    @GetMapping("/api/customer/{id}")
    CustomerResponse getCustomer(@PathVariable("id") Long id);
    
    @PostMapping("/api/customer")
    CustomerResponse createCustomer(@RequestBody CreateCustomerRequest request);
}
```

### 2. 异步消息 (RocketMQ)
```java
@Component
public class ContractEventHandler {
    
    @RocketMQMessageListener(
        topic = "contract-topic",
        consumerGroup = "order-service-group"
    )
    public class ContractSignedListener implements RocketMQListener<ContractSignedEvent> {
        
        @Override
        public void onMessage(ContractSignedEvent event) {
            // 处理合同签署事件
            orderService.enableOrderCreation(event.getContractId());
        }
    }
}
```

## 事务管理

### 1. 本地事务
```java
@Transactional(rollbackFor = Exception.class)
public void processOrder(OrderRequest request) {
    // 本地事务处理
}
```

### 2. 分布式事务 (Seata)
```java
@GlobalTransactional
public void createOrderWithPayment(OrderRequest request) {
    // 1. 创建订单
    orderService.createOrder(request);
    
    // 2. 扣减库存
    inventoryService.reduceInventory(request.getItems());
    
    // 3. 创建支付单
    paymentService.createPayment(request.getPaymentInfo());
}
```

# 第三层：数据访问层 (Data Access Layer)

## 层级职责
- 数据持久化操作
- 数据库连接管理
- 缓存数据管理
- 搜索引擎操作
- 文件存储管理

## 数据访问组件

### 1. 数据库访问 (MyBatis-Plus)
```java
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    
    @Select("SELECT * FROM customer WHERE tenant_id = #{tenantId} AND level = #{level}")
    List<Customer> findByTenantAndLevel(@Param("tenantId") Long tenantId, 
                                       @Param("level") CustomerLevel level);
    
    @Update("UPDATE customer SET level = #{level} WHERE id = #{id}")
    int updateCustomerLevel(@Param("id") Long id, @Param("level") CustomerLevel level);
}
```

### 2. 缓存访问 (Redis)
```java
@Service
public class CustomerCacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CUSTOMER_KEY_PREFIX = "customer:";
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    
    public void cacheCustomer(Customer customer) {
        String key = CUSTOMER_KEY_PREFIX + customer.getId();
        redisTemplate.opsForValue().set(key, customer, CACHE_TTL);
    }
    
    public Customer getCustomerFromCache(Long customerId) {
        String key = CUSTOMER_KEY_PREFIX + customerId;
        return (Customer) redisTemplate.opsForValue().get(key);
    }
}
```

### 3. 搜索引擎 (Elasticsearch)
```java
@Repository
public class CustomerSearchRepository {
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    
    public List<Customer> searchCustomers(String keyword) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "address", "contact"))
            .withPageable(PageRequest.of(0, 20))
            .build();
            
        SearchHits<Customer> searchHits = elasticsearchTemplate.search(searchQuery, Customer.class);
        return searchHits.stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }
}
```

### 4. 文件存储 (MinIO)
```java
@Service
public class FileStorageService {
    
    @Autowired
    private MinioClient minioClient;
    
    public String uploadFile(MultipartFile file, String bucketName) throws Exception {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );
        
        return fileName;
    }
}
```

## 数据源配置

### 1. 多数据源配置
```java
@Configuration
public class DataSourceConfig {
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
    
    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
    
    @Bean
    public DataSource routingDataSource() {
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource());
        dataSourceMap.put("slave", slaveDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        return routingDataSource;
    }
}
```

### 2. 读写分离
```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value() default "master";
}

@Aspect
@Component
public class DataSourceAspect {
    
    @Around("@annotation(dataSource)")
    public Object around(ProceedingJoinPoint point, DataSource dataSource) throws Throwable {
        DynamicDataSourceContextHolder.setDataSourceKey(dataSource.value());
        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
    }
}
```

# 第四层：基础设施层 (Infrastructure Layer)

## 层级职责
- 基础中间件服务
- 系统资源管理
- 网络通信支持
- 监控和日志收集
- 安全和备份服务

## 基础设施组件

### 1. 数据库集群 (MySQL)
```yaml
# MySQL主从配置
master:
  host: mysql-master
  port: 3306
  database: bioBt_platform
  username: ${DB_USERNAME}
  password: ${DB_PASSWORD}
  
slaves:
  - host: mysql-slave-1
    port: 3306
  - host: mysql-slave-2
    port: 3306
```

### 2. 缓存集群 (Redis)
```yaml
# Redis Cluster配置
spring:
  redis:
    cluster:
      nodes:
        - redis-node-1:6379
        - redis-node-2:6379
        - redis-node-3:6379
        - redis-node-4:6379
        - redis-node-5:6379
        - redis-node-6:6379
      max-redirects: 3
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
```

### 3. 消息队列 (RocketMQ)
```yaml
# RocketMQ配置
rocketmq:
  name-server: rocketmq-nameserver:9876
  producer:
    group: bioBt-producer-group
    send-message-timeout: 3000
    retry-times-when-send-failed: 2
  consumer:
    group: bioBt-consumer-group
    consume-thread-min: 5
    consume-thread-max: 32
```

### 4. 服务注册中心 (Nacos)
```yaml
# Nacos配置
spring:
  cloud:
    nacos:
      discovery:
        server-addr: nacos-server:8848
        namespace: bioBt-platform
        group: DEFAULT_GROUP
      config:
        server-addr: nacos-server:8848
        namespace: bioBt-platform
        group: DEFAULT_GROUP
        file-extension: yaml
```

## 容器化部署

### 1. Docker配置
```dockerfile
# 应用服务Dockerfile
FROM openjdk:17-jre-slim

VOLUME /tmp
COPY target/user-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 2. Kubernetes部署
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: bioBt/user-service:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

## 监控和日志

### 1. 应用监控 (Prometheus + Grafana)
```yaml
# Prometheus配置
global:
  scrape_interval: 15s
  
scrape_configs:
  - job_name: 'bioBt-services'
    static_configs:
      - targets: ['user-service:8080', 'customer-service:8080']
    metrics_path: '/actuator/prometheus'
```

### 2. 日志收集 (ELK Stack)
```yaml
# Logstash配置
input {
  beats {
    port => 5044
  }
}

filter {
  if [fields][service] {
    mutate {
      add_field => { "service_name" => "%{[fields][service]}" }
    }
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "bioBt-logs-%{+YYYY.MM.dd}"
  }
}
```

# 跨层交互规范

## 接口设计规范

### 1. RESTful API设计
```java
// 统一响应格式
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}
```

### 2. 异常处理
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ApiResponse<Void> response = ApiResponse.error(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected error", e);
        ApiResponse<Void> response = ApiResponse.error(500, "Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

## 数据传输对象 (DTO)

### 1. 请求对象
```java
@Data
@Valid
public class CreateCustomerRequest {
    
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 100, message = "客户名称长度不能超过100个字符")
    private String name;
    
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @NotNull(message = "客户类型不能为空")
    private CustomerType type;
}
```

### 2. 响应对象
```java
@Data
public class CustomerResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private CustomerType type;
    private CustomerLevel level;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public static CustomerResponse from(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        BeanUtils.copyProperties(customer, response);
        return response;
    }
}
```

# 性能优化策略

## 缓存策略

### 1. 多级缓存
```java
@Service
public class CustomerService {
    
    @Cacheable(value = "customer", key = "#id")
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
    
    @CacheEvict(value = "customer", key = "#customer.id")
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
```

### 2. 缓存预热
```java
@Component
public class CacheWarmupService {
    
    @EventListener(ApplicationReadyEvent.class)
    public void warmupCache() {
        // 预加载热点数据
        List<Customer> hotCustomers = customerRepository.findHotCustomers();
        hotCustomers.forEach(customer -> {
            customerCacheService.cacheCustomer(customer);
        });
    }
}
```

## 数据库优化

### 1. 索引优化
```sql
-- 客户表索引
CREATE INDEX idx_customer_tenant_level ON customer(tenant_id, level);
CREATE INDEX idx_customer_name ON customer(name);
CREATE INDEX idx_customer_phone ON customer(phone);

-- 合同表索引
CREATE INDEX idx_contract_customer ON contract(customer_id);
CREATE INDEX idx_contract_status ON contract(status);
CREATE INDEX idx_contract_create_time ON contract(create_time);
```

### 2. 分页查询优化
```java
@Repository
public class CustomerRepository {
    
    public Page<Customer> findCustomersWithCursor(Long lastId, int pageSize) {
        // 使用游标分页避免深分页问题
        String sql = "SELECT * FROM customer WHERE id > ? ORDER BY id LIMIT ?";
        List<Customer> customers = jdbcTemplate.query(sql, 
            new Object[]{lastId, pageSize}, 
            new CustomerRowMapper());
        return new PageImpl<>(customers);
    }
}
```

# 安全架构设计

## 认证授权架构

### 1. JWT Token设计
```java
@Component
public class JwtTokenProvider {
    
    private final String secretKey = "bioBt-platform-secret-key";
    private final long validityInMilliseconds = 3600000; // 1小时
    
    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());
        claims.put("tenantId", getCurrentTenantId());
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
}
```

### 2. 权限控制
```java
@Component
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {
    
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String resource = targetDomainObject.toString();
        String action = permission.toString();
        
        return userPrincipal.getPermissions().contains(resource + ":" + action);
    }
}
```

## 数据安全

### 1. 敏感数据加密
```java
@Component
public class DataEncryptionService {
    
    private final AESUtil aesUtil;
    
    @EventListener
    public void handleCustomerSaveEvent(CustomerSaveEvent event) {
        Customer customer = event.getCustomer();
        // 加密敏感字段
        customer.setPhone(aesUtil.encrypt(customer.getPhone()));
        customer.setEmail(aesUtil.encrypt(customer.getEmail()));
    }
}
```

### 2. 数据脱敏
```java
@JsonSerialize(using = PhoneDesensitizeSerializer.class)
public class CustomerResponse {
    private String phone; // 显示为 138****1234
}

public class PhoneDesensitizeSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        if (StringUtils.isNotBlank(value) && value.length() == 11) {
            String desensitized = value.substring(0, 3) + "****" + value.substring(7);
            gen.writeString(desensitized);
        } else {
            gen.writeString(value);
        }
    }
}
```

# 总结

本分层架构设计文档详细描述了生物技术平台的四层架构实现方案：

1. **表现层**：负责用户交互和请求处理，包括Web前端、移动端、API网关等
2. **业务层**：实现核心业务逻辑，采用微服务架构，服务间通过同步和异步方式通信
3. **数据访问层**：提供统一的数据访问接口，支持多数据源、缓存、搜索等
4. **基础设施层**：提供底层技术支撑，包括数据库、缓存、消息队列等中间件

通过清晰的分层设计和标准化的接口规范，确保系统的可维护性、可扩展性和高性能。同时，完善的安全机制和监控体系保障了系统的稳定运行。