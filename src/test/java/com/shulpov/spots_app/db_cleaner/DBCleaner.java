package com.shulpov.spots_app.db_cleaner;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCleaner {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;

    //TODO �� properties �������� �� ����������, ��-�������� ���� ��� �������
    public DBCleaner() {
        this.dbUrl = "jdbc:postgresql://localhost:5432/test_spot_map";
        this.dbUsername = "postgres";
        this.dbPassword = "12345";
    }

    private static Connection connection;

    public void setup() throws SQLException {
        // ������������� � �������� ���������� � ����� ������
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    // ��������� ����� ��� �������� ����������
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanupDatabase(String scriptFilePath) throws SQLException {
        // ����� �������� SQL-������ ��� ������� ���� ������ �� �����
        try {
            executeScript(scriptFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ��������� ���������� ����� ����� ���������� ���� ������!
    }

    public void executeScript(String scriptFilePath) throws IOException, SQLException {
        Resource resource = new ClassPathResource(scriptFilePath);

        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sql.append(line).append("\n");
                }
                connection.createStatement().executeUpdate(sql.toString());
            }
        } else {
            System.err.println("Script file not found: " + scriptFilePath);
        }
    }

}
