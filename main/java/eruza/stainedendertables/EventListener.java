package eruza.stainedendertables;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventListener {

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if (event.modID.equals(ModInfo.MOD_ID)) StainedEnderTables.setConfig();
	}

}