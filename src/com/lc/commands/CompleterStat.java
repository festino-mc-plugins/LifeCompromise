package com.lc.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CompleterStat implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) {
			return list;
		}

		String option = args[0];
		
		if (args.length == 1) {
			String options[] = {"?", "info"};
			String op_options[] = {"set", "add"};

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
		
		if (args.length == 1) {
			// ?
			// info [nick]
			
			// set [key]
			// add [key]
		}
		return list;
	}
}
