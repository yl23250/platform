# 生物技术平台项目结构设计

## 整体项目结构

```
bioBt-platform/
├── README.md
├── docker-compose.yml
├── kubernetes/
│   ├── namespace.yaml
│   ├── configmap.yaml
│   └── services/
├── docs/
│   ├── prd.md
│   ├── architecture-design.md
│   └── api/
├── scripts/
│   ├── build.sh
│   ├── deploy.sh
│   └── init-db.sql
├── common/
│   ├── common-core/
│   ├── common-security/
│   ├── common-redis/
│   ├── common-database/
│   └── common-web/
├── gateway/
│   └── api-gateway/
├── services/
│   ├── user-service/
│   ├── tenant-service/
│   ├── customer-service/
│   ├── project-service/
│   ├── contract-service/
│   ├── order-service/
│   ├── payment-service/
│   └── report-service/
├── web/
│   ├── admin-web/
│   ├── tenant-web/
│   └── mobile-app/
└── infrastructure/
    ├── mysql/
    ├── redis/
    ├── elasticsearch/
    ├── rocketmq/
    └── nacos/
```

# 公共模块 (common/)

## common-core 核心工具模块

```
common-core/
├── pom.xml
└── src/main/java/com/biobt/common/core/
    ├── annotation/
    │   ├── DataSource.java
    │   ├── TenantScope.java
    │   └── ApiVersion.java
    ├── constant/
    │   ├── CommonConstants.java
    │   ├── SecurityConstants.java
    │   └── CacheConstants.java
    ├── domain/
    │   ├── BaseEntity.java
    │   ├── PageRequest.java
    │   ├── PageResponse.java
    │   └── ApiResponse.java
    ├── enums/
    │   ├── ResponseCode.java
    │   ├── UserStatus.java
    │   └── TenantStatus.java
    ├── exception/
    │   ├── BusinessException.java
    │   ├── SystemException.java
    │   └── GlobalExceptionHandler.java
    ├── utils/
    │   ├── DateUtils.java
    │   ├── StringUtils.java
    │   ├── JsonUtils.java
    │   ├── BeanUtils.java
    │   └── ValidationUtils.java
    └── config/
        ├── JacksonConfig.java
        └── WebMvcConfig.java
```

### 核心基础类示例

```java
// BaseEntity.java
@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @Column(name = "create_by")
    private String createBy;
    
    @Column(name = "update_by")
    private String updateBy;
    
    @Column(name = "deleted")
    private Boolean deleted = false;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (tenantId == null) {
            tenantId = TenantContextHolder.getCurrentTenantId();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
```

```java
// ApiResponse.java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
```

## common-security 安全模块

```
common-security/
├── pom.xml
└── src/main/java/com/biobt/common/security/
    ├── annotation/
    │   ├── PreAuthorize.java
    │   └── RequirePermission.java
    ├── config/
    │   ├── SecurityConfig.java
    │   └── JwtConfig.java
    ├── filter/
    │   ├── JwtAuthenticationFilter.java
    │   └── TenantFilter.java
    ├── handler/
    │   ├── AuthenticationEntryPointImpl.java
    │   └── AccessDeniedHandlerImpl.java
    ├── service/
    │   ├── TokenService.java
    │   ├── PermissionService.java
    │   └── UserDetailsServiceImpl.java
    └── domain/
        ├── LoginUser.java
        ├── UserPrincipal.java
        └── JwtToken.java
```

### 安全配置示例

```java
// SecurityConfig.java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
```

## common-database 数据库模块

```
common-database/
├── pom.xml
└── src/main/java/com/biobt/common/database/
    ├── config/
    │   ├── MybatisPlusConfig.java
    │   ├── DataSourceConfig.java
    │   └── TransactionConfig.java
    ├── handler/
    │   ├── TenantLineHandler.java
    │   └── MetaObjectHandler.java
    ├── interceptor/
    │   ├── TenantInterceptor.java
    │   └── DataPermissionInterceptor.java
    └── datasource/
        ├── DynamicDataSource.java
        ├── DataSourceContextHolder.java
        └── DataSourceAspect.java
```

### 数据库配置示例

```java
// MybatisPlusConfig.java
@Configuration
@MapperScan("com.biobt.**.mapper")
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 多租户插件
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Long tenantId = TenantContextHolder.getCurrentTenantId();
                return tenantId != null ? new LongValue(tenantId) : null;
            }
            
            @Override
            public boolean ignoreTable(String tableName) {
                return "sys_tenant".equals(tableName) || 
                       "sys_user".equals(tableName);
            }
        });
        interceptor.addInnerInterceptor(tenantInterceptor);
        
        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL);
        paginationInterceptor.setOverflow(false);
        interceptor.addInnerInterceptor(paginationInterceptor);
        
        return interceptor;
    }
    
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
            }
            
            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
```

