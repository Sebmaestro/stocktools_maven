package com.example;
import java.sql.Date;
import java.time.LocalDate;

public class Company {
    private String name;
    private String ticker;
    private boolean owned;
    private LocalDate latestReport;
    private LocalDate upcomingReport;

    public Company(String name, String ticker, boolean owned, LocalDate latestReport, LocalDate upcomingReport) {
        this.name = name;
        this.ticker = ticker;
        this.owned = owned;
        this.latestReport = latestReport;
        this.upcomingReport = upcomingReport;
    }

    public String getName() {
        return name;
    }
    public String getTicker() {
        return ticker;
    }
    public boolean isOwned() {
        return owned;
    }
    public LocalDate getLatestReport() {
        return latestReport;
    }
    public LocalDate getUpcomingReport() {
        return upcomingReport;
    }
}
