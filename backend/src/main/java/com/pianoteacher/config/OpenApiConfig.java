package com.pianoteacher.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pianoTeacherOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Piano Teacher Management System API")
                        .description("Comprehensive REST API for managing piano students, schedules, lessons, homework, and progress reports. " +
                                "This API supports both English and Chinese languages and provides intelligent scheduling suggestions.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Piano Teacher Development Team")
                                .email("support@pianoteacher.com")
                                .url("https://github.com/pianoteacher/piano-teacher-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.pianoteacher.com")
                                .description("Production Server")))
                .tags(List.of(
                        createTag("Student Management", "Operations for managing student information and pricing"),
                        createTag("Schedule Management", "Operations for managing teaching schedules and availability"),
                        createTag("Lesson Content", "Operations for managing lesson content and materials"),
                        createTag("Progress Reports", "Operations for managing student progress reports and analytics"),
                        createTag("Scheduling Suggestions", "Intelligent scheduling suggestions based on patterns"),
                        createTag("System", "System health and configuration operations")));
    }

    private Tag createTag(String name, String description) {
        return new Tag()
                .name(name)
                .description(description);
    }
}
