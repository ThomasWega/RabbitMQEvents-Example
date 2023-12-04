package me.wega.testmq;

import me.wega.mq_events.rabbit.RabbitEventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.UUID;

public class TestEventBukkitListener implements Listener {
    private final RabbitEventManager rabbitEventManager;

    public TestEventBukkitListener(RabbitEventManager rabbitEventManager) {
        this.rabbitEventManager = rabbitEventManager;
    }

    @EventHandler
    private void onPlayerMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        player.sendMessage("BUKKIT EVENT - " + uuid);
        try {
            this.rabbitEventManager.publish(new TestEvent(uuid), new TestEventConfig().config());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
