package com.austinv11.husbandry.compat.waila;

import com.austinv11.husbandry.Husbandry;
import com.austinv11.husbandry.api.genetics.Gender;
import com.austinv11.husbandry.api.genetics.Gene;
import com.austinv11.husbandry.api.genetics.Traits;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class HusbandryEntityProvider implements IWailaEntityProvider {
	
	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return accessor.getEntity();
	}
	
	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		if (accessor.getNBTData().hasKey("traits") || Traits.hasTraits((EntityLiving) entity)) {
			if (accessor.getNBTData().hasKey("traits"))
				entity.getEntityData().setTag("genes", accessor.getNBTData());
			Traits traits = new Traits((EntityLiving) entity);
			String genderSymbol;
			if (traits.gender == Gender.UNKNOWN)
				genderSymbol = "\u263F";
			else {
				genderSymbol = traits.gender == Gender.MALE ? SpecialChars.BLUE+"\u2642" : SpecialChars.LPURPLE+"\u2640";
			}
			currenttip.add(traits.gender.getLocalizedName()+genderSymbol);
			if (!Husbandry.proxy.isShiftDown()) {
				currenttip.add("<"+StatCollector.translateToLocal("husbandry.waila.moreinfo.1")+">");
				currenttip.add("<"+StatCollector.translateToLocal("husbandry.waila.moreinfo.2")+">");
			} else if (Husbandry.proxy.isCtrlDown()) {
				List<Gene> activeGenes = traits.getActiveGenes();
				for (Gene gene : traits.fatherGenes) {
					String additionalSymbols = "";
					if (Traits.searchForGene(activeGenes, gene) != -1) {
						additionalSymbols = additionalSymbols+SpecialChars.GREEN+"\u2611";
					} else {
						additionalSymbols = additionalSymbols+SpecialChars.RED+"\u2612";
					}
					additionalSymbols = additionalSymbols+SpecialChars.LPURPLE+"\u24DC";
					currenttip.add(StatCollector.translateToLocal(gene.getUnlocalizedName())+additionalSymbols);
				}
			} else {
				List<Gene> activeGenes = traits.getActiveGenes();
				for (Gene gene : traits.fatherGenes) {
					String additionalSymbols = "";
					if (Traits.searchForGene(activeGenes, gene) != -1) {
						additionalSymbols = additionalSymbols+SpecialChars.GREEN+"\u2611";
					} else {
						additionalSymbols = additionalSymbols+SpecialChars.RED+"\u2612";
					}
					additionalSymbols = additionalSymbols+SpecialChars.BLUE+"\u24DF";
					currenttip.add(StatCollector.translateToLocal(gene.getUnlocalizedName())+additionalSymbols);
				}
			}
		}
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
		if (Traits.hasTraits((EntityLiving) ent)) {
			Traits traits = new Traits((EntityLiving) ent);
			tag.setTag("traits", traits.writeToTag());
		}
		return tag;
	}
}
