package com.kamesuta.mc.carrotmod;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class CarrotCommand extends CommandBase {
	private final CarrotBubu bubu;

	//	protected static final Pattern p = Pattern.compile("https?://[-_.!~*\'()a-zA-Z0-9;/?:@&=+$,%#]+");

	public CarrotCommand(final CarrotBubu bubu) {
		this.bubu = bubu;
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("h");
	}

	@Override
	public String getName() {
		return "carrot";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return true;
	}

	@Override
	public String getUsage(final ICommandSender icommandsender) {
		return "h <command>";
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		if (args.length>=1&&(StringUtils.equalsIgnoreCase(args[0], "bubu")||StringUtils.equalsIgnoreCase(args[0], "!bubu"))) {
			final boolean bubu = StringUtils.equalsIgnoreCase(args[0], "bubu");
			final int count = NumberUtils.toInt(args[args.length-1], -1);
			if (args.length>=2&&!(NumberUtils.toInt(args[1], -1)>=0)) {
				final boolean hasPermission = !(sender instanceof EntityPlayer)||sender.canUseCommand(2, "");
				if (hasPermission) {
					final EntityPlayerMP entityplayermp = getPlayer(server, sender, args[1]);
					ChatBuilder.sendServer(ChatBuilder.create(sender.getName()+" had "+entityplayermp.getName()+" BUBUed!")
							.setStyle(new Style().setColor(TextFormatting.GOLD).setItalic(true)));
					if (bubu)
						this.bubu.addPlayer(getCommandSenderAsPlayer(entityplayermp), count);
					else
						this.bubu.removePlayer(getCommandSenderAsPlayer(entityplayermp));
				} else
					ChatBuilder.create("You don't have permission to use [bubu.other]").setStyle(new Style().setColor(TextFormatting.RED)).sendPlayer(sender);
			} else if (bubu)
				this.bubu.addPlayer(getCommandSenderAsPlayer(sender), count);
			else {
				if (args.length>=2&&StringUtils.equalsIgnoreCase(args[1], "@a"))
					this.bubu.clear();

				this.bubu.removePlayer(getCommandSenderAsPlayer(sender));
				notifyCommandListener(sender, this, "canceled BUBU!.", new Object[0]);
			}
		} else if (args.length>=1&&StringUtils.equalsIgnoreCase(args[0], "me")) {
			final ItemStack item = getItem(sender);
			final ITextComponent c0 = getNameWithItem(sender, item);
			final String chat = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer)).getUnformattedText();
			if (item!=null||StringUtils.isNotBlank(chat)) {
				final String chatcolor = chat.replaceAll("&", "\u00A7");
				ChatBuilder.sendServer(ChatBuilder.create("chat.type.emote").useTranslation().setParams(c0, chatcolor));
			}
		} else if (args.length>=1&&StringUtils.equalsIgnoreCase(args[0], "t")) {
			final ItemStack item = getItem(sender);
			final ITextComponent c0 = getNameWithItem(sender, item);
			final String chat = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer)).getUnformattedText();
			if (item!=null||StringUtils.isNotBlank(chat)) {
				final String chatcolor = chat.replaceAll("&", "\u00A7");
				ChatBuilder.sendServer(ChatBuilder.create("chat.type.text").useTranslation().setParams(c0, chatcolor));
				//				sendLinkChat(sender, args);
			}
		} else if (args.length>=2&&(StringUtils.equalsIgnoreCase(args[0], "tell")||StringUtils.equalsIgnoreCase(args[0], "w")||StringUtils.equalsIgnoreCase(args[0], "msg"))) {
			final EntityPlayerMP entityplayermp = getPlayer(server, sender, args[1]);
			final ItemStack item = getItem(sender);
			final String chat = getChatComponentFromNthArg(sender, args, 2, !(sender instanceof EntityPlayer)).getUnformattedText();
			final String chatcolor = chat.replaceAll("&", "\u00A7");
			final ITextComponent c0 = getNameWithItem(sender, item);
			if (item!=null||StringUtils.isNotBlank(chat)) {
				ChatBuilder.sendPlayer(entityplayermp, ChatBuilder.create("commands.message.display.incoming").useTranslation().setParams(c0, chatcolor).setStyle(new Style().setColor(TextFormatting.GRAY).setItalic(true)));
				ChatBuilder.sendPlayer(sender, ChatBuilder.create("commands.message.display.outgoing").useTranslation().setParams(entityplayermp.getDisplayName(), chatcolor).setStyle(new Style().setColor(TextFormatting.GRAY).setItalic(true)));
				//					sendLinkChat(sender, args);

			}
			//		} else if (StringUtils.equalsIgnoreCase(args[0], "allplayer")&&sender.equals(MinecraftServer.getServer())) {
			//			final MinecraftServer s = MinecraftServer.getServer();
			//			Reference.logger.info("CurrentPlayerCount: {}", s.getCurrentPlayerCount());
			//			final String[] userNames = s.getAllUsernames();
			//			if (userNames.length>0)
			//				Reference.logger.info(StringUtils.join(userNames, " "));
		} else
			throw new WrongUsageException("/h <bubu|me|t|tell>", new Object[0]);
	}

	public static ItemStack getItem(final ICommandSender icommandsender) {
		ItemStack item = null;
		if (icommandsender instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) icommandsender;
			item = player.getHeldItemMainhand();

			if (item==ItemStack.EMPTY)
				item = null;
		}
		return item;
	}

	public static ITextComponent getNameWithItem(final ICommandSender icommandsender, final ItemStack item) {
		final ITextComponent c0 = icommandsender.getDisplayName();
		if (item!=null)
			c0.appendSibling(item.getTextComponent());
		return c0;
	}

	/*	public static void sendLinkChat(final ICommandSender icommandsender, final String[] astring) {
			final List<String> links = new ArrayList<String>();
			final String[] linkstr = { "http://", "https://" };
			for (final String str : astring)
				for (final String str1 : linkstr) {
					final int index = str.indexOf(str1);
					if (index!=-1)
						links.add(str.substring(index).trim());
				}
			if (!links.isEmpty()) {
				final IChatComponent line = ChatUtil.byText("");
				final boolean oneLink = links.size()==1;
				for (int i = 0; i<links.size(); i++) {
					final String link = links.get(i);
					final ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtil.byText(link)))
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
							.setColor(EnumChatFormatting.GOLD);
					line.appendSibling(ChatUtil.byText(oneLink ? "[ Link ]" : "[ Link #"+(i+1)+" ]").setChatStyle(chatStyle));
					if (!oneLink)
						line.appendSibling(ChatUtil.byText(" "));
				}
				ChatUtil.sendPlayerChat(icommandsender, line);
			}
		}
	*/
	@Override
	public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
		if (args.length<=1)
			return getListOfStringsMatchingLastWord(args, "bubu", "t", "me", "tell", "msg");
		else if (args.length<=2)
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		else
			return null;
	}

	@Override
	public boolean isUsernameIndex(final String[] astring, final int i) {
		return i==2;
	}

}