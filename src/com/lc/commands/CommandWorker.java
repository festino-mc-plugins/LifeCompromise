package com.lc.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.lc.old.Config;
import com.lc.old.LCPlayer;

import net.md_5.bungee.api.ChatColor;

public class CommandWorker implements Listener {

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("lc"))
		{
			if(args.length == 0)
			{
				sender.sendMessage(ChatColor.GRAY+"LifeCompromise commands:");
				if(sender instanceof Player)
					sender.sendMessage(ChatColor.GRAY+"   \"/lc <on/off>\" or \"/lc <1/0>\" - switchs plugin for you.");
				else
					sender.sendMessage(ChatColor.GRAY+"   \"/lc <on/off>\" or \"/lc <1/0>\" - switchs plugin for server.");
				sender.sendMessage(ChatColor.GRAY+"   \"/lc debug\" - shows functions execution time, that helps with troubleshooting.");
				sender.sendMessage(ChatColor.GRAY+"   \"/temp\" - shows your plugin parameters.");
				sender.sendMessage(ChatColor.GRAY+"   \"/temp d\" - shows your advanced plugin parameters.");
				if(!sender.isOp()) return true;
				sender.sendMessage(ChatColor.GRAY+"Admin commands:");
				sender.sendMessage(ChatColor.GRAY+"   \"lc reload\" - reloads config");
				sender.sendMessage(ChatColor.GRAY+"   \"temp set <value>\"");
				sender.sendMessage(ChatColor.GRAY+"   \"temp add <value\"");
				return true;
			}
			if(args[0].equalsIgnoreCase("reload") && sender.isOp())
			{
				reloadConfig();
				Config.loadConfig(getConfig(), this);
				sender.sendMessage("Config reloaded.");
				return true;
			}
			if(args[0].equalsIgnoreCase("debug"))
			{
				for(int i=0;i < ExecutionTimes.length;i++)
					sender.sendMessage(ExecutingFunctionName[i]+ ": " + ExecutionAllTime[i]/ExecutionTimes[i]);
				return true;
			}
			if(args[0].equalsIgnoreCase("conf") && sender.isOp())
			{
				if(args.length == 1)
				{
					
				}
				if(args.length == 2)
				{
					if(args[1].equalsIgnoreCase("on")) {
						Config.setLCon(this, true);
						sender.sendMessage("LifeCompromise turned on.");
						return true;
					}
					if(args[1].equalsIgnoreCase("off")) {
						Config.setLCon(this, false);
						sender.sendMessage("LifeCompromise turned off.");
						return true;
					}
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("set"))
			{
				if(args.length == 1)
				{
					
				}
				if(args.length == 2)
				{
					try {
						PlayerList.get(find(PlayerList, ((Player)sender).getUniqueId())).maxOutputTicks = Integer.parseInt(args[1]);
						return true;
					} catch (Exception e) {
						sender.sendMessage("Введите целое число тиков.");
						return false;
					}
				}
				if(args.length == 3)
				{
					args[1] = args[1].toLowerCase();
					if(args[1].contains("output") && args[1].contains("ticks")) {
						try {
							PlayerList.get(find(PlayerList, ((Player)sender).getUniqueId())).maxOutputTicks = Integer.parseInt(args[2]);
							return true;
						} catch (Exception e) {
							sender.sendMessage("Введите целое число тиков.");
							return false;
						}
					}
				}
				return true;
			}
			if( ( args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("1") ))
			{
				if(args.length == 1 && sender instanceof Player)
				{
					UUID tofind = ((Player)sender).getUniqueId();
					for(LCPlayer lcpp : PlayerList)
						if(lcpp.Tid != null && lcpp.Tid == tofind)
								lcpp.isOnLC = true;
					sender.sendMessage("LifeCompromise turned on for you.");
					return true;
				}
				else if(args.length == 1 && sender.isOp())
				{
					Config.setLCon(this, true);
					sender.sendMessage("LifeCompromise is turned on.");
					return true;
				}
				if(args.length == 2 && sender.isOp())
				{
					if(!getServer().getPlayer(args[1]).isOnline())
					{
						sender.sendMessage("Player "+args[1]+" is not online.");
						return false;
					}
					UUID tofind = getServer().getPlayer(args[1]).getUniqueId();
					for(LCPlayer lcpp : PlayerList)
						if(lcpp.Tid != null && lcpp.Tid == tofind)
								lcpp.isOnLC = true;
					sender.sendMessage("LifeCompromise is turned on for player "+args[1]+".");
					return true;
				}
			}
			if( ( args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("0") ))
			{
				if(args.length == 1 && sender instanceof Player)
				{
					UUID tofind = ((Player)sender).getUniqueId();
					for(LCPlayer lcpp : PlayerList)
						if(lcpp.Tid != null && lcpp.Tid == tofind)
							if(lcpp.TDebuff == 0 && lcpp.ThirstDebuff == 0) {
								lcpp.isOnLC = false;
								lcpp.normalize();
							}
					sender.sendMessage("LifeCompromise is turned off for you.");
					return true;
				}
				else if(args.length == 1 && sender.isOp())
				{
					Config.setLCon(this, false);
					sender.sendMessage("LifeCompromise is turned off.");
					return true;
				}
				if(args.length == 2 && sender.isOp())
				{
					if(!getServer().getPlayer(args[1]).isOnline())
					{
						sender.sendMessage("Player "+args[1]+" is not online.");
						return false;
					}
					UUID tofind = getServer().getPlayer(args[1]).getUniqueId();
					for(LCPlayer lcpp : PlayerList)
						if(lcpp.Tid != null && lcpp.Tid == tofind)
							{
								lcpp.isOnLC = false;
								lcpp.normalize();
							}
					sender.sendMessage("LifeCompromise turned off for player "+args[1]+".");
					return true;
				}
				sender.sendMessage(ChatColor.RED + "Usage: /lc <on/off> [PlayerName]");
			}
		}
		if(cmd.getName().equalsIgnoreCase("temp"))
		{
			// /temp
			if(args.length == 0)
			{
				int currentPlayer = find(PlayerList,((Player)sender).getUniqueId());
				if(sender instanceof Player && currentPlayer >= 0)
				{
					sender.sendMessage(ChatColor.GREEN + "Твои данные:");
					Location l = ((Player)sender).getLocation();
					l.setY(l.getY()+1);
					sender.sendMessage(ChatColor.GREEN + "  Очков жажды:         " + (double)Math.round(PlayerList.get(currentPlayer).Thirst*100)/100);
					sender.sendMessage(ChatColor.GREEN + "  Температура тела:    " + (double)Math.round(PlayerList.get(currentPlayer).T*100)/100+"°C."); //ms
					sender.sendMessage(ChatColor.GREEN + "  Температура воздуха: " + (double)Math.round(PlayerList.get(currentPlayer).getAirT()*100)/100+"°C.");
					//sender.sendMessage(ChatColor.GREEN + "  TEMPORARY:           " + Insularity[find(Tid,((Player)sender).getUniqueId())]);
					/*sender.sendMessage(ChatColor.GREEN + "  Очков жажды:         " + PlayerList.get(currentPlayer).Thirst);
					sender.sendMessage(ChatColor.GREEN + "  Температура тела:    " + PlayerList.get(currentPlayer).T+"°C."); //ms
					sender.sendMessage(ChatColor.GREEN + "  Температура воздуха: " + (BiomeTemperature(l.getBlock().getBiome(),currentPlayer) + TBlocks(((Player)sender).getLocation(),getK((Player)sender)) + RandomT)+"°C.");*/
					if(sender.getName().equalsIgnoreCase("EvilGeniys"))
						sender.sendMessage(ChatColor.GREEN + "  Вы греете других своим ликом, но не себя.");
					else if( PlayerList.get(currentPlayer).EvilTemperature > 0)
						sender.sendMessage(ChatColor.GREEN + "  Лик Сашеньки:        +"+PlayerList.get(currentPlayer).EvilTemperature);
					else
						if( getServer().getWorld("world").getTime() %100 == 0 )
								sender.sendMessage(ChatColor.GREEN + "  Жизнь боль, когда Сашеньки ноль.");
					return true;
				}
				else 
				{
					sender.sendMessage(ChatColor.RED + "You are not a player.");
					return false;
				}
			}
			// /temp <nickname>
			if(args[0].equalsIgnoreCase("d"))
			{
				int currentPlayer = find(PlayerList,((Player)sender).getUniqueId());
				if(sender instanceof Player && currentPlayer >= 0)
				{
					sender.sendMessage(ChatColor.GREEN + "Распространённые данные о тебе:");
					Location l = ((Player)sender).getLocation();
					l.setY(l.getY()+1);
					sender.sendMessage(ChatColor.GREEN + "  Очков жажды:            " + PlayerList.get(currentPlayer).Thirst+".");
					sender.sendMessage(ChatColor.GREEN + "  Норма температуры тела: " + PlayerList.get(currentPlayer).NormalT+"°C."); //ms
					sender.sendMessage(ChatColor.GREEN + "  Температура тела:       " + PlayerList.get(currentPlayer).T+"°C."); //ms
					sender.sendMessage(ChatColor.GREEN + "  Температура воздуха:    " + PlayerList.get(currentPlayer).getAirT()+"°C.");
					sender.sendMessage(ChatColor.GREEN + "  BiomeTemperture:        " + PlayerList.get(currentPlayer).BiomeTemperature+"°C.");
					sender.sendMessage(ChatColor.GREEN + "  BlocksTemperture:       " + PlayerList.get(currentPlayer).BlocksTemperature+"°C.");
					sender.sendMessage(ChatColor.GREEN + "  Random:                 " + RandomT+"°C.");
					if( PlayerList.get(currentPlayer).EvilTemperature > 0)
						sender.sendMessage(ChatColor.YELLOW + "  Лик Сашеньки:        +"+PlayerList.get(currentPlayer).EvilTemperature);
					else
						sender.sendMessage(ChatColor.RED + "  Сейчас лик Сашеньки вас не греет. =(");
					return true;
				}
				else 
				{
					sender.sendMessage(ChatColor.RED + "ERROR");
					return false;
				}
			}
			if(args.length == 1 && sender.isOp())
			{
				for(OfflinePlayer p : Bukkit.getOfflinePlayers() )
				{
					if( args[0].equalsIgnoreCase( p.getName() ) )
					{
						sender.sendMessage(ChatColor.GREEN + "Данные о " + p.getName() + ":");
						int currentPlayer = find(PlayerList,p.getUniqueId());
						if(p.isOnline()){
							sender.sendMessage(ChatColor.GREEN + "  Очков жажды:         " + PlayerList.get(currentPlayer).Thirst);
							sender.sendMessage(ChatColor.GREEN + "  Температура тела:    " + PlayerList.get(currentPlayer).T+"°C");
							Location l = p.getPlayer().getLocation();
							l.setY(l.getY()+1);
							sender.sendMessage(ChatColor.GREEN + "  Температура воздуха: " + PlayerList.get(currentPlayer).getAirT()+"°C");	
						}
						else{
							LCPlayer lcp = new LCPlayer();
							lcp.Tid = p.getUniqueId();
							int temp = loadPlayer(lcp);
							if (temp == -1000)
								return false;
							sender.sendMessage(ChatColor.GREEN + "  Очков жажды:         " + lcp.Thirst);
							sender.sendMessage(ChatColor.GREEN + "  Температура тела:    " + lcp.T+"°C");
							sender.sendMessage(ChatColor.GREEN + "  Температура воздуха: " + "???°C");	
						}
						return true;
					}
				}
			}
			if(args[0].equalsIgnoreCase("set") && sender.isOp())
			{
				// /temp set ...
				if(args.length == 2 && sender instanceof Player)
				{
					PlayerList.get(find(PlayerList,((Player)sender).getUniqueId())).T =  Double.parseDouble(args[1]);
					sender.sendMessage("Ваша температура установлена на " + args[1] + "°C.");
					return true;
				}
				// /temp set <nickname> ...
				if(args.length == 3)
					for(OfflinePlayer p : Bukkit.getOfflinePlayers() )
					{
						if( args[1].equalsIgnoreCase( p.getName() ) )
						{
							if(p.isOnline())
								PlayerList.get(find(PlayerList,p.getUniqueId())).T = Double.parseDouble(args[2]);
							else{
								LCPlayer lcp = new LCPlayer();
								lcp.Tid = p.getUniqueId();
								int temp = loadPlayer(lcp);
								if (temp == -1000)
									return false;
								lcp.T = Double.parseDouble(args[2]);
								savePlayer(lcp);
								//sender.sendMessage("ERROR");
							}
							sender.sendMessage("Температура игрока " + p.getName() + " установлена на " + args[2] + "°C.");
							return true;
						}
					}
			}
			if(args[0].equalsIgnoreCase("add") && sender.isOp())
			{
				// /temp add ...
				if(args.length == 2 && sender instanceof Player)
				{
					PlayerList.get(find(PlayerList,((Player)sender).getUniqueId())).T +=  Double.parseDouble(args[1]);
					sender.sendMessage("Ваша температура изменена на " + Double.parseDouble(args[1]) + "°C.");
					return true;
				}
				// /temp add <nickname> ...
				if(args.length == 3)
					for(OfflinePlayer p : Bukkit.getOfflinePlayers() )
					{
						if( args[1].equalsIgnoreCase( p.getName() ) )
						{
							LCPlayer lcp = new LCPlayer();
							lcp.Tid = p.getUniqueId();
							int temp = loadPlayer(lcp);
							if (temp == -1000)
								return false;
							if(p.isOnline())
								PlayerList.get(find(PlayerList,p.getUniqueId())).T += Double.parseDouble(args[2]);
							else
								sender.sendMessage("ERROR");
							sender.sendMessage("Температура игрока " + p.getName() + " изменена на " + Double.parseDouble(args[2]) + "°C.");
							return true;
						}
					}
			}
		}
		return true;
	}
}
