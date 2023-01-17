package com.ngenebio.msa.chart.test.support;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

public interface TestContainersReactiveMongoDBTest {
    ReactiveMongoTemplate getReactiveMongoTemplate();
}
