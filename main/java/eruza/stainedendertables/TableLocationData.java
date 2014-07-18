package eruza.stainedendertables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class TableLocationData extends WorldSavedData {
	/**
	 * Stores the location, color (respectively) of ender tables.  This is per dimension.
	 */
	protected HashMap<String, String> points = new HashMap<String, String>();
	private static String IDENTIFIER = "StainedEnderTables";

	public TableLocationData(String mapName) {
		super(mapName);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		try
		{
			NBTTagList nbttaglist = tagCompound.getTagList("Points", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < nbttaglist.tagCount(); i++)
			{
				String point = nbttaglist.getStringTagAt(i);
				String location = point.split(":")[0].replace("{", "");
				String color = point.split(":")[1];
				color = color.replaceAll("[},\"]", "");
				points.put(location, color);
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList nbttaglist = new NBTTagList();
		Iterator it = points.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry table = (Map.Entry)it.next();
			NBTTagCompound subCompound = new NBTTagCompound();
			String location = (String) table.getKey();
			String tableColor = (String) table.getValue();
			subCompound.setString(location, tableColor);
			nbttaglist.appendTag(subCompound);
		}
		tagCompound.setTag("Points", nbttaglist);
	}

	/**
	 * Adds an ender table to {@link #points} and marks this as dirty so the data will be saved.
	 * 
	 * @param world
	 * @param color
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addEnderTable(World world, String color, int x, int y, int z)
	{
		String location = x + " " + y + " " + z;
		this.points.put(location, color);
		markDirty();
	}

	/**
	 * Removes an ender table from {@link #points} and marks this as dirty so the data will be saved.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void removeEnderTable(World world, int x, int y, int z)
	{
		String location = x + " " + y + " " + z;
		this.points.remove(location);
		markDirty();
	}
	
	/**
	 * Returns the appropriate instance of this class for the given world.
	 * 
	 * @param world
	 * @return The instance of this class corresponding to world
	 */
	public static TableLocationData getData(World world) {
		TableLocationData data = (TableLocationData)world.perWorldStorage.loadData(TableLocationData.class, IDENTIFIER);
		if (data == null) {
			data = new TableLocationData(IDENTIFIER);
			world.perWorldStorage.setData(IDENTIFIER, data);
		}
		return data;
	}

}
