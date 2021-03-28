package modist.glasschest.common.recipe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import modist.glasschest.common.block.BlockLoader;
import modist.glasschest.common.event.CommonRegistryEventHandler;
import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GlassCubeCraftingRecipe extends SpecialRecipe {

	private int[] glassSlots = new int[] { 0, 1, 2, 3, 5, 6, 7, 8 };

	public GlassCubeCraftingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		return !inv.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {	
		ItemStack result = ItemStack.EMPTY;
		ItemStack[] shearStack = getShear(inv);
		if(shearStack[0]!=null && shearStack[1]!=null) {
			CompoundNBT nbt = shearStack[1].getChildTag("BlockEntityTag");
			if (nbt == null) {
				nbt = new CompoundNBT();
			} else {
				nbt = nbt.copy();
			}
			for (Direction d : Direction.values()) {
				nbt.putInt(d.getName(), 0);
			}
			result = new ItemStack(BlockLoader.GLASS_CUBE_ITEM);
			result.setTagInfo("BlockEntityTag", nbt);
			if (shearStack[1].hasDisplayName()) {
				result.setDisplayName(shearStack[1].getDisplayName());
			}
			return result;	
		}
		
		int[] glasses = new int[8];
		for (int i = 0; i < glassSlots.length; i++) {
			int index = GlassCubeTileEntity.GLASS_PANES_ARRAY.indexOf(inv.getStackInSlot(glassSlots[i]).getItem());
			if (index == -1) {
				return result;
			}
			glasses[i] = index;
		}
		
		ItemStack core = inv.getStackInSlot(4);
		if (core.getItem().equals(BlockLoader.GLASS_CHEST_ITEM) || core.getItem().equals(BlockLoader.GLASS_CUBE_ITEM)) {
			Arrays.sort(glasses);
			if (glasses[0] != glasses[glasses.length - 1]) {
				return result;
			}
			CompoundNBT nbt = core.getChildTag("BlockEntityTag");
			if (nbt == null && core.getItem().equals(BlockLoader.GLASS_CUBE_ITEM)) {
				nbt = new CompoundNBT();
				for(Direction d : Direction.values()) {
					nbt.putInt(d.getName(), 1);
				}
			} else {
				nbt = nbt.copy();
			}
			
			int glasses1[] = new int[6];
			for (Direction d : Direction.values()) {
				if (nbt.contains(d.getName())) {
					glasses1[d.getIndex()] = nbt.getInt(d.getName());
				}
				nbt.putInt(d.getName(), glasses[0]);
			}
			Arrays.sort(glasses1);
			if (glasses1[0] == glasses1[glasses1.length - 1]) {
				if (glasses1[0] == glasses[0]) {
					return result;
				}
			}
			
			result = new ItemStack(BlockLoader.GLASS_CUBE_ITEM);
			result.setTagInfo("BlockEntityTag", nbt);
			if (core.hasDisplayName()) {
				result.setDisplayName(core.getDisplayName());
			}
			return result;
		}
		return result;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		ItemStack[] shearStack = getShear(inv);
		if(shearStack[0]!=null && shearStack[1]!=null) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if(inv.getStackInSlot(i).equals(shearStack[0])) {
					shearStack[0] = shearStack[0].copy();
					int damage = shearStack[0].getDamage()+8;
					if(shearStack[0].getMaxDamage() >= damage) {
					shearStack[0].setDamage(damage);
					nonnulllist.set(i, shearStack[0]);
					}
					return nonnulllist;
				}
			}
		}
		
		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack item = inv.getStackInSlot(i);
			if (item.hasContainerItem()) {
				nonnulllist.set(i, item.getContainerItem());
			}
		}

		return nonnulllist;
	}

	private ItemStack[] getShear(CraftingInventory inv) {
		ItemStack[] shearStack = new ItemStack[] {null, null};
		Set<Item> invSet = new HashSet<Item>();
		Set<Item> targetSet1 = new HashSet<Item>();
		targetSet1.add(Items.SHEARS);
		targetSet1.add(BlockLoader.GLASS_CHEST_ITEM);
		Set<Item> targetSet2 = new HashSet<Item>();
		targetSet2.add(Items.SHEARS);
		targetSet2.add(BlockLoader.GLASS_CUBE_ITEM);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if(!inv.getStackInSlot(i).isEmpty()) {
				if(!invSet.add(inv.getStackInSlot(i).getItem())) {
					return shearStack;
				}
			}
		}
		if(invSet.equals(targetSet1)||invSet.equals(targetSet2)) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if(inv.getStackInSlot(i).getItem().equals(Items.SHEARS)) {
					shearStack[0] = inv.getStackInSlot(i);
				}
				if(inv.getStackInSlot(i).getItem().equals(BlockLoader.GLASS_CHEST_ITEM)||
						inv.getStackInSlot(i).getItem().equals(BlockLoader.GLASS_CUBE_ITEM)) {
					CompoundNBT nbt = inv.getStackInSlot(i).getChildTag("BlockEntityTag");
					boolean flag = false;
					if (nbt == null) {
						nbt = new CompoundNBT();
					} else {
						nbt = nbt.copy();
					}
					for (Direction d : Direction.values()) {
						if(!nbt.contains(d.getName())||nbt.getInt(d.getName())!=0) {
							flag = true;
							break;
						}
					}
					if(flag) {
					shearStack[1] = inv.getStackInSlot(i);
					}
				}
			}
		}	
		return shearStack;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return CommonRegistryEventHandler.CRAFTING_GLASS_CUBE;
	}

}
