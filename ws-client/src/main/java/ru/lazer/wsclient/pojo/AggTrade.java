package ru.lazer.wsclient.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AggTrade {
    @JsonProperty("e")
    private String e; // Event type "markPriceUpdate"
    @JsonProperty("E")
    private long E;   // Event time
    @JsonProperty("s")
    private String s; // Symbol (trade pair)
    @JsonProperty("a")
    private int a; // Aggregate trade ID
    @JsonProperty("p")
    private double p; // Price
    @JsonProperty("q")
    private double q; // Quantity
    @JsonProperty("f")
    private int f; // First trade ID
    @JsonProperty("l")
    private int l; // Last trade ID
    @JsonProperty("T")
    private long T; // Trade time
    @JsonProperty("m")
    private boolean m; // Is the buyer the market maker?
}
