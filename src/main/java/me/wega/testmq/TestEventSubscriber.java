package me.wega.testmq;

import me.wega.mq_events.event.EventSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestEventSubscriber implements EventSubscriber<TestEvent> {
    @Override
    public void onEvent(@NotNull TestEvent testEvent) {
        UUID uuid = testEvent.getUUID();
        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage("RABBIT EVENT - " + uuid);
    }
}
