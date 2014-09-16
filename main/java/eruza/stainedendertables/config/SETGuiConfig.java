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
		list.add(new ConfigElement(StainedEnderTables.config.get(Configuration.CATEGORY_GENERAL, "Difficulty Based Behavior", StainedEnderTables.isDifficultyBasedBehaviorEnabled(), "When true Peaceful/Easy: Tables deal no damage; Normal: Tables deal ender pearl damage; Hard: Tables deal damage and consume an ender pearl. false: Tables deal damage, and do not consume ender pearls.")));
		return list;
	}
}