package ru.lazer.wsclient.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MarkPriceStream {
    @JsonProperty("stream")
    public String stream;

    @JsonProperty("data")
    private MarkPrice data;

    public MarkPrice getData() {
        return data;
    }

    public void setData(MarkPrice data) {
        this.data = data;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
