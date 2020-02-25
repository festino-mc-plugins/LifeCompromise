package com.lc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.lc.config.PlayerFileManager;

public class LCPlayerList {
	private final PlayerFileManager file_manager; 
	private List<LCPlayer> players = new ArrayList<>();

	public LCPlayerList(PlayerFileManager file_manager) {
		this.file_manager = file_manager;
	}
	
	public LCPlayer[] getSnapshot() {
		return players.toArray(new LCPlayer[0]);
	}
	
	public LCPlayer load(Player p) {
		LCPlayer lcp = get(p);
		if (lcp != null)
			return lcp;
		
		lcp = file_manager.load(p);
		if (lcp != null)
			players.add(lcp);
		return lcp;
	}
	
	public boolean isLoaded(Player p) {
		return get(p) != null;
	}
	
	private LCPlayer get(Player p) {
		for (LCPlayer lcp : players)
			if (lcp.p == p)
				return lcp;
		
		return null;
	}
	
	public boolean unload(Player p) {
		if (!p.isOnline())
			return false;
		
		return force_unload(p);
	}
	
	private boolean force_unload(Player p) {
		for (int i = 0; i < players.size(); i++) {
			LCPlayer lcp = players.get(i);
			if (lcp.p == p) {
				save(lcp);
				players.remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	public void save(LCPlayer lcp) {
		file_manager.save(lcp);
	}
	
	public void saveAll() {
		for (LCPlayer lcp : players) {
			save(lcp);
		}
	}
	
	public void onDisable() {
		saveAll();
	}
}
