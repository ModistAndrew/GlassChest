package modist.glasschest.client.event;

import java.util.Map;

import modist.glasschest.GlassChest;
import modist.glasschest.client.model.GlassChestBakedModel;
import modist.glasschest.client.model.GlassCubeBakedModel;
import modist.glasschest.client.tileentity.GlassChestTileEntityRenderer;
import modist.glasschest.client.tileentity.GlassCubeTileEntityRenderer;
import modist.glasschest.common.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GlassChest.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistryEventHandler {
	
	@SubscribeEvent
	public static void onTextureStitch(final TextureStitchEvent.Pre event) {
		if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
			event.addSprite(GlassChestTileEntityRenderer.GLASS_CHEST_TEXTURE);
		}
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ClientRegistry.bindTileEntityRenderer(BlockLoader.GLASS_CHEST_TILE_ENTITY, GlassChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BlockLoader.GLASS_CUBE_TILE_ENTITY, GlassCubeTileEntityRenderer::new);
		RenderTypeLookup.setRenderLayer(BlockLoader.GLASS_CHEST, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockLoader.GLASS_CUBE, RenderType.getTranslucent());
	}
	
	@SubscribeEvent
	public static void onGlassChestModelBaked(ModelBakeEvent event) {
	    Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
	    ModelResourceLocation location = new ModelResourceLocation(BlockLoader.GLASS_CHEST.getRegistryName(), "inventory");
	    IBakedModel existingModel = modelRegistry.get(location);
	    GlassChestBakedModel bakedModel = new GlassChestBakedModel(existingModel);
	    event.getModelRegistry().put(location, bakedModel);
	}
	
	@SubscribeEvent
	public static void onGlassCubeModelBaked(ModelBakeEvent event) {
	    Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
	    ModelResourceLocation location = new ModelResourceLocation(BlockLoader.GLASS_CUBE.getRegistryName(), "inventory");
	    IBakedModel existingModel = modelRegistry.get(location);
	    GlassCubeBakedModel bakedModel = new GlassCubeBakedModel(existingModel);
	    event.getModelRegistry().put(location, bakedModel);
	}
}
