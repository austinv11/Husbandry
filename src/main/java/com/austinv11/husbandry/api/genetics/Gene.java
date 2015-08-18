package com.austinv11.husbandry.api.genetics;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Represents a base gene. Do NOT implement this directly! Use the mutated or natural variations
 * @see MutatedGene For genes which can only occur through specifc breeding conditions
 * @see NaturalGene For genes which can occur naturally
 */
public abstract class Gene {
	
	/**
	 * This constructor is used only for methods which should really be static
	 * Said methods:
	 * -{@link MutatedGene#getTransmissionChance()}
	 * -{@link MutatedGene#canTransmit(EntityLiving)}
	 * -{@link NaturalGene#getSpawnChance()}
	 * -{@link NaturalGene#canSpawn(EntityLiving)}
	 */
	public Gene() {}
	
	/**
	 * This is used to construct and initialize genes for an animal
	 * This is only used if the gene is a {@link NaturalGene} as there are no parents when an animal is generated and
	 * if the gene is manually inserted into the animal
	 * @param animal The entity this gene is being constructed for
	 */
	public Gene(EntityLiving animal) {}
	
	/**
	 * This is used to construct and initialize genes for an animal from mating
	 * @param animal The entity this gene is being constructed for
	 * @param father The father of the entity
	 * @param mother The mother of the entity
	 */
	public Gene(EntityLiving animal, EntityLiving father, EntityLiving mother) {}
	
	/**
	 * This is used to construct genes when the nbt of an animal is loaded
	 * @param animal The entity this gene is being constructed for
	 * @param tag The gene's tag
	 */
	public Gene(EntityLiving animal, NBTTagCompound tag) {}
	
	/**
	 * Writes the gene to nbt
	 * @return The nbt tag representing the gene
	 */
	public abstract NBTTagCompound toNBT();
	
	/**
	 * Gets the unlocalized name of the gene, this is used for display and persistence
	 * @return The unlocalized name
	 */
	public abstract String getUnlocalizedName();
	
	/**
	 * Returns whether or not this gene is recessive
	 * If recessive, an animal must have two genes of this type to have an effect
	 * @return True if recessive, false if otherwise
	 */
	public abstract boolean isRecessive();
	
	/**
	 * Returns whether the gene is currently active on an animal
	 * If inactive, the gene will not work, preventing {@link #onUpdate()} from being called. This may be false
	 * due to having the wrong gender of animal, etc
	 * @return True if active, false if otherwise
	 */
	public abstract boolean isActive();
	
	/**
	 * Called when an animal with this gene is updated
	 */
	public abstract void onUpdate();
	
	/**
	 * Called when a gene is forcibly activated
	 */
	public abstract void activate();
	
	@Override
	public String toString() {
		return "[Gene "+getUnlocalizedName()+" from class "+this.getClass().getName()+"]";
	}
}
