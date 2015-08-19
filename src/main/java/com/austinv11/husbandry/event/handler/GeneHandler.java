package com.austinv11.husbandry.event.handler;

import com.austinv11.husbandry.api.genetics.Gene;
import com.austinv11.husbandry.api.genetics.Traits;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingEvent;

public class GeneHandler { //Handles all events related to genes
	
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityLiving) { //We don't want to mess with players
			if (Traits.hasTraits((EntityLiving) event.entityLiving)) {
				Traits traits = new Traits((EntityLiving) event.entityLiving);
				for (Gene gene : traits.getActiveGenes())
					gene.onUpdate();
			}
		}
	}
}
