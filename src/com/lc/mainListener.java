package com.lc;

//import com.fetw.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Cauldron;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/*import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;*/

public class mainListener extends Insularity implements Listener
{
	double sleepinessFromFETW = 100;
	boolean sleepToFETW = false;

	int ticks = 0;
	double dayticks = 0;
	int lastDay = 0;
	
	Random rand = new Random();
	int RandomTicks = 0;
	static double RandomT = 0;
	static double RandomTDayDifference = 0;
	
	double Tbody = 26;
	
	int N = getServer().getMaxPlayers();
	List<LCPlayer> PlayerList = new ArrayList<>();
	
	PotionEffect Confusion = new PotionEffect(PotionEffectType.CONFUSION,100000,0,true,false);//100
	PotionEffect Hunger = new PotionEffect(PotionEffectType.HUNGER,100000,0,true,false);//100
	PotionEffect Wither = new PotionEffect(PotionEffectType.WITHER,100000,0,true,false);//30
	
	double Taround;
	double dT;
	
	boolean LCon = false;
	int output_ticks = 20;
	
	String ExecutingFunctionName[] = {"BiomeT","TBlocks","AlterTemperature","AlterThirst","DataTest","Output","TAdaptation"};
	static double ExecutionAllTime[] = new double[7]; //BiomeT, BlocksT, AlterT, AlterThirst, DataTest, Output, TAdaptation
	static double ExecutionTimes[] = new double[7]; 
	
