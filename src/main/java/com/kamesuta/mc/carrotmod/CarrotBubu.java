package com.kamesuta.mc.carrotmod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CarrotBubu {
	private final List<BubingPlayer> bubuingplayers = new LinkedList<BubingPlayer>();

	@SubscribeEvent
	public void onServerTick(final ServerTickEvent event) {
		for (final Iterator<BubingPlayer> it = this.bubuingplayers.iterator(); it.hasNext();)
		{
			final BubingPlayer bubu = it.next();

			if (bubu.shouldBubu())
			{
				bubu.player.addChatMessage(bubu.message);
				bubu.player.worldObj.createExplosion(
						bubu.player,
						bubu.player.getPlayerCoordinates().posX,
						bubu.player.getPlayerCoordinates().posY,
						bubu.player.getPlayerCoordinates().posZ,
						2F,
						false
						);
				bubu.player.attackEntityFrom(bubu.source, Float.MIN_VALUE);
			} else {
				bubu.player.attackEntityFrom(bubu.source, Float.MAX_VALUE);
				if (bubu.player.isDead) {
					it.remove();
				}
			}

			bubu.next();
		}
	}

	public void addPlayer(final EntityPlayer player, final int count)
	{
		final IChatComponent c0 = CarrotCommand.getNameWithItem(player);
		ChatUtil.sendServerChat(ChatUtil.byTranslation("chat.type.text", c0, "BUBUBUBUBUBUBUBU!!!!!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
		this.bubuingplayers.add(new BubingPlayer(player, count));
	}

	public void removePlayer(final EntityPlayer player) {
		final String name = player.getCommandSenderName();
		for (final Iterator<BubingPlayer> it = this.bubuingplayers.iterator(); it.hasNext();)
		{
			final BubingPlayer bubu = it.next();

			if (StringUtils.equals(name, bubu.player.getCommandSenderName())) {
				final IChatComponent c0 = CarrotCommand.getNameWithItem(player);
				ChatUtil.sendServerChat(ChatUtil.byTranslation("chat.type.text", c0, "canceled BUBU!.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
				it.remove();
			}
		}
	}

	private class BubingPlayer {
		public static final int DefaultBubuCount = 50;
		public int remaining;

		public EntityPlayer player;
		public IChatComponent message;
		public DamageSource source;

		public BubingPlayer(final EntityPlayer player, final int maxnum)
		{
			this.player = player;
			this.message = ChatUtil.byText("BUBU! " + this.player.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
			this.source = new EntityDamageSource("explosion.player", player).setExplosion().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
			this.remaining = (maxnum >= 0) ? maxnum : DefaultBubuCount;
		}

		public boolean shouldBubu()
		{
			return this.remaining > 0;
		}

		public void next()
		{
			this.remaining--;
		}
	}
}
