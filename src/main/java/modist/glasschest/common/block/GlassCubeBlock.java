package modist.glasschest.common.block;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class GlassCubeBlock extends AbstractGlassBlock{

	public GlassCubeBlock() {
		super(Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid());
	}
}
