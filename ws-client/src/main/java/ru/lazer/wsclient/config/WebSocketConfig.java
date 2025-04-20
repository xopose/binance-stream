package ru.lazer.wsclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.lazer.wsclient.component.TradeWebSocketHandler;

@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    private final TradeWebSocketHandler handler;

    public WebSocketConfig(TradeWebSocketHandler handler) {
        this.handler = handler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/trade").setAllowedOrigins("*");
    }
}