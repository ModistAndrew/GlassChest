package modist.glasschest.client.tileentity;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import modist.glasschest.GlassChest;
import modist.glasschest.common.tileentity.GlassCubeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ILightReader;
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
		//addQuad(Direction.UP, Blocks.YELLOW_STAINED_GLASS.getDefaultState(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		renderBase(bufferIn, tileEntityIn::getGlass, matrixStackIn, combinedLightIn, combinedOverlayIn);
	}

	//the base of glass cube, requiring extra data
	public static void renderBase(IRenderTypeBuffer bufferIn, Function<Direction, Block> glassProvider,
			MatrixStack matrixStackIn, int combinedLightIn, int combinedOverlayIn) {
		//IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutout(PlayerContainer.LOCATION_BLOCKS_TEXTURE));
		for (Direction d : Direction.values()) {
			if (glassProvider.apply(d) != null) {
				/*TextureAtlasSprite sprite = Minecraft.getInstance()
						.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(baseProvider.apply(d));
				addQuad(builder, matrixStackIn, d, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(),
						sprite.getMaxV(), combinedLightIn, combinedOverlayIn);*/
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

	private static void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float nx, float ny,
			float nz, float u, float v, int combinedLightIn, int combinedOverlayIn) {
		renderer.pos(stack.getLast().getMatrix(), x, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).tex(u, v)
				.overlay(combinedOverlayIn).lightmap(combinedLightIn).normal(stack.getLast().getNormal(), nx, ny, nz)
				.endVertex();
	}

	private static void addQuad(IVertexBuilder renderer, MatrixStack stack, Direction direction, float au, float av, float bu,
			float bv, int combinedLightIn, int combinedOverlayIn) {
		Vec3i[] vecs = new Vec3i[4];
		switch (direction) {
		case DOWN:
			vecs[0] = new Vec3i(0, 0, 0);
			vecs[1] = new Vec3i(1, 0, 0);
			vecs[2] = new Vec3i(1, 0, 1);
			vecs[3] = new Vec3i(0, 0, 1);
			break;
		case UP:
			vecs[0] = new Vec3i(0, 1, 0);
			vecs[1] = new Vec3i(0, 1, 1);
			vecs[2] = new Vec3i(1, 1, 1);
			vecs[3] = new Vec3i(1, 1, 0);
			break;
		case NORTH:
			vecs[0] = new Vec3i(0, 0, 0);
			vecs[1] = new Vec3i(0, 1, 0);
			vecs[2] = new Vec3i(1, 1, 0);
			vecs[3] = new Vec3i(1, 0, 0);
			break;
		case SOUTH:
			vecs[0] = new Vec3i(0, 0, 1);
			vecs[1] = new Vec3i(1, 0, 1);
			vecs[2] = new Vec3i(1, 1, 1);
			vecs[3] = new Vec3i(0, 1, 1);
			break;
		case WEST:
			vecs[0] = new Vec3i(0, 0, 0);
			vecs[1] = new Vec3i(0, 0, 1);
			vecs[2] = new Vec3i(0, 1, 1);
			vecs[3] = new Vec3i(0, 1, 0);
			break;
		case EAST:
			vecs[0] = new Vec3i(1, 0, 0);
			vecs[1] = new Vec3i(1, 1, 0);
			vecs[2] = new Vec3i(1, 1, 1);
			vecs[3] = new Vec3i(1, 0, 1);
			break;
		}
		Vec3i nvec = direction.getDirectionVec();
		add(renderer, stack, vecs[0].getX(), vecs[0].getY(), vecs[0].getZ(), nvec.getX(), nvec.getX(), nvec.getX(), au,
				av, combinedLightIn, combinedOverlayIn);
		add(renderer, stack, vecs[1].getX(), vecs[1].getY(), vecs[1].getZ(), nvec.getX(), nvec.getX(), nvec.getX(), bu,
				av, combinedLightIn, combinedOverlayIn);
		add(renderer, stack, vecs[2].getX(), vecs[2].getY(), vecs[2].getZ(), nvec.getX(), nvec.getX(), nvec.getX(), bu,
				bv, combinedLightIn, combinedOverlayIn);
		add(renderer, stack, vecs[3].getX(), vecs[3].getY(), vecs[3].getZ(), nvec.getX(), nvec.getX(), nvec.getX(), au,
				bv, combinedLightIn, combinedOverlayIn);
	}

}
