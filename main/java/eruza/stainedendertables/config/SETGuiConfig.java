package eruza.stainedendertables.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import eruza.stainedendertables.StainedEnderTables;




public class SETGuiConfig extends GuiConfig {

	public SETGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), StainedEnderTables.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(StainedEnderTables.config.toString()));
	}

	public static List<IConfigElement> getConfigElements() {
		List list = new ArrayList<IConfigElement>();
		list.add(new ConfigElement(StainedEnderTables.config.get(Configuration.CATEGORY_GENERAL, "Deals Fall Damage", StainedEnderTables.dealsFallDamage(), "When enabled, Stained Ender Tables deal damage on use identical to an ender pearl.")));
		list.add(new ConfigElement(StainedEnderTables.config.get(Configuration.CATEGORY_GENERAL, "Consumes Ender Pearl", StainedEnderTables.consumesEnderPearl(), "When enabled, Stained Ender Tables consume an ender pearl from the player's inventory on use.")));
		return list;
	}
}