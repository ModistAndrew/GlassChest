package modist.glasschest.common.tileentity;

import modist.glasschest.common.block.BlockLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
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
	private boolean shouldUpdate = false;
	
	protected GlassChestTileEntity(TileEntityType<?> typeIn) {
	      super(typeIn);
	}

	public GlassChestTileEntity() {
		super(BlockLoader.GLASS_CHEST_TILE_ENTITY);
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
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
		}
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
	/*
	 * @Override public void read(CompoundNBT compound) { super.read(compound); }
	 * 
	 * @Override public CompoundNBT write(CompoundNBT compound) { return
	 * super.write(compound); }
	 * 
	 * @Override public float getLidAngle(float partialTicks) { return 0; }
	 */
}