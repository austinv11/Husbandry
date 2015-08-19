package com.austinv11.husbandry.test;

import com.austinv11.husbandry.api.genetics.NaturalGene;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.nbt.NBTTagCompound;

public class TestGene extends NaturalGene {
	
	public TestGene() {
		super();
	}
	
	public TestGene(EntityLiving animal) {
		super(animal);
	}
	
	public TestGene(EntityLiving animal, EntityLiving father, EntityLiving mother) {
		super(animal, father, mother);
	}
	
	public TestGene(EntityLiving animal, NBTTagCompound tag) {
		super(animal, tag);
	}
	
	@Override
	public float getSpawnChance() {
		return 1F;
	}
	
	@Override
	public boolean canSpawn(EntityLiving animal) {
		return animal instanceof EntityPig;
	}
	
	@Override
	public float getTransmissionChance() {
		return 1F;
	}
	
	@Override
	public boolean canTransmit(EntityLiving child) {
		return child instanceof EntityPig;
	}
	
	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("test?", "test!");
		return tag;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "test";
	}
	
	@Override
	public boolean isRecessive() {
		return false;
	}
	
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		
	}
	
	@Override
	public void activate() {
		
	}
}
