package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        //Stockdata data = new Stockdata();
        //data.getReportDate("AAPL");
        //Database db = new Database();
        //db.insertData();
        Stockdata data = new Stockdata();

        System.out.println("Enter ticker (and name) of stock:");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        String[] parts = response.split(" ");
        System.out.println(parts[0]);
        scanner.close();

        if (parts.length > 1) {
            System.out.println(parts[1]);
            data.getReportDate(parts[0], parts[1]);
        } else if (parts[0] != "") {
            data.getReportDate(parts[0], "");
        } 
        
        // data.getReportDate("evo.st", "");
        DataHandler present = new DataHandler();
        present.sortReports();
    }
}