package modist.glasschest.common.event;

import modist.glasschest.GlassChest;
import modist.glasschest.common.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GlassChest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonRegistryEventHandler {

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
		BlockLoader.registerBlocks(event);
	}

	@SubscribeEvent
	public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
		BlockLoader.registerBlockItems(event);
	}
	
	@SubscribeEvent
	public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
		BlockLoader.registerTileEntities(event);
	}

}
