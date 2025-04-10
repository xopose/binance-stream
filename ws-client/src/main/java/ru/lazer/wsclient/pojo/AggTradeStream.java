package ru.lazer.wsclient.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AggTradeStream {
    @JsonProperty("stream")
    public String stream;

    @JsonProperty("data")
    private AggTrade data;

    public AggTrade getData() {
        return data;
    }

    public void setData(AggTrade data) {
        this.data = data;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
