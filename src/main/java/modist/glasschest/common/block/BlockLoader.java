package modist.glasschest.common.block;

import modist.glasschest.GlassChest;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;

public class BlockLoader {

	public static final Block GLASS_CHEST = new GlassChestBlock();
	//public static final TileEntityType<DiyBlockTileEntity> DIY_BLOCK_TILE_ENTITY = TileEntityType.Builder.create(DiyBlockTileEntity::new, BlockLoader.DIY_BLOCK).build(null);
	public static final Block GLASS_CUBE = new GlassCubeBlock();
	//public static final TileEntityType<TestTileEntity> TEST_TILE_ENTITY = TileEntityType.Builder.create(TestTileEntity::new, BlockLoader.TEST_BLOCK).build(null);

	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		registerBlock(GLASS_CHEST, "glass_chest", event);
		registerBlock(GLASS_CUBE, "glass_cube", event);
	}

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		registerBlockItem(GLASS_CHEST, event);
		registerBlockItem(GLASS_CUBE, event);
	}
	
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
	//	registerTileEntity(DIY_BLOCK_TILE_ENTITY, "diy_block_tile_entity", event);
	//	registerTileEntity(TEST_TILE_ENTITY, "test_tile_entity", event);
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
