package com.demo.microservices.orders.ordersview;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderViewTest {

    @Test
    void shouldCreateMaterializedView() {
        StreamsBuilder builder = new StreamsBuilder();
        new OrderView().orderView(builder);

        Properties properties = new Properties();
        properties.putAll(
                Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "orders-view"
                , StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092"));;

        TopologyTestDriver driver = new TopologyTestDriver(builder.build(), properties);

        var orders = driver.createInputTopic("orders", Serdes.Integer().serializer(), Serdes.String().serializer());

        orders.pipeInput(1, "moto-gp");
        orders.pipeInput(2, "iphone-X");
        orders.pipeInput(3, "ipod-1");

        var keyValueStore = driver.getKeyValueStore("orders-store");

        assertEquals("moto-gp", keyValueStore.get(1) );
    }
}
