package com.austinv11.husbandry.proxy;

import com.austinv11.husbandry.event.handler.EntityHandler;
import com.austinv11.husbandry.event.handler.GeneHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new GeneHandler());
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
	}
}
