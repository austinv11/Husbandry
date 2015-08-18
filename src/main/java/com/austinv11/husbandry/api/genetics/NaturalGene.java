package com.austinv11.husbandry.api.genetics;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Represents a gene that can naturally occur
 */
public abstract class NaturalGene extends MutatedGene {
	
	/**
	 * This constructor is used only for methods which should really be static
	 * Said methods:
	 * -{@link MutatedGene#getTransmissionChance()}
	 * -{@link MutatedGene#canTransmit(EntityLiving)}
	 * -{@link NaturalGene#getSpawnChance()}
	 * -{@link NaturalGene#canSpawn(EntityLiving)}
	 */
	public NaturalGene() {}
	
	/**
	 * This is used to construct and initialize genes for an animal
	 * This is only used if the gene is a {@link NaturalGene} as there are no parents when an animal is generated and
	 * if the gene is manually inserted into the animal
	 * @param animal The entity this gene is being constructed for
	 */
	public NaturalGene(EntityLiving animal) {}
	
	/**
	 * This is used to construct and initialize genes for an animal from mating
	 * @param animal The entity this gene is being constructed for
	 * @param father The father of the entity
	 * @param mother The mother of the entity
	 */
	public NaturalGene(EntityLiving animal, EntityLiving father, EntityLiving mother) {}
	
	/**
	 * This is used to construct genes when the nbt of an animal is loaded
	 * @param animal The entity this gene is being constructed for
	 * @param tag The gene's tag
	 */
	public NaturalGene(EntityLiving animal, NBTTagCompound tag) {}
	
	/**
	 * The percentage that this gene will spawned with this gene
	 * @return The percentage out of 1
	 */
	public abstract float getSpawnChance();
	
	/**
	 * Returns whether this gene can be spawned on an animal
	 * This is independent of {@link #getSpawnChance()}!
	 * @param animal The potential inheritor
	 * @return True if the gene can be spawned, false if otherwise
	 */
	public abstract boolean canSpawn(EntityLiving animal);
}
