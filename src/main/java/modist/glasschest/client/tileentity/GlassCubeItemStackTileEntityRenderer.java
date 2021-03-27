package modist.glasschest.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class GlassCubeItemStackTileEntityRenderer extends GlassChestItemStackTileEntityRenderer {
	
	private final ShulkerModel<?> model;

	@SuppressWarnings("rawtypes")
	public GlassCubeItemStackTileEntityRenderer() {
		super();
		this.model = new ShulkerModel();
	}
	
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		GlassCubeTileEntityRenderer.renderBase(bufferIn, itemStackIn, matrixStackIn, combinedLightIn, combinedOverlayIn);
		GlassChestTileEntityRenderer.renderContents
		(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);
		}
	
}