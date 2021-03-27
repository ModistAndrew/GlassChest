package modist.glasschest.common.event;

import modist.glasschest.GlassChest;
import modist.glasschest.common.block.BlockLoader;
import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GlassChest.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

	@SubscribeEvent
	public static void handleGlassCube(final RightClickBlock event) {
		World world = event.getWorld();
		BlockState state = world.getBlockState(event.getPos());
		TileEntity tile0 = world.getTileEntity(event.getPos());
		if (state.getBlock().equals(BlockLoader.GLASS_CUBE) && tile0 instanceof GlassCubeTileEntity
				&& event.getPlayer().isSneaking()) {
			GlassCubeTileEntity tile = (GlassCubeTileEntity) tile0;
			ItemStack item = event.getItemStack();
			if (item.getItem().equals(Items.SHEARS)) {
				if (!world.isRemote) {
					if(tile.shouldSetSide(event.getFace(), null)) {
						tile.setSide(event.getFace(), null);
						if(!event.getPlayer().isCreative()) {
							item.damageItem(1, event.getPlayer(), e -> e.sendBreakAnimation(event.getHand()));
						}
					}
				}
				event.setCanceled(true);
			}
			if (GlassCubeTileEntity.GLASS_PANES_ARRAY.contains(item.getItem())) {
				if (!world.isRemote) {
					if(tile.shouldSetSide(event.getFace(), item.getItem())) {
						tile.setSide(event.getFace(), item.getItem());
						if(!event.getPlayer().isCreative()) {
							item.shrink(1);
						}
					}
				}	
				event.setCanceled(true);
			}
		}
	}

}
