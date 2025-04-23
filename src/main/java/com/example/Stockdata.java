package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.crypto.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Stockdata {
    //private String apiKey = "6800f6cfb97fa3.05372477";
    private String ninjaKey = "J73WqaimDahdvNFX68LkFQ==gjvEXuL8nKc5uHA1";
    private String companyName = "";

    public void getReportDate(String ticker, String name) {
        List<LocalDate> parsedDates = new ArrayList<>();
        List<LocalDate> dateList = new ArrayList<>();
        parsedDates = ApiCall(ticker,
                "https://api.api-ninjas.com/v1/earningscalendar?ticker=" + ticker + "&show_upcoming=true",
                "date", dateList);

        System.out.println(parsedDates);

        parsedDates = ApiCall(ticker,
                "https://api.api-ninjas.com/v1/earningscalendar?ticker=" + ticker + "&show_upcoming=false",
                "date", dateList);

        System.out.println(parsedDates);
        parsedDates = parseDates(parsedDates);
        LocalDate closestBefore = parsedDates.get(0);
        LocalDate closestAfter = parsedDates.get(1);

         
        parsedDates = ApiCall(ticker,
                "https://api.api-ninjas.com/v1/logo?ticker=" + ticker,
                "name", dateList);
   
                
        System.out.println("Closest date before today: " + (closestBefore != null ? closestBefore : "None"));
        System.out.println("todays date: " + LocalDate.now());
        System.out.println("Closest date after today: " + (closestAfter != null ? closestAfter : "None"));
        Database database = new Database();

        if (companyName == "" && name == "") {
            System.out.println("Need to include company name since it doesnt exist in API!");
            return;
        }

        if (name == "") {
            database.insertData(companyName, ticker, true, closestBefore, closestAfter);    
        } else {
            database.insertData(name, ticker, true, closestBefore, closestAfter);
        }

        
    }

    public List<LocalDate> ApiCall(String ticker, String URL, String type, List<LocalDate> dateList) {        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL)).header("X-Api-Key", ninjaKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                //System.out.println("Response: " + response.body());
                String json = response.body();

                // Tolka JSON-arrayen
                JSONArray array = new JSONArray(json);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject earnings = array.getJSONObject(i);

                    // String ticker = earnings.optString("ticker", "okänt");
                    if (type == "date") {
                        String reportDate = earnings.optString(type);
                        dateList.add(LocalDate.parse(reportDate));
                        //System.out.println("Date: " + reportDate);               
                    } else {
                        companyName = earnings.optString(type);
                        System.out.println("Name: " + companyName);
                    }
                }
                return dateList;
            } else {
                System.out.println("Error: " + response.statusCode());
                System.out.println("Error: " + response.body());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<LocalDate> parseDates(List<LocalDate> arr) {
        LocalDate today = LocalDate.now();
        LocalDate closestBefore = null;
        LocalDate closestAfter = null;
        for (int i = 0; i < arr.size(); i++) {
            LocalDate date = arr.get(i);
            //System.out.println("date: " + date);
            if (date.isBefore(today)) {
                if (closestBefore == null || date.isAfter(closestBefore)) {
                    closestBefore = date;
                }
            } else if (date.isAfter(today)) {
                if (closestAfter == null || date.isBefore(closestAfter)) {
                    closestAfter = date;
                }
            }
        }

        return Arrays.asList(closestBefore, closestAfter);
    }

    //Ta in namn från alla företag sen kalla funktionen som hämtar rapportdatan för alla bolag, sedan uppdatera databasen med
    //rapportdatum om det behövs 
    public void updateReportDatesOfEveryCompany(List<Company> companyList) {
        Database db = new Database();




        //db.updateData(ticker, "previous report", upcoming report);
    }

    public void scraping() {
        try {
            // Hämta sidan
            Document doc = Jsoup.connect("https://finance.yahoo.com/quote/AAPL").get();

            // Sök efter texten "Earnings Date"
            Elements rows = doc.select("td:contains(Earnings Date) + td");

            for (Element row : rows) {
                System.out.println("Earnings Date: " + row.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
