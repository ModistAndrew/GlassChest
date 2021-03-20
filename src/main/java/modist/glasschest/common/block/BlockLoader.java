package modist.glasschest.common.block;

import modist.glasschest.GlassChest;
import modist.glasschest.client.tileentity.GlassChestItemStackTileEntityRenderer;
import modist.glasschest.client.tileentity.GlassCubeItemStackTileEntityRenderer;
import modist.glasschest.common.tileentity.GlassChestTileEntity;
import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;

public class BlockLoader {

	public static final Block GLASS_CHEST = new GlassChestBlock();
	public static final TileEntityType<GlassChestTileEntity> GLASS_CHEST_TILE_ENTITY = 
			TileEntityType.Builder.create(GlassChestTileEntity::new, BlockLoader.GLASS_CHEST).build(null);
	public static final Block GLASS_CUBE = new GlassCubeBlock();
	public static final TileEntityType<GlassCubeTileEntity> GLASS_CUBE_TILE_ENTITY = 
			TileEntityType.Builder.create(GlassCubeTileEntity::new, BlockLoader.GLASS_CUBE).build(null);

	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		registerBlock(GLASS_CHEST, "glass_chest", event);
		registerBlock(GLASS_CUBE, "glass_cube", event);
	}

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		//registerBlockItem(GLASS_CHEST, event);
		event.getRegistry().register(new BlockItem(GLASS_CHEST, 
				new Properties().maxStackSize(1).setISTER(()->()->new GlassChestItemStackTileEntityRenderer())).setRegistryName(GLASS_CHEST.getRegistryName()));
		event.getRegistry().register(new BlockItem(GLASS_CUBE, 
				new Properties().maxStackSize(1).setISTER(()->()->new GlassCubeItemStackTileEntityRenderer())).setRegistryName(GLASS_CUBE.getRegistryName()));
		
	}
	
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
		registerTileEntity(GLASS_CHEST_TILE_ENTITY, "glass_chest_tile_entity", event);
		registerTileEntity(GLASS_CUBE_TILE_ENTITY, "glass_cube_tile_entity", event);
	}

	private static void registerBlock(Block block, String name, final RegistryEvent.Register<Block> event) {
		block.setRegistryName(GlassChest.MODID, name);
		event.getRegistry().register(block);

	}

	private static void registerBlockItem(Block block, final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new BlockItem(block, new Properties()).setRegistryName(block.getRegistryName()));
	}
	
	private static void registerTileEntity(TileEntityType<?> tileentitytype, String name, final RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().register(tileentitytype.setRegistryName(GlassChest.MODID, name));
	}
	
}
