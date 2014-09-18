package eruza.stainedendertables.utilities;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import eruza.stainedendertables.StainedEnderTables;

public class SETLog {

	public static void log(Level level, String msg) {
		FMLLog.log(StainedEnderTables.MOD_ID, level, msg);
	}

	public static void error(String msg) {
		log(Level.ERROR, msg);
	}

	public static void info(String msg) {
		if(StainedEnderTables.debug) log(Level.INFO, msg);
	}

	public static void warn(String msg) {
		log(Level.WARN, msg);
	}

	public static String coordToString(int x, int y, int z) {
		return x + " " + y + " " + z;
	}

}
