package com.lc;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lc.commands.CommandLC;
import com.lc.commands.CommandStat;
import com.lc.commands.CompleterLC;
import com.lc.commands.CompleterStat;
import com.lc.config.Config;
import com.lc.config.PlayerFileManager;
import com.lc.utils.AuthMeHook;
import com.lc.utils.AuthMeListener;

public class Main extends JavaPlugin {

	public final Config config = new Config(this);
	
	private final LCPlayerList lcp_list = new LCPlayerList(
			new PlayerFileManager(getDataFolder().getAbsolutePath(), getLogger(), this));
	private PlayerHandler main_handler = new PlayerHandler(lcp_list, config, getServer().getWorlds());
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
		
		UranHandler uran_handler = new UranHandler(config);
		getServer().getPluginManager().registerEvents(uran_handler, this);
		
		CommandLC cmd_lc = new CommandLC(config, lcp_list);
		CompleterLC completer_lc = new CompleterLC();
		CommandStat cmd_stat = new CommandStat(config, lcp_list, main_handler);
		CompleterStat completer_stat = new CompleterStat();
		
		getCommand(CommandLC.LC_COMMAND).setExecutor(cmd_lc);
		getCommand(CommandLC.LC_COMMAND).setTabCompleter(completer_lc);
		
		getCommand(CommandStat.STAT_COMMAND).setExecutor(cmd_stat);
		getCommand(CommandStat.STAT_COMMAND).setTabCompleter(completer_stat);

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
