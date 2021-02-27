package modist.glasschest.client.event;

import modist.glasschest.GlassChest;
import modist.glasschest.client.tileentity.GlassChestTileEntityRenderer;
import modist.glasschest.client.tileentity.GlassCubeTileEntityRenderer;
import modist.glasschest.common.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GlassChest.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistryEventHandler {
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ClientRegistry.bindTileEntityRenderer(BlockLoader.GLASS_CHEST_TILE_ENTITY, GlassChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockLoader.GLASS_CUBE_TILE_ENTITY, GlassCubeTileEntityRenderer::new);
		RenderTypeLookup.setRenderLayer(BlockLoader.GLASS_CHEST, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(BlockLoader.GLASS_CUBE, RenderType.getCutout());
	}
}
