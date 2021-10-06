package com.github.gunirs.overenchanter.proxy;

import com.github.gunirs.overenchanter.OverEnchanter;
import com.github.gunirs.overenchanter.Registry;
import com.github.gunirs.overenchanter.network.CPacketEnchantItemUpgrade;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Registry.Blocks.register();
        Registry.Items.register();
        Registry.TileEntity.register();
        Registry.Gui.register();
    }

    public void init(FMLInitializationEvent event) { }

    public void postInit(FMLPostInitializationEvent event) {
        registerPackets();
    }

    private void registerPackets() {
        OverEnchanter.networkWrapper.registerMessage(CPacketEnchantItemUpgrade.Handler.class, CPacketEnchantItemUpgrade.class,
                0, Side.SERVER);
    }
}
