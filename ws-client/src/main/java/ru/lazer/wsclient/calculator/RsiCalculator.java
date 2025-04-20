package ru.lazer.wsclient.calculator;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import ru.lazer.wsclient.pojo.AggTradeFlinkPojo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RsiCalculator extends ProcessWindowFunction<AggTradeFlinkPojo, Tuple2<String, String>, String, TimeWindow> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(String symbol, Context context, Iterable<AggTradeFlinkPojo> elements, Collector<Tuple2<String, String>> out) {
        List<Double> prices = new ArrayList<>();
        for (AggTradeFlinkPojo trade : elements) {
            prices.add(trade.getP());
        }

        if (prices.size() < 2) return;

        double gain = 0, loss = 0;
        for (int i = 1; i < prices.size(); i++) {
            double delta = prices.get(i) - prices.get(i - 1);
            if (delta > 0) gain += delta;
            else loss -= delta;
        }

        double avgGain = gain / 14;
        double avgLoss = loss / 14;

        double rs = avgLoss == 0 ? 100 : avgGain / avgLoss;
        double rsi = 100 - (100 / (1 + rs));

        long timestamp = context.window().getEnd();

        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("timestamp", timestamp);
            jsonMap.put("value", rsi);

            String json = objectMapper.writeValueAsString(jsonMap);
            out.collect(Tuple2.of("RSI", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
