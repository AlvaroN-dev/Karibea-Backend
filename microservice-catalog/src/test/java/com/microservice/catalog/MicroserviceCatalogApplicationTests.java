package com.microservice.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        topics = {"products", "inventory.updated", "inventory.low-stock", "inventory.out-of-stock"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
class MicroserviceCatalogApplicationTests {

	@Test
	void contextLoads() {
	}

}
