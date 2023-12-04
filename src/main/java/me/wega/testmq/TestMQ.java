package me.wega.testmq;

import me.wega.mq_events.event.EventBus;
import me.wega.mq_events.rabbit.RabbitEvent;
import me.wega.mq_events.rabbit.RabbitEventBus;
import me.wega.mq_events.rabbit.RabbitEventManager;
import me.wega.mq_events.rabbit.RabbitManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class TestMQ extends JavaPlugin {

    @Override
    public void onEnable() {
        this.initialize();
        try {
            this.rabbitEventBus.subscribe(TestEvent.class, new TestEventConfig().config(), new TestEventSubscriber());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getServer().getPluginManager().registerEvents(new TestEventBukkitListener(rabbitEventManager), this);
    }

    public RabbitManager rabbitManager = new RabbitManager("guest", "guest", "127.0.0.1", 5672);
    public RabbitEventManager rabbitEventManager;
    public RabbitEventBus<RabbitEvent> rabbitEventBus;

    public void initialize() {
        this.rabbitManager.onChannelInitialized(() -> {
            this.rabbitEventManager = new RabbitEventManager("events", this.rabbitManager.getChannel());
            this.rabbitEventBus = EventBus.rabbitEventBus(this.rabbitEventManager, RabbitEvent.class);
        }, 10, 250);
    }

    @Override
    public void onDisable() {
        try {
            this.rabbitManager.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
