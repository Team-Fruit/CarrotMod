package com.kamesuta.mc.carrotmod;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CarrotCommand extends CommandBase {
	protected final CarrotBubu bubu;

	private final List<String> aliases;

	public CarrotCommand() {
		this.bubu = new CarrotBubu();
		FMLCommonHandler.instance().bus().register(this.bubu);

		this.aliases = new ArrayList<String>();
		this.aliases.add("carrot");
		this.aliases.add("h");
	}

	@Override
	public String getCommandName() {
		return "carrot";
	}

	@Override
	public String getCommandUsage(final ICommandSender icommandsender) {
		return "carrot <command>";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender icommandsender, final String[] astring) {
		if (astring.length >= 1 && "bubu".equals(astring[0]))
		{
			final int count = parseIntDefault(astring[astring.length-1], -1);

			if (astring.length >= 2 && !(parseIntDefault(astring[1], -1) >= 0)) {
				final boolean hasPermission = (icommandsender instanceof EntityPlayer) ?
						MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer)icommandsender).getGameProfile()) :
							true;
						if(hasPermission) {
							//					EntityPlayer reciever = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(astring[1]);
							final EntityPlayerMP reciever = getPlayer(icommandsender, astring[1]);
							if ("-all".equals(astring[1])) {
								sendServerChat(new ChatComponentText(
										icommandsender.getCommandSenderName() + " had all players BUBUed!")
										.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
								final List<?> players = MinecraftServer.getServer().getEntityWorld().playerEntities;
								for (final Object playerobj : players)
								{
									final EntityPlayer play = (EntityPlayer) playerobj;
									this.bubu.addPlayer(play, count);
								}
							} else if ("-other".equals(astring[1])) {
								sendServerChat(new ChatComponentText(
										icommandsender.getCommandSenderName() + " had all players BUBUed!")
										.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
								final List<?> players = MinecraftServer.getServer().getEntityWorld().playerEntities;
								for (final Object playerobj : players)
								{
									final EntityPlayer play = (EntityPlayer) playerobj;
									if (!icommandsender.equals(play)) this.bubu.addPlayer(play, count);
								}
							} else if (reciever != null) {
								sendServerChat(new ChatComponentText(
										icommandsender.getCommandSenderName() + " had " + reciever.getCommandSenderName() + " BUBUed!")
										.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
								this.bubu.addPlayer(reciever, count);
							} else {
								sendClientChat(icommandsender, new ChatComponentText("Invailed name " + astring[1] + ".").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
							}
						} else {
							sendClientChat(icommandsender, new ChatComponentText("You don't have permission to use [bubu.other]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
						}
			} else {
				if (icommandsender instanceof EntityPlayer) {
					this.bubu.addPlayer(((EntityPlayer)icommandsender), count);
				} else {
					sendClientChat(icommandsender, new ChatComponentText("Usage /h bubu <[name]|-all|-other>").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
			}
		} else if (astring.length >= 1 && "me".equals(astring[0])) {
			IChatComponent c0 = icommandsender.func_145748_c_();
			final IChatComponent ichatcomponent = func_147176_a(icommandsender, astring, 1, icommandsender.canCommandSenderUseCommand(1, "carrot"));
			if (icommandsender instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer)icommandsender;
				final ItemStack item = player.getHeldItem();
				if (item != null) {
					c0 = c0.appendSibling(item.func_151000_E());
				}
			}
			sendServerChat(new ChatComponentTranslation("chat.type.emote", new Object[] {c0, ichatcomponent}));
		} else {
			sendClientChat(icommandsender, new ChatComponentText("/carrot <-bubu>"));
		}
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public List<String> addTabCompletionOptions(final ICommandSender icommandsender, final String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(final String[] astring, final int i) {
		return false;
	}

	@Override
	public int compareTo(final Object o) {
		return 0;
	}

	public static int parseIntDefault(final String s, final int d)
	{
		int count = d;
		try {
			count = Integer.parseInt(s);
		} catch (final NumberFormatException e) {}
		return count;
	}

	public static void sendServerChat(final IChatComponent chat)
	{
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chat);
	}

	public static void sendClientChat(final ICommandSender sender, final IChatComponent chat)
	{
		sender.addChatMessage(chat);
	}
}