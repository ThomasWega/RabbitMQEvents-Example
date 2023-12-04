package me.wega.testmq;

import com.rabbitmq.client.AMQP;
import me.wega.mq_events.rabbit.config.RabbitEventConfig;
import me.wega.mq_events.rabbit.config.RabbitEventConfigBuilder;
import me.wega.mq_events.rabbit.config.RabbitEventConfigFactory;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.UUID;

public class TestEventConfig implements RabbitEventConfigFactory<TestEvent> {

    @Override
    public @NotNull RabbitEventConfig<TestEvent> config() {
        return new RabbitEventConfigBuilder<TestEvent>()
                .exchangeName("event.test")
                .exchangeType(com.rabbitmq.client.BuiltinExchangeType.FANOUT)
                .exchangeRoutingKey("test.#")
                .properties(new AMQP.BasicProperties().builder()
                        .expiration("10000")
                        .build()
                )
                .toJson(event -> new JSONObject()
                        .put("uuid", event.getUUID())
                )
                .fromJson(jsonObject -> new TestEvent(UUID.fromString(jsonObject.getString("uuid"))))
                .build();
    }
}
