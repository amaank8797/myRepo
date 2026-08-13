package com.amaan.servlethdemo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@WebServlet("/promote")
public class BuildPromotionServlet extends HttpServlet {

    private Connection connection;

    @Override
    public void init() throws ServletException {

        System.out.println("=================================");
        System.out.println("Servlet init() called");
        System.out.println("=================================");

        String username = "root";
        String password = "YOUR_PASSWORD";

        String serverUrl = "jdbc:mysql://localhost:3306/";
        String databaseUrl = "jdbc:mysql://localhost:3306/servlet_demo";

        try {

            // Step 1: Connect to MySQL server
            try (Connection serverConnection =
                         DriverManager.getConnection(
                                 serverUrl,
                                 username,
                                 password
                         )) {

                System.out.println("Connected to MySQL server");

                // Step 2: Create database if it does not exist
                try (Statement statement = serverConnection.createStatement()) {

                    statement.executeUpdate(
                            "CREATE DATABASE IF NOT EXISTS servlet_demo"
                    );

                    System.out.println("Database checked/created");
                }
            }

            // Step 3: Connect to servlet_demo database
            connection = DriverManager.getConnection(
                    databaseUrl,
                    username,
                    password
            );

            System.out.println("Connected to servlet_demo database");

            // Step 4: Create table if it does not exist
            try (Statement statement = connection.createStatement()) {

                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS build_promotion (" +
                                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                                "application VARCHAR(100), " +
                                "version VARCHAR(50), " +
                                "environment VARCHAR(50)" +
                                ")"
                );

                System.out.println("Table checked/created");
            }

            System.out.println("Servlet initialization completed");

        } catch (Exception e) {

            System.out.println("Database initialization failed");

            throw new ServletException(
                    "Database initialization failed",
                    e
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        System.out.println("doGet() called");

        request.getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        System.out.println("doPost() called");

        // Get values from request
        String application = request.getParameter("application");
        String version = request.getParameter("version");
        String environment = request.getParameter("environment");

        // Prefix the values
        String prefixedApplication = "APP-" + application;
        String prefixedVersion = "BUILD-" + version;
        String prefixedEnvironment = "TARGET-" + environment;

        // Create session
        HttpSession session = request.getSession();

        session.setAttribute(
                "application",
                application
        );

        // Create cookie
        Cookie cookie = new Cookie(
                "application",
                application
        );

        response.addCookie(cookie);

        // Send processed values to JSP
        request.setAttribute(
                "application",
                prefixedApplication
        );

        request.setAttribute(
                "version",
                prefixedVersion
        );

        request.setAttribute(
                "environment",
                prefixedEnvironment
        );

        // Forward request to result JSP
        request.getRequestDispatcher(
                "/WEB-INF/result.jsp"
        ).forward(request, response);
    }

    @Override
    public void destroy() {

        System.out.println("=================================");
        System.out.println("Servlet destroy() called");
        System.out.println("=================================");

        try {

            if (connection != null) {

                connection.close();

                System.out.println(
                        "Database connection closed"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}