package eruza.stainedendertables.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eruza.stainedendertables.StainedEnderTables;

public class BlockEnderClay extends Block
{
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockEnderClay()
	{
		super(Material.rock);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.75F, 1.0F);
		this.setLightOpacity(0);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setHardness(4.0F);
		this.setResistance(9.0F);
		this.setBlockName("enderClay");
		this.textureName = StainedEnderTables.MOD_ID + ":ender_clay";
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

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return par1 == 0 ? this.iconBottom : (par1 == 1 ? this.iconTop : this.blockIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(this.getTextureName() + "_" + "side");
		this.iconTop = par1IconRegister.registerIcon(this.getTextureName() + "_" + "top");
		this.iconBottom = par1IconRegister.registerIcon(this.getTextureName() + "_" + "bottom");
	}
}
