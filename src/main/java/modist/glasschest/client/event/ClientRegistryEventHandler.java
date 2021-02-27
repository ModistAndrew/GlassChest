package modist.glasschest.client.event;

import modist.glasschest.GlassChest;
import modist.glasschest.common.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GlassChest.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistryEventHandler {
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		//ClientRegistry.bindTileEntityRenderer(BlockLoader.DIY_BLOCK_TILE_ENTITY, DiyBlockTileEntityRenderer::new);
		//ClientRegistry.bindTileEntityRenderer(BlockLoader.TEST_TILE_ENTITY, TestTER::new);
		RenderTypeLookup.setRenderLayer(BlockLoader.GLASS_CHEST, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(BlockLoader.GLASS_CUBE, RenderType.getCutout());
	}
}
