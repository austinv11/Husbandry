package com.austinv11.husbandry.api.genetics;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Represents a gene that can only be obtained through mutation
 */
public abstract class MutatedGene extends Gene {
	
	/**
	 * This constructor is used only for methods which should really be static
	 * Said methods:
	 * -{@link MutatedGene#getTransmissionChance()}
	 * -{@link MutatedGene#canTransmit(EntityLiving)}
	 * -{@link NaturalGene#getSpawnChance()}
	 * -{@link NaturalGene#canSpawn(EntityLiving)}
	 */
	public MutatedGene() {}
	
	/**
	 * This is used to construct and initialize genes for an animal
	 * This is only used if the gene is a {@link NaturalGene} as there are no parents when an animal is generated and
	 * if the gene is manually inserted into the animal
	 * @param animal The entity this gene is being constructed for
	 */
	public MutatedGene(EntityLiving animal) {}
	
	/**
	 * This is used to construct and initialize genes for an animal from mating
	 * @param animal The entity this gene is being constructed for
	 * @param father The father of the entity
	 * @param mother The mother of the entity
	 */
	public MutatedGene(EntityLiving animal, EntityLiving father, EntityLiving mother) {}
	
	/**
	 * This is used to construct genes when the nbt of an animal is loaded
	 * @param animal The entity this gene is being constructed for
	 * @param tag The gene's tag
	 */
	public MutatedGene(EntityLiving animal, NBTTagCompound tag) {}
	
	/**
	 * The percentage that this gene will be transmitted through mating
	 * @return The percentage out of 1
	 */
	public abstract float getTransmissionChance();
	
	/**
	 * Returns whether this gene can be transmitted to a child
	 * This is independent of {@link #getTransmissionChance()}!
	 * @param child The child
	 * @return True if the gene can be transmitted, false if otherwise
	 */
	public abstract boolean canTransmit(EntityLiving child);
}
