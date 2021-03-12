package co.ata.epicpsi.client;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import co.ata.epicpsi.EpicPsi;

@EventBusSubscriber(value = Dist.CLIENT, modid = EpicPsi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModelHandler {
    @SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onStitch(TextureStitchEvent.Pre event) {
        EpicPsi.LOGGER.info("Prestich Fired! " + event.getMap().toString());
        System.out.println("PLEASE WORK!");
		if (event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            EpicPsi.LOGGER.info("Stitching in shield..?");
            event.addSprite(ShieldTextures.LOCATION_PSI_SHIELD_BASE.getTextureLocation());
            event.addSprite(ShieldTextures.LOCATION_PSI_SHIELD_BASE_NOPATTERN.getTextureLocation());
		}
	}
}
