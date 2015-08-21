package com.austinv11.husbandry.event.handler;

import com.austinv11.collectiveframework.minecraft.event.ProcreationEvent;
import com.austinv11.collectiveframework.minecraft.utils.Location;
import com.austinv11.collectiveframework.minecraft.utils.WorldUtils;
import com.austinv11.husbandry.Husbandry;
import com.austinv11.husbandry.api.HusbandryAPI;
import com.austinv11.husbandry.api.entity.IEntityWrapper;
import com.austinv11.husbandry.api.genetics.Gender;
import com.austinv11.husbandry.api.genetics.Traits;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.HashMap;

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
	
	@SubscribeEvent
	public void onEntityProcreate(ProcreationEvent.Pre event) {
		if (Traits.hasTraits(event.parent1) || Traits.hasTraits(event.parent2)) {
			EntityLiving father;
			EntityLiving mother;
			if (Traits.hasTraits(event.parent1)) {
				if (new Traits(event.parent1).gender == Gender.MALE) {
					father = event.parent1;
					mother = event.parent2;
				} else {
					mother = event.parent1;
					father = event.parent2;
				}
			} else {
				if (new Traits(event.parent2).gender == Gender.MALE) {
					father = event.parent2;
					mother = event.parent1;
				} else {
					mother = event.parent2;
					father = event.parent1;
				}
			}
			try {
				HashMap<Class, IEntityWrapper> entities = HusbandryAPI.retrieveRegistry().getEntities();
				if (entities.containsKey(mother.getClass())) {
					IEntityWrapper wrapper = entities.get(mother.getClass());
					Item egg = wrapper.getEgg(mother, father);
					if (egg != null) {
						WorldUtils.spawnItemInWorld(new Location(event.child.posX, event.child.posY, event.child.posZ,
								event.child.worldObj), new ItemStack(egg));
						event.setCanceled(true);
						return;
					} else {
						EntityLiving newChild = wrapper.mate(mother, father);
						if (newChild != null) {
							if (!Traits.hasTraits(newChild)) {
								Traits bredTraits = new Traits(father, mother, newChild);
								bredTraits.writeToAnimal(newChild);
							}
							event.child = (EntityAgeable) newChild; //TODO: Create a wrapper for EntityAgeable
							return;
						}
					}
				}
				if (entities.containsKey(father.getClass())) {
					IEntityWrapper wrapper = entities.get(father.getClass());
					Item egg = wrapper.getEgg(father, mother);
					if (egg != null) {
						WorldUtils.spawnItemInWorld(new Location(event.child.posX, event.child.posY, event.child.posZ,
								event.child.worldObj), new ItemStack(egg));
						event.setCanceled(true);
					} else {
						EntityLiving newChild = wrapper.mate(father, mother);
						if (!Traits.hasTraits(newChild)) {
							Traits bredTraits = new Traits(father, mother, newChild);
							bredTraits.writeToAnimal(newChild);
						}
						if (newChild != null) {
							event.child = (EntityAgeable) newChild; //TODO: Create a wrapper for EntityAgeable
						}
					}
				}
			} catch (Exception e) {
				Husbandry.LOGGER.error("Unexpected exception during entity mating! Please report this");
				e.printStackTrace();
			}
		}
	}
}
