package modist.glasschest.common.itemgroup;

import modist.glasschest.common.block.BlockLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class GlassChestGroup extends ItemGroup {
	
	public static final ItemGroup GLASS_CHEST_GROUP = new GlassChestGroup();
	
    public GlassChestGroup() {
        super("glass_chest_group");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockLoader.GLASS_CHEST_ITEM);
    }
}
