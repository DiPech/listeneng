FROM openjdk:11-jre-slim
WORKDIR /app
COPY ./dist/app.war /app/app.war
COPY ./dist/app.env /app/conf/app.env
CMD ["java", "-jar", "/app/app.war", "--CONF_DIR=/app/conf"]
