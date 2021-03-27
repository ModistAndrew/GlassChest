package modist.glasschest.common.tileentity;

import java.util.function.Function;

import modist.glasschest.common.block.BlockLoader;
import modist.glasschest.common.block.GlassChestBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class GlassChestTileEntity extends ChestTileEntity {
	private boolean shouldUpdate = true;
	private int light = 0;
	
	protected GlassChestTileEntity(TileEntityType<?> typeIn) {
	      super(typeIn);
	}

	public GlassChestTileEntity() {
		super(BlockLoader.GLASS_CHEST_TILE_ENTITY);
	}
	
	public int getLight() {
		return light;
	}
	
	private int getLight(Function<Integer, ItemStack> itemStackProvider) {
		int max = 0;
		for (int i = 0; i < 27; i++) {
			int light = 0;
			ItemStack itemStack = itemStackProvider.apply(i);
			Item item = itemStack.getItem();
			if (item instanceof BlockItem) {
				BlockItem blockItem = (BlockItem) item;
				if(blockItem.getBlock().equals(BlockLoader.GLASS_CHEST)||blockItem.getBlock().equals(BlockLoader.GLASS_CUBE)) {
				light = getLight(itemStack);
				} else {
				light = blockItem.getBlock().getDefaultState().getLightValue();
				}
			}
			if(light > max) {
				max = light;
			}
		}
		return max;
	}
		
	private int getLight(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
		if (compoundnbt != null) {
				return compoundnbt.getInt("lightValue");
		}
		return 0;
	}

	@Override
	protected ITextComponent getDefaultName() {
	      return new TranslationTextComponent("block.glasschest.glass_chest");
	}
	
	@Override
	public void tick() {
		super.tick();
		if (shouldUpdate) {
			shouldUpdate = false;
			int light1 = this.getLight(this::getStackInSlot);
			if(light!=light1) {
			light = light1;
			world.setBlockState(pos, getBlockState().with(GlassChestBlock.LIGHT, light));
			} else {
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		shouldUpdate = true;
		super.setItems(itemsIn);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		shouldUpdate = true;
		return super.decrStackSize(index, count);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		shouldUpdate = true;
		super.setInventorySlotContents(index, stack);
	}


	@Override
	public ItemStack removeStackFromSlot(int index) {
		shouldUpdate = true;
		return super.removeStackFromSlot(index);
	}

	@Override
	public void clear() {
		shouldUpdate = true;
		super.clear();
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT compoundNBT = super.getUpdateTag();
		this.write(compoundNBT);
		return compoundNBT;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		light = compound.getInt("lightValue");
		if(world!=null) {
		if(getBlockState().get(GlassChestBlock.LIGHT)!=light) {
			world.setBlockState(pos, getBlockState().with(GlassChestBlock.LIGHT, light));
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("lightValue", light);
		super.write(compound);
		return compound;
	}

}