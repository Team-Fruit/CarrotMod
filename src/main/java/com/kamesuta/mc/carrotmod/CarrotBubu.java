package com.kamesuta.mc.carrotmod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;

public class CarrotBubu {
	private List<BubingPlayer> bubuingplayers = new LinkedList<BubingPlayer>();

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		for (Iterator<BubingPlayer> it = bubuingplayers.iterator(); it.hasNext();)
		{
			BubingPlayer bubu = it.next();

			DamageSource source = new EntityDamageSource("explosion.player", bubu.player).setExplosion().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
//			DamageSource source = new DamageSource("explosion").setExplosion().setDamageBypassesArmor().setDamageAllowedInCreativeMode();

			if (bubu.shouldBubu())
			{
				bubu.player.addChatMessage(new ChatComponentText("× " + bubu.player.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				bubu.player.worldObj.createExplosion(
						bubu.player,
						bubu.player.getPlayerCoordinates().posX,
						bubu.player.getPlayerCoordinates().posY,
						bubu.player.getPlayerCoordinates().posZ,
						2F,
						false
				);
				bubu.player.attackEntityFrom(source, Float.MIN_VALUE);
			} else {
				bubu.player.attackEntityFrom(source, Float.MAX_VALUE);
				if (bubu.player.isDead) {
					it.remove();
				}
			}
			bubu.num++;
		}
	}

	public void addPlayer(EntityPlayer player, int count)
	{
		CarrotCommand.sendServerChat(new ChatComponentText(player.getCommandSenderName() + "> BUBUBUBUBUBUBUBU!!!!!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
		bubuingplayers.add(new BubingPlayer(player, count));
	}

	private class BubingPlayer {
		public static final int DefaultBubuCount = 50;
		public int maxnum;
		public int num;
		public EntityPlayer player;

		public BubingPlayer(EntityPlayer player, int maxnum)
		{
			this.player = player;
			this.maxnum = (maxnum >= 0) ? maxnum : DefaultBubuCount;
		}

		public boolean shouldBubu()
		{
			return this.num <= this.maxnum;
		}
	}
}
