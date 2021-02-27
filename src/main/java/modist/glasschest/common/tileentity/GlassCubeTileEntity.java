package modist.glasschest.common.tileentity;

import modist.glasschest.common.block.BlockLoader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class GlassCubeTileEntity extends TileEntity {
	public GlassCubeTileEntity() {
		super(BlockLoader.GLASS_CUBE_TILE_ENTITY);
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