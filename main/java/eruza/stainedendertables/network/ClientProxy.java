package eruza.stainedendertables.network;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.world.World;

import java.util.Random;

public class ClientProxy extends CommonProxy {
    @Override
    public void spawnParticles(double x, double y, double z) {
        World world = FMLClientHandler.instance().getClient().theWorld;
        Random rand = new Random();
        for (int i = 0; i < 32; ++i)
        {
            world.spawnParticle("portal", x, y + rand.nextDouble() * 2.0D, z, rand.nextGaussian(), 0.0D, rand.nextGaussian());
        }
    }
}
