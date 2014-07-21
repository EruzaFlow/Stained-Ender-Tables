package eruza.stainedendertables.config;


import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class SETConfigGuiFactory implements IModGuiFactory {
	private Minecraft minecraft;

	@Override
	public void initialize(Minecraft minecraftInstance) {
		this.minecraft = minecraftInstance;		
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return SETGuiConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

}