	TempandArmorFunctions functions = new TempandArmorFunctions();
	//System.currentTimeMillis(); System.nanoTime();
	
	
	public void onEnable()
	{
		String WORLD_NAME = "world_TEST";
		
		TempandArmorFunctions.p = this;
		//Temperature, (T)Adaptation, (T)Insularity, Thirst, ArworWeight, WaterSurface, Urandrop, Reactors, extendedSleep, day timeskip with sleep, DaySpeed, 
		Config.FunctionsON.put("Temperature", false);
		Config.FunctionsON.put("Adaptation", false);
		Config.FunctionsON.put("Insularity", false);
		Config.FunctionsON.put("Thirst", false);
		Config.FunctionsON.put("ArmorWeight", false);
		Config.FunctionsON.put("WaterSurface", true);
		Config.FunctionsON.put("Urandrop", true);
		Config.FunctionsON.put("Reactors", false);
		//Config.FunctionsON.put("extendedSleep", true);
		//Config.FunctionsON.put("daySLeepSkip", true);
		Config.FunctionsON.put("DaySpeed", false);
		getServer().getPluginManager().registerEvents(this, this);
		Config.loadConfig(getConfig(), this);
		for(int i = 0;i < ExecutionTimes.length;i++)
		{
			ExecutionAllTime[i] = 0;
			ExecutionTimes[i] = 0;
		}
		RandomTDayDifference = 0.1*(double)rand.nextInt(200);
		dayticks = getServer().getWorld(WORLD_NAME).getTime();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
	            new Runnable() {
			public void run() {
				if(LCon) {
							////////////////////////
							ItemWaterSurface();
							for(Player p : getServer().getOnlinePlayers())
							{
								boolean sleepFromFETW = sleepToFETW;
								//System.out.println(""+((EntityPlayer)((CraftPlayer)p).getHandle()).aO);
								if(sleepFromFETW)
								{
									if(p.isSneaking())
									{
										sleepToFETW = false;
										//Нормальная камера
										Location l = p.getLocation();
										l.setY(l.getY()+1.3);
										p.teleport(l);
										p.setGravity(true);
									}
									//Вверх камера, вниз игрок, (пробел)шифт - встать
									
								}
							}
							 //СЛИШКОМ НЕ СОННЫМ СПАТЬ НЕЛЬЗЯ
							if(Config.timeK != 1 && getServer().getWorld(WORLD_NAME).getGameRuleValue("doDaylightCycle").equals("true"))
							{
								if(Config.FunctionsON.get("DaySpeed")) {
									if(getServer().getWorld(WORLD_NAME).getTime()-dayticks > -100 && getServer().getWorld(WORLD_NAME).getTime()-dayticks < 100)
										dayticks += Config.timeK;
									else
										dayticks = getServer().getWorld(WORLD_NAME).getTime() + Config.timeK;
									if(dayticks > 24000)
									{
										//getServer().getWorld("world").setTime(9);
									}
									else
									{
										//getServer().getWorld("world").setTime(Math.round(dayticks));
									}
									getServer().getWorld(WORLD_NAME).setFullTime(
											getServer().getWorld(WORLD_NAME).getFullTime() - getServer().getWorld(WORLD_NAME).getFullTime() % 24000 + Math.round(dayticks));
								}
							}
							////////////////////////////////////
							
							RandomTicks += 1;
							if(RandomTicks == Config.RandomMaxTicks)
							{
								RandomTicks -= Config.RandomMaxTicks;
								RandomT = 1-0.1*rand.nextInt((int)(Config.RandomTDifference*10));
							}
							if((int)(getServer().getWorld(WORLD_NAME).getFullTime()/24000) != lastDay) {
								lastDay = (int) (getServer().getWorld(WORLD_NAME).getFullTime()/24000);
								RandomTDayDifference = 0.1*(double)rand.nextInt(200);
							}
							/*TempandArmorFunctions.updateTime();
							for(LCPlayer lcpp : PlayerList)
								if(lcpp.Tid != null)
								{
									getServer().getPlayer(lcpp.Tid).setFlySpeed((float)0.1);
									getServer().getPlayer(lcpp.Tid).setWalkSpeed((float)0.2);
									if(lcpp.isOnLC)
									{
										if(!lcpp.Logged)
										{
											///////////////////////
											if(getServer().getPlayer(lcpp.Tid).getLocation().getY()-lcpp.Y < 0.1)
											{
												getServer().getPlayer(lcpp.Tid).setVelocity(new Vector(0.0,0.2,0.0));
												continue;
											}
											else
											{
												getServer().getPlayer(lcpp.Tid).setVelocity(new Vector(0.0,0.0,0.0));
												lcpp.Logged = true;
											}
											/////////////////////////
											continue;
										}
										if(LCon && !getServer().getPlayer(lcpp.Tid).isDead() && getServer().getPlayer(lcpp.Tid).getGameMode() != GameMode.CREATIVE && getServer().getPlayer(lcpp.Tid).getGameMode() != GameMode.SPECTATOR)
										{
											if(lcpp.isOnLC)
											{
											    Location l = lcpp.p.getLocation();
												lcpp.BiomeTemperature = functions.BiomeTemperature(lcpp);
												lcpp.BlocksTemperature = functions.TBlocks(l,functions.getK(lcpp.p),lcpp.BiomeTemperature);
												lcpp.EvilTemperature = functions.EvilT(lcpp.p.getLocation());

												lcpp.PlayerHandling();
												
												if(lcpp.TDebuff != 0)
													if(Config.FunctionsON.get("Adaptation")) {
														lcpp.TemperatureAdaptation();
													}
												
												lcpp.DataTest();
												
												if(Config.isBuffing && lcpp.T<Config.BuffUpperLimitT && lcpp.T>Config.BuffBottomLimitT && lcpp.Thirst>Config.BuffLimitThirst)
													Buff(lcpp);
												if(Config.isDebuffing)
													Debuff(lcpp);
												lcpp.dY = getServer().getPlayer(lcpp.Tid).getLocation().getY()-lcpp.Y;
												lcpp.Y = getServer().getPlayer(lcpp.Tid).getLocation().getY();
											}
										}
										else
										{
										    Location l = lcpp.p.getLocation();
										    l.setY(l.getY()+1);
											lcpp.BiomeTemperature = functions.BiomeTemperature(lcpp);
											lcpp.BlocksTemperature = functions.TBlocks(l,functions.getK(lcpp.p),lcpp.BiomeTemperature);
											lcpp.EvilTemperature = functions.EvilT(lcpp.p.getLocation());
											lcpp.setAirT(lcpp.BiomeTemperature + lcpp.BlocksTemperature + mainListener.RandomT + lcpp.EvilTemperature)	;
											lcpp.DataTest();
											lcpp.dTair = 0;
											getServer().getPlayer(lcpp.Tid).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.DEFAULT_SPEED);
										}
										if(RandomTicks % 20 == 0)
										{
											if(Config.FunctionsON.get("Insularity")) {
												lcpp.Insularity = BiomityWithSunlight(getServer().getPlayer(lcpp.Tid).getLocation());
											}
										}
									}
								}
							
								for(LCPlayer lcpp : PlayerList)
									if(lcpp.outputTicks < lcpp.maxOutputTicks) lcpp.outputTicks++;
									else 
									{
										lcpp.outputTicks = 0;
										lcpp.Output(lcpp.p);
									}*/
				}
		    }
		},
	            0L,1L);
	}
	
	public void onDisable()
	{
		int i = PlayerList.size()-1;
    	while(i >= 0)
    	{
    		if(PlayerList.get(i).Tid != null)
    		{
        		savePlayer(PlayerList.get(i));
    		}
    		i -= 1;
    	}
	}
	
	
	
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
				sender.sendMessage(ChatColor.GRAY+"   \"/sleep\" - sleep everywhere.");
				if(!sender.isOp()) return true;
				sender.sendMessage(ChatColor.GRAY+"Admin commands:");
				sender.sendMessage(ChatColor.GRAY+"   \"lc reload\" - reloads config");
				sender.sendMessage(ChatColor.GRAY+"   \"temp set <value>\" - ...");
				sender.sendMessage(ChatColor.GRAY+"   \"temp add <value\" - ...");
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
			/*if(args[0].equalsIgnoreCase("login") && args[1].equalsIgnoreCase("me"))
				Logged[find(Tid,((Player)sender).getUniqueId())] = true;*/
		}
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("sleep"))
		{
			LCPlayer lcpp = PlayerList.get(find(PlayerList, ((Player)sender).getUniqueId()));
			if(lcpp.p.isSleeping()) lcpp.isSleeping = true;
			if(lcpp.Sleepiness > 50 && !lcpp.isSleeping)
			{
				sleepToFETW = true;
				Location l = ((Player)sender).getLocation();
				l.setY(l.getY()-1.3);
				((Player)sender).teleport(l);
				((Player)sender).setGravity(false);
				/*System.out.println(((CraftPlayer)(Player)sender).getHandle());
				System.out.println(((CraftPlayer)(Player)sender).getHandle().sleeping);
				System.out.println((EntityPlayer)((CraftPlayer)(Player)sender).getHandle());
				System.out.println(((EntityPlayer)((CraftPlayer)(Player)sender).getHandle()).sleeping);*/
				//((EntityPlayer)((CraftPlayer)(Player)sender).getHandle()).sleeping = true;
			}
			else if(!lcpp.isSleeping)
			{
				sleepToFETW = false;
				Location l = ((Player)sender).getLocation();
				l.setY(l.getY()+1.3);
				((Player)sender).teleport(l);
				((Player)sender).setGravity(true);
			}
		}
		return true;
	}
	
	/*@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		event.getEntity().sendMessage(event.getDeathMessage());
		event.setDeathMessage(" ");
	}*/
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		int i = PlayerList.size();
		PlayerList.add(new LCPlayer());
		PlayerList.get(i).Tid = event.getPlayer().getUniqueId();
		PlayerList.get(i).p = event.getPlayer();
		int test = loadPlayer(PlayerList.get(i)); //T, Thirst, ...
		if(test == -1000){
    		File file = new File("plugins/LifeCompromise/playerdata/"+event.getPlayer().getUniqueId());
			try {
				file.createNewFile();
				FileWriter fp = new FileWriter(file);
				fp.write( (Config.defaultLCon ? 1: 0)+Double.toString(Config.skinTemperature)+" 100.0 "); //TO DO: UNIVERSAL SAVE
	    		PlayerList.get(i).NormalT = Config.skinTemperature;
	    		PlayerList.get(i).T = PlayerList.get(i).NormalT;
	    		PlayerList.get(i).Thirst = 100.0;
				fp.close();
			} catch(IOException ex){
		    	System.out.println(ex.getMessage());
		    }
		}
		//((EntityPlayer)((CraftPlayer)getServer().getPlayer(PlayerList.get(i).Tid)).getHandle()).getHeadRotation()
        //((EntityPlayer)((CraftPlayer)getServer().getPlayer(PlayerList.get(i).Tid)).getHandle()).aO
		PlayerList.get(i).Y = event.getPlayer().getLocation().getY();
		PlayerList.get(i).dY = 0;
		PlayerList.get(i).Logged = false;
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		int i = PlayerList.size()-1;
    	while(i >= 0 && PlayerList.get(i).Tid != event.getPlayer().getUniqueId())
    		i -= 1;
    	if(i >= 0)
    	{
    		savePlayer(PlayerList.get(i));
    		PlayerList.remove(i);
    	}
    }
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		int i = find(PlayerList,event.getPlayer().getUniqueId());
		PlayerList.get(i).T = PlayerList.get(i).NormalT;
		PlayerList.get(i).Thirst = 100.0;
	}
	
	/*@EventHandler
	public void OnAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
	}*/

	@EventHandler
	public void OnPlayerStatisticIncrement(PlayerStatisticIncrementEvent event)
	{
		if(event.getStatistic() == Statistic.USE_ITEM && event.getMaterial() != null)
		{
			int i = find(PlayerList,event.getPlayer().getUniqueId());
			int ad = -1;
			if(event.getMaterial() == Material.POTION)
			{
				//System.out.println("Type: " + event.getPlayer().getInventory().getItemInMainHand().getType());
				if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.POTION && PlayerList.get(i).AdditionalDataMain >= 0)
					ad = PlayerList.get(i).AdditionalDataMain;
				else if(event.getPlayer().getInventory().getItemInOffHand().getType() != Material.POTION && PlayerList.get(i).AdditionalDataOff >= 0)
					ad = PlayerList.get(i).AdditionalDataOff;
				if(event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
					ad = PlayerList.get(i).AdditionalDataMain; ///?
				//System.out.println("ad: " + ad + " 1: " + AdditionalDataMain[i] + " 2: " + AdditionalDataOff[i]);
				double waterPlus = 20;
				double potionPlus = 25;
				switch(ad)
				{
				case 0:    PlayerList.get(i).Thirst += waterPlus; break;  //обычное
				case 16:   PlayerList.get(i).Thirst += potionPlus; break; //грубое(нарост)
				case 32:   PlayerList.get(i).Thirst += waterPlus; break; //густое(светопыль)
				case 64:   PlayerList.get(i).Thirst += waterPlus; break; //непримечательное усиленное(долгое)
				case 8192: PlayerList.get(i).Thirst += waterPlus; break; //непримечательное
				case 8193: PlayerList.get(i).Thirst += potionPlus+2; break; //регенерация
				case 8225: PlayerList.get(i).Thirst += potionPlus+2; break; //регенерация 2
				case 8257: PlayerList.get(i).Thirst += potionPlus+2; break; //регенерация долгий
				case 8194: PlayerList.get(i).Thirst += potionPlus-5; break; //ускорение
				case 8226: PlayerList.get(i).Thirst += potionPlus-5; break; //ускорение 2
				case 8258: PlayerList.get(i).Thirst += potionPlus-5; break; //ускорение долгий
				case 8195: PlayerList.get(i).Thirst += potionPlus; break; //огнестойкость
				case 8259: PlayerList.get(i).Thirst += potionPlus; break; //огнестойкость долгий
				case 8196: PlayerList.get(i).Thirst += potionPlus+1; break; //отравление
				case 8228: PlayerList.get(i).Thirst += potionPlus+1; break; //отравление 2
				case 8260: PlayerList.get(i).Thirst += potionPlus+1; break; //отравление долгий
				case 8197: PlayerList.get(i).Thirst += potionPlus+5; break; //лечение
				case 8229: PlayerList.get(i).Thirst += potionPlus+5; break; //лечение 2
				case 8198: PlayerList.get(i).Thirst += potionPlus+2; break; //ночное зрение
				case 8262: PlayerList.get(i).Thirst += potionPlus+2; break; //ночное зрение долгий
				case 8200: PlayerList.get(i).Thirst += potionPlus+1; break; //слабость
				case 8264: PlayerList.get(i).Thirst += potionPlus+1; break; //слабость долгий
				case 8201: PlayerList.get(i).Thirst += potionPlus; break; //сила
				case 8233: PlayerList.get(i).Thirst += potionPlus; break; //сила 2
				case 8265: PlayerList.get(i).Thirst += potionPlus; break; //сила долгий
				case 8202: PlayerList.get(i).Thirst += potionPlus+1; break; //замедление
				case 8266: PlayerList.get(i).Thirst += potionPlus+1; break; //замедление долгий
				case 8203: PlayerList.get(i).Thirst += potionPlus; break; //прыгучесть
				case 8235: PlayerList.get(i).Thirst += potionPlus; break; //прыгучесть долгий
				case 8267: PlayerList.get(i).Thirst += potionPlus; break; //прыгучесть 2
				case 8204: PlayerList.get(i).Thirst += potionPlus+4; break; //урон
				case 8236: PlayerList.get(i).Thirst += potionPlus+4; break; //урон 2
				case 8205: PlayerList.get(i).Thirst += potionPlus; break; //подводное дыхание
				case 8269: PlayerList.get(i).Thirst += potionPlus; break; //подводное дыхание долгое
				case 8206: PlayerList.get(i).Thirst += potionPlus+3; break; //невидимость
				case 8270: PlayerList.get(i).Thirst += potionPlus+3; break; //невидимость долгий
				default:   PlayerList.get(i).Thirst += potionPlus;
				}
			}
			else if(event.getMaterial() == Material.MILK_BUCKET)
				PlayerList.get(i).Thirst += 100;
			//event.getStatistic().hashCode() event.getMaterial().hashCode() event.hashCode()
			if(PlayerList.get(i).Thirst > 100) PlayerList.get(i).Thirst = 100.0;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.isCancelled()) return;
		//System.out.println((event.hasBlock()) + event.getMaterial().toString());
		if(event.hasBlock()) //not shifting
			if(event.getClickedBlock().getType() == Material.BED_BLOCK)
				PlayerList.get(find(PlayerList,event.getPlayer().getUniqueId())).Unbed = 40;
			else if(event.getClickedBlock().getType() == Material.CAULDRON)
			{
				LCPlayer lcp = PlayerList.get(find(PlayerList,event.getPlayer().getUniqueId()));
				if(lcp.interacted) return;
				lcp.interacted = true;
				Cauldron caul = (Cauldron) event.getClickedBlock().getState().getData();
                BlockState d = event.getClickedBlock().getState();
                //System.out.println(lcp.AdditionalDataMain +" "+ lcp.AdditionalDataOff + "   " + event.getClickedBlock().getState().getRawData() + " " + caul.getData());
				if(lcp.AdditionalDataMain < -1 && lcp.AdditionalDataMain > -1000)
					return;
				if(lcp.AdditionalDataMain >= 0 && event.getClickedBlock().getState().getRawData() < 3){
					event.setCancelled(true);
	                d.getData().setData((byte) (caul.getData()+1));
	                d.update();
	                playBottleOutSound(lcp.p);
	                if(lcp.p.getGameMode() != GameMode.CREATIVE && lcp.p.getGameMode() != GameMode.SPECTATOR)
		                lcp.p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);
					return;
				} else if(lcp.AdditionalDataOff >= 0 && event.getClickedBlock().getState().getRawData() < 3) {
					event.setCancelled(true);
	                d.getData().setData((byte) (caul.getData()+1));
	                d.update();
	                playBottleOutSound(lcp.p);
	                if(lcp.p.getGameMode() != GameMode.CREATIVE && lcp.p.getGameMode() != GameMode.SPECTATOR)
	                	lcp.p.getInventory().getItemInOffHand().setType(Material.GLASS_BOTTLE);
					return;
				} else if(event.getClickedBlock().getState().getRawData() > 0) { //hand with no items?
					Material hand1 = event.getPlayer().getInventory().getItemInMainHand().getType();
					Material hand2 = event.getPlayer().getInventory().getItemInOffHand().getType();
					if( lcp.AdditionalDataMain < 0 && !functions.isColorable(hand1) && ( (!hand1.isEdible() && !(hand1.isBlock() && lcp.p.isSneaking()) ) ||
						( lcp.AdditionalDataOff < 0 &&  !functions.isColorable(hand2) && !hand2.isEdible() && !(hand2.isBlock() && lcp.p.isSneaking()) ) ) ) {
		                playBottleInSound(lcp.p);
		                event.setCancelled(true);
		                d.getData().setData((byte) (caul.getData()-1));
		                d.update();
						lcp.Thirst += 20;
						return;
					}
				}
			}
	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event)
	{
		PlayerList.get(find(PlayerList,event.getPlayer().getUniqueId())).Unbed = 0;
	}

	@EventHandler
	public void onPlayerBedLeave(PlayerBedLeaveEvent event)
	{
		//System.out.println(event.getPlayer().getBedSpawnLocation() + " " + event.getPlayer().getWorld().getSpawnLocation());
		if(event.getPlayer().getBedSpawnLocation() != null)
			event.getPlayer().setCompassTarget(event.getPlayer().getBedSpawnLocation());
		else
			event.getPlayer().setCompassTarget(event.getPlayer().getWorld().getSpawnLocation());
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()) return;
		if(event.getBlock().getType().equals(Material.IRON_ORE)) //new ore
		{
			if(Config.FunctionsON.get("Urandrop")) {
				int r = rand.nextInt(Config.RAND_ALL_CHANCES);
				//if
				if(r <= Config.uranRand1)
				{
					//lvl1
					ItemStack IS = new ItemStack(Material.IRON_ORE, 1);
					ItemMeta IM = IS.getItemMeta();
					IM.setDisplayName("Урановая руда");
					IM.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
					IS.setItemMeta(IM);
	                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), IS);
	                event.setCancelled(true);
	                event.getBlock().setType(Material.AIR);
					/*for(ItemStack is : event.getBlock().getDrops())
					{
						ItemMeta IM = is.getItemMeta();
						IM.setDisplayName("Урановая руда");
						IM.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
						is.setItemMeta(IM);
					}*/
				}
				else if(r <= Config.uranRand2)
				{
					//lvl2
					ItemStack IS = new ItemStack(Material.IRON_ORE, 1);
					ItemMeta IM = IS.getItemMeta();
					IM.setDisplayName("Урановая руда");
					IM.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
					IS.setItemMeta(IM);
	                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), IS);
	                event.setCancelled(true);
	                event.getBlock().setType(Material.AIR);
				}
				else if(r <= Config.uranRand3)
				{
					//lvl3
					ItemStack IS = new ItemStack(Material.IRON_ORE, 1);
					ItemMeta IM = IS.getItemMeta();
					IM.setDisplayName("Урановая руда");
					IM.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
					IS.setItemMeta(IM);
	                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), IS);
	                event.setCancelled(true);
	                event.getBlock().setType(Material.AIR);
				}
			}
		}
	}
	
	/*
	public void Thirst(LCPlayer lcpp)
	{
		lcpp.Thirst -= (double)1/Config.thirstTicks;
		if(Config.frostbite1 > lcpp.T || lcpp.T > Config.heatstroke1)
		{
			lcpp.Thirst -= (double)1/Config.thirstTicks/2;
					if(Config.frostbite1 > lcpp.T || lcpp.T > Config.heatstroke1)
						lcpp.Thirst -= (double)1/Config.thirstTicks/2;
				}
				Location l = getServer().getPlayer(lcpp.Tid).getLocation();
				l.setY(l.getY()+0.99);
				if(l.getBlock().getType() == Material.STATIONARY_WATER/* && l.getBlock().getMetadata("") == * /) ////////
					lcpp.Thirst += Config.WaterRegen;
				if(lcpp.Thirst > 100) lcpp.Thirst = 100.0;
				else if(lcpp.Thirst < 0) lcpp.Thirst = 0.0;
	}*/
	
	/*public void Temperature(LCPlayer lcpp)
	{
		//double TimeAndWeather = TimeWeatherTemperature();
	    Player p = getServer().getPlayer(lcpp.Tid);
	    Location l = p.getLocation();
	    l.setY(l.getY()+1);
		Taround = BiomeTemperature(lcpp) + TBlocks(p.getLocation(),getK(p),BiomeTemperature(lcpp)) + RandomT + EvilT(p.getLocation());
		//Tbody = (k*Tbody+Taround)/(k+1);
	    dT = lcpp.T-Taround;
		ItemStack CurrentSlot = p.getInventory().getHelmet();
	    double KTime2 = Config.KTime;
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_HELMET)
	    		if(dT > 0) KTime2 += Config.leatherColdBonus;
	    		else       KTime2 += Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_HELMET)
	    		if(dT > 0) KTime2 += Config.goldColdBonus;
	    		else       KTime2 += Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_HELMET)
	    		if(dT > 0) KTime2 += Config.chainmailColdBonus;
	    		else       KTime2 += Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_HELMET)
	    		if(dT > 0) KTime2 += Config.ironColdBonus;
	    		else       KTime2 += Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_HELMET)
	    		if(dT > 0) KTime2 += Config.diamondColdBonus;
	    		else       KTime2 += Config.diamondWarmBonus;
		CurrentSlot = p.getInventory().getChestplate();
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_CHESTPLATE)
	    		if(dT > 0) KTime2 += 3*Config.leatherColdBonus;
	    		else       KTime2 += 3*Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_CHESTPLATE)
	    		if(dT > 0) KTime2 += 3*Config.goldColdBonus;
	    		else       KTime2 += 3*Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_CHESTPLATE)
	    		if(dT > 0) KTime2 += 3*Config.chainmailColdBonus;
	    		else       KTime2 += 3*Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_CHESTPLATE)
	    		if(dT > 0) KTime2 += 3*Config.ironColdBonus;
	    		else       KTime2 += 3*Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_CHESTPLATE)
	    		if(dT > 0) KTime2 += 3*Config.diamondColdBonus;
	    		else       KTime2 += 3*Config.diamondWarmBonus;
		CurrentSlot = p.getInventory().getLeggings();
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_LEGGINGS)
	    		if(dT > 0) KTime2 += 2*Config.leatherColdBonus;
	    		else       KTime2 += 2*Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_LEGGINGS)
	    		if(dT > 0) KTime2 += 2*Config.goldColdBonus;
	    		else       KTime2 += 2*Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_LEGGINGS)
	    		if(dT > 0) KTime2 += 2*Config.chainmailColdBonus;
	    		else       KTime2 += 2*Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_LEGGINGS)
	    		if(dT > 0) KTime2 += 2*Config.ironColdBonus;
	    		else       KTime2 += 2*Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_LEGGINGS)
	    		if(dT > 0) KTime2 += 2*Config.diamondColdBonus;
	    		else       KTime2 += 2*Config.diamondWarmBonus;
		CurrentSlot = p.getInventory().getBoots();
	    if(CurrentSlot != null)
	    	if(CurrentSlot.getType() == Material.LEATHER_BOOTS)
	    		if(dT > 0) KTime2 += Config.leatherColdBonus;
	    		else       KTime2 += Config.leatherWarmBonus;
	    	else if(CurrentSlot.getType() == Material.GOLD_BOOTS)
	    		if(dT > 0) KTime2 += Config.goldColdBonus;
	    		else       KTime2 += Config.goldWarmBonus;
	    	else if(CurrentSlot.getType() == Material.CHAINMAIL_BOOTS)
	    		if(dT > 0) KTime2 += Config.chainmailColdBonus;
	    		else       KTime2 += Config.chainmailWarmBonus;
	    	else if(CurrentSlot.getType() == Material.IRON_BOOTS)
	    		if(dT > 0) KTime2 += Config.ironColdBonus;
	    		else       KTime2 += Config.ironWarmBonus;
	    	else if(CurrentSlot.getType() == Material.DIAMOND_BOOTS)
	    		if(dT > 0) KTime2 += Config.diamondColdBonus;
	    		else       KTime2 += Config.diamondWarmBonus;
	    if(p.getLocation().getBlock().getType() == Material.WATER ||
		   p.getLocation().getBlock().getType() == Material.STATIONARY_WATER)
	    	if(dT > 0) //Tskin-Tair
	    		KTime2 *= Config.WaterColdBonus;
	    	else
	    		KTime2 *= Config.WaterWarmBonus;
	    double mT = (-dT/Config.KRatio-sgn(dT))/KTime2;
	    if(sgn(lcpp.T-Config.skinTemperature) == sgn(dT)) //dT = T[i]-Taround;
	    	mT = mT*Config.skinTendencyK;
	    if(-mT < dT && dT < mT)
	    	lcpp.T = Taround;
	    else
	    	lcpp.T += mT;
	}*/
	//PermissionsEx.getUser(p).has("fest.skeleton")

	
	
	
	public void Buff(LCPlayer lcpp)
	{
		//Скорость копания, бега, прыга и чутка регенерации.
		//((EntityPlayer)(CraftPlayer)getServer().getPlayer(Tid[i])).
		Player p = lcpp.p;
		p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.DEFAULT_SPEED*Config.GoodHealthSpeedBonus);
		if(p.getHealth()<20-Config.GoodHealthTickRegen/*getServer().getPlayer(Tid[i]).getHealthScale()*/)
			p.setHealth(p.getHealth()+Config.GoodHealthTickRegen);
		else
			p.setHealth(20);
		/*if(getServer().getPlayer(Tid[i]).getLocation().getY()-Y[i] > dY[i] && dY[i] <= 0 &&
		   getServer().getPlayer(Tid[i]).getLocation().getY()-Y[i] > 0 &&
		   getServer().getPlayer(Tid[i]).getVelocity().getY() > 0 && getServer().getPlayer(Tid[i]).getLocation().getBlock().getType() != Material.LADDER && getServer().getPlayer(Tid[i]).getLocation().getBlock().getType() != Material.VINE)
		{
			getServer().getPlayer(Tid[i]).setVelocity(new Vector(getServer().getPlayer(Tid[i]).getVelocity().getX(),getServer().getPlayer(Tid[i]).getVelocity().getY()-0.1+GoodHealthBonusJumpVelocity,getServer().getPlayer(Tid[i]).getVelocity().getZ()));
		}*/
	}
	
	public void Debuff(LCPlayer lcpp)
	{
		Player p = lcpp.p;
    	p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.DEFAULT_SPEED);
		if(lcpp.TDebuff != 0)
	    	p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.DEFAULT_SPEED * Config.SpeedTempDecrease);
	    else
	    	p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Config.DEFAULT_SPEED);
		if(lcpp.Thirst < Config.minSlownessThirstProcent)	
			p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * Config.SpeedThirstDecrease);
		if(lcpp.TDebuff <-2 || lcpp.TDebuff > 2)
			p.setHealth(0);
		/*Тогда короч 7 ничего не дает, от 7.5 до 10 - замедление 1 или лучше еще меньше через плагины, процентов на 10,
		 сопротивление 1, от 10.5 до 14, больше замедления, медленнее удары, защита все еще 1, но еще сила 1, топчется
		 трава, цветы и пашни, тонет в воде, от 14.5 до 21. Сопротивление 2, замедление ударов 2, сила 3, замедление
		 на 25%, топчется трава, пашни, ломается лед, от прыжков всему что ближе двух блоков наносится урон.*/
		//Еще сделать 23 масса. Делает сверхтяжелым, еще больше замедления, еще больший радиус поражения прыжком, еще
		//больше урона и все в таком духе, но плюс еще трава затаптывается до состояния дорожки, а от прыжка вообще до земли.
		if(lcpp.ArmorWeight >= Config.WeightLimit4) //>21
		{
			p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
					getBaseValue()*0.7);
			if(p.getPotionEffect(PotionEffectType.SLOW_DIGGING) == null || p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getDuration() < 1000 )
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,100000,0,true,false));
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(3.3);
			if(p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null || (p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() < 1000 || p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() != 1))
				{p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100000,1,true,false));}
			if(p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) == null || ((p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() < 3000 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() == 2) || (p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() > 3000 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() != 2)))
				{p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000,2,true,false));}
			Material m = p.getLocation().getBlock().getType();
			if(m == Material.LONG_GRASS || m == Material.RED_ROSE || m == Material.YELLOW_FLOWER || m == Material.WATER_LILY)
				p.getLocation().getBlock().breakNaturally();
			else if((m == Material.WATER || m == Material.STATIONARY_WATER) && p.getVelocity().getY() < -0.03 && p.getLocation().getY() - lcpp.Y >= -0.01 )
				p.setVelocity(new Vector(p.getVelocity().getX(),-0.045,p.getVelocity().getZ()));
			Location l = p.getLocation();
			l.setY(l.getY()-0.1);
			m = l.getBlock().getType();
			if(m == Material.SOIL){
				l.getBlock().setType(Material.DIRT);
				l.setY(l.getY()+0.2);
				p.teleport(l);}
			else if(m == Material.GRASS)
				l.getBlock().setType(Material.GRASS_PATH);
			/*else if(m == Material.ICE)
				l.getBlock().breakNaturally();*/
			//System.out.println("Y:"+Y[i]+" VY:"+getServer().getPlayer(Tid[i]).getVelocity().getY());
			if(lcpp.Y - p.getLocation().getY() > 0.3 && p.getVelocity().getY() < -0.07 && p.getVelocity().getY() > -0.08)
			{
				//getServer().getPlayer(Tid[i]).damage(1);
				for(Entity e : p.getNearbyEntities(1.5,1.5,1.5))
					if(e instanceof CraftLivingEntity && e != (Entity)p)
						((CraftLivingEntity) e).damage(2);
			}
		}
		else if(lcpp.ArmorWeight > Config.WeightLimit3) //>14
		{
			p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
					getBaseValue()*0.76);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(3.4);
			if(p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null || (p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() < 1000 || p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() != 1))
				{p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100000,1,true,false));}
			if(p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) == null || ((p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() < 6000 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() == 2) || (p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() > 6000 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() != 2)))
				{p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000,2,true,false));}
			Material m = p.getLocation().getBlock().getType();
			if(m == Material.LONG_GRASS || m == Material.RED_ROSE || m == Material.YELLOW_FLOWER || m == Material.WATER_LILY)
				p.getLocation().getBlock().breakNaturally();
			else if(p.getVelocity().getY() < -0.04 && (m == Material.WATER || m == Material.STATIONARY_WATER))
			{
				Location l = p.getLocation();
				l.setY(l.getY()+0.5);
				m = l.getBlock().getType(); //УСКОР�?ТЬ ОПУЩЕН�?Е, ОСЛАБЛЯТЬ ЭФФЕКТ
				if((m == Material.WATER || m == Material.STATIONARY_WATER) && p.getLocation().getY() - lcpp.Y > 0 )
						p.setVelocity(new Vector(p.getVelocity().getX(),-0.03,p.getVelocity().getZ()));}
			Location l = p.getLocation();
			l.setY(l.getY()-0.1);
			m = l.getBlock().getType();
			if(m == Material.SOIL){
				l.getBlock().setType(Material.DIRT);
				l.setY(l.getY()+0.2);
				p.teleport(l);}
			/*else if(m == Material.ICE)
				l.getBlock().breakNaturally();*/
			if(lcpp.Y- p.getLocation().getY() > 0.3 && p.getVelocity().getY() < -0.07 && p.getVelocity().getY() > -0.08)
			{
				//getServer().getPlayer(Tid[i]).damage(0.01);
				for(Entity e : p.getNearbyEntities(1,1,1))
					if(e instanceof CraftLivingEntity && e != (Entity)p)
						((CraftLivingEntity) e).damage(1);
			}
		}
		else if(lcpp.ArmorWeight > Config.WeightLimit2) //>10
		{
			p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
					getBaseValue()*0.84);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(3.7);
			if(p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null || (p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() < 1000 || p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() != 0))
				{p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100000,0,true,false));}
			if(p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) == null || ((p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() < 6000 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() == 0) || (p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() > 6000 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() != 1)))
				{p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000,0,true,false));}
			Material m = p.getLocation().getBlock().getType();
			if(m == Material.LONG_GRASS || m == Material.RED_ROSE || m == Material.YELLOW_FLOWER || m == Material.WATER_LILY)
				p.getLocation().getBlock().breakNaturally();
			else if(m == Material.SOIL){
				p.getLocation().getBlock().setType(Material.DIRT);
				Location l = p.getLocation();
				l.setY(l.getY()+0.1);
				p.teleport(l);}
			//else if(getServer().getPlayer(Tid[i]).getVelocity().getY() >= -0.025 && (m == Material.WATER || m == Material.STATIONARY_WATER) ) // плавание
			//	getServer().getPlayer(Tid[i]).setVelocity(new Vector(getServer().getPlayer(Tid[i]).getVelocity().getX(),getServer().getPlayer(Tid[i]).getVelocity().getY()-0.005,getServer().getPlayer(Tid[i]).getVelocity().getZ()));
			else if(p.getVelocity().getY() < -0.04 && (m == Material.WATER || m == Material.STATIONARY_WATER))
			{
				Location l = p.getLocation();
				l.setY(l.getY()+0.5);
				m = l.getBlock().getType(); //УСКОР�?ТЬ ОПУЩЕН�?Е, ОСЛАБЛЯТЬ ЭФФЕКТ
				if((m == Material.WATER || m == Material.STATIONARY_WATER) && p.getLocation().getY() - lcpp.Y > 0 )
					p.setVelocity(new Vector(p.getVelocity().getX(),-0.02,p.getVelocity().getZ()));}
			//getServer().getPlayer(Tid[i]).setFlySpeed((float) 0.1);
			//getServer().getPlayer(Tid[i]).getFlySpeed() = 0.1
			//getServer().getPlayer(Tid[i]).getWalkSpeed() = 0.2
			//.getOpenInventory()
		}
		else if(lcpp.ArmorWeight > Config.WeightLimit1) //>7
		{
			p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
					getBaseValue()*0.92);
			if(p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null || (p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() < 1000 || p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() != 0))
				{p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100000,0,true,false));}
		}
		if(lcpp.ArmorWeight <= Config.WeightLimit1 && p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) != null && p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getDuration() > 1000)
			p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		if(lcpp.ArmorWeight <= Config.WeightLimit2 && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null && p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration() > 6000)
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		if(lcpp.ArmorWeight <= Config.WeightLimit1 && p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 4.0)
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
		if(lcpp.ArmorWeight < Config.WeightLimit4 && p.getPotionEffect(PotionEffectType.SLOW_DIGGING) != null && p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getDuration() > 1000)
			p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		else if(p.getHealth() > 0 && (lcpp.TDebuff != 0 || lcpp.Thirst < Config.minNauseaThirstProcent))	
		{
	    	//if(getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.CONFUSION) == null || getServer().getPlayer(Tid[i]).getPotionEffect(PotionEffectType.CONFUSION).getDuration() < 100)
    		//	getServer().getPlayer(Tid[i]).addPotionEffect(Confusion);
			p.setExhaustion((float) (p.getExhaustion()+Config.hungerPower));;
    		if(p.getPotionEffect(PotionEffectType.HUNGER) == null || p.getPotionEffect(PotionEffectType.HUNGER).getDuration() < 10000)
    			{p.removePotionEffect(PotionEffectType.HUNGER);
    	    	p.addPotionEffect(Hunger);}
	    	if(getServer().getWorld("world").getTime()%20 == 0 && (lcpp.TDebuff <-1 || lcpp.TDebuff>1 || lcpp.Thirst <= 0))
	    		if(p.getHealth()-1>0)
	    			p.setHealth(p.getHealth()-1);
	    		else
	    			p.setHealth(0);
	    		//p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,30000,0,false,false),false);
    		//getServer().getPlayer(Tid[i]).damage(1);
	    	if(getServer().getWorld("world").getTime()%20 == 10 && ((lcpp.TDebuff <-1 || lcpp.TDebuff>1) && lcpp.Thirst <= 0))
	    		if(p.getHealth()-1>0)
	    			p.setHealth(p.getHealth()-1);
    			else
    				p.setHealth(0);
    		//getServer().getPlayer(Tid[i]).damage(1);
	    }
	    else if(p.getPotionEffect(PotionEffectType.HUNGER) != null && p.getPotionEffect(PotionEffectType.HUNGER).getDuration() > 10000)
	    {
	    	//getServer().getPlayer(Tid[i]).removePotionEffect(PotionEffectType.CONFUSION);
	    	p.removePotionEffect(PotionEffectType.HUNGER);
	    }
	}
	
	/*public void TestData()
	{
		for(int i=PlayerList.size()-1;i>=0;i--)
		{
			if(PlayerList.get(i).Tid != null && !getServer().getPlayer(PlayerList.get(i).Tid).isDead())
			{
				if(getServer().getPlayer(PlayerList.get(i).Tid).getInventory().getItemInMainHand().getType() == Material.POTION)
					PlayerList.get(i).AdditionalDataMain = getServer().getPlayer(PlayerList.get(i).Tid).getInventory().getItemInMainHand().getDurability();
				else
					PlayerList.get(i).AdditionalDataMain = -1;
				if(getServer().getPlayer(PlayerList.get(i).Tid).getInventory().getItemInOffHand().getType() == Material.POTION)
					PlayerList.get(i).AdditionalDataOff = getServer().getPlayer(PlayerList.get(i).Tid).getInventory().getItemInOffHand().getDurability();
				else
					PlayerList.get(i).AdditionalDataOff = -1;
			}
		}
	}*/

	
	
	
	public void ItemWaterSurface()
	{
		if(Config.FunctionsON.get("WaterSurface")) {
			for(World w : getServer().getWorlds())
			{
				for(Entity e : w.getEntitiesByClass(Item.class))
				{
					Material m = ((Item)e).getItemStack().getType();
					if( m == Material.WOOD || m == Material.WOOD_AXE || m == Material.WOOD_BUTTON || m == Material.WOOD_DOOR ||
						m == Material.WOOD_DOUBLE_STEP || m == Material.WOOD_HOE || m == Material.WOOD_PICKAXE || m == Material.WOOD_PLATE ||
						m == Material.WOOD_SPADE || m == Material.WOOD_STAIRS || m == Material.WOOD_STEP || m == Material.WOOD_SWORD ||
						m == Material.WOODEN_DOOR || m == Material.WORKBENCH || m == Material.ACACIA_DOOR || m == Material.ACACIA_DOOR_ITEM ||
						m == Material.ACACIA_FENCE || m == Material.ACACIA_FENCE_GATE || m == Material.ACACIA_STAIRS || m == Material.DARK_OAK_DOOR ||
						m == Material.DARK_OAK_DOOR_ITEM || m == Material.DARK_OAK_FENCE || m == Material.DARK_OAK_FENCE ||
						m == Material.DARK_OAK_FENCE_GATE || m == Material.DARK_OAK_STAIRS || m == Material.LADDER || m == Material.TRAP_DOOR ||
						m == Material.CHEST || m == Material.TRAPPED_CHEST  || m == Material.TRIPWIRE_HOOK || m == Material.BIRCH_DOOR ||
						m == Material.BIRCH_DOOR_ITEM || m == Material.BIRCH_FENCE || m == Material.BIRCH_FENCE_GATE || m == Material.BIRCH_WOOD_STAIRS ||
						m == Material.SPRUCE_DOOR || m == Material.SPRUCE_DOOR_ITEM || m == Material.SPRUCE_FENCE || m == Material.SPRUCE_FENCE_GATE ||
						m == Material.SPRUCE_WOOD_STAIRS || m == Material.JUKEBOX || m == Material.JUNGLE_DOOR || m == Material.JUNGLE_DOOR_ITEM ||
						m == Material.JUNGLE_FENCE || m == Material.JUNGLE_FENCE_GATE || m == Material.JUNGLE_WOOD_STAIRS || m == Material.STICK ||
						m == Material.SIGN || m == Material.SIGN_POST || m == Material.ITEM_FRAME || m == Material.BOW || m == Material.BOWL ||
						m == Material.FISHING_ROD || m == Material.BOAT || m == Material.BOAT_ACACIA || m == Material.BOAT_BIRCH ||
						m == Material.BOAT_DARK_OAK || m == Material.BOAT_JUNGLE || m == Material.BOAT_SPRUCE || m == Material.FENCE ||
						m == Material.FENCE_GATE || m == Material.LOG || m == Material.LOG_2)
					{
						Location l1 = e.getLocation();
						l1.setY(l1.getY()+0.73);
						Location l2 = e.getLocation();
						l2.setY(l2.getY()+0.63);
						if(l1.getBlock().getType() == Material.STATIONARY_WATER)
						{
							/*int n = 8;
							double g = -0.03*e.getVelocity().getY()/Config.VELOCITY*3.2;//0.07
							System.out.println(e.getVelocity().getY() +" "+g+" "+ Config.VELOCITY/n);
							e.setVelocity(new Vector(e.getVelocity().getX()*0.09,e.getVelocity().getY()*0.9+g+Config.VELOCITY/n,e.getVelocity().getZ()*0.09));*/
							e.setVelocity(new Vector(e.getVelocity().getX()*0.09, Math.min(e.getVelocity().getY()*0.9+Config.VELOCITY/4,Config.VELOCITY) ,e.getVelocity().getZ()*0.09));
							e.setGravity(false);
							//System.out.println(e.getLocation().getY()+" "+l1.getY()+" "+l2.getY());
						}
						else if(l2.getBlock().getType() == Material.STATIONARY_WATER && e.getVelocity().getY()>=0)
						{
							Location end = e.getLocation();
							end.setY(end.getBlockY()+1-0.68);
							e.teleport(end);
							e.setVelocity(new Vector(e.getVelocity().getX()*0.09,0,e.getVelocity().getZ()*0.09));
							e.setGravity(false);
							//System.out.println(e.getLocation().getY()+" "+l1.getY()+" "+l2.getY());
						}
						else
						{
							e.setGravity(true);
						}
					}
				}
				
			}
		}
	}
	
	public static double distance(double l1,double l2,double l3,double l4,double l5,double l6)
	{
		return Math.sqrt((l1-l4)*(l1-l4)+(l2-l5)*(l2-l5)+(l3-l6)*(l3-l6));
	}
	
	public static void playBottleOutSound(Player p) {
		p.playSound(p.getLocation(), Sound.ITEM_BOTTLE_EMPTY, 1F, 0.7F);
	}
	
	public static void playBottleInSound(Player p) {
		p.playSound(p.getLocation(), Sound.ITEM_BOTTLE_FILL, 1F, 0.7F);
	}
	
	/*public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        if(p.getItemInHand().containsEnchantment(Enchantment.DIG_SPEED)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, amplifier));
            }
        }
    }*/
	
	public static int sgn(double x)
	{
		if(x == 0)
			return 0;
		if(x > 0)
			return 1;
		return -1;
	}
	
	public int find(List<LCPlayer> playerList2, UUID tofind)
	{
		int i = playerList2.size()-1;
		while(i >= 0 && playerList2.get(i).Tid != tofind)
			i -= 1;
		return i;
	}
	
	

	public int loadPlayer(LCPlayer p)
	{
		new File("plugins/LifeCompromise/playerdata/").mkdirs();
		File file = new File("plugins/LifeCompromise/playerdata/"+p.Tid);
	    if(!file.exists()){
	    	return -1000;
	    }
	    try {
	        FileReader fr = new FileReader(file);
	        int c,i = 0;
        	c = fr.read();
	        p.isOnLC = (c=='1') ? true : false;
	        String str = "";
	        c = fr.read();
	        while(c != -1){
	        	if(c == ' ') {
	        		if(i == 0) p.T = Double.parseDouble(str);
	        		if(i == 1) p.Thirst = Double.parseDouble(str);
	        		if(i == 2) p.NormalT = Double.parseDouble(str);
	        		i+=1;
	        		str = "";
	        	}
	        	else str += (char)c;
	        	c = fr.read();
	        }
	        if(p.NormalT == null) p.NormalT = Config.skinTemperature;
	    	if(p.T == null) p.T = p.NormalT;
	        if(p.Thirst == null) p.Thirst = 100.0;
	        p.setPlayer(getServer().getPlayer(p.Tid));
	        fr.close();
			return 0;
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	public void savePlayer(LCPlayer p)
	{

		File file = new File("plugins/LifeCompromise/playerdata/"+p.Tid);
		try {
	        FileWriter fw = new FileWriter(file);
	        file.delete();
	        file.createNewFile();
	        fw.write((p.isOnLC ? 1 : 0) + Double.toString(p.T)+" "+Double.toString(p.Thirst)+" "+Double.toString(p.NormalT)+" ");
	        fw.close();
	    } catch(IOException e) {
	        throw new RuntimeException(e);
	    }
	}
}


