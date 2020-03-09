package com.lc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lc.commands.PermissionWorker.Result;
import com.lc.config.Config;
import com.lc.config.Config.Key;

public class CompleterLC implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) {
			return list;
		}

		String option = args[0];
		
		if (args.length == 1) {
			String options[] = {"on", "off", "?", "info", "output_ticks", "config", "forceON", "forceOFF", "forceNONE"};

			for (String op : options)
				if (op.startsWith(option)) {
					String completed_args[] = args.clone();
					completed_args[0] = op;
					if (PermissionWorker.canDispatch(sender, cmd.getName(), completed_args) == Result.ALLOW)
						list.add(op);
				}
			
			return list;
		}
		
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("config")) {
				return onConfigComplete(sender, Arrays.copyOfRange(args, 1, args.length));
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("output_ticks")) {
					list.add("" + Config.Key.DEFAULT_OUTPUT_TICKS.getDefault());
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("forceON") || args[0].equalsIgnoreCase("forceOFF") || args[0].equalsIgnoreCase("forceNONE")) {
					String[] trunc_args = Arrays.copyOfRange(args, 0, 1);
					if (PermissionWorker.canDispatch(sender, cmd.getName(), trunc_args) == Result.ALLOW)
						for (Player p : Bukkit.getOnlinePlayers())
							list.add(p.getName());
				}
			}
		}
		
		return list;
	}
	
	public List<String> onConfigComplete(CommandSender sender, String[] args) {
		List<String> list = new ArrayList<>();
		if (args.length == 0) {
			return list;
		}
		
		String option = args[0].toLowerCase();
		if (args.length == 1) {
			String options[] = {"reload", "save", "set", "get"};
			for (String op : options)
				if (op.startsWith(option))
					list.add(op);
			return list;
		}

		String key_arg = args[1].toLowerCase();
		if (args.length == 2) {
			// reload
			// save
			// set [...], get [...]
			if (option.equalsIgnoreCase("set") || option.equalsIgnoreCase("get")) {
				List<String> keys = Key.getKeys();
				for (String k : keys)
					if (k.toLowerCase().contains(key_arg))
						list.add(k);
				return list;
			}
		}
		
		if (args.length == 3) {
			// reload
			// save
			// get ...
			// set ... [...]
			if (option.equalsIgnoreCase("set")) {
				if (!Key.isValidKey(key_arg)) {
					return list;
				}
				
				Key key = Key.getKey(key_arg);
				Class<?> clazz = key.getValueClass();
				if (clazz == Boolean.class) {
					list.add("true");
					list.add("false");
				} else {
					list.add(key.getDefault().toString());
				}
			}
		}
		return list;
	}
}
