package modist.glasschest.common.block;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import modist.glasschest.common.tileentity.GlassChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GlassChestBlock extends ChestBlock {
	
	public static final IntegerProperty LIGHT = IntegerProperty.create("light", 0, 15);

	protected GlassChestBlock(Supplier<TileEntityType<? extends ChestTileEntity>> tileEntityTypeIn) {
		super(Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid(),
				tileEntityTypeIn);
		this.setDefaultState(this.stateContainer.getBaseState().with(LIGHT, 0).with(FACING, Direction.NORTH).with(TYPE, ChestType.SINGLE).with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	public GlassChestBlock() {
		super(Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid(),
				() -> BlockLoader.GLASS_CHEST_TILE_ENTITY);
		this.setDefaultState(this.stateContainer.getBaseState().with(LIGHT, 0));
	}
	
	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
	      return false;
	}
	
	@Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIGHT);
        super.fillStateContainer(builder);
    }
	
	@Override
	public int getLightValue(BlockState state)
    {
		return state.get(LIGHT);
    }
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		  return VoxelShapes.fullCube();
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		ChestType chesttype = ChestType.SINGLE;
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, direction).with(TYPE, chesttype).with(WATERLOGGED,
				Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new GlassChestTileEntity();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
		return false;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof GlassChestTileEntity) {
			GlassChestTileEntity glassChestTileEntity = (GlassChestTileEntity) tileEntity;
			if (!worldIn.isRemote) {
				ItemStack itemStack = new ItemStack(this);
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, 
						player.getHeldItem(player.getActiveHand())) == 0 && !player.isCreative()) {
					InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileEntity);	
					InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), 
							new ItemStack(Items.SHULKER_SHELL, 2));
				} else {
				CompoundNBT compoundNbt = glassChestTileEntity.write(new CompoundNBT());
				if (!compoundNbt.isEmpty()) {
					itemStack.setTagInfo("BlockEntityTag", compoundNbt);
				}

				if (glassChestTileEntity.hasCustomName()) {
					itemStack.setDisplayName(glassChestTileEntity.getCustomName());
				}

				ItemEntity itemEntity = new ItemEntity(worldIn, (double) pos.getX(), (double) pos.getY(),
						(double) pos.getZ(), itemStack);
				itemEntity.setDefaultPickupDelay();
				worldIn.addEntity(itemEntity);
				}
			}
		}
	      super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
	      if (state.getBlock() != newState.getBlock()) {
	         TileEntity tileentity = worldIn.getTileEntity(pos);
	         if (tileentity instanceof IInventory) {
	            worldIn.updateComparatorOutputLevel(pos, this);
	         }
	         if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
	             worldIn.removeTileEntity(pos);
	         }
	      }
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("LootTable", 8)) {
				tooltip.add(new StringTextComponent("???????"));
			}

			if (compoundnbt.contains("Items", 9)) {
				NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
				int i = 0;
				int j = 0;

				for (ItemStack itemstack : nonnulllist) {
					if (!itemstack.isEmpty()) {
						++j;
						if (i <= 4) {
							++i;
							ITextComponent itextcomponent = itemstack.getDisplayName().deepCopy();
							itextcomponent.appendText(" x").appendText(String.valueOf(itemstack.getCount()));
							tooltip.add(itextcomponent);
						}
					}
				}

				if (j - i > 0) {
					tooltip.add((new TranslationTextComponent("container.shulkerBox.more", j - i))
							.applyTextStyle(TextFormatting.ITALIC));
				}
			}
		}

	}

}
