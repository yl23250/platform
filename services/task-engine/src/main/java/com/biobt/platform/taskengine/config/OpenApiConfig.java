package com.biobt.platform.taskengine.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAPI文档配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Value("${spring.application.name:task-engine}")
    private String applicationName;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    @Value("${app.description:BioBt平台任务引擎服务}")
    private String appDescription;
    
    /**
     * OpenAPI配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(serverList())
                .components(securityComponents())
                .addSecurityItem(securityRequirement());
    }
    
    /**
     * API信息
     */
    private Info apiInfo() {
        return new Info()
                .title("BioBt任务引擎API")
                .description(appDescription + "\n\n" +
                        "## 功能特性\n" +
                        "- 任务定义管理\n" +
                        "- 任务调度执行\n" +
                        "- 任务监控统计\n" +
                        "- 分布式任务协调\n" +
                        "- 高可用性保障\n\n" +
                        "## 认证方式\n" +
                        "API使用Bearer Token进行认证，请在请求头中添加：\n" +
                        "```\n" +
                        "Authorization: Bearer <your-token>\n" +
                        "```\n\n" +
                        "## 错误码说明\n" +
                        "- 200: 成功\n" +
                        "- 400: 请求参数错误\n" +
                        "- 401: 未授权\n" +
                        "- 403: 禁止访问\n" +
                        "- 404: 资源不存在\n" +
                        "- 500: 服务器内部错误\n")
                .version(appVersion)
                .contact(new Contact()
                        .name("BioBt Platform Team")
                        .email("support@biobt.com")
                        .url("https://www.biobt.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }
    
    /**
     * 服务器列表
     */
    private List<Server> serverList() {
        return Arrays.asList(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("本地开发环境"),
                new Server()
                        .url("https://dev-api.biobt.com")
                        .description("开发环境"),
                new Server()
                        .url("https://test-api.biobt.com")
                        .description("测试环境"),
                new Server()
                        .url("https://api.biobt.com")
                        .description("生产环境")
        );
    }
    
    /**
     * 安全组件
     */
    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT认证令牌"))
                .addSecuritySchemes("apiKey", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-API-Key")
                        .description("API密钥认证"));
    }
    
    /**
     * 安全要求
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("bearerAuth")
                .addList("apiKey");
    }
}