package eruza.stainedendertables;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class ItemEnderTable extends ItemBlock {

	public ItemEnderTable(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata (int damageValue) {
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		String color = ItemDye.field_150921_b[itemstack.getItemDamage()].toString();
		return color + "." + getUnlocalizedName();
	}
}
