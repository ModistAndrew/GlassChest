package modist.glasschest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("glasschest")
public class GlassChest {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "glasschest";

	public GlassChest() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		
	}

}
