package com.kamesuta.mc.carrotmod;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

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
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(this.bubu);
	}

	@EventHandler
	public void serverLoad(final FMLServerStartingEvent event) {
		event.registerServerCommand(this.command);
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(final Map<String, String> mods, final Side side) {
		return true;
	}
}
