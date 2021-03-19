package co.ata.epicpsi.items;

import co.ata.epicpsi.EpicPsi;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.psi.api.PsiAPI;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EpicPsi.MODID);

    public static final RegistryObject<Item> PSI_SPEAR = ITEMS.register("psi_spear", () -> new PsiSpear(new Item.Properties().group(EpicPsiGroup.ITEMS), PsiAPI.PSIMETAL_TOOL_MATERIAL));
    public static final RegistryObject<Item> PSI_GREATSWORD = ITEMS.register("psi_greatsword", () -> new PsiGreatsword(new Item.Properties().group(EpicPsiGroup.ITEMS)));
    public static final RegistryObject<Item> PSI_KATANA = ITEMS.register("psi_katana", () -> new PsiKatana(new Item.Properties().group(EpicPsiGroup.ITEMS).rarity(Rarity.RARE).defaultMaxDamage(650)));

    public static final RegistryObject<Item> PSI_SHIELD = ITEMS.register("psi_shield", () -> new PsiShield(new Item.Properties().group(EpicPsiGroup.ITEMS)));
}