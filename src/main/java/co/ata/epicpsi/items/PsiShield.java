package co.ata.epicpsi.items;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import co.ata.epicpsi.EpicPsi;
import co.ata.epicpsi.client.ShieldTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;

public class PsiShield extends ShieldItem implements IPsimetalTool{

    private static final String TAG_TIMES_CAST = "timesCast";

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event){
        Entity entity = event != null ? event.getEntity() : null;
        float damage = event.getAmount();
        if (entity != null && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            ItemStack stack = player.getActiveItemStack();
            PlayerData data = PlayerDataHandler.get(player);
		    ItemStack playerCad = PsiAPI.getPlayerCAD(player);

            if (player.isActiveItemStackBlocking() == true && ((stack).getItem() instanceof PsiShield) && isEnabled(stack) && !playerCad.isEmpty() && !event.getSource().isUnblockable()) {
                int timesCast = stack.getOrCreateTag().getInt(TAG_TIMES_CAST);

                ItemStack bullet = ISocketable.socketable(stack).getSelectedBullet();
                ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
                    context.tool = stack;
                    Entity source = event.getSource().getTrueSource();
                    if(source instanceof LivingEntity){
                        context.attackingEntity = (LivingEntity)source;
                    }
                    context.damageTaken = event.getAmount();
                    context.loopcastIndex = timesCast;
                }, (int) (data.calculateDamageDeduction((float) event.getAmount()) * 0.75));

                stack.getOrCreateTag().putInt(TAG_TIMES_CAST, timesCast + 1);
            }
        }
    }

    public int getCastCooldown(ItemStack stack) {
		return 5;
	}

	public float getCastVolume() {
		return 0.025F;
	}

    public PsiShield(Properties builder) {
        super(builder);
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
