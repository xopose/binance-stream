package ru.lazer.wsclient.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import ru.lazer.wsclient.pojo.AggTradeFlinkPojo;

import java.util.*;

public class MacdCalculator extends ProcessWindowFunction<AggTradeFlinkPojo, Tuple2<String, String>, String, TimeWindow> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(String symbol, Context context, Iterable<AggTradeFlinkPojo> elements, Collector<Tuple2<String, String>> out) {
        List<AggTradeFlinkPojo> trades = new ArrayList<>();
        elements.forEach(trades::add);

        trades.sort(Comparator.comparingLong(AggTradeFlinkPojo::getT));

        List<Double> prices = new ArrayList<>();
        for (AggTradeFlinkPojo trade : trades) {
            prices.add(trade.getP());
        }

        if (prices.size() < 26) {
            return; // недостаточно данных для EMA26
        }

        double ema12 = calculateEMA(prices, 12);
        double ema26 = calculateEMA(prices, 26);
        double macd = ema12 - ema26;

        long timestamp = context.window().getEnd();

        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("timestamp", timestamp);
            jsonMap.put("value", macd);

            String json = objectMapper.writeValueAsString(jsonMap);
            out.collect(Tuple2.of("MACD", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateEMA(List<Double> prices, int period) {
        double k = 2.0 / (period + 1);
        double ema = prices.get(0);

        for (int i = 1; i < prices.size(); i++) {
            ema = prices.get(i) * k + ema * (1 - k);
        }
        return ema;
    }
}
