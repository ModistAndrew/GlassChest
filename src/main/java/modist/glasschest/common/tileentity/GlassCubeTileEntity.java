package modist.glasschest.common.tileentity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import modist.glasschest.common.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GlassCubeTileEntity extends GlassChestTileEntity {

	public static final Item[] GLASS_PANES = { null, Items.GLASS_PANE, Items.WHITE_STAINED_GLASS_PANE,
			Items.ORANGE_STAINED_GLASS_PANE, Items.MAGENTA_STAINED_GLASS_PANE, Items.LIGHT_BLUE_STAINED_GLASS_PANE,
			Items.YELLOW_STAINED_GLASS_PANE, Items.LIME_STAINED_GLASS_PANE, Items.PINK_STAINED_GLASS_PANE,
			Items.GRAY_STAINED_GLASS_PANE, Items.LIGHT_GRAY_STAINED_GLASS_PANE, Items.CYAN_STAINED_GLASS_PANE,
			Items.PURPLE_STAINED_GLASS_PANE, Items.BLUE_STAINED_GLASS_PANE, Items.BROWN_STAINED_GLASS_PANE,
			Items.GREEN_STAINED_GLASS_PANE, Items.RED_STAINED_GLASS_PANE, Items.BLACK_STAINED_GLASS_PANE };
	public static final Block[] GLASSES = { null, Blocks.GLASS, Blocks.WHITE_STAINED_GLASS,
			Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS,
			Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS,
			Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS,
			Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS,
			Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS };
	public static final List<Item> GLASS_PANES_ARRAY = Arrays.asList(GLASS_PANES);

	private Map<Direction, Integer> sides = new HashMap<Direction, Integer>();

	public GlassCubeTileEntity() {
		super(BlockLoader.GLASS_CUBE_TILE_ENTITY);
		for (Direction d : Direction.values()) {
			sides.put(d, 1);
		}
	}

	public boolean shouldSetSide(Direction d, @Nullable Item glass) {
		return sides.get(d) != GLASS_PANES_ARRAY.indexOf(glass);
	}
	
	public void setSide(Direction d, @Nullable Item glass) {
		sides.put(d, GLASS_PANES_ARRAY.indexOf(glass));
		this.markDirty();
		world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
	}
	
	public @Nullable Block getGlass(Direction d) {
		return GLASSES[sides.get(d)];
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("block.glasschest.glass_cube");
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
		for (Direction d : Direction.values()) {
			sides.put(d, compound.getInt(d.getName()));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		sides.forEach((d, b) -> compound.putInt(d.getName(), b));
		super.write(compound);
		return compound;
	}
}