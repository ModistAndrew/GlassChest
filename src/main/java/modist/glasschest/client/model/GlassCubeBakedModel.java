package modist.glasschest.client.model;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlassCubeBakedModel extends GlassChestBakedModel {
	
    public GlassCubeBakedModel(IBakedModel existingModel) {
        super(existingModel);
    }

}