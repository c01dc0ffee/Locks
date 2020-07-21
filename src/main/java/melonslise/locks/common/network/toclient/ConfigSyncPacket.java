package melonslise.locks.common.network.toclient;

import io.netty.buffer.ByteBuf;
import melonslise.locks.common.config.LocksConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigSyncPacket implements IMessage
{
	private int maxLockableVolume;
	private String[] lockableBlocks;
	private boolean allowRemovingLocks;
	private boolean protectLockables;

	public ConfigSyncPacket() {}

	public ConfigSyncPacket(LocksConfig.Server cfg)
	{
		this.maxLockableVolume = cfg.maxLockableVolume;
		this.lockableBlocks = cfg.lockableBlocks;
		this.allowRemovingLocks = cfg.allowRemovingLocks;
		this.protectLockables = cfg.protectLockables;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.maxLockableVolume = buf.readInt();
		this.lockableBlocks = new String[buf.readByte()];
		for(int a = 0; a < this.lockableBlocks.length; ++a)
			this.lockableBlocks[a] = ByteBufUtils.readUTF8String(buf);
		this.allowRemovingLocks = buf.readBoolean();
		this.protectLockables = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.maxLockableVolume);
		buf.writeByte(this.lockableBlocks.length);
		for(String str : this.lockableBlocks)
			ByteBufUtils.writeUTF8String(buf, str);
		buf.writeBoolean(this.allowRemovingLocks);
		buf.writeBoolean(this.protectLockables);
	}

	public static class Handler implements IMessageHandler<ConfigSyncPacket, IMessage>
	{
		@Override
		public IMessage onMessage(ConfigSyncPacket pkt, MessageContext ctx)
		{
			Minecraft mc = Minecraft.getMinecraft();
			mc.addScheduledTask(() ->
			{
				LocksConfig.Server cfg = LocksConfig.getServer(mc.world);
				cfg.maxLockableVolume = pkt.maxLockableVolume;
				cfg.lockableBlocks = pkt.lockableBlocks;
				cfg.allowRemovingLocks = pkt.allowRemovingLocks;
				cfg.protectLockables = pkt.protectLockables;
				cfg.init();
			});
			return null;
		}
	}
}