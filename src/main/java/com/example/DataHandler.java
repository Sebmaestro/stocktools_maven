package com.example;

//import java.time.LocalDate;
import java.util.List;

public class DataHandler {
    
    public void sortReports() {
        Database db = new Database();
        List<Company> company = db.getAllData();

        // for (Company c : company) {
            // LocalDate latestReport = c.getLatestReport();
            // LocalDate upcomingReport = c.getUpcomingReport();
            // String name = c.getName();
            // String ticker = c.getTicker();
            // boolean owned = c.isOwned();

            // System.out.println("Name: " + name + ", Ticker: " + ticker + ", Owned: " + owned +
            //         ", Latest Report: " + latestReport + ", Upcoming Report: " + upcomingReport);
        // }
        company.sort((a, b) -> a.getUpcomingReport().compareTo(b.getUpcomingReport()));
        System.out.println("📈 Upcoming reports sorted by date: 📉");
        for (Company c : company) {
            System.out.println(c.getName() + " " + c.getUpcomingReport());
        }
    }
}
