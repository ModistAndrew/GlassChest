package modist.glasschest.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class GlassCubeItemStackTileEntityRenderer extends GlassChestItemStackTileEntityRenderer {
	
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
		CompoundNBT compoundnbt = itemStackIn.getChildTag("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("Items", 9)) {
				ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
				}
		}
		GlassChestTileEntityRenderer.renderContents
		(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, i -> nonnulllist.get(i));
	}
	
}