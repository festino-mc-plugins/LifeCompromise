package com.lc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.lc.config.Config.Key;

public class CompleterLC implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) {
			return list;
		}

		String option = args[0];
		if (args.length == 1) {
			String options[] = {"on", "off", "?", "info", "output_ticks"};
			String op_options[] = {"config"};
			
			for (String op : options)
				if (op.startsWith(option))
					list.add(op);
			
			if (sender.isOp()) { // TODO permission system
				for (String op : op_options)
					if (op.startsWith(option))
						list.add(op);
			}
			return list;
		}
		
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("config")) {
				return onConfigComplete(sender, Arrays.copyOfRange(args, 1, args.length));
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
