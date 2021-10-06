package com.github.gunirs.overenchanter;

import com.github.gunirs.overenchanter.blocks.BlockOverEnchanter;
import com.github.gunirs.overenchanter.handlers.GuiHandler;
import com.github.gunirs.overenchanter.tileentity.TileEntityOverEnchanter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;

public class Registry {
    public static class Blocks {
        public static Block blockOverEnchanter = new BlockOverEnchanter("over_enchanter");

        public static void register() {
            ForgeRegistries.BLOCKS.register(blockOverEnchanter);
        }
    }

    public static class Items {
        public static final ItemBlock itemOverEnchanter = new ItemBlock(Blocks.blockOverEnchanter);

        public static void register() {
            setup();
            ForgeRegistries.ITEMS.register(itemOverEnchanter);
        }

        private static void setup() {
            itemOverEnchanter.setRegistryName(Objects.requireNonNull(Blocks.blockOverEnchanter.getRegistryName()));
        }
    }

    public static class TileEntity {
        public static void register() {
            GameRegistry.registerTileEntity(TileEntityOverEnchanter.class, new ResourceLocation("tileOverEnchanter"));
        }
    }

    public static class Gui {
        public static void register() {
            NetworkRegistry.INSTANCE.registerGuiHandler(OverEnchanter.instance, new GuiHandler());
        }
    }
}
