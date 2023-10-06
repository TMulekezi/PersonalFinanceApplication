package com.gfinance.application.service;

import com.gfinance.application.user.WebInvestment;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Service class that handles Alphavantage api calls made by the controllers.
@Service
public class FinanceService {

    private WebInvestment webInvestment;

    private String price;
    private String change;

    private String changePercent;


    public FinanceService() {

    }

    public WebInvestment getWebInvestment() {
        return webInvestment;
    }

    // method that searches and retrieves a stock given a ticker symbol
    public boolean setWebInvestment(String symbol, long id) {
        symbol = symbol.toUpperCase();
        // https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo

        try {
        URL url = new URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=KP07LP7E9V05EGHJ");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader input = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();




        while ((inputLine = input.readLine()) != null) {
            content.append(inputLine);
        }



        JSONObject jsonObject = new JSONObject(content.toString());

        try {
            JSONObject dailyInfo = (JSONObject) jsonObject.get("Global Quote");
            price = dailyInfo.get("05. price").toString();

            change = dailyInfo.get("09. change").toString();

            changePercent = dailyInfo.get("10. change percent").toString();

            webInvestment = new WebInvestment(id ,symbol, price, change, changePercent);
        } catch (JSONException e) {
            throw new IOException();
        }





        input.close();
        } catch (IOException e) {
            System.out.println("Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 100 calls per day. Please visit https://www.alphavantage.co/premium/ if you would like to target a higher API call frequency.");
            return false;
        }
        return true;
    }

    // method that checks if a stock exists for a given ticker
    public boolean hasTicker(String symbol) {
        symbol = symbol.toUpperCase();
        // https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo
        try {
            URL url = new URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=KP07LP7E9V05EGHJ");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();


            while ((inputLine = input.readLine()) != null) {
                content.append(inputLine);
            }

            JSONObject jsonObject = new JSONObject(content.toString());

            try {
                JSONObject dailyInfo = (JSONObject) jsonObject.get("Global Quote");
                if (dailyInfo.length() == 0) {
                    return false;
                }
            } catch (JSONException e) {
                throw new IOException();
            }


            input.close();
        } catch (IOException e) {
            System.out.println("Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 100 calls per day. Please visit https://www.alphavantage.co/premium/ if you would like to target a higher API call frequency.");
        }


        return true;
    }
}
