package eruza.stainedendertables.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import eruza.stainedendertables.StainedEnderTables;

public class EventListener {

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if (event.modID.equals(StainedEnderTables.MOD_ID)) StainedEnderTables.setConfig();
	}

}