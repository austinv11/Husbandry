package com.austinv11.husbandry.api;

import com.austinv11.husbandry.api.entity.IEntityWrapper;
import com.austinv11.husbandry.api.genetics.Gene;

import java.util.HashMap;

/**
 * An abstraction of the husbandry registry, retrieve the implemented version through {@link HusbandryAPI#retrieveRegistry()}
 */
public interface IHusbandryRegistry {
	
	/**
	 * Registers a gene with Husbandry
	 * @param gene The gene to register
	 */
	public void registerGene(Gene gene);
	
	/**
	 * Gets the currently registered genes, the string key represents the unlocalized identifier for the gene
	 * @return A copy of the map of genes
	 */
	public HashMap<String, Gene> getGenes();
	
	/**
	 * Registers an entity with Husbandry
	 * @param entityWrapper The entity wrapper
	 */
	public void registerEntity(IEntityWrapper entityWrapper);
	
	/**
	 * Gets the currently registered entities, the class key represents the entity class for the corresponding wrapper
	 * @return A copy of the map of entities
	 */
	public Object getEntities();
}
