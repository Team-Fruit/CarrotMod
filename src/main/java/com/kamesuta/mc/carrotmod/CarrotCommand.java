package com.kamesuta.mc.carrotmod;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CarrotCommand implements ICommand {
	protected final CarrotBubu bubu;

	private List<String> aliases;

	public CarrotCommand() {
		bubu = new CarrotBubu();
		FMLCommonHandler.instance().bus().register(bubu);

		this.aliases = new ArrayList<String>();
		this.aliases.add("carrot");
		this.aliases.add("h");
	}

	@Override
	public String getCommandName() {
		return "carrot";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "carrot <command>";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length >= 1 && "bubu".equals(astring[0])) {
			int count = parseIntDefault(astring[astring.length-1], -1);

			EntityPlayer player;
			if (icommandsender instanceof EntityPlayer) {
				player = (EntityPlayerMP) icommandsender;
				if (astring.length >= 2 && !(parseIntDefault(astring[1], -1) >= 0)) {
					if (MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
						EntityPlayer reciever = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(astring[1]);
						if (reciever != null) {
							sendServerChat(new ChatComponentText(
									icommandsender.getCommandSenderName() + " had " + reciever.getCommandSenderName() + " BUBUed!")
											.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
							bubu.addPlayer(reciever, count);
						} else {
							sendClientChat(icommandsender, new ChatComponentText("Invailed name " + astring[1] + ".").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
						}
					} else {
						sendClientChat(icommandsender, new ChatComponentText("You don't have permission to use [bubu.other]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					}
				} else {
					bubu.addPlayer(player, count);
				}
			} else {
				if (astring.length >= 2) {
					EntityPlayer reciever = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(astring[1]);
					bubu.addPlayer(reciever, count);
				} else {
					sendClientChat(icommandsender, new ChatComponentText("Usage /h bubu <name>").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
			}
		} else {
			sendClientChat(icommandsender, new ChatComponentText("/carrot <-bubu>"));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	public static int parseIntDefault(String s, int d)
	{
		int count = d;
		try {
			count = Integer.parseInt(s);
		} catch (NumberFormatException e) {}
		return count;
	}

	public static void sendServerChat(IChatComponent chat)
	{
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chat);
	}

	public static void sendClientChat(ICommandSender sender, IChatComponent chat)
	{
		sender.addChatMessage(chat);
	}
}