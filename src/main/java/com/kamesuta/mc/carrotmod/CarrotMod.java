package com.kamesuta.mc.carrotmod;

import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class CarrotMod {
	public final CarrotBubu bubu = new CarrotBubu();
	public final CarrotCommand command = new CarrotCommand(this.bubu);

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Reference.logger = event.getModLog();
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		Reference.logger.info("Welcome to carrot.");
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this.bubu);
	}

	@EventHandler
	public void serverLoad(final FMLServerStartingEvent event)
	{
		event.registerServerCommand(this.command);
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(final Map<String, String> mods, final Side side)
	{
		return true;
	}
}
