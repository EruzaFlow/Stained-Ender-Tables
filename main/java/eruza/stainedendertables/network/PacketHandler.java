package eruza.stainedendertables.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import eruza.stainedendertables.ModInfo;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID.toLowerCase());

	public static void init()
	{
		INSTANCE.registerMessage(MessageSpawnParticles.class, MessageSpawnParticles.class, 0, Side.CLIENT);
	}
}
