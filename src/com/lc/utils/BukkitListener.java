package com.lc.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.lc.Main;

/**
 * Listener for standard events fired by Bukkit.
 * <p>
 * This class detects when AuthMe is enabled or disabled and initializes or removes
 * {@link AuthMeHook the hook} accordingly.
 */
public class BukkitListener implements Listener {

    private final Main plugin;

    public BukkitListener(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if AuthMe has been enabled and hooks into it in such a case.
     *
     * @param event the plugin enable event to process
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPluginEnable(PluginEnableEvent event) {
        if ("AuthMe".equals(event.getPlugin().getName())) {
            plugin.registerAuthMeComponents();
        }
    }

    /**
     * Checks if AuthMe has been disabled and unhooks from it in such a case.
     *
     * @param event the plugin disable event to process
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent event) {
        if ("AuthMe".equals(event.getPlugin().getName())) {
            plugin.removeAuthMeHook();
        }
    }
}
