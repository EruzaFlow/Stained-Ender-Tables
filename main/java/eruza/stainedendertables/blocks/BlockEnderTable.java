package eruza.stainedendertables.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eruza.stainedendertables.StainedEnderTables;
import eruza.stainedendertables.network.MessageSpawnParticles;
import eruza.stainedendertables.network.PacketHandler;
import eruza.stainedendertables.utilities.EnderTableUtilities;
import eruza.stainedendertables.utilities.TableLocationData;

public class BlockEnderTable extends BlockColored {
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	private int direction = -1;

	public BlockEnderTable() {
		super(Material.rock);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.75F, 1.0F);
		this.setLightOpacity(0);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setHardness(4.0F);
		this.setResistance(9.0F);
		this.setBlockName("enderTable");
		this.textureName = StainedEnderTables.MOD_ID + ":";
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List subItems)
	{
		for (int i = 0; i < 16; ++i) subItems.add(new ItemStack(p_149666_1_, 1, i));
	}

	private String getColor(int metadata) {
		return ItemDye.field_150921_b[metadata].toString();
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
	{
		String color = getColor(this.getDamageValue(world, posX, posY, posZ));
		String dest = EnderTableUtilities.getClosestEnderTable(world, color, posX, posY, posZ);
		if (EnderTableUtilities.canActivate(entityPlayer, world) && dest != null)
		{
			double [] destCoordsCentered = EnderTableUtilities.centerCoordinates(dest);
			Random rand = new Random();
			world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 1.0F, 1.0F);
			sendSpawnParticlesPacket(world, EnderTableUtilities.centerCoordinates(posX, posY, posZ));
			teleport(world, entityPlayer, destCoordsCentered[0], destCoordsCentered[1], destCoordsCentered[2]);
			world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 1.0F, 1.0F);
			EnderTableUtilities.playerTeleported(entityPlayer);
			sendSpawnParticlesPacket(world, destCoordsCentered);
		}
		return true;
	}



	/**
	 * Sends the packet which triggers particle spawning on the client
	 * 
	 * @param world
	 * @param coords
	 */
	private void sendSpawnParticlesPacket(World world, double[] coords) {
		IMessage message2 = new MessageSpawnParticles(coords);
		TargetPoint point2 = new TargetPoint(world.provider.dimensionId, coords[0], coords[1], coords[2], 64);
		PacketHandler.INSTANCE.sendToAllAround(message2, point2);
	}

	/**
	 * Teleports the given player to the given coordinates
	 *
	 * @param world The World Object
	 * @param entityPlayer Player to be teleported
	 * @param posX X coord to be teleported to
	 * @param posY Y coord to be teleported to
	 * @param posZ Z coord to be teleported to
	 */
	private void teleport(World world, EntityPlayer entityPlayer, double posX, double posY, double posZ) {
		if (!world.isRemote) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP)entityPlayer;
			if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == world) {
				EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, posX, posY, posZ, 5.0F);
				if (!MinecraftForge.EVENT_BUS.post(event)) {
					if (entityPlayer.isRiding()) entityPlayer.mountEntity((Entity)null);
					entityPlayer.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
					if (hurtPlayer(world)) {
						entityPlayer.fallDistance = 0.0F;
						entityPlayer.attackEntityFrom(DamageSource.fall, event.attackDamage);
					}
				}
			}
		}
	}

	/**
	 * If difficulty based behavior is off in config it always hurts the player.  Otherwise
	 * it checks against the worlds difficulty and hurts on normal and hard.
	 * 
	 * @param world
	 * @return whether or not to hurt the player after using the ender table
	 */
	private boolean hurtPlayer(World world) {
		if(!StainedEnderTables.isDifficultyBasedBehaviorEnabled()) return true;
		if(world.difficultySetting == EnumDifficulty.NORMAL || world.difficultySetting == EnumDifficulty.HARD) return true;
		return false;
	}

	@Override
	public void onBlockAdded(World world, int posX, int posY, int posZ)
	{
		super.onBlockAdded(world, posX, posY, posZ);
		if (!world.isRemote)
		{
			TableLocationData data = TableLocationData.getData(world);
			data.addEnderTable(world, getColor(this.getDamageValue(world, posX, posY, posZ)), posX, posY, posZ);			
		}
	}

	@Override
	public void breakBlock(World world, int posX, int posY, int posZ, Block par5, int par6)
	{
		super.breakBlock(world, posX, posY, posZ, par5, par6);
		if (!world.isRemote)
		{
			TableLocationData data = TableLocationData.getData(world);
			data.removeEnderTable(world, posX, posY, posZ);
		}
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		par2 = par2*3;
		if(par1>=2) return this.icons[par2+2];
		return this.icons[par2+par1];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		icons = new IIcon[48];
		for(int i=0;i<16;i++) {
			int colorArrayLocation = i*3;
			String name = this.getTextureName() + this.getColor(i) + "_ender_table";
			icons[colorArrayLocation] = par1IconRegister.registerIcon(name + "_" + "bottom");
			icons[colorArrayLocation+1] = par1IconRegister.registerIcon(name + "_" + "top");
			icons[colorArrayLocation+2] = par1IconRegister.registerIcon(name + "_" + "side");
		}
	}
}
