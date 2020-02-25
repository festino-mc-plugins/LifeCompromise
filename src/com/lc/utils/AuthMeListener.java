package com.lc.utils;

import fr.xephi.authme.events.LoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.lc.LCPlayer;
import com.lc.LCPlayerList;

/**
 * Listener for AuthMe events.
 */
public class AuthMeListener implements Listener {
	
	LCPlayerList lcplist;
	
	public AuthMeListener(LCPlayerList lcplist) {
		this.lcplist = lcplist;
	}

    /**
     * Catches AuthMe's {@link LoginEvent} (thrown after player successfully logged in)
     * and makes the player write some greeting to chat.
     *
     * @param event the event to process
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(LoginEvent event) {
    	Player p = event.getPlayer();
    	LCPlayer lcp = lcplist.load(p);
    	if (lcp != null) {
    		lcp.setLoggedIn(true);
    	}
    }
}
