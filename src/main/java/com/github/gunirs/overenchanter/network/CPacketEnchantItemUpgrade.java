package com.github.gunirs.overenchanter.network;

import com.github.gunirs.overenchanter.inventory.ContainerOverEnchanter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketEnchantItemUpgrade implements IMessage {
    public CPacketEnchantItemUpgrade() {}

    private int buttonId, windowId;

    public CPacketEnchantItemUpgrade(int buttonId, int windowId) {
        this.buttonId = buttonId;
        this.windowId = windowId;
    }

    public static class Handler implements IMessageHandler<CPacketEnchantItemUpgrade, IMessage> {
        @Override
        public IMessage onMessage(CPacketEnchantItemUpgrade message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                if(player.openContainer.windowId == message.windowId) {
                    ContainerOverEnchanter container = (ContainerOverEnchanter) player.openContainer;

                    if(container.inventoryItemStacks.isEmpty())
                        return;

                    if(player.experienceTotal >= 3000) {
                        player.addExperience(-3000);
                        container.enchant(message.buttonId);
                    }
                }
            });

            return null;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.buttonId);
        buf.writeInt(this.windowId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.buttonId = buf.readInt();
        this.windowId = buf.readInt();
    }
}