package co.ata.epicpsi.client;

import co.ata.epicpsi.EpicPsi;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Thanks @Better-Shields
// https://github.com/ToMe25/Better-Shields/blob/1.16.3/src/main/java/com/tome/bettershields/client/ShieldTextures.java
@OnlyIn(Dist.CLIENT)
public class ShieldTextures {
	public static final RenderMaterial LOCATION_PSI_SHIELD_BASE = material("entity/psi_shield_base");
	public static final RenderMaterial LOCATION_PSI_SHIELD_BASE_NOPATTERN = material("entity/psi_shield_base_nopattern");

	@SuppressWarnings("deprecation")
	private static RenderMaterial material(String path) {
		return new RenderMaterial(
				AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(EpicPsi.MODID, path));
    }
}
