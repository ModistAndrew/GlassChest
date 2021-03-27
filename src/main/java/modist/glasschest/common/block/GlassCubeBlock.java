package modist.glasschest.common.block;

import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class GlassCubeBlock extends GlassChestBlock{

	public GlassCubeBlock() {
		super(()->BlockLoader.GLASS_CUBE_TILE_ENTITY);
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new GlassCubeTileEntity();
	}
	
}
