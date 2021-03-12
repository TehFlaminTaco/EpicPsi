package co.ata.epicpsi.client;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import co.ata.epicpsi.items.ModItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Thanks @Better-Shields
// https://github.com/ToMe25/Better-Shields/blob/1.16.3/src/main/java/com/tome/bettershields/client/ShieldTileEntityRenderer.java
@OnlyIn(Dist.CLIENT)
public class ShieldTileEntityRenderer extends ItemStackTileEntityRenderer {

	// Why is this private in the renderer?
	private final ShieldModel modelShield = new ShieldModel();

	@Override
	public void func_239207_a_(ItemStack stack, TransformType p_239207_2_, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		matrixStack.push();
		matrixStack.scale(1, -1, -1);
		boolean flag = stack.getChildTag("BlockEntityTag") != null;
		RenderMaterial rendermaterial = flag ? ShieldTextures.LOCATION_PSI_SHIELD_BASE
		: ShieldTextures.LOCATION_PSI_SHIELD_BASE_NOPATTERN;

		IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(
				buffer, modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
		this.modelShield.func_228294_b_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
				1.0F, 1.0F, 1.0F);
		if (flag) {
			List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.getPatternColorData(ShieldItem.getColor(stack),
					BannerTileEntity.getPatternData(stack));
			BannerTileEntityRenderer.func_241717_a_(matrixStack, buffer, combinedLight, combinedOverlay,
					this.modelShield.func_228293_a_(), rendermaterial, false, list, stack.hasEffect());
		} else {
			this.modelShield.func_228293_a_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
					1.0F, 1.0F, 1.0F);
		}
		matrixStack.pop();
	}

}
