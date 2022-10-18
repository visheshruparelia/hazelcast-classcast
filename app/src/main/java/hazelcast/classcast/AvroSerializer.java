package hazelcast.classcast;

import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.avro.AvroSchemaUtils;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.generic.GenericRecord;

import java.io.Serializable;
import java.util.Map;

public class AvroSerializer extends AbstractKafkaAvroSerializer implements Serializable {
    private boolean isKey;
    private Map<String, ?> configs;

    public AvroSerializer() {
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        this.isKey = isKey;
        this.configure(new KafkaAvroSerializerConfig(configs));
        this.configs = configs;
    }

    public <T extends GenericRecord> byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        } else {
            if (this.schemaRegistry == null) {
                this.configure(new KafkaAvroSerializerConfig(configs));
            }
            AvroSchema schema = new AvroSchema(AvroSchemaUtils.getSchema(data, this.useSchemaReflection, this.avroReflectionAllowNull, this.removeJavaProperties));
            return this.serializeImpl(this.getSubjectName(topic, this.isKey, data, schema), data, schema);
        }
    }
}