# 网关服务 (gateway/)

## api-gateway API网关

```
api-gateway/
├── pom.xml
├── Dockerfile
└── src/main/
    ├── java/com/biobt/gateway/
    │   ├── GatewayApplication.java
    │   ├── config/
    │   │   ├── GatewayConfig.java
    │   │   ├── CorsConfig.java
    │   │   └── SwaggerConfig.java
    │   ├── filter/
    │   │   ├── AuthGlobalFilter.java
    │   │   ├── LogGlobalFilter.java
    │   │   └── RateLimitGlobalFilter.java
    │   ├── handler/
    │   │   ├── HystrixFallbackHandler.java
    │   │   └── SwaggerResourceHandler.java
    │   └── service/
    │       └── ValidateCodeService.java
    └── resources/
        ├── application.yml
        ├── application-dev.yml
        ├── application-prod.yml
        └── bootstrap.yml
```

### 网关配置示例

```yaml
# application.yml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
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
                key-resolver: "#{@ipKeyResolver}"
        - id: customer-service
          uri: lb://customer-service
          predicates:
            - Path=/api/customer/**
          filters:
            - StripPrefix=2
            - name: Hystrix
              args:
                name: customer-service
                fallbackUri: forward:/fallback/customer
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin
        - DedupeResponseHeader=Access-Control-Allow-Credentials
```

```java
// AuthGlobalFilter.java
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    
    @Autowired
    private TokenService tokenService;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 跳过认证的路径
        if (isSkipAuth(path)) {
            return chain.filter(exchange);
        }
        
        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            return unauthorized(exchange.getResponse());
        }
        
        try {
            Claims claims = tokenService.parseToken(token);
            String userId = claims.getSubject();
            String tenantId = claims.get("tenantId", String.class);
            
            // 设置用户信息到请求头
            ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", userId)
                .header("X-Tenant-Id", tenantId)
                .build();
                
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return unauthorized(exchange.getResponse());
        }
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
}
```

# 业务服务 (services/)

## user-service 用户服务

```
user-service/
├── pom.xml
├── Dockerfile
└── src/main/
    ├── java/com/biobt/user/
    │   ├── UserServiceApplication.java
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   ├── UserController.java
    │   │   └── RoleController.java
    │   ├── service/
    │   │   ├── AuthService.java
    │   │   ├── UserService.java
    │   │   ├── RoleService.java
    │   │   └── impl/
    │   ├── mapper/
    │   │   ├── UserMapper.java
    │   │   ├── RoleMapper.java
    │   │   └── UserRoleMapper.java
    │   ├── domain/
    │   │   ├── entity/
    │   │   │   ├── User.java
    │   │   │   ├── Role.java
    │   │   │   └── UserRole.java
    │   │   ├── dto/
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── RegisterRequest.java
    │   │   │   └── UserResponse.java
    │   │   └── vo/
    │   ├── config/
    │   │   ├── SecurityConfig.java
    │   │   └── RedisConfig.java
    │   └── feign/
    │       └── TenantServiceClient.java
    └── resources/
        ├── application.yml
        ├── mapper/
        │   ├── UserMapper.xml
        │   └── RoleMapper.xml
        └── db/migration/
            └── V1__Create_user_tables.sql
```

### 用户服务示例代码

```java
// UserController.java
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取用户信息")
    @PreAuthorize("hasPermission('user', 'read')")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ApiResponse.success(response);
    }
    
    @GetMapping
    @ApiOperation("用户列表")
    @PreAuthorize("hasPermission('user', 'read')")
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @Valid PageRequest pageRequest,
            @RequestParam(required = false) String keyword) {
        PageResponse<UserResponse> response = userService.getUsers(pageRequest, keyword);
        return ApiResponse.success(response);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("更新用户")
    @PreAuthorize("hasPermission('user', 'write')")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ApiResponse.success(response);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    @PreAuthorize("hasPermission('user', 'delete')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}
```

```java
// UserService.java
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public UserResponse register(RegisterRequest request) {
        // 1. 验证用户名是否存在
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 2. 验证邮箱是否存在
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }
        
        // 3. 创建用户
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .phone(request.getPhone())
            .status(UserStatus.ACTIVE)
            .tenantId(TenantContextHolder.getCurrentTenantId())
            .build();
            
        userMapper.insert(user);
        
        // 4. 分配默认角色
        assignDefaultRole(user.getId());
        
        // 5. 发送欢迎邮件
        sendWelcomeEmail(user);
        
        return UserResponse.from(user);
    }
    
    @Cacheable(value = "user", key = "#id")
    public UserResponse getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return UserResponse.from(user);
    }
    
    public PageResponse<UserResponse> getUsers(PageRequest pageRequest, String keyword) {
        Page<User> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("username", keyword)
                       .or()
                       .like("email", keyword);
        }
        queryWrapper.eq("deleted", false)
                   .orderByDesc("create_time");
        
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        
        List<UserResponse> userResponses = userPage.getRecords().stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
            
        return PageResponse.<UserResponse>builder()
            .records(userResponses)
            .total(userPage.getTotal())
            .pageNum(pageRequest.getPageNum())
            .pageSize(pageRequest.getPageSize())
            .build();
    }
}
```

