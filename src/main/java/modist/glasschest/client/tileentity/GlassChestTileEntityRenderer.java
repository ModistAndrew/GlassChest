package modist.glasschest.client.tileentity;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import modist.glasschest.GlassChest;
import modist.glasschest.common.block.BlockLoader;
import modist.glasschest.common.tileentity.GlassChestTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public class GlassChestTileEntityRenderer extends TileEntityRenderer<GlassChestTileEntity> {

	private final ShulkerModel<?> model;
	public static final ResourceLocation GLASS_CHEST_TEXTURE = new ResourceLocation(GlassChest.MODID,
			"block/glass_chest");

	@SuppressWarnings("rawtypes")
	public GlassChestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		this.model = new ShulkerModel();
	}

	@Override
	public void render(GlassChestTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		renderBase(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model, -tileEntityIn.getLidAngle(partialTicks));
		renderContents(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, tileEntityIn::getStackInSlot, model);
	}

	private static BlockPos getPosFromIndex(int index) {
		return new BlockPos(2 - index / 9, index % 9 / 3, index % 9 % 3);
	}

	private static float diffFunction(long time, long delta, float scale) {
		long dt = time % (delta * 2);
		if (dt > delta) {
			dt = 2 * delta - dt;
		}
		return dt * scale;
	}
	
	//the base of glass chest
	public static void renderBase(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, 
			int combinedLightIn, int combinedOverlayIn, ShulkerModel<?> model, float lid) {
		matrixStackIn.push();
		Direction direction = Direction.UP;
		Material material = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, GLASS_CHEST_TEXTURE);
		matrixStackIn.translate(0.5D, 0.5D, 0.5D);
		matrixStackIn.scale(0.99995F, 0.99995F, 0.99995F);
		matrixStackIn.rotate(direction.getRotation());
		matrixStackIn.scale(1.0F, -1.0F, -1.0F);
		matrixStackIn.translate(0.0D, -1.0D, 0.0D);
		IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);
		model.getBase().render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
		matrixStackIn.translate(0.0D, (double) (lid * 0.5F), 0.0D);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270.0F * lid));
		model.getLid().render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
		matrixStackIn.pop();
	}
	
	//for tileentity
	public static void renderContents(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, 
			int combinedLightIn, int combinedOverlayIn, Function<Integer, ItemStack> itemStackProvider, ShulkerModel<?> model) {
		matrixStackIn.push();
		matrixStackIn.translate(0.5D, 0.5D, 0.5D);
		matrixStackIn.scale(0.9995F, 0.9995F, 0.9995F);
		//matrixStackIn.translate(-0.5D/0.95F, -0.5D/0.95F, -0.5D/0.95F);
		matrixStackIn.scale(1F / 3, 1F / 3, 1F / 3);
		for (int i = 0; i < 27; i++) {
			BlockPos renderPos = getPosFromIndex(i);
			matrixStackIn.push();
			matrixStackIn.translate(-1.5D, -1.5D, -1.5D);
			matrixStackIn.translate(renderPos.getX(), renderPos.getY(), renderPos.getZ());
			ItemStack itemStack = itemStackProvider.apply(i);
			Item item = itemStack.getItem();
			if (item instanceof BlockItem) {
				BlockItem blockItem = (BlockItem) item;
				if(blockItem.getBlock().equals(BlockLoader.GLASS_CHEST)) {
					renderContents(itemStack, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);
					renderBase(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model, 0);
				} else if (blockItem.getBlock().equals(BlockLoader.GLASS_CUBE)) {
					renderContents(itemStack, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);
					GlassCubeTileEntityRenderer.renderBase(bufferIn, itemStack, matrixStackIn, combinedLightIn, combinedOverlayIn);
				} else {
				Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockItem.getBlock().getDefaultState(),
						matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
				}
			} else {
				long time = System.currentTimeMillis();
				float angle = (time / 100) % 360;
				Quaternion rotation = Vector3f.YP.rotationDegrees(angle);
				float trans = diffFunction(time, 1000, 0.0002F);
				matrixStackIn.scale(0.8F, 0.8F, 0.8F);
				matrixStackIn.translate(0.5F, 0.55F, 0.5F);
				matrixStackIn.translate(0, trans, 0);
				matrixStackIn.rotate(rotation);
				Minecraft.getInstance().getItemRenderer().renderItem(itemStack,
						ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn,
						bufferIn);
			}
			matrixStackIn.pop();
		}
		matrixStackIn.pop();
	}
	
	//for item
	public static void renderContents(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn, ShulkerModel<?> model) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
		CompoundNBT compoundnbt = itemStackIn.getChildTag("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("Items", 9)) {
				ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
				}
		}
		renderContents(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, i -> nonnulllist.get(i), model);
	}
		

}
