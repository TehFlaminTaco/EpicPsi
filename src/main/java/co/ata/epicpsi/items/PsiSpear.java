package co.ata.epicpsi.items;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ToolSocketable;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import yesman.epicfight.animation.LivingMotion;
import yesman.epicfight.animation.types.StaticAnimation;
import yesman.epicfight.capabilities.item.CapabilityItem.HoldOption;
import yesman.epicfight.capabilities.item.CapabilityItem.WeaponCategory;
import yesman.epicfight.capabilities.item.CapabilityItem.Style;
import yesman.epicfight.capabilities.item.ModWeaponCapability;
import yesman.epicfight.capabilities.ModCapabilities;
import yesman.epicfight.gamedata.Animations;
import yesman.epicfight.gamedata.Colliders;
import yesman.epicfight.gamedata.Skills;
import yesman.epicfight.gamedata.Sounds;
import yesman.epicfight.item.WeaponItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

// Yoinked word for word from the SpearItem source because it didn't support IItemTier, I assume as a mistake.
public class PsiSpear extends WeaponItem implements IPsimetalTool {

    public PsiSpear(Item.Properties build, IItemTier materialIn) {
        super(materialIn, 3, -2.8F, build);
    }
    
    @Override
	public boolean canHarvestBlock(BlockState blockIn)
    {
        return false;
    }

    @Override
	public boolean hitEntity(ItemStack itemstack, LivingEntity target, @Nonnull LivingEntity attacker) {
		super.hitEntity(itemstack, target, attacker);

		if (isEnabled(itemstack) && attacker instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) attacker;

			PlayerData data = PlayerDataHandler.get(player);
			ItemStack playerCad = PsiAPI.getPlayerCAD(player);

			if (!playerCad.isEmpty()) {
				ItemStack bullet = ISocketable.socketable(itemstack).getSelectedBullet();
				ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F,
						(SpellContext context) -> {
							context.attackedEntity = target;
							context.tool = itemstack;
						});
			}
		}

		return true;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (!isEnabled(stack)) {
			modifiers.removeAll(Attributes.ATTACK_DAMAGE);
		}
		return modifiers;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		if (damage > stack.getMaxDamage()) {
			damage = stack.getDamage();
		}
		super.setDamage(stack, damage);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (!isEnabled(stack)) {
			return 1;
		}
		return super.getDestroySpeed(stack, state);
	}


    @Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
		ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
		tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ToolSocketable(stack, 3);
	}
}
