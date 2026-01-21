package org.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@SpringBootApplication
public class ExpenseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseManagementApplication.class, args);
    }

    @Bean
    public OpenAPI expenseManagementAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Expense Management API")
                        .description("REST API for Expense Management System with AI-powered currency predictions")
                        .version("1.0"));
    }
}