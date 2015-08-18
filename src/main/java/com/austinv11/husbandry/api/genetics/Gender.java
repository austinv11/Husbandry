package com.austinv11.husbandry.api.genetics;

import net.minecraft.util.StatCollector;

/**
 * Represents a gender for an animal
 */
public enum Gender {
	MALE("husbandry.genetics.gender.male"),FEMALE("husbandry.genetics.gender.female"),
	UNKNOWN("husbandry.genetics.gender.unknown");
	
	public final String unlocalizedName;
	
	Gender(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}
	
	/**
	 * Gets the gender's localized name
	 * @return The localized name
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal(unlocalizedName);
	}
}
