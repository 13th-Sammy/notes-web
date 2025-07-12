# Use an official OpenJDK 21 base image
FROM eclipse-temurin:21-jdk

# Set working dir
WORKDIR /app

# Copy the jar file from your local machine to container
COPY app/build/libs/app.jar /app/app.jar

# Copy static files to container
COPY app/public/ /app/public/

# Copy config files
COPY app/config/ /app/config/

# Expose port your server runs on
EXPOSE 8080

# Run the application 
CMD ["java", "-jar", "app.jar"]