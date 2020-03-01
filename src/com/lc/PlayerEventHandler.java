package com.lc;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lc.utils.AuthMeHook;
import com.lc.utils.Utils;

public class PlayerEventHandler implements Listener {
	
	private static final int TITLE_COOLDOWN = 40;
	private static final int CAULDRON_N = 3;
	private static final double CAULDRON_WATER = 30 * CAULDRON_N;
	private static final int CAULDRON_COOLDOWN = 5;
	
	private final DrinkPair drinkables[] = {
			new DrinkPair(Material.MILK_BUCKET, 100),
			new DrinkPair(Material.POTION, 30) };
	
	private final LCPlayerList lcp_list;
	private final AuthMeHook hook;
	
	public PlayerEventHandler(LCPlayerList lcp_list, AuthMeHook hook) {
		this.lcp_list = lcp_list;
		this.hook = hook;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		LCPlayer lcp = lcp_list.load(event.getPlayer());
		if (hook.isHookActive()) {
			lcp.setLoggedIn(false);
		}
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		lcp_list.unload(event.getPlayer());
    }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		LCPlayer lcp = lcp_list.load(event.getEntity());
		lcp.reset();
	}
	
	@EventHandler
	public void onPlayerResurrect(EntityResurrectEvent event)
	{
		if (event.isCancelled()) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		
		LCPlayer lcp = lcp_list.load((Player) event.getEntity());
		lcp.reset();
	}
	

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event)
	{
		if (event.isCancelled()) return;
		LCPlayer lcp = lcp_list.load(event.getPlayer());
		lcp.setTitleCooldown(0);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.isCancelled()) return;
		if (!event.hasBlock()) return;

		LCPlayer lcp = lcp_list.load(event.getPlayer());
		Block b = event.getClickedBlock();
		if (b.getType().toString().toUpperCase().contains("BED")) {
			lcp.setTitleCooldown(TITLE_COOLDOWN);
		}
		// cauldron drink
		if (lcp.canDrinkCauldron() && Utils.getCauldronLevel(b) > 0) {
			lcp.addThirst(CAULDRON_WATER / CAULDRON_N);
			lcp.setCauldronTicks(CAULDRON_COOLDOWN);
			Utils.decreaseCauldronLevel(b);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onItemConsume(PlayerItemConsumeEvent event)
	{
		LCPlayer lcp = lcp_list.load(event.getPlayer());
		// drink
		for (DrinkPair drink : drinkables)
			if (event.getItem().getType() == drink.drinkable) {
				lcp.addThirst(drink.thirst);
			}
		
	}
	
	private class DrinkPair {
		Material drinkable;
		double thirst;
		public DrinkPair(Material drinkable, double thirst) {
			this.drinkable = drinkable;
			this.thirst = thirst;
		}
	}
}
