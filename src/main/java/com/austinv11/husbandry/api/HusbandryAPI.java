package com.austinv11.husbandry.api;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;

/**
 * The main entry point into direct Husbandry methods
 */
public class HusbandryAPI {
	
	private static Class mainClass;
	private static boolean didCheck = false;
	private static boolean classPresent = false;
	
	private static IHusbandryRegistry registry;
	
	/**
	 * Checks if the main Husbandry class is present
	 * @return True if present, false if otherwise
	 */
	public static boolean isMainClassPresent() {
		if (mainClass == null && !didCheck) {
			try {
				mainClass = Class.forName("com.austinv11.husbandry.Husbandry");
				classPresent = true;
			} catch (ClassNotFoundException e) {
				FMLLog.log("HusbandryAPI", Level.WARN, "Main Husbandry class not found! If Husbandry is loaded, please report this!");
				classPresent = false;
			}
			didCheck = true;
		}
		return classPresent;
	}
	
	/**
	 * Retrieves the registry implementation for the api
	 * @return The registry
	 * @throws Exception Thrown if there is an issue with the retrieval of the registry
	 */
	public static IHusbandryRegistry retrieveRegistry() throws Exception {
		if (registry == null) {
			if (isMainClassPresent()) {
				try {
					Field registryField = mainClass.getField("registry");
					registry = (IHusbandryRegistry) registryField.get(null);
				} catch (Exception e) {
					throw new Exception("Husbandry#registry not found!");
				}
			} else
				throw new Exception("Husbandry not found!");
		}
		return registry;
	}
}
