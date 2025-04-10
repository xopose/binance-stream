package ru.lazer.wsclient.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkPrice {
    @JsonProperty("e")
    private String e; // Event type "markPriceUpdate"
    @JsonProperty("E")
    private long E;   // Event time
    @JsonProperty("s")
    private String s; // Symbol (trade pair)
    @JsonProperty("p")
    private double p; // Mark price
    @JsonProperty("P")
    private double P; // Estimated Settle Price, only useful in the last hour before the settlement starts
    @JsonProperty("i")
    private double i; // High price
    @JsonProperty("r")
    private double r; // Funding rate
    @JsonProperty("T")
    private long T; // Next funding time
}
