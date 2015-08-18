package com.austinv11.husbandry.impl;

import com.austinv11.husbandry.api.IHusbandryRegistry;
import com.austinv11.husbandry.api.entity.IEntityWrapper;
import com.austinv11.husbandry.api.genetics.Gene;

import java.util.HashMap;

public class HusbandryRegistry implements IHusbandryRegistry {
	
	public static HashMap<String, Gene> genes = new HashMap<String, Gene>();
	public static HashMap<Class, IEntityWrapper> wrappers = new HashMap<Class, IEntityWrapper>();
	
	@Override
	public void registerGene(Gene gene) {
		genes.put(gene.getUnlocalizedName(), gene);
	}
	
	@Override
	public HashMap<String, Gene> getGenes() {
		return (HashMap<String, Gene>) genes.clone();
	}
	
	@Override
	public void registerEntity(IEntityWrapper entityWrapper) {
		wrappers.put(entityWrapper.getEntityClass(), entityWrapper);
	}
	
	@Override
	public HashMap<Class, IEntityWrapper> getEntities() {
		return (HashMap<Class, IEntityWrapper>) wrappers.clone();
	}
}
