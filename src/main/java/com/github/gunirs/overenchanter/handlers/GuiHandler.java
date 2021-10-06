package com.github.gunirs.overenchanter.handlers;

import com.github.gunirs.overenchanter.gui.GuiOverEnchanter;
import com.github.gunirs.overenchanter.inventory.ContainerOverEnchanter;
import com.github.gunirs.overenchanter.tileentity.TileEntityOverEnchanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y ,z);

        TileEntity te = world.getTileEntity(blockPos);

        if(te != null) {
            if(ID == List.OVER_ENCHANTER.ordinal())
                return new ContainerOverEnchanter(player.inventory, world, blockPos);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y ,z);

        TileEntity te = world.getTileEntity(blockPos);

        if(te != null) {
            if(ID == List.OVER_ENCHANTER.ordinal())
                return new GuiOverEnchanter(player.inventory, world);
        }

        return null;
    }

    public enum List {
        OVER_ENCHANTER
    }
}
