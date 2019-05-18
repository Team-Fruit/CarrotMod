package com.kamesuta.mc.carrotmod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class CarrotBubu {
	private final List<BubingPlayer> bubuingplayers = new LinkedList<BubingPlayer>();

	@SubscribeEvent
	public void onServerTick(final ServerTickEvent event) {
		for (final Iterator<BubingPlayer> it = this.bubuingplayers.iterator(); it.hasNext();) {
			final BubingPlayer bubu = it.next();

			if (bubu.shouldBubu()) {
				ChatBuilder.sendPlayer(bubu.player, bubu.message);
				bubu.player.world.createExplosion(
						bubu.player,
						bubu.player.getPosition().getX(),
						bubu.player.getPosition().getY(),
						bubu.player.getPosition().getZ(),
						2F,
						false);
				bubu.player.attackEntityFrom(bubu.source, Float.MIN_VALUE);
			} else {
				bubu.player.attackEntityFrom(bubu.source, Float.MAX_VALUE);
				if (bubu.player.isDead)
					it.remove();
			}

			bubu.next();
		}
	}

	@SubscribeEvent
	public void onLoggedOut(final PlayerLoggedOutEvent event) {
		removePlayer(event.player);
	}

	public void addPlayer(final EntityPlayer player, final int count) {
		final ITextComponent c0 = player.getDisplayName();
		ChatBuilder.sendServer(ChatBuilder.create("chat.type.text").useTranslation().setParams(c0, "BUBUBUBUBUBUBUBU!!!!!").setStyle(new Style().setColor(TextFormatting.GOLD)));
		this.bubuingplayers.add(new BubingPlayer(player, count));
	}

	public void removePlayer(final EntityPlayer player) {
		final String name = player.getDisplayNameString();
		for (final Iterator<BubingPlayer> it = this.bubuingplayers.iterator(); it.hasNext();) {
			final BubingPlayer bubu = it.next();

			if (StringUtils.equals(name, bubu.player.getDisplayNameString()))
				it.remove();
		}
	}

	private class BubingPlayer {
		public static final int DefaultBubuCount = 50;
		public int remaining;

		public EntityPlayer player;
		public ChatBuilder message;
		public DamageSource source;

		public BubingPlayer(final EntityPlayer player, final int maxnum) {
			this.player = player;
			this.message = ChatBuilder.create("BUBU! "+this.player.getDisplayNameString()).setStyle(new Style().setColor(TextFormatting.RED));
			this.source = new EntityDamageSource("explosion.player", player).setExplosion().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
			this.remaining = maxnum>=0 ? maxnum : DefaultBubuCount;
		}

		public boolean shouldBubu() {
			return this.remaining>0;
		}

		public void next() {
			this.remaining--;
		}
	}
}