## customer-service 客户服务

```
customer-service/
├── pom.xml
├── Dockerfile
└── src/main/
    ├── java/com/biobt/customer/
    │   ├── CustomerServiceApplication.java
    │   ├── controller/
    │   │   ├── CustomerController.java
    │   │   └── AddressController.java
    │   ├── service/
    │   │   ├── CustomerService.java
    │   │   ├── AddressService.java
    │   │   └── CustomerLevelService.java
    │   ├── mapper/
    │   │   ├── CustomerMapper.java
    │   │   └── AddressMapper.java
    │   ├── domain/
    │   │   ├── entity/
    │   │   │   ├── Customer.java
    │   │   │   └── CustomerAddress.java
    │   │   ├── dto/
    │   │   └── vo/
    │   ├── config/
    │   ├── listener/
    │   │   └── CustomerEventListener.java
    │   └── feign/
    │       └── ProjectServiceClient.java
    └── resources/
        ├── application.yml
        └── mapper/
```

# 前端应用 (web/)

## admin-web 管理后台

```
admin-web/
├── package.json
├── vite.config.ts
├── tsconfig.json
├── Dockerfile
├── public/
│   ├── index.html
│   └── favicon.ico
└── src/
    ├── main.ts
    ├── App.vue
    ├── components/
    │   ├── common/
    │   │   ├── PageHeader.vue
    │   │   ├── SearchForm.vue
    │   │   └── DataTable.vue
    │   └── business/
    │       ├── UserForm.vue
    │       ├── CustomerForm.vue
    │       └── ContractForm.vue
    ├── views/
    │   ├── login/
    │   │   └── index.vue
    │   ├── dashboard/
    │   │   └── index.vue
    │   ├── system/
    │   │   ├── user/
    │   │   ├── role/
    │   │   └── tenant/
    │   ├── business/
    │   │   ├── customer/
    │   │   ├── project/
    │   │   ├── contract/
    │   │   └── order/
    │   └── layout/
    │       ├── index.vue
    │       ├── Header.vue
    │       ├── Sidebar.vue
    │       └── Breadcrumb.vue
    ├── router/
    │   ├── index.ts
    │   └── modules/
    ├── store/
    │   ├── index.ts
    │   ├── modules/
    │   │   ├── user.ts
    │   │   ├── app.ts
    │   │   └── permission.ts
    ├── api/
    │   ├── user.ts
    │   ├── customer.ts
    │   ├── project.ts
    │   └── contract.ts
    ├── utils/
    │   ├── request.ts
    │   ├── auth.ts
    │   ├── permission.ts
    │   └── validate.ts
    ├── styles/
    │   ├── index.scss
    │   ├── variables.scss
    │   └── mixins.scss
    └── types/
        ├── api.d.ts
        ├── user.d.ts
        └── global.d.ts
```

### 前端配置示例

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false,
    rollupOptions: {
      output: {
        chunkFileNames: 'js/[name]-[hash].js',
        entryFileNames: 'js/[name]-[hash].js',
        assetFileNames: '[ext]/[name]-[hash].[ext]'
      }
    }
  }
})
```

```typescript
// src/utils/request.ts
import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import router from '@/router'

