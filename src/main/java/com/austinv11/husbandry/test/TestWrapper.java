package com.austinv11.husbandry.test;

import com.austinv11.husbandry.api.entity.IEntityWrapper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class TestWrapper implements IEntityWrapper {
	
	@Override
	public Class<? extends EntityLiving> getEntityClass() {
		return EntityPig.class;
	}
	
	@Override
	public Item getEgg(EntityLiving animal, EntityLiving mate) {
		return null;
	}
	
	@Override
	public long getMaxAge() {
		return 20;
	}
	
	@Override
	public Item[] getPrefferedBreedingItems() {
		return new Item[]{Items.porkchop};
	}
	
	@Override
	public Class<? extends EntityLiving>[] getPossibleMates() {
		return new Class[]{EntityChicken.class};
	}
	
	@Override
	public EntityLiving mate(EntityLiving animal, EntityLiving mate) {
		return null;
	}
}
