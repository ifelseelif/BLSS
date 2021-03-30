package com.ifelseelif.blsslab1.Models.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;

public class Review {
    private int scoreOfLocation;
    private int scoreOfService;
    private int scoreOfStuff;
    private int scoreOfFood;
    @JsonProperty
    private boolean isGoodHotel;
    private Date visitedDate;
    private String advantages;
    private String disadvantages;
    private String mainText;
    private long hotelId;

    public int getScoreOfLocation() {
        return scoreOfLocation;
    }

    public int getScoreOfService() {
        return scoreOfService;
    }

    public int getScoreOfStuff() {
        return scoreOfStuff;
    }

    public int getScoreOfFood() {
        return scoreOfFood;
    }

    public boolean getIsGoodHotel() {
        return isGoodHotel;
    }

    public Date getVisitedDate() {
        return visitedDate;
    }

    public String getAdvantages() {
        return advantages;
    }

    public String getDisadvantages() {
        return disadvantages;
    }

    public String getMainText() {
        return mainText;
    }

    public long getHotelId() {
        return hotelId;
    }
}