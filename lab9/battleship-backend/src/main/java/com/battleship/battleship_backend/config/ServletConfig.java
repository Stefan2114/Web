package com.battleship.battleship_backend.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan(basePackages = "com.battleship.battleship_backend.servlet")
public class ServletConfig {
    // This enables scanning for @WebServlet annotations
}