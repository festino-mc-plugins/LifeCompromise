package com.lc;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lc.config.Config;
import com.lc.config.Config.Key;

public class UranHandler implements Listener {

	Config config;
	private static final int MAX_LVL = 3;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()) return;
		if(event.getBlock().getType().equals(Material.IRON_ORE)) //new ore (test???)
		{
			if ((Boolean) config.get(Key.MODULE_URAN_DROP)) {
				double rand = UtilsRandom.nextDouble();
				double rates[] = {config.get(Key.URAN_1_RATE), config.get(Key.URAN_2_RATE), config.get(Key.URAN_3_RATE)};
				
				for (int i = 0; i < MAX_LVL; i++) {
					if (rand >= rates[i]) {
						rand -= rates[i];
						continue;
					}
					
					int lvl = i + 1;

					ItemStack is = new ItemStack(Material.IRON_ORE, 1);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName("Урановая руда");
					im.addEnchant(Enchantment.ARROW_DAMAGE, lvl, true);
					is.setItemMeta(im);
	                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), is);
	                event.setCancelled(true);
	                event.getBlock().setType(Material.AIR);
					/*for(ItemStack is : event.getBlock().getDrops())
					{
						ItemMeta IM = is.getItemMeta();
						IM.setDisplayName("Урановая руда");
						IM.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
						is.setItemMeta(IM);
					}*/
	                break;
				}
			}
		}
	}
}
