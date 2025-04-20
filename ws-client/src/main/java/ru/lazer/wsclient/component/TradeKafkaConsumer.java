package ru.lazer.wsclient.component;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TradeKafkaConsumer {

    private final TradeWebSocketHandler webSocketHandler;

    public TradeKafkaConsumer(TradeWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }


    @KafkaListener(topics = "trade_info", groupId = "ws-client-group")
    public void listen(String message) {
        System.out.println(">>> [Kafka] Получено сообщение: " + message);
        webSocketHandler.broadcastMessage(message);
    }
}