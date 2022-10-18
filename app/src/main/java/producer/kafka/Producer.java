package producer.kafka;

import demo.Emp;
import hazelcast.classcast.AvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS;
import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static io.confluent.kafka.serializers.KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG;
import static io.confluent.kafka.serializers.KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG;

public class Producer {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
         String TOPIC = "testTopic";

         String BOOTSTRAP_SERVERS = "localhost:9094";
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ClassCastDataProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                ByteArraySerializer.class.getName());
        KafkaProducer kafkaProducer  = new KafkaProducer(props);
        AvroSerializer serializer = new AvroSerializer();
        serializer.configure(Map.of(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081",
                AUTO_REGISTER_SCHEMAS, true,
                SPECIFIC_AVRO_READER_CONFIG, true,
                AVRO_REMOVE_JAVA_PROPS_CONFIG, true), false);

        int i = 0;

        while(true) {
            Emp emp = Emp.newBuilder()
                    .setName("testName")
                    .setAddress("testAddress")
                    .setId(1)
                    .setSalary(0)
                    .setAge(25)
                    .build();
            i++;
            byte[] bytes = serializer.serialize(TOPIC, emp);
            Thread.sleep(2000);
            kafkaProducer.send(
                    new ProducerRecord(TOPIC, bytes)
             ).get();
            System.out.println( i + " packets written.");
        }
    }

}
