package com.austinv11.husbandry.api.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;

/**
 * Implement this to create an entity wrapper in order to add compatibility to entities
 * This is basically an intermediary between Husbandry and an entity used to communicate
 * Your entity MUST be in some way an extension of {@link net.minecraft.entity.EntityLiving}
 */
public interface IEntityWrapper {
	
	/**
	 * Gets the entity's class
	 * @return The class
	 */
	public Class<? extends EntityLiving> getEntityClass();
	
	/**
	 * Gets the entities egg item, dropped through mating in place of creating a baby
	 * @param animal The animal this wrapper belongs to
	 * @param mate The animal's mate
	 * @return The egg item, or null if this entity mates normally
	 */
	public Item getEgg(EntityLiving animal, EntityLiving mate);
	
	/**
	 * The age (in mc ticks) required for a baby to become an adult
	 * @return The max age
	 */
	public long getMaxAge();
	
	/**
	 * The items required to allow the animals to mate
	 * @return The preferred breeding items
	 */
	public Item[] getPrefferedBreedingItems();
	
	/**
	 * Returns all the possible mates
	 * @return The possible mates
	 */
	public Class<? extends EntityLiving>[] getPossibleMates();
	
	/**
	 * Called to produce an offspring. {@link #getPossibleMates()} is checked before this method is called
	 * @param animal The animal this wrapper belongs to
	 * @param mate The animal's mate
	 * @return The offspring, or null if none can be created
	 */
	public EntityLiving mate(EntityLiving animal, EntityLiving mate);
}
