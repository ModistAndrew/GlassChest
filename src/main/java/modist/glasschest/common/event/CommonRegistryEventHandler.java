package modist.glasschest.common.event;

import modist.glasschest.GlassChest;
import modist.glasschest.common.block.BlockLoader;
import modist.glasschest.common.recipe.GlassCubeCraftingRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GlassChest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonRegistryEventHandler {
	
	public static final IRecipeSerializer<GlassCubeCraftingRecipe> CRAFTING_GLASS_CUBE = IRecipeSerializer.register
			(new ResourceLocation(GlassChest.MODID, "crafting_glass_cube").toString(),
					new SpecialRecipeSerializer<>(GlassCubeCraftingRecipe::new));

	@SubscribeEvent(priority = EventPriority.HIGH)
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
	
	@SubscribeEvent
	public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
	event.getRegistry().register(CRAFTING_GLASS_CUBE);
	}

}
