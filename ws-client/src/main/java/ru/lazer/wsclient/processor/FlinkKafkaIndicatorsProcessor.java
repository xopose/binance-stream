package ru.lazer.wsclient.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.springframework.stereotype.Component;
import ru.lazer.wsclient.calculator.MacdCalculator;
import ru.lazer.wsclient.calculator.RsiCalculator;
import ru.lazer.wsclient.pojo.AggTrade;
import ru.lazer.wsclient.pojo.AggTradeFlinkPojo;
import ru.lazer.wsclient.pojo.JsonToAggTradePojoMapper;
import ru.lazer.wsclient.schema.KeyValueDeserializationSchema;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class FlinkKafkaIndicatorsProcessor {

    private static final String inputTopic = "binance";
    private static final String outputTopic = "trade_info";
    private static final String jobTitle = "MathIndicators";
    private static final String bootstrapServers = "host.docker.internal:29092";

    @PostConstruct
    public void startJob() {
        new Thread(() -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
                env.setParallelism(4);
                env.getConfig().disableGenericTypes();

                KafkaSource<Tuple2<String, String>> kafkaSource = KafkaSource.<Tuple2<String, String>>builder()
                        .setBootstrapServers(bootstrapServers)
                        .setTopics(inputTopic)
                        .setGroupId("flink-consumer-group")
                        .setStartingOffsets(OffsetsInitializer.earliest())
                        .setDeserializer(new KeyValueDeserializationSchema())
                        .build();

                KafkaSink<Tuple2<String, String>> kafkaSink = KafkaSink.<Tuple2<String, String>>builder()
                        .setBootstrapServers(bootstrapServers)
                        .setRecordSerializer(
                                KafkaRecordSerializationSchema.<Tuple2<String, String>>builder()
                                        .setTopic(outputTopic)
                                        .setKeySerializationSchema(tuple -> tuple.f0.getBytes(StandardCharsets.UTF_8))
                                        .setValueSerializationSchema(tuple -> tuple.f1.getBytes(StandardCharsets.UTF_8))
                                        .build()
                        )
                        .build();

                DataStream<AggTradeFlinkPojo> aggTrades = env
                        .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source")
                        .filter(record -> "bnbusdt@aggTrade".equals(record.f0))
                        .map(record -> objectMapper.readValue(record.f1, AggTradeFlinkPojo.class))
                        .returns(TypeInformation.of(AggTradeFlinkPojo.class))
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy
                                        .<AggTradeFlinkPojo>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                        .withTimestampAssigner((event, ts) -> event.getETime())
                        );


                aggTrades
                        .keyBy(AggTradeFlinkPojo::getS)
                        .window(SlidingProcessingTimeWindows.of(Time.seconds(30), Time.seconds(15)))
                        .process(new RsiCalculator())
                        .sinkTo(kafkaSink);
                aggTrades
                        .keyBy(AggTradeFlinkPojo::getS)
                        .window(SlidingProcessingTimeWindows.of(Time.seconds(30), Time.seconds(15)))
                        .process(new MacdCalculator())
                        .sinkTo(kafkaSink);
                env.execute(jobTitle);

            } catch (Exception e) {
                System.err.println("Ошибка при запуске Flink job: " + e.getMessage());
                e.printStackTrace();
            }
        }, "flink-job-thread").start();
    }
}