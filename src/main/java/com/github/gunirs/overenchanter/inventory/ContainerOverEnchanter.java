package com.github.gunirs.overenchanter.inventory;

import com.github.gunirs.overenchanter.api.EnchantmentData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContainerOverEnchanter extends Container {
    private IInventory tileInventory;
    private World world;
    private InventoryPlayer inventoryPlayer;
    private BlockPos pos;

    private List<EnchantmentData> enchantments = new LinkedList<>();

    @SideOnly(Side.CLIENT)
    public ContainerOverEnchanter(InventoryPlayer playerInv, World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerOverEnchanter(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        this.tileInventory = new InventoryBasic("OverEnchanter", true, 1) {
            @Override
            public int getInventoryStackLimit() {
                return 64;
            }

            @Override
            public void markDirty() {
                super.markDirty();
                ContainerOverEnchanter.this.onCraftMatrixChanged(this);
            }
        };
        this.world = worldIn;
        this.inventoryPlayer = playerInv;
        this.pos = pos;

        this.addSlotToContainer(new Slot(this.tileInventory, 0, 25, 47) {
            public boolean isItemValid(ItemStack stack) {
                return stack.isItemEnchanted();
            }

            public int getSlotStackLimit() {
                return 1;
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    public List<EnchantmentData> getEnchantments() {
        return enchantments;
    }

    public void addEnchantment(EnchantmentData enchantmentData) {
        for(EnchantmentData e : enchantments) {
            if(e.getName().equalsIgnoreCase(enchantmentData.getName()))
                return;
        }
        enchantments.add(enchantmentData);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if(inventoryIn == this.tileInventory) {
            ItemStack stack = inventoryIn.getStackInSlot(0);

            if(!stack.isEmpty() && stack.isItemEnchanted()) {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

                for(Enchantment ench : map.keySet()) {
                    addEnchantment(new EnchantmentData(ench, map.get(ench)));
                }
            } else if(stack.isEmpty() && !enchantments.isEmpty()) {
                enchantments.clear();
            }
        }
        this.detectAndSendChanges();
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE) {
                if (!this.mergeItemStack(itemstack1, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.inventorySlots.get(0).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1)) {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.hasTagCompound()) {
                    this.inventorySlots.get(0).putStack(itemstack1.splitStack(1));
                } else if (!itemstack1.isEmpty()) {
                    this.inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                    itemstack1.shrink(1);
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote) {
            this.clearContainer(playerIn, playerIn.world, this.tileInventory);
        }
    }

    public void enchant(int id) {
        ItemStack stack = this.tileInventory.getStackInSlot(0);
        EnchantmentData enchantmentData = getEnchantments().get(id);

        Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.getEnchantments(stack);
        for(Enchantment e : enchantmentIntegerMap.keySet()) {
            if(e.getName().equalsIgnoreCase(enchantmentData.getName())) {
                Integer level = enchantmentIntegerMap.get(e);
                enchantmentIntegerMap.replace(e, level + 5);
            }
        }
        EnchantmentHelper.setEnchantments(enchantmentIntegerMap, stack);
    }
}
