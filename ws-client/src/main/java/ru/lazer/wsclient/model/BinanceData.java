package ru.lazer.wsclient.model;

import jakarta.persistence.*;


@Entity
@Table(name = "binance_data")
public class BinanceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String value;

    private Long timestamp;

    public BinanceData(String name, String value, Long timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }

    public BinanceData() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
