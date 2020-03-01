package com.lc.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.lc.commands.PermissionWorker.Result;
import com.lc.config.Config;
import com.lc.utils.UtilsThirst;

public class CompleterStat implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) {
			return list;
		}

		String option = args[0];

		if (args.length == 1) {
			String options[] = {"?", "info", "set", "add"};

			// [?]
			// [info]
			
			// [set]
			// [add]
			for (String op : options) {
				if (op.startsWith(option)) {
					String completed_args[] = args.clone();
					completed_args[0] = op;
					if (PermissionWorker.canDispatch(sender, cmd.getName(), completed_args) == Result.ALLOW)
						list.add(op);
				}
			}
				
			return list;
		}
		
		if (args.length == 2) {
			option = args[0];
			// ?
			// info [nick]
			if (option.equalsIgnoreCase("info")) {
				
			}
			// set [key]
			// add [key]
			if (option.equalsIgnoreCase("set") || option.equalsIgnoreCase("add")) {
				list.add("temp");
				list.add("thirst");
			}
		}
		
		if (args.length == 3) {
			String key = args[1];
			// set [key]
			// add [key]
			if (key.equalsIgnoreCase("temp")) {
				if (option.equalsIgnoreCase("set")) {
					double skin_temp = (Double) Config.Key.SKIN_T_DEFAULT.getDefault();
					list.add("" + skin_temp);
				}
			}
			if (key.equalsIgnoreCase("thirst")) {
				if (option.equalsIgnoreCase("set")) {
					double max_thirst = UtilsThirst.MAX_THIRST;
					list.add("" + max_thirst);
				}
			}
		}
		return list;
	}
}
