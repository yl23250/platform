# 使用OpenJDK 17作为基础镜像
FROM openjdk:17-jdk-slim

# 设置维护者信息
LABEL maintainer="BioBt Team <dev@biobt.com>"
LABEL description="BioBt Platform User Service"
LABEL version="1.0.0"

# 设置工作目录
WORKDIR /app

# 创建应用用户（安全考虑）
RUN groupadd -r biobt && useradd -r -g biobt biobt

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    && rm -rf /var/lib/apt/lists/*

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置JVM参数
ENV JAVA_OPTS="-server -Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+OptimizeStringConcat"

# 设置应用参数
ENV APP_OPTS="--spring.profiles.active=prod"

# 复制jar文件
ARG JAR_FILE=target/user-service.jar
COPY ${JAR_FILE} app.jar

# 创建日志目录
RUN mkdir -p /app/logs && chown -R biobt:biobt /app

# 切换到应用用户
USER biobt

# 暴露端口
EXPOSE 8081

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/user/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar $APP_OPTS"]

# 默认命令
CMD []