const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const userStore = useUserStore()
    const token = userStore.token
    
    if (token) {
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${token}`
      }
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data
    
    if (code === 200) {
      return data
    } else {
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  (error) => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default service
```

# 基础设施 (infrastructure/)

## Docker Compose 配置

```yaml
# docker-compose.yml
version: '3.8'

services:
  # MySQL主库
  mysql-master:
    image: mysql:8.0
    container_name: biobt-mysql-master
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: biobt_platform
    ports:
      - "3306:3306"
    volumes:
      - ./infrastructure/mysql/master/data:/var/lib/mysql
      - ./infrastructure/mysql/master/conf:/etc/mysql/conf.d
      - ./scripts/init-db.sql:/docker-entrypoint-initdb.d/init.sql
    command: --server-id=1 --log-bin=mysql-bin --binlog-format=ROW
    
  # MySQL从库
  mysql-slave:
    image: mysql:8.0
    container_name: biobt-mysql-slave
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: biobt_platform
    ports:
      - "3307:3306"
    volumes:
      - ./infrastructure/mysql/slave/data:/var/lib/mysql
      - ./infrastructure/mysql/slave/conf:/etc/mysql/conf.d
    command: --server-id=2 --relay-log=mysql-relay-bin --read-only=1
    depends_on:
      - mysql-master
      
  # Redis集群
  redis-node-1:
    image: redis:7-alpine
    container_name: biobt-redis-1
    ports:
      - "6379:6379"
      - "16379:16379"
    volumes:
      - ./infrastructure/redis/node1:/data
    command: redis-server /data/redis.conf
    
  # Nacos
  nacos:
    image: nacos/nacos-server:v2.2.0
    container_name: biobt-nacos
    environment:
      MODE: standalone
      SPRING_DATASOURCE_PLATFORM: mysql
      MYSQL_SERVICE_HOST: mysql-master
      MYSQL_SERVICE_DB_NAME: nacos
      MYSQL_SERVICE_USER: root
      MYSQL_SERVICE_PASSWORD: root123
    ports:
      - "8848:8848"
      - "9848:9848"
    depends_on:
      - mysql-master
      
  # RocketMQ NameServer
  rocketmq-nameserver:
    image: apache/rocketmq:4.9.4
    container_name: biobt-rocketmq-nameserver
    ports:
      - "9876:9876"
    environment:
      JAVA_OPT_EXT: "-server -Xms512m -Xmx512m"
    command: sh mqnamesrv
    
  # RocketMQ Broker
  rocketmq-broker:
    image: apache/rocketmq:4.9.4
    container_name: biobt-rocketmq-broker
    ports:
      - "10909:10909"
      - "10911:10911"
    environment:
      NAMESRV_ADDR: rocketmq-nameserver:9876
      JAVA_OPT_EXT: "-server -Xms512m -Xmx512m"
    command: sh mqbroker -c /opt/rocketmq-4.9.4/conf/broker.conf
    depends_on:
      - rocketmq-nameserver
      
  # Elasticsearch
  elasticsearch:
    image: elasticsearch:8.5.0
    container_name: biobt-elasticsearch
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
    ports:
      - "9200:9200"
      - "9300:9300"
    volumes:
      - ./infrastructure/elasticsearch/data:/usr/share/elasticsearch/data
      
  # Kibana
  kibana:
    image: kibana:8.5.0
    container_name: biobt-kibana
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
```

## Kubernetes 配置

```yaml
# kubernetes/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: biobt-platform
  labels:
    name: biobt-platform
```

```yaml
# kubernetes/services/user-service.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
  namespace: biobt-platform
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
        image: biobt/user-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "k8s"
        - name: NACOS_SERVER_ADDR
          value: "nacos-service:8848"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: user-service
  namespace: biobt-platform
spec:
  selector:
    app: user-service
  ports:
  - port: 8080
    targetPort: 8080
  type: ClusterIP
```

# 构建和部署脚本

## 构建脚本

```bash
#!/bin/bash
# scripts/build.sh

set -e

echo "开始构建生物技术平台..."

# 构建公共模块
echo "构建公共模块..."
cd common
mvn clean install -DskipTests
cd ..

# 构建网关
echo "构建API网关..."
cd gateway/api-gateway
mvn clean package -DskipTests
docker build -t biobt/api-gateway:latest .
cd ../..

# 构建各个微服务
services=("user-service" "tenant-service" "customer-service" "project-service" "contract-service" "order-service" "payment-service" "report-service")

for service in "${services[@]}"; do
    echo "构建 $service..."
    cd services/$service
    mvn clean package -DskipTests
    docker build -t biobt/$service:latest .
    cd ../..
done

# 构建前端应用
echo "构建管理后台..."
cd web/admin-web
npm install
npm run build
docker build -t biobt/admin-web:latest .
cd ../..

echo "构建完成！"
```

## 部署脚本

```bash
#!/bin/bash
# scripts/deploy.sh

set -e

ENVIRONMENT=${1:-dev}

echo "部署到 $ENVIRONMENT 环境..."

if [ "$ENVIRONMENT" = "dev" ]; then
    echo "启动开发环境..."
    docker-compose up -d
elif [ "$ENVIRONMENT" = "prod" ]; then
    echo "部署到生产环境..."
    kubectl apply -f kubernetes/namespace.yaml
    kubectl apply -f kubernetes/configmap.yaml
    kubectl apply -f kubernetes/services/
else
    echo "未知环境: $ENVIRONMENT"
    exit 1
fi

echo "部署完成！"
```

# 总结

本项目结构设计遵循以下原则：

1. **模块化设计**：清晰的模块划分，便于开发和维护
2. **分层架构**：严格的分层设计，职责明确
3. **微服务架构**：服务独立部署，可扩展性强
4. **标准化规范**：统一的代码结构和命名规范
5. **容器化部署**：支持Docker和Kubernetes部署
6. **自动化构建**：完整的CI/CD流水线支持

通过这样的项目结构设计，可以确保系统的可维护性、可扩展性和开发效率。