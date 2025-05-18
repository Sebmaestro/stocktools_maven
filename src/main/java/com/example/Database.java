package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url = "jdbc:postgresql://localhost:5432/stocktools";
    private String user = "";
    private String password = "";

    private void connect() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        }
    }

    public void insertData(String name, String ticker, boolean owned, LocalDate latest_report, LocalDate upcoming_report) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {;
            String sql = "INSERT INTO company (name, ticker, owned, latest_report, upcoming_report) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql); 

            pstmt.setString(1, name);
            pstmt.setString(2, ticker);
            pstmt.setBoolean(3, owned);
            pstmt.setDate(4, java.sql.Date.valueOf(latest_report)); // YYYY-MM-DD
            pstmt.setDate(5, java.sql.Date.valueOf(upcoming_report)); // YYYY-MM-DD
            
            pstmt.executeUpdate();
            System.out.println("✅ Insatt i databasen!");
        } catch (SQLException e) {
            System.out.println("❌ Fel vid insättning i databas:");
            e.printStackTrace();   
        }
    }

    public List<Company> getAllData() {
        List<Company> companyList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM company";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String ticker = rs.getString("ticker");
                boolean owned = rs.getBoolean("owned");
                LocalDate latest_report = rs.getDate("latest_report").toLocalDate();
                LocalDate upcoming_report = rs.getDate("upcoming_report").toLocalDate();

                //System.out.println("Name: " + name + ", Ticker: " + ticker + ", Owned: " + owned + ", Latest Report: " + latest_report + ", Upcoming Report: " + upcoming_report);

                companyList.add(new Company(name, ticker, owned, latest_report, upcoming_report));
            }
        } catch (SQLException e) {
            System.out.println("❌ Fel vid hämtning av data:");
            e.printStackTrace();
        }
        return companyList;
    }
}
