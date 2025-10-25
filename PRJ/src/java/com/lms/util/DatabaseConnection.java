package com.lms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility
 * Quản lý kết nối đến SQL Server
 */
public class DatabaseConnection {
    
    // Cấu hình database - THAY ĐỔI THEO MÁY CỦA BẠN
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=LeaveManagementSystem;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123456";
    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
    // Static block để load driver
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("SQL Server JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy connection đến database
     * @return Connection object
     * @throws SQLException nếu không kết nối được
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            throw e;
        }
    }
    
    /**
     * Đóng connection
     * @param conn Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed!");
            } catch (SQLException e) {
                System.err.println("Error closing connection!");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test connection
     */
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("Connection test successful!");
                closeConnection(conn);
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed!");
            e.printStackTrace();
        }
    }
}