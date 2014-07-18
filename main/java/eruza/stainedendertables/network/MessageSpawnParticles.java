package eruza.stainedendertables.network;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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
		World world = FMLClientHandler.instance().getClient().theWorld;
		Random rand = new Random();
		for (int i = 0; i < 32; ++i)
		{
			world.spawnParticle("portal", message.x, message.y + rand.nextDouble() * 2.0D, message.z, rand.nextGaussian(), 0.0D, rand.nextGaussian());
		}
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
