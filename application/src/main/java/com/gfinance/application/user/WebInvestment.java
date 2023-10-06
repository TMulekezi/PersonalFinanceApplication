package com.gfinance.application.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
// Investment object used to process Entity investments retrieved from a database for view display
@Validated
public class WebInvestment {


    private long id;

    // spring validation criteria
    @NotNull(message = "required")
    private String symbol;

    private String price;
    private String change;

    private String changePercent;


    public  WebInvestment() {

    }


    public WebInvestment(long id, String symbol, String price, String change, String changePercent) {
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    @Override
    public String toString() {
        return "WebInvestment{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", price='" + price + '\'' +
                ", change='" + change + '\'' +
                ", changePercent='" + changePercent + '\'' +
                '}';
    }
}
