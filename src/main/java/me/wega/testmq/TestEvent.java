package me.wega.testmq;

import me.wega.mq_events.rabbit.RabbitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestEvent implements RabbitEvent {
    private final @NotNull UUID uuid;

    public TestEvent(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }
}
