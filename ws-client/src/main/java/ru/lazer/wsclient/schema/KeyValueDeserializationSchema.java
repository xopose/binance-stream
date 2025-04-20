package ru.lazer.wsclient.schema;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KeyValueDeserializationSchema implements KafkaRecordDeserializationSchema<Tuple2<String, String>> {

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<Tuple2<String, String>> out) {
        String key = record.key() != null ? new String(record.key()) : null;
        String value = record.value() != null ? new String(record.value()) : null;
        out.collect(Tuple2.of(key, value));
    }

    @Override
    public TypeInformation<Tuple2<String, String>> getProducedType() {
        return new TupleTypeInfo<>(
                TypeInformation.of(String.class),
                TypeInformation.of(String.class)
        );
    }
}
