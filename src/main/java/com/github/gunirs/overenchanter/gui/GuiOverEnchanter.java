package com.github.gunirs.overenchanter.gui;

import com.github.gunirs.overenchanter.OverEnchanter;
import com.github.gunirs.overenchanter.api.EnchantmentData;
import com.github.gunirs.overenchanter.inventory.ContainerOverEnchanter;
import com.github.gunirs.overenchanter.network.CPacketEnchantItemUpgrade;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiOverEnchanter extends GuiContainer {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation(OverEnchanter.MODID, "textures/gui/over_enchanter.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("minecraft:textures/entity/enchanting_table_book.png");
    private static final ModelBook MODEL_BOOK = new ModelBook();

    private final InventoryPlayer playerInventory;
    private final ContainerOverEnchanter container;

    private final Random random = new Random();
    public int ticks;
    public float flip, oFlip, flipT, flipA, open, oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private final World world;

    private int scrollIndex = 0;

    public GuiOverEnchanter(InventoryPlayer inventory, World worldIn) {
        super(new ContainerOverEnchanter(inventory, worldIn));
        this.playerInventory = inventory;
        this.container = (ContainerOverEnchanter)this.inventorySlots;
        this.world = worldIn;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("Over Enchanter"), 12, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.tickBook();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        for (int k = 0; k < 3; ++k) {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && !this.container.enchantItem(this.mc.player, k)) {
                //this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
                OverEnchanter.networkWrapper.sendToServer(new CPacketEnchantItemUpgrade(k + scrollIndex, this.container.windowId));
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.scale(5.0F, 5.0F, 5.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        float f2 = this.oOpen + (this.open - this.oOpen) * partialTicks;
        GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
        GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float f3 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.25F;
        float f4 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.75F;
        f3 = (f3 - (float) MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.fastFloor(f4)) * 1.6F - 0.3F;

        if(f3 < 0.0F)
            f3 = 0.0F;

        if(f4 < 0.0F)
            f4 = 0.0F;

        if(f3 > 1.0F)
            f3 = 1.0F;

        if(f4 > 1.0F)
            f4 = 1.0F;

        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render(null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        List<EnchantmentData> enchantments = container.getEnchantments();
        int size;
        if(enchantments.size() > 2) {
            if(enchantments.size() < this.scrollIndex + 3)
                this.scrollIndex = enchantments.size() - 3;

            enchantments = enchantments.subList(scrollIndex, scrollIndex + 3);
            size = 3;
        } else {
            size = enchantments.size();
        }

        if(!enchantments.isEmpty()) {
            for (int l = 0; l < size; l++) {
                renderEnchantmentLine(enchantments.get(l), mouseX, mouseY, l);
            }
        }
    }

    private void renderEnchantmentLine(EnchantmentData data, int mouseX, int mouseY, int line) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int i1 = i + 60;
        int j1 = i1 + 20;
        this.zLevel = 0.0F;

        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int l1 = 86 - this.fontRenderer.getStringWidth("");
        FontRenderer fontRenderer = this.mc.fontRenderer;
        int color = 6839882;

        int j2 = mouseX - (i + 60);
        int k2 = mouseY - (j + 14 + 19 * line);

        if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19) {
            this.drawTexturedModalRect(i1, j + 14 + 19 * line, 0, 204, 108, 19);
            color = 16777088;
        } else {
            this.drawTexturedModalRect(i1, j + 14 + 19 * line, 0, 166, 108, 19);
        }

        fontRenderer.drawSplitString(I18n.format(data.getName()), i1 + 4, j + 16 + 19 * line, l1, color);
        if(data.getLevel() < 1000 && playerInventory.player.experienceTotal >= 3000)
            color = 8453920;
        else
            color = -3669502;
        fontRenderer.drawStringWithShadow("3k (+5)", (float)(j1 + 86 - fontRenderer.getStringWidth("3k (+5)")),
                (float)(j + 16 + 19 * line + 7), color);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Integer.signum(Mouse.getEventDWheel());

        scrollIndex += (i * -1);
        if(scrollIndex < 0)
            scrollIndex = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.mc.getTickLength();
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public void tickBook() {
        ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(itemstack, this.last)) {
            this.last = itemstack;

            do {
                this.flipT += (float) (this.random.nextInt(4) - this.random.nextInt(4));
            } while (!(this.flip > this.flipT + 1.0F) && !(this.flip < this.flipT - 1.0F));
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;

        this.open += 0.2F;

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}