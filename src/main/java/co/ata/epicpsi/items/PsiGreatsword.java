package co.ata.epicpsi.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import maninhouse.epicfight.capabilities.item.ModWeaponCapability;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Colliders;
import maninhouse.epicfight.gamedata.Skills;
import maninhouse.epicfight.gamedata.Sounds;
import maninhouse.epicfight.item.WeaponItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ToolSocketable;
import vazkii.psi.common.lib.ModTags;

public class PsiGreatsword extends WeaponItem implements IPsimetalTool {

    protected static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("16295ED8-B092-4A75-9A94-BCD8D56668BB");

    public PsiGreatsword(Properties build) {
        super(ModItemTier.PSI_GREATSWORD, 0, 0.0F, build);
    }

    @Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return ModTags.BLOCK_PSIMETAL.contains(repair.getItem());
    }
	
	@Override
	public int getItemEnchantability()
    {
        return 5;
    }
    
    @Override
	public boolean canHarvestBlock(BlockState blockIn)
    {
        return false;
    }

    @Override
    public void setWeaponCapability()
    {
		this.capability = new ModWeaponCapability(CapabilityItem.WeaponCategory.GREATSWORD, playerdata -> CapabilityItem.WieldStyle.TWO_HAND, null, Sounds.WHOOSH_BIG, Sounds.BLADE_HIT, Colliders.greatSword, CapabilityItem.HandProperty.TWO_HANDED);
		this.capability.addStyleCombo(CapabilityItem.WieldStyle.TWO_HAND, new StaticAnimation[] { Animations.GREATSWORD_AUTO_1, Animations.GREATSWORD_AUTO_2, Animations.GREATSWORD_DASH });
		this.capability.addStyleSpecialAttack(CapabilityItem.WieldStyle.TWO_HAND, Skills.GIANT_WHIRLWIND);
		this.capability.addStyleAttributeSimple(CapabilityItem.WieldStyle.TWO_HAND, 0.0D, 4.3D, 4);
		this.capability.addLivingMotionChanger(LivingMotion.IDLE, Animations.BIPED_IDLE_MASSIVE_HELD);
		this.capability.addLivingMotionChanger(LivingMotion.WALKING, Animations.BIPED_WALK_MASSIVE_HELD);
		this.capability.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_MASSIVE_HELD);
		this.capability.addLivingMotionChanger(LivingMotion.JUMPING, Animations.BIPED_JUMP_MASSIVE_HELD);
		this.capability.addLivingMotionChanger(LivingMotion.KNEELING, Animations.BIPED_KNEEL_MASSIVE_HELD);
		this.capability.addLivingMotionChanger(LivingMotion.SNEAKING, Animations.BIPED_SNEAK_MASSIVE_HELD);
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

        if (slot == EquipmentSlotType.MAINHAND)
        {
    		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 13.0D, Operation.ADDITION));
    		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.0D, Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Weapon modifier", -0.02D, Operation.ADDITION));
    	    return builder.build();
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
        ToolSocketable socketable = new ToolSocketable(stack, 3);
        return new ICapabilityProvider(){
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                LazyOptional<T> psiCap = socketable.getCapability(cap, side);
                if(psiCap != null && psiCap.isPresent()){
                    return psiCap;
                }
                return cap == ModCapabilities.CAPABILITY_ITEM ? optional.cast() : LazyOptional.empty();
            }
        };
	}
}
