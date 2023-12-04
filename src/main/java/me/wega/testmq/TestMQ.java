package me.wega.testmq;

import me.wega.mq_events.Main;
import me.wega.mq_events.TestEvent;
import me.wega.mq_events.TestEventConfig;
import me.wega.mq_events.event.EventSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public final class TestMQ extends JavaPlugin implements Listener, EventSubscriber<TestEvent> {
    private final Main main = new Main();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.main.initialize();
        try {
            this.main.rabbitEventBus.subscribe(TestEvent.class, new TestEventConfig().config(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            this.main.rabbitManager.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    private void onPlayerMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        player.sendMessage("BUKKIT EVENT - " + uuid);
        try {
            this.main.rabbitEventManager.publish(new TestEvent(uuid), new TestEventConfig().config());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(@NotNull TestEvent testEvent) {
        UUID uuid = testEvent.getUUID();
        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage("RABBIT EVENT - " + uuid);
    }
}
