package modist.glasschest.common.tileentity;

import modist.glasschest.common.block.BlockLoader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class GlassChestTileEntity extends TileEntity {
	public GlassChestTileEntity() {
		super(BlockLoader.GLASS_CHEST_TILE_ENTITY);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		return super.write(compound);
	}
}