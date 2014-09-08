package eruza.stainedendertables.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eruza.stainedendertables.StainedEnderTables;
import io.netty.buffer.ByteBuf;

public class MessageSpawnParticles implements IMessage, IMessageHandler<MessageSpawnParticles, IMessage> {
	public double x, y, z;

	public MessageSpawnParticles() {}

	public MessageSpawnParticles(double[] coords) {
		this.x = coords[0];
		this.y = coords[1];
		this.z = coords[2];
	}

	@Override
	public IMessage onMessage(MessageSpawnParticles message, MessageContext ctx) {
        // Added proxy method to avoid crashes on the server.
        StainedEnderTables.proxy.spawnParticles(message.x, message.y, message.z);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

}
