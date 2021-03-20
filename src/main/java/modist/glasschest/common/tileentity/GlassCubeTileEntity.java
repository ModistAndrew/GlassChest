package modist.glasschest.common.tileentity;

import modist.glasschest.common.block.BlockLoader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GlassCubeTileEntity extends GlassChestTileEntity {
	public GlassCubeTileEntity() {
		super(BlockLoader.GLASS_CUBE_TILE_ENTITY);
	}

	@Override
	protected ITextComponent getDefaultName() {
	      return new TranslationTextComponent("block.glasschest.glass_cube");
	}
}