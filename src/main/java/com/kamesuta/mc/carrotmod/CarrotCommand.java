package com.kamesuta.mc.carrotmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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

	//	protected static final Pattern p = Pattern.compile("https?://[-_.!~*\'()a-zA-Z0-9;/?:@&=+$,%#]+");

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
			final int count = NumberUtils.toInt(astring[astring.length-1], -1);
			if (astring.length >= 2 && !(NumberUtils.toInt(astring[1], -1) >= 0)) {
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
				if (bubu) {
					this.bubu.addPlayer(getCommandSenderAsPlayer(icommandsender), count);
				} else {
					this.bubu.removePlayer(getCommandSenderAsPlayer(icommandsender));
					func_152373_a(icommandsender, this, "canceled BUBU!.", new Object[0]);
				}
			}
		} else if (astring.length >= 1 && StringUtils.equalsIgnoreCase(astring[0], "me")) {
			final IChatComponent c0 = getNameWithItem(icommandsender);
			final String chat = func_82360_a(icommandsender, astring, 1);
			if (c0 != null || StringUtils.isNotBlank(chat)) {
				final String chatcolor = chat.replaceAll("&", "\u00A7");
				ChatUtil.sendServerChat(ChatUtil.byTranslation("chat.type.emote", c0, chatcolor));
			}
		} else if (astring.length >= 1 && StringUtils.equalsIgnoreCase(astring[0], "t")) {
			final IChatComponent c0 = getNameWithItem(icommandsender);
			final String chat = func_82360_a(icommandsender, astring, 1);
			if (c0 != null || StringUtils.isNotBlank(chat)) {
				final String chatcolor = chat.replaceAll("&", "\u00A7");
				ChatUtil.sendServerChat(ChatUtil.byTranslation("chat.type.text", c0, chatcolor));
			}
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
				if (c0 != null || StringUtils.isNotBlank(chat))
					return;
				final IChatComponent chatcomponenttranslation = ChatUtil.byTranslation("commands.message.display.incoming", c0, chatcolor);
				final IChatComponent chatcomponenttranslation1 = ChatUtil.byTranslation("commands.message.display.outgoing", entityplayermp.func_145748_c_(), chatcolor);
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				entityplayermp.addChatMessage(chatcomponenttranslation);
				icommandsender.addChatMessage(chatcomponenttranslation1);

				final List<String> links = new ArrayList<String>();
				final String[] linkstr = {"http://", "https://"};
				for (final String str : linkstr) {
					final int index = str.indexOf(str);
					if (index != -1)
						links.add(str.substring(index).trim());
				}
				if (!links.isEmpty()) {
					final IChatComponent line = ChatUtil.byText("");
					final boolean oneLink = links.size() == 1;
					for(int i = 0; i < links.size(); i++) {
						final String link = links.get(i);
						final IChatComponent c = ChatUtil.byText(oneLink ? "[ Link ]" : ("[ Link #" + (i + 1) + " ]"));
						c.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtil.byText(link)));
						c.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
						c.getChatStyle().setColor(EnumChatFormatting.GOLD);
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
		if (item != null)
			c0.appendSibling(item.func_151000_E());
		else
			return null;
		return c0;
	}

	@Override
	public List<String> addTabCompletionOptions(final ICommandSender icommandsender, final String[] astring) {
		if (astring.length <= 1) {
			return Arrays.asList("bubu", "t", "me", "tell", "msg");
		} else if (astring.length <= 2) {
			return getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames());
		} else {
			return null;
		}
	}

	@Override
	public boolean isUsernameIndex(final String[] astring, final int i) {
		return false;
	}

	@Override
	public int compareTo(final Object o) {
		return 0;
	}

}