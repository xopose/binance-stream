package ru.lazer.wsclient.pojo;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToAggTradePojoMapper implements MapFunction<Tuple2<String, String>, AggTradeFlinkPojo> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AggTradeFlinkPojo map(Tuple2<String, String> record) throws Exception {
        return objectMapper.readValue(record.f1, AggTradeFlinkPojo.class);
    }
}