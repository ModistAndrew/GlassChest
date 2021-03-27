package modist.glasschest.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlassChestItemStackTileEntityRenderer extends ItemStackTileEntityRenderer {
	private final ShulkerModel<?> model;

	@SuppressWarnings("rawtypes")
	public GlassChestItemStackTileEntityRenderer() {
		super();
		this.model = new ShulkerModel();
	}

	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		GlassChestTileEntityRenderer.renderBase(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model, 0);
		GlassChestTileEntityRenderer.renderContents
		(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);
	}

}