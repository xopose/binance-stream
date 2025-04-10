package ru.lazer.wsclient.client;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@AllArgsConstructor
public class BinanceKafkaProducer {

    @Bean
    public int produce() {
        URI uri = URI.create("wss://fstream.binance.com/stream?streams=bnbusdt@aggTrade/btcusdt@markPrice");
        BinanceWebSocketClient client = new BinanceWebSocketClient(uri, "host.docker.internal:29092");
        client.connect();
        return 1;
    }
}
