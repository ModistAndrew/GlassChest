package modist.glasschest.client.tileentity;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public class GlassCubeTileEntityRenderer extends TileEntityRenderer<GlassCubeTileEntity> {

	private final ShulkerModel<?> model;

	@SuppressWarnings("rawtypes")
	public GlassCubeTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		this.model = new ShulkerModel();
	}

	@Override
	public void render(GlassCubeTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		GlassChestTileEntityRenderer.renderContents(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn,
				tileEntityIn::getStackInSlot, model);
		renderBase(bufferIn, tileEntityIn::getGlass, matrixStackIn, combinedLightIn, combinedOverlayIn);
	}

	//the base of glass cube, requiring extra data
	public static void renderBase(IRenderTypeBuffer bufferIn, Function<Direction, Block> glassProvider,
			MatrixStack matrixStackIn, int combinedLightIn, int combinedOverlayIn) {
		for (Direction d : Direction.values()) {
			if (glassProvider.apply(d) != null) {
				addQuad(d, glassProvider.apply(d).getDefaultState(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
			}
		}
	}
	
	//the base of glass cube for item
	public static void renderBase(IRenderTypeBuffer bufferIn, ItemStack itemStackIn,
			MatrixStack matrixStackIn, int combinedLightIn, int combinedOverlayIn) {
		CompoundNBT compoundnbt = itemStackIn.getChildTag("BlockEntityTag");
		if(compoundnbt!=null) {
		renderBase(bufferIn, 
		d -> GlassCubeTileEntity.GLASSES[compoundnbt.getInt(d.getName())], matrixStackIn, combinedLightIn, combinedOverlayIn);
		} else {
		renderBase(bufferIn, 
		d -> GlassCubeTileEntity.GLASSES[1], matrixStackIn, combinedLightIn, combinedOverlayIn);	
		}
	}
	
	private static void addQuad(Direction d, BlockState blockStateIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferTypeIn, int combinedLightIn, int combinedOverlayIn) {
		BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		IBakedModel ibakedmodel = dispatcher.getModelForState(blockStateIn);
		Random random = new Random();
	    random.setSeed(42L);
	    renderModelBrightnessColorQuads(matrixStackIn.getLast(), bufferTypeIn.getBuffer(RenderTypeLookup.getRenderType(blockStateIn)), 
	    1.0F, 1.0F, 1.0F, ibakedmodel.getQuads(blockStateIn, d, random, EmptyModelData.INSTANCE), combinedLightIn, combinedOverlayIn);
	}
	
	private static void renderModelBrightnessColorQuads(MatrixStack.Entry matrixEntry, IVertexBuilder buffer, float red, float green, float blue, List<BakedQuad> listQuads, int combinedLightIn, int combinedOverlayIn) {
	      for(BakedQuad bakedquad : listQuads) {
	         float f;
	         float f1;
	         float f2;
	         if (bakedquad.hasTintIndex()) {
	            f = MathHelper.clamp(red, 0.0F, 1.0F);
	            f1 = MathHelper.clamp(green, 0.0F, 1.0F);
	            f2 = MathHelper.clamp(blue, 0.0F, 1.0F);
	         } else {
	            f = 1.0F;
	            f1 = 1.0F;
	            f2 = 1.0F;
	         }

	         buffer.addQuad(matrixEntry, bakedquad, f, f1, f2, combinedLightIn, combinedOverlayIn);
	      }

	}

}
