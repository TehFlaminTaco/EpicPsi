package co.ata.epicpsi.items;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import co.ata.epicpsi.client.ShieldTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.common.item.tool.IPsimetalTool;

public class PsiShield extends ShieldItem implements IPsimetalTool{

    public PsiShield(Properties builder) {
        super(builder.maxDamage(900).setISTER(() -> getISTER()));
    }

    @Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

    @OnlyIn(Dist.CLIENT)
	public static Callable<ItemStackTileEntityRenderer> getISTER() {
		return ShieldTileEntityRenderer::new;
	}

    @Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return IPsimetalTool.super.initCapabilities(stack, nbt);
	}

    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return stack.getItem() instanceof PsiShield;
    }
}
