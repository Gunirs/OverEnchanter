package com.github.gunirs.overenchanter.blocks;

import com.github.gunirs.overenchanter.OverEnchanter;
import com.github.gunirs.overenchanter.handlers.GuiHandler;
import com.github.gunirs.overenchanter.tileentity.TileEntityOverEnchanter;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockOverEnchanter extends BlockContainer {
    private static final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockOverEnchanter(String blockName) {
        super(Material.ROCK, MapColor.PURPLE);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setRegistryName(blockName);
        this.setUnlocalizedName(blockName);
        this.setHardness(5.0F);
        this.setResistance(2000.0F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return axisAlignedBB;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecated")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1)
                    j = 2;

                if (rand.nextInt(16) == 0) {
                    for (int k = 0; k <= 1; ++k) {
                        BlockPos blockpos = pos.add(i, k, j);

                        if (net.minecraftforge.common.ForgeHooks.getEnchantPower(worldIn, blockpos) > 0) {
                            if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2)))
                                break;

                            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double)pos.getX() + 0.5D,
                                    (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D,
                                    (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F),
                                    (double)((float)j + rand.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecated")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityOverEnchanter();
    }

    @SuppressWarnings("deprecated")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        System.out.println("fff " + this.getUnlocalizedName());
        if(!worldIn.isRemote)
            playerIn.openGui(OverEnchanter.instance, GuiHandler.List.OVER_ENCHANTER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }
}
