package com.biobt.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网关服务类
 * 提供路由管理和服务发现功能
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Service
@Slf4j
public class GatewayService {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Autowired
    private RouteLocator routeLocator;
    
    /**
     * 获取所有已注册的服务
     */
    public List<String> getRegisteredServices() {
        List<String> services = discoveryClient.getServices();
        log.info("已注册的服务: {}", services);
        return services;
    }
    
    /**
     * 获取指定服务的实例信息
     */
    public List<ServiceInstance> getServiceInstances(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        log.info("服务 {} 的实例: {}", serviceId, instances.size());
        return instances;
    }
    
    /**
     * 获取所有服务的健康状态
     */
    public Map<String, List<ServiceInstance>> getAllServicesHealth() {
        return discoveryClient.getServices().stream()
                .collect(Collectors.toMap(
                        serviceId -> serviceId,
                        this::getServiceInstances
                ));
    }
    
    /**
     * 获取所有路由信息
     */
    public Flux<Route> getAllRoutes() {
        return routeLocator.getRoutes()
                .doOnNext(route -> log.debug("路由信息: {} -> {}", route.getId(), route.getUri()));
    }
    
    /**
     * 检查服务是否可用
     */
    public boolean isServiceAvailable(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        boolean available = !instances.isEmpty();
        log.debug("服务 {} 可用性: {}", serviceId, available);
        return available;
    }
    
    /**
     * 获取服务的负载均衡地址
     */
    public String getServiceLoadBalancerUri(String serviceId) {
        return "lb://" + serviceId;
    }
    
    /**
     * 获取网关统计信息
     */
    public Map<String, Object> getGatewayStats() {
        List<String> services = getRegisteredServices();
        Map<String, List<ServiceInstance>> servicesHealth = getAllServicesHealth();
        
        int totalServices = services.size();
        int availableServices = (int) servicesHealth.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .count();
        
        int totalInstances = servicesHealth.values().stream()
                .mapToInt(List::size)
                .sum();
        
        return Map.of(
                "totalServices", totalServices,
                "availableServices", availableServices,
                "totalInstances", totalInstances,
                "services", services,
                "timestamp", System.currentTimeMillis()
        );
    }
}