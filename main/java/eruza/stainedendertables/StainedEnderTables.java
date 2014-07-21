package eruza.stainedendertables;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import eruza.stainedendertables.blocks.BlockEnderClay;
import eruza.stainedendertables.blocks.BlockEnderTable;
import eruza.stainedendertables.blocks.BlockHardenedEnderClay;
import eruza.stainedendertables.network.CommonProxy;
import eruza.stainedendertables.network.PacketHandler;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, guiFactory = ModInfo.GUI_FACTORY, version = ModInfo.VERSION)
public class StainedEnderTables
{
	@Instance(ModInfo.MOD_ID)
	public static StainedEnderTables instance;
	public static final String ModId = ModInfo.MOD_ID;
	public static Configuration config;

	private static Block blockEnderClay;
	private static Block blockHardenedEnderClay;
	private static Block blockEnderTable;
	private static boolean dbb;

	@SidedProxy(clientSide = "eruza.stainedendertables.network.ClientProxy", serverSide = "eruza.stainedendertables.network.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new EventListener());

		String dir = event.getModConfigurationDirectory().getAbsolutePath();
		File configFile = new File(dir + File.separator + ModInfo.MOD_ID.toLowerCase() + ".cfg");
		config = new Configuration(configFile);
		config.load();
		setConfig();
		PacketHandler.init();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		instantiateBlocks();

		GameRegistry.registerBlock(blockEnderClay, "blockEnderClay");
		GameRegistry.registerBlock(blockHardenedEnderClay, "blockHardenedEnderClay");
		GameRegistry.registerBlock(blockEnderTable, ItemEnderTable.class, "blockEnderTable");

		ItemStack eyeStack = new ItemStack(Items.ender_eye);
		ItemStack pearlStack = new ItemStack(Items.ender_pearl);
		ItemStack clayStack = new ItemStack(Blocks.clay);
		ItemStack diamondStack = new ItemStack(Items.diamond);
		GameRegistry.addRecipe(new ItemStack(blockEnderClay), " E ", "DCD", "CPC",
				'E', eyeStack, 'D', diamondStack, 'C', clayStack, 'P', pearlStack);
		ItemStack hardenedEnderClay = new ItemStack(blockHardenedEnderClay);
		GameRegistry.addSmelting(blockEnderClay, hardenedEnderClay, 0f);
		ItemStack redDustStack = new ItemStack(Items.redstone);
		for (int i = 0; i < 16; i++)
		{
			ItemStack dyeStack = new ItemStack(Items.dye, 1, i);
			GameRegistry.addShapelessRecipe(new ItemStack(blockEnderTable, 1, i), redDustStack, dyeStack, hardenedEnderClay);
		}
	}

	private void instantiateBlocks()
	{
		blockEnderClay = new BlockEnderClay();
		blockHardenedEnderClay = new BlockHardenedEnderClay();
		blockEnderTable = new BlockEnderTable();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	public static void setConfig() {
		dbb = config.getBoolean("Difficulty Based Behavior", Configuration.CATEGORY_GENERAL, false, "Enabled: Peaceful/Easy: Tables deal no damage; Normal: Tables deal ender pearl damage; Hard: Tables deal damage and consume an ender pearl. Disabled: Tables deal damage, and do not consume ender pearls on all difficulties.");
		System.out.println("Config set to " + dbb);
		config.save();
	}

	/**
	 * Difficulty based behavior. If enabled the behavior of ender tables is based on difficulty.
	 * 
	 * @return {@link #dbb}
	 */
	public static boolean isDifficultyBasedBehaviorEnabled()
	{
		return dbb;
	}
}
