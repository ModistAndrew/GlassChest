package modist.glasschest.client.model;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public class GlassChestBakedModel implements IBakedModel {
    private IBakedModel existingModel;

    public GlassChestBakedModel(IBakedModel existingModel) {
        this.existingModel = existingModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return this.existingModel.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.existingModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.existingModel.isGui3d();
    }

    @Override
    public boolean func_230044_c_() {
        return this.existingModel.func_230044_c_();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.existingModel.getParticleTexture(EmptyModelData.INSTANCE);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.existingModel.getOverrides();
    }
    
    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
    	this.existingModel.handlePerspective(cameraTransformType, mat);
    	return this;
    }

}