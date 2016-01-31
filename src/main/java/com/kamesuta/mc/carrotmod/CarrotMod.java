package com.kamesuta.mc.carrotmod;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = CarrotMod.modid, name = CarrotMod.modname, version = CarrotMod.modversion)
public class CarrotMod {
	public static final String modid = "carrotmod";
	public static final String modname = "Carrot Mod";
	public static final String modversion = "1.01";
	public static final Logger logger = LogManager.getLogger(modname);

	private CarrotCommand command = new CarrotCommand();

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Welcome to carrot.");
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(command);
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side)
	{
		return true;
	}
}
