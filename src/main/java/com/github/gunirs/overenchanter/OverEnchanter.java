package com.github.gunirs.overenchanter;

import com.github.gunirs.overenchanter.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = OverEnchanter.MODID, name = OverEnchanter.MODNAME, version = OverEnchanter.MODVERSION)
public class OverEnchanter {
    public static final String MODID = "overenchanter";
    public static final String MODNAME = "OverEnchanter";
    public static final String MODVERSION = "1.0-1.12";

    @SidedProxy(
            clientSide = "com.github.gunirs.overenchanter.proxy.ClientProxy",
            serverSide = "com.github.gunirs.overenchanter.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(OverEnchanter.MODID);

    @Instance(OverEnchanter.MODID)
    public static OverEnchanter instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
