package hazelcast.classcast;

import io.confluent.kafka.serializers.AbstractKafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord;

import java.io.Serializable;
import java.util.Map;

public class AvroDeserializer extends AbstractKafkaAvroDeserializer implements Serializable {
    private boolean isKey;
    private Map<String, ?> configs;

    public AvroDeserializer() {
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        this.isKey = isKey;
        this.configure(new KafkaAvroDeserializerConfig(configs));
        this.configs = configs;
    }

    public <T extends GenericRecord> T deserialize(byte[] bytes, T record) {
        if (this.schemaRegistry == null) {
            this.configure(new KafkaAvroDeserializerConfig(configs));
        }
        // This is where the Class cast exception happens.
        return (T) this.deserialize(bytes);
    }
}
