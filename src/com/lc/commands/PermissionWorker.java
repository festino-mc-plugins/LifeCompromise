package com.lc.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionWorker {
	
	public enum Result {
		ALLOW, DENY_PERM, DENY_NOPLAYER
	};
	
	public static Result canDispatch(CommandSender sender, String cmd, String[] args)
	{
		boolean is_op = sender.isOp();
		// + lc
		// + lc ?
		// + lc on/off
		// + lc output_ticks
		// + lc info
		// - lc config set/get/reload/save
		if (cmd.equalsIgnoreCase(CommandLC.LC_COMMAND))
		{
			if (args.length == 0)
				return allowPlayer(sender);

			String arg = args[0];
			if (arg.equalsIgnoreCase("?"))
				return Result.ALLOW;
			if (arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("off")) 
				return allowPlayer(sender);
			if (arg.equalsIgnoreCase("output_ticks"))
				return allowPlayer(sender);
			if (arg.equalsIgnoreCase("info"))
				return allowPlayer(sender);
			if (arg.equalsIgnoreCase("config") && is_op)
				return Result.ALLOW;
		}
		
		// + stat
		// + stat ?
		// + stat info
		// - stat add
		// - stat set
		if (cmd.equalsIgnoreCase(CommandStat.STAT_COMMAND))
		{
			if (args.length == 0)
				return allowPlayer(sender);

			String arg = args[0];
			if (arg.equalsIgnoreCase("?"))
				return Result.ALLOW;
			if (arg.equalsIgnoreCase("info"))
				return allowPlayer(sender);
			if (arg.equalsIgnoreCase("add") && is_op)
				return Result.ALLOW;
			if (arg.equalsIgnoreCase("set") && is_op)
				return Result.ALLOW;
		}
		
		return Result.DENY_PERM;
	}
	
	public static Result allowPlayer(CommandSender sender) {
		if (sender instanceof Player)
			return Result.ALLOW;
		else
			return Result.DENY_NOPLAYER;
	}
}
