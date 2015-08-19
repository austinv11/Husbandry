package com.austinv11.husbandry.event.handler;

import com.austinv11.husbandry.Husbandry;
import com.austinv11.husbandry.api.HusbandryAPI;
import com.austinv11.husbandry.api.genetics.Traits;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntityHandler {
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityLiving) { //No players allowed
			try {
				if (!Traits.hasTraits((EntityLiving) event.entity)) {
					if (HusbandryAPI.retrieveRegistry().getEntities().containsKey(event.entity.getClass())) {
						Traits traits = Traits.generateTraits((EntityLiving) event.entity);
						traits.writeToAnimal((EntityLiving) event.entity);
					}
				}
			} catch (Exception e) {
				Husbandry.LOGGER.error("Unexpected exception generating traits! Please report this");
				e.printStackTrace();
			}
		}
	}
}
