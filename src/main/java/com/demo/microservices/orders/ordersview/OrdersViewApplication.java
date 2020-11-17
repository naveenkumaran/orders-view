package com.demo.microservices.orders.ordersview;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableKafkaStreams
public class OrdersViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersViewApplication.class, args);
	}


}
@Component
class OrderView {

	public void orderView(StreamsBuilder builder) {
		builder.table("orders"
				, Consumed.with(Serdes.Integer(), Serdes.Integer())
				, Materialized.as("orders-store"));
	}
}

@Component
@RequiredArgsConstructor
class Producer {
	KafkaTemplate<Integer, String> template;

	@EventListener(ApplicationStartedEvent.class)
	public void produce() {
		template.send("orders", "moto-gp");
		template.send("orders", "iphone-x");
	}
}

@RestController
@RequiredArgsConstructor
class OrdersController {

	private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

	@GetMapping("/orders/{id}")
	public String get(@PathVariable final Integer id ) {
		var keyValueStore = streamsBuilderFactoryBean.getKafkaStreams().store(StoreQueryParameters.fromNameAndType("orders-store", QueryableStoreTypes.keyValueStore()));
		return (String)(keyValueStore.get(Integer.valueOf(id)));
	}
}
