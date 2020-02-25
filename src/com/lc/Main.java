package com.lc;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lc.commands.CommandLC;
import com.lc.commands.CommandTemp;
import com.lc.config.Config;
import com.lc.config.PlayerFileManager;
import com.lc.utils.AuthMeHook;
import com.lc.utils.AuthMeListener;

public class Main extends JavaPlugin {

	public final Config config = new Config(this);
	
	private final LCPlayerList lcp_list = new LCPlayerList(
			new PlayerFileManager(getDataFolder().getAbsolutePath(), getLogger(), this));
	private PlayerHandler main_handler = new PlayerHandler(lcp_list, config);
	// AuthMe integration
    private Listener authMeListener;
    private AuthMeHook authMeHook;
	
	public void onEnable()
	{
        final PluginManager pluginManager = getServer().getPluginManager();
    	authMeHook = new AuthMeHook();
        if (pluginManager.isPluginEnabled("AuthMe")) {
            registerAuthMeComponents();
        }
        
		PlayerEventHandler ehandler = new PlayerEventHandler(lcp_list, authMeHook);
		getServer().getPluginManager().registerEvents(ehandler, this);
		
		CommandLC cmd_lc = new CommandLC(config, lcp_list);
		CommandTemp cmd_temp = new CommandTemp(config, lcp_list);
		
		getCommand("lc").setExecutor(cmd_lc);
		getCommand("lc").setTabCompleter(cmd_lc);
		
		getCommand("temp").setExecutor(cmd_temp);
		getCommand("temp").setTabCompleter(cmd_temp);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, main_handler, 0L, 1L);
	}
	
	public void onDisable()
	{
		lcp_list.saveAll();
	}
	
	public PlayerHandler getPlayerHandler() {
		return main_handler;
	}
	
    public void registerAuthMeComponents() {
    	if (authMeHook == null) {
            authMeHook.initializeAuthMeHook();
    	}
        if (authMeListener == null) {
            authMeListener = new AuthMeListener(lcp_list);
            getServer().getPluginManager().registerEvents(authMeListener, this);
        }
    }
    
    public void removeAuthMeHook() {
        authMeHook.removeAuthMeHook();
        authMeHook = null;
        authMeListener = null;
    }
}
