package ru.lazer.wsclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import ru.lazer.wsclient.pojo.AggTradeStream;
import ru.lazer.wsclient.pojo.MarkPriceStream;
import ru.lazer.wsclient.service.JasksonService;

import java.net.URI;
import java.util.Properties;

public class BinanceWebSocketClient extends WebSocketClient {
    String bootstrapServer;
    KafkaProducer<String, String> producer;

    JasksonService jasksonService;
    AggTradeStream aggTradeStream;
    MarkPriceStream markPriceStream;

    public BinanceWebSocketClient(URI serverUri, String bootstrapServer) {
        super(serverUri, new Draft_6455());
        this.bootstrapServer = bootstrapServer;

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(properties);
        jasksonService = new JasksonService();
        aggTradeStream = new AggTradeStream();
        markPriceStream = new MarkPriceStream();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(String message) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try {
            aggTradeStream = jasksonService.jsonStringToPojo(AggTradeStream.class, message);
            sendProducerRecord(
                    new ProducerRecord<>(
                            "binance",
                            aggTradeStream.getStream(),
                            jasksonService.pojoToJsonString(aggTradeStream.getData())
                    )
            );
        } catch (JsonProcessingException e) {
            try {
                markPriceStream = jasksonService.jsonStringToPojo(MarkPriceStream.class, message);
                sendProducerRecord(
                        new ProducerRecord<>(
                                "binance",
                                markPriceStream.getStream(),
                                jasksonService.pojoToJsonString(markPriceStream.getData())
                        )
                );
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    private void sendProducerRecord(ProducerRecord<String, String> producerRecord) {
        producer.send(producerRecord);
        producer.flush();
    }
}