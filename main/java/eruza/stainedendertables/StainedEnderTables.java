package eruza.stainedendertables;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import eruza.stainedendertables.blocks.BlockEnderClay;
import eruza.stainedendertables.blocks.BlockEnderTable;
import eruza.stainedendertables.blocks.BlockHardenedEnderClay;
import eruza.stainedendertables.config.EventListener;
import eruza.stainedendertables.network.PacketHandler;
import eruza.stainedendertables.utilities.SETLog;

@Mod(modid = StainedEnderTables.MOD_ID, name = StainedEnderTables.MOD_NAME, guiFactory = StainedEnderTables.GUI_FACTORY, version = StainedEnderTables.VERSION)
public class StainedEnderTables
{
	@Instance(StainedEnderTables.MOD_ID)
	public static StainedEnderTables instance;
	public static final String MOD_ID = "StainedEnderTables";
	public static final String MOD_NAME = "Stained Ender Tables";
	public static final String VERSION = "1.0.4";
	public static final String GUI_FACTORY = "eruza.stainedendertables.config.SETConfigGuiFactory";
	public static Configuration config;

	private static Block blockEnderClay;
	private static Block blockHardenedEnderClay;
	private static Block blockEnderTable;
	private static boolean dbb;
	public static boolean debug = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new EventListener());

		String dir = event.getModConfigurationDirectory().getAbsolutePath();
		File configFile = new File(dir + File.separator + StainedEnderTables.MOD_ID + ".cfg");
		config = new Configuration(configFile);
		config.load();
		setConfig();
		PacketHandler.init();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		blockEnderClay = new BlockEnderClay();
		blockHardenedEnderClay = new BlockHardenedEnderClay();
		blockEnderTable = new BlockEnderTable();

		GameRegistry.registerBlock(blockEnderClay, "blockEnderClay");
		GameRegistry.registerBlock(blockHardenedEnderClay, "blockHardenedEnderClay");
		GameRegistry.registerBlock(blockEnderTable, ItemEnderTable.class, "blockEnderTable");

		ItemStack eyeStack = new ItemStack(Items.ender_eye);
		ItemStack pearlStack = new ItemStack(Items.ender_pearl);
		ItemStack clayStack = new ItemStack(Blocks.clay);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockEnderClay), " E ", "DCD", "CPC",
				'E', eyeStack, 'D', "gemDiamond", 'C', clayStack, 'P', pearlStack));
		ItemStack hardenedEnderClay = new ItemStack(blockHardenedEnderClay);
		GameRegistry.addSmelting(blockEnderClay, hardenedEnderClay, 0f);
		for (int i = 0; i < 16; i++)
		{
			String color = ItemDye.field_150921_b[i].toString();
			String dye = "dye" + Character.toUpperCase(color.charAt(0)) + color.substring(1);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockEnderTable, 1, i), "dustRedstone", dye, hardenedEnderClay));
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	public static void setConfig() {
		dbb = config.getBoolean("Difficulty Based Behavior", Configuration.CATEGORY_GENERAL, false, "Enabled: Peaceful/Easy: Tables deal no damage; Normal: Tables deal ender pearl damage; Hard: Tables deal damage and consume an ender pearl. Disabled: Tables deal damage, and do not consume ender pearls on all difficulties.");
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
