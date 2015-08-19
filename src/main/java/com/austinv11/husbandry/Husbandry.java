package com.austinv11.husbandry;

import com.austinv11.collectiveframework.minecraft.config.ConfigException;
import com.austinv11.collectiveframework.minecraft.config.ConfigRegistry;
import com.austinv11.collectiveframework.minecraft.logging.Logger;
import com.austinv11.collectiveframework.minecraft.reference.ModIds;
import com.austinv11.husbandry.api.IHusbandryRegistry;
import com.austinv11.husbandry.compat.waila.WAILAHandler;
import com.austinv11.husbandry.impl.HusbandryRegistry;
import com.austinv11.husbandry.proxy.CommonProxy;
import com.austinv11.husbandry.reference.Config;
import com.austinv11.husbandry.reference.Reference;
import com.austinv11.husbandry.test.TestGene;
import com.austinv11.husbandry.test.TestWrapper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "after:CollectiveFramework")
public class Husbandry {
	
	@Mod.Instance(Reference.MOD_ID)
	public static Husbandry instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static Logger LOGGER = new Logger(Reference.MOD_ID);
	
	public static IHusbandryRegistry registry;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		try {
			ConfigRegistry.registerConfig(new Config());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		registry = new HusbandryRegistry();
		registry.registerEntity(new TestWrapper());
		registry.registerGene(new TestGene());
		proxy.registerEventHandlers();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded(ModIds.WAILA))
			WAILAHandler.setup();
	}
}
