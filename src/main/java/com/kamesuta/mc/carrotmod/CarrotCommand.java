package com.kamesuta.mc.carrotmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CarrotCommand extends CommandBase {
	private final CarrotBubu bubu;

	public CarrotCommand(final CarrotBubu bubu) {
		this.bubu = bubu;
	}

	@Override
	public List<String> getCommandAliases()
	{
		return Arrays.asList("h");
	}

	@Override
	public String getCommandName()
	{
		return "carrot";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public String getCommandUsage(final ICommandSender icommandsender) {
		return "carrot <command>";
	}

	@Override
	public void processCommand(final ICommandSender icommandsender, final String[] astring) {
		if (astring.length >= 1 && (StringUtils.equalsIgnoreCase(astring[0], "bubu") || StringUtils.equalsIgnoreCase(astring[0], "!bubu")))
		{
			final boolean bubu = StringUtils.equalsIgnoreCase(astring[0], "bubu");
			final int count = parseIntDefault(astring[astring.length-1], -1);
			if (astring.length >= 2 && !(parseIntDefault(astring[1], -1) >= 0)) {
				final boolean hasPermission = (!(icommandsender instanceof EntityPlayer)) || MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer)icommandsender).getGameProfile());
				if(hasPermission) {
					final EntityPlayerMP entityplayermp = getPlayer(icommandsender, astring[1]);
					if (entityplayermp == null)
					{
						throw new PlayerNotFoundException();
					}
					else
					{
						final IChatComponent chatcomponent = ChatUtil.byText(icommandsender.getCommandSenderName() + " had " + entityplayermp.getCommandSenderName() + " BUBUed!");
						chatcomponent.getChatStyle().setColor(EnumChatFormatting.GOLD).setItalic(true);
						ChatUtil.sendServerChat(chatcomponent);
						if (bubu) this.bubu.addPlayer(getCommandSenderAsPlayer(entityplayermp), count); else this.bubu.removePlayer(getCommandSenderAsPlayer(entityplayermp));
					}
				} else {
					ChatUtil.sendPlayerChat(icommandsender, ChatUtil.byText("You don't have permission to use [bubu.other]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
			} else {
				if (bubu) this.bubu.addPlayer(getCommandSenderAsPlayer(icommandsender), count); else this.bubu.removePlayer(getCommandSenderAsPlayer(icommandsender));
			}
		} else if (astring.length >= 1 && StringUtils.equalsIgnoreCase(astring[0], "me")) {
			final IChatComponent c0 = getNameWithItem(icommandsender);
			final String chat = func_82360_a(icommandsender, astring, 1);
			final String chatcolor = chat.replaceAll("&", "\u00A7");
			ChatUtil.sendServerChat(ChatUtil.byTranslation("chat.type.emote", c0, chatcolor));
		} else if (astring.length >= 1 && StringUtils.equalsIgnoreCase(astring[0], "t")) {
			final IChatComponent c0 = getNameWithItem(icommandsender);
			final String chat = func_82360_a(icommandsender, astring, 1);
			final String chatcolor = chat.replaceAll("&", "\u00A7");
			ChatUtil.sendServerChat(ChatUtil.byTranslation("chat.type.text", c0, chatcolor));
		} else if (astring.length >= 2 && (StringUtils.equalsIgnoreCase(astring[0], "tell") || StringUtils.equalsIgnoreCase(astring[0], "w") || StringUtils.equalsIgnoreCase(astring[0], "msg"))) {
			final EntityPlayerMP entityplayermp = getPlayer(icommandsender, astring[1]);
			if (entityplayermp == null)
			{
				throw new PlayerNotFoundException();
			}
			else
			{
				final String chat = func_82360_a(icommandsender, astring, 2);
				final String chatcolor = chat.replaceAll("&", "\u00A7");
				final IChatComponent c0 = getNameWithItem(icommandsender);
				final IChatComponent chatcomponenttranslation = ChatUtil.byTranslation("commands.message.display.incoming", c0, chatcolor);
				final IChatComponent chatcomponenttranslation1 = ChatUtil.byTranslation("commands.message.display.outgoing", entityplayermp.func_145748_c_(), chatcolor);
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				entityplayermp.addChatMessage(chatcomponenttranslation);
				icommandsender.addChatMessage(chatcomponenttranslation1);

				final String[] msg =astring[2].split(" ");
				final List<String> links = new ArrayList<String>();
				final String[] linkstr = {"http://", "https://"};
				for (final String str : msg) {
					for (final String str1 : linkstr) {
						final int index = str.indexOf(str1);
						if (index != -1)
							links.add(str.substring(index).trim());
					}
				}
				if (!links.isEmpty()) {
					final IChatComponent line = ChatUtil.byText("");
					final boolean oneLink = links.size() == 1;
					for(int i = 0; i < links.size(); i++) {
						final String link = links.get(i);
						final IChatComponent c = ChatUtil.byText(oneLink ? "[ Link ]" : ("[ Link #" + (i + 1) + " ]"));
						c.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtil.byText(link)));
						c.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
						line.appendSibling(c);
						if(!oneLink)
							line.appendSibling(ChatUtil.byText(" "));
					}
					ChatUtil.sendPlayerChat(entityplayermp, line);
				}
			}
		} else {
			ChatUtil.sendPlayerChat(icommandsender, ChatUtil.byText("/carrot <bubu|me|t|tell>"));
		}
	}

	public static ItemStack getItem(final ICommandSender icommandsender) {
		ItemStack item = null;
		if (icommandsender instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer)icommandsender;
			item = player.getHeldItem();
		}
		return item;
	}

	public static IChatComponent getNameWithItem(final ICommandSender icommandsender) {
		final IChatComponent c0 = icommandsender.func_145748_c_();
		final ItemStack item = getItem(icommandsender);
		if (item != null) c0.appendSibling(item.func_151000_E());
		return c0;
	}

	@Override
	public List<String> addTabCompletionOptions(final ICommandSender icommandsender, final String[] astring) {
		if (astring.length <= 1)
			return Arrays.asList("bubu", "t", "me", "tell");
		else
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
}