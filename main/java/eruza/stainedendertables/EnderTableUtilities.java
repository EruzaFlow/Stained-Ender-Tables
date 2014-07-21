package eruza.stainedendertables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EnderTableUtilities
{
	private static HashMap<UUID, Long> playerLastTeleport = new HashMap<UUID, Long>();

	private EnderTableUtilities() {}

	/**
	 * Converts a point in string format to an array of doubles
	 *
	 * @param string string to be converted
	 * @return int array containing x, y, z coordinates
	 */
	public static int[] stringToCoords(String string)
	{
		int[] coords = new int[3];
		String[] stringCoords = string.split(" ");
		for (int i = 0; i < 3; i++) coords[i] = Integer.parseInt(stringCoords[i]);
		return coords;
	}

	/**
	 * Returns the square of the distance between two points.
	 * 
	 * @param point1 The first point (must not contain dimension/color identifiers)
	 * @param point2 The second point (must not contain dimension/color identifiers)
	 * @return the square of the distance between the two points
	 */
	protected static double getDistanceFrom(String point1, String point2)
	{
		int[] point1Coords = stringToCoords(point1);
		int[] point2Coords = stringToCoords(point2);
		double d3 = point2Coords[0] - point1Coords[0];
		double d4 = point2Coords[1] - point1Coords[1];
		double d5 = point2Coords[2] - point1Coords[2];
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	/**
	 * Given a point, returns the closest ender table in the same dimension of the same color
	 * 
	 * @param world 
	 * @param point
	 * @param posZ 
	 * @param posY 
	 * @param posX 
	 * @return the coordinates of the identified ender table
	 */
	public static String getClosestEnderTable(World world, String color, int posX, int posY, int posZ)
	{
		double closestDist = -1;
		String closest = null;

		TableLocationData data = TableLocationData.getData(world);
		if(data == null) return closest;
		Iterator it = data.points.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry table = (Map.Entry)it.next();
			String tableColor = (String) table.getValue();
			if(tableColor.equals(color)) {
				String location = (String) table.getKey();
				double dist = getDistanceFrom(posX + " " + posY + " " + posZ, location);
				if ((closest == null || dist < closestDist) && (dist > 0)) {
					closest = location;
					closestDist = dist;
				}
			}
		}

		return closest;
	}

	/**
	 * Centers the coordinates on an ender table given the size of an ender table
	 *
	 * @param destCoords point to be centered
	 * @return centered coords in an array
	 */
	public static double[] centerCoordinates(String coords)
	{
		int[] destCoords = stringToCoords(coords);
		return centerCoordinates(destCoords[0], destCoords[1], destCoords[2]);
	}

	/**
	 * Centers the coordinates on an ender table given the size of an ender table
	 *
	 * @param x ender table x coord
	 * @param y ender table y coord
	 * @param z ender table z coord
	 * @return centered coords in an array
	 */
	public static double[] centerCoordinates(double x, double y, double z)
	{
		double[] coords = new double[3];
		coords[0] = x + 0.5D;
		coords[1] = y + 0.75D;
		coords[2] = z + 0.5D;
		return coords;
	}

	/**
	 * Checks if it's been at least 500 milliseconds since the player last teleported, this is
	 * so that the player doesn't accidentally teleport again. Also checks that the player
	 * has an ender pearl if {@link StainedEnderTables#isDifficultyBasedBehaviorEnabled()} is true and
	 * the game is on hard difficulty.
	 * 
	 * @param entityPlayer
	 * @param world
	 * @return
	 */
	public static boolean canActivate(EntityPlayer entityPlayer, World world) {
		if(!playerLastTeleport.isEmpty()) {
			long lastTeleport = playerLastTeleport.get(entityPlayer.getUniqueID());
			if(System.currentTimeMillis()-lastTeleport < 500) return false;
		}
		if(world.difficultySetting == EnumDifficulty.HARD && StainedEnderTables.isDifficultyBasedBehaviorEnabled()) {
			System.out.println("DIFFICULTY: " + world.difficultySetting);
			if (!entityPlayer.inventory.hasItem(Items.ender_pearl) || !entityPlayer.inventory.consumeInventoryItem(Items.ender_pearl)) {
				if(!world.isRemote) entityPlayer.addChatComponentMessage(new ChatComponentText("You have no ender pearls"));
				return false;
			}
		}
		return true;
	}

	/**
	 * Stores the player's last teleport time in {@link #playerLastTeleport}
	 * 
	 * @param player The player that teleported
	 */
	public static void playerTeleported(EntityPlayer player) {
		playerLastTeleport.put(player.getUniqueID(), System.currentTimeMillis());
	}
}
