package com.biobt.platform.taskengine.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DistributedLockConfig {
    
    @Value("${spring.redis.host:localhost}")
    private String redisHost;
    
    @Value("${spring.redis.port:6379}")
    private int redisPort;
    
    @Value("${spring.redis.password:}")
    private String redisPassword;
    
    @Value("${spring.redis.database:0}")
    private int redisDatabase;
    
    @Value("${spring.redis.timeout:3000}")
    private int redisTimeout;
    
    /**
     * Redisson客户端配置
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        
        // 单机模式配置
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setDatabase(redisDatabase)
                .setConnectTimeout(redisTimeout)
                .setIdleConnectionTimeout(10000)
                .setKeepAlive(true)
                .setRetryAttempts(3)
                .setRetryInterval(1500)
                .setTimeout(3000)
                .setConnectionMinimumIdleSize(8)
                .setConnectionPoolSize(32);
        
        if (redisPassword != null && !redisPassword.trim().isEmpty()) {
            config.useSingleServer().setPassword(redisPassword);
        }
        
        // 编解码器
        config.setCodec(new org.redisson.codec.JsonJacksonCodec());
        
        // 线程池配置
        config.setThreads(16);
        config.setNettyThreads(32);
        
        RedissonClient redissonClient = Redisson.create(config);
        
        log.info("Redisson客户端初始化完成 - Redis地址: {}:{}", redisHost, redisPort);
        
        return redissonClient;
    }
    
    /**
     * 分布式锁服务
     */
    @Bean
    public DistributedLockService distributedLockService(RedissonClient redissonClient, RedisTemplate<String, Object> redisTemplate) {
        return new DistributedLockService(redissonClient, redisTemplate);
    }
    
    /**
     * Redis解锁脚本
     */
    @Bean
    public RedisScript<Long> unlockScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "    return redis.call('del', KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end"
        );
        redisScript.setResultType(Long.class);
        return redisScript;
    }
    
    /**
     * 分布式锁服务实现
     */
    public static class DistributedLockService {
        
        private final RedissonClient redissonClient;
        private final RedisTemplate<String, Object> redisTemplate;
        private final RedisScript<Long> unlockScript;
        
        public DistributedLockService(RedissonClient redissonClient, RedisTemplate<String, Object> redisTemplate) {
            this.redissonClient = redissonClient;
            this.redisTemplate = redisTemplate;
            this.unlockScript = createUnlockScript();
        }
        
        /**
         * 获取锁（使用Redisson）
         */
        public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
            try {
                return redissonClient.getLock(lockKey).tryLock(waitTime, leaseTime, unit);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("获取分布式锁被中断: {}", lockKey);
                return false;
            } catch (Exception e) {
                log.error("获取分布式锁失败: {}", lockKey, e);
                return false;
            }
        }
        
        /**
         * 释放锁（使用Redisson）
         */
        public void unlock(String lockKey) {
            try {
                redissonClient.getLock(lockKey).unlock();
            } catch (Exception e) {
                log.error("释放分布式锁失败: {}", lockKey, e);
            }
        }
        
        /**
         * 获取锁（使用Redis模板）
         */
        public boolean tryLockWithRedisTemplate(String lockKey, String lockValue, long expireTime, TimeUnit unit) {
            try {
                Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireTime, unit);
                return Boolean.TRUE.equals(result);
            } catch (Exception e) {
                log.error("获取分布式锁失败: {}", lockKey, e);
                return false;
            }
        }
        
        /**
         * 释放锁（使用Redis模板）
         */
        public boolean unlockWithRedisTemplate(String lockKey, String lockValue) {
            try {
                Long result = redisTemplate.execute(unlockScript, Collections.singletonList(lockKey), lockValue);
                return result != null && result > 0;
            } catch (Exception e) {
                log.error("释放分布式锁失败: {}", lockKey, e);
                return false;
            }
        }
        
        /**
         * 检查锁是否存在
         */
        public boolean isLocked(String lockKey) {
            try {
                return redissonClient.getLock(lockKey).isLocked();
            } catch (Exception e) {
                log.error("检查分布式锁状态失败: {}", lockKey, e);
                return false;
            }
        }
        
        /**
         * 强制释放锁
         */
        public void forceUnlock(String lockKey) {
            try {
                redissonClient.getLock(lockKey).forceUnlock();
            } catch (Exception e) {
                log.error("强制释放分布式锁失败: {}", lockKey, e);
            }
        }
        
        /**
         * 获取任务执行锁
         */
        public boolean tryLockTaskExecution(String taskId, long waitTime, long leaseTime) {
            String lockKey = "task:execution:" + taskId;
            return tryLock(lockKey, waitTime, leaseTime, TimeUnit.SECONDS);
        }
        
        /**
         * 释放任务执行锁
         */
        public void unlockTaskExecution(String taskId) {
            String lockKey = "task:execution:" + taskId;
            unlock(lockKey);
        }
        
        /**
         * 获取任务调度锁
         */
        public boolean tryLockTaskSchedule(String taskId, long waitTime, long leaseTime) {
            String lockKey = "task:schedule:" + taskId;
            return tryLock(lockKey, waitTime, leaseTime, TimeUnit.SECONDS);
        }
        
        /**
         * 释放任务调度锁
         */
        public void unlockTaskSchedule(String taskId) {
            String lockKey = "task:schedule:" + taskId;
            unlock(lockKey);
        }
        
        /**
         * 创建解锁脚本
         */
        private RedisScript<Long> createUnlockScript() {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(
                    "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "    return redis.call('del', KEYS[1]) " +
                    "else " +
                    "    return 0 " +
                    "end"
            );
            redisScript.setResultType(Long.class);
            return redisScript;
        }
    }
}