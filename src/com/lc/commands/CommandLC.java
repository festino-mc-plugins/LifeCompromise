package com.lc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lc.LCPlayer;
import com.lc.LCPlayerList;
import com.lc.config.Config;
import com.lc.config.Config.Key;

public class CommandLC implements CommandExecutor, TabCompleter {
	
	private final Config config;
	private final LCPlayerList lcp_list;
	
	public CommandLC(Config config, LCPlayerList lcp_list) {
		this.config = config;
		this.lcp_list = lcp_list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				LCPlayer lcp = lcp_list.load((Player) sender);
				String state = lcp.getSoftLC() ? "on" : "off";
				sender.sendMessage("LC is " + state);
			}
			return true;
		}
		
		String option = args[0];
		if (args.length > 0) {
			if (option.equalsIgnoreCase("?")) {
				sender.sendMessage("HELP!");
			}
			if (sender instanceof Player) {
				LCPlayer lcp = lcp_list.load((Player) sender);
				// TODO messages
				if (option.equalsIgnoreCase("on")) {
					lcp.setSoftLC(true);
					sender.sendMessage("LC is on");
					return true;
				} else if (option.equalsIgnoreCase("off")) {
					lcp.setSoftLC(false);
					sender.sendMessage("LC is off");
					return true;
				}
			}
			
			if (option.equalsIgnoreCase("config")) {
				return onConfigCommand(sender, Arrays.copyOfRange(args, 1, args.length));
			}
			
			// reload (???), debug (removed)
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) {
			return list;
		}

		String option = args[0];
		if (args.length == 1) {
			String options[] = {"on", "off", "?"};
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
	
	public boolean onConfigCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		} else {
			String option = args[0];
			if (option.equalsIgnoreCase("reload")) {
				config.load();
				return true;
			}
			if (option.equalsIgnoreCase("save")) {
				config.save();
				return true;
			}
			if (option.equalsIgnoreCase("set")) {
				if (args.length == 1) {
					// TODO help (options/"follow autocompleting")
					sender.sendMessage("FOLLOW AUTOCOMPLETION");
					return false;
				} else if (args.length > 1) {
					String key_str = args[1];
					if (!Key.isValidKey(key_str)) {
						// TODO msg
						sender.sendMessage("INVALID KEY, PLEASE FOLLOW AUTOCOMPLETION");
						return false;
					}
					Key key = Key.getKey(key_str);
					if (args.length == 2) {
						// TODO help var type
						sender.sendMessage("VALID TYPE: " + key.getValueClass());
						return false;
					} else if (args.length > 2) {
						String value_str = args[2];
						Object value = key.validateValue(value_str);
						if (value == null) {
							// TODO help var type
							sender.sendMessage("INVALID TYPE, " + key.getValueClass() + " EXPECTED");
							return false;
						}
						// TODO msg OK
						sender.sendMessage("OK, set \"" + key + "\" to " + value);
						config.set(key, value);
						return true;
					}
				}
			}
			if (option.equalsIgnoreCase("get")) {
				if (args.length == 1) {
					// TODO help (options/"follow autocompleting")
					sender.sendMessage("FOLLOW AUTOCOMPLETION");
					return false;
				} else if (args.length > 1) {
					String key_str = args[1];
					if (!Key.isValidKey(key_str)) {
						// TODO msg
						sender.sendMessage("INVALID KEY, PLEASE FOLLOW AUTOCOMPLETION");
						return false;
					}
					Key key = Key.getKey(key_str);
					if (args.length == 2) {
						// TODO help var type
						sender.sendMessage("VALID TYPE: " + key.getValueClass());
						return false;
					}
					// TODO msg OK
					sender.sendMessage("OK, value of \"" + key + "\" is " + config.get(key).toString());
				}
			}
			
		}
		return false;
	}
	
	public List<String> onConfigComplete(CommandSender sender, String[] args) {
		List<String> list = new ArrayList<>();
		if (args.length == 0) {
			return list;
		}
		
		String option = args[0].toLowerCase();
		System.out.print(args.length + " " + args.toString());
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
