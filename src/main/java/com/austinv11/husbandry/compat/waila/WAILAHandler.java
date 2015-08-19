package com.austinv11.husbandry.compat.waila;

import com.austinv11.husbandry.Husbandry;
import com.austinv11.husbandry.api.HusbandryAPI;
import com.austinv11.husbandry.api.entity.IEntityWrapper;
import mcp.mobius.waila.api.impl.ModuleRegistrar;

public class WAILAHandler {
	
	public static void setup() {
		HusbandryEntityProvider provider = new HusbandryEntityProvider();
		try {
			for (IEntityWrapper wrapper : HusbandryAPI.retrieveRegistry().getEntities().values())
				ModuleRegistrar.instance().registerBodyProvider(provider, wrapper.getEntityClass());
		} catch (Exception e) {
			Husbandry.LOGGER.error("Error loading WAILA compatibility, please report this!");
			e.printStackTrace();
		}
	}
}
