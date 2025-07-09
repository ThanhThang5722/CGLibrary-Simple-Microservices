package com.thangBook.employeeservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Employee API Specification - ThangBook",
        description = "API documentation for Employee Service",
        version = "1.0",
        contact = @Contact(
            name = "Thanh Thang",
            email = "theonguyen.it@gmail.com",
            url = "https://github.com/ThanhThang5722"
        ),
        license = @License(
            name="MIT License",
            url="https://github.com/ThanhThang5722/CGLibrary-Simple-Microservices"
        ),
        termsOfService = "https://github.com/ThanhThang5722/CGLibrary-Simple-Microservices"
    ),
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:9002"
        ),
        @Server(
            description = "Dev ENV",
            url = "http://employee-service.dev.com"
        ),
        @Server(
            description = "Prod ENV",
            url = "http://employee-service.prod.com"
        )
    }
)
public class OpenApiConfig {
    
}
