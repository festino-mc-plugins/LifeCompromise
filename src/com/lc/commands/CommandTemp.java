package com.lc.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.lc.LCPlayerList;
import com.lc.config.Config;

public class CommandTemp implements CommandExecutor, TabCompleter {
	
	private final Config config;
	private final LCPlayerList lcp_list;
	
	public CommandTemp(Config config, LCPlayerList lcp_list) {
		this.config = config;
		this.lcp_list = lcp_list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0) {
			// help
		}
		else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("info")) {
				if (args.length > 1) {
					// other players
				}
				// your dataa
			}
			
			if (args[0].equalsIgnoreCase("set")) {
				
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		return null;
	}
	
}
