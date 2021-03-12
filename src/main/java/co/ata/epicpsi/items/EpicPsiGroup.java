package co.ata.epicpsi.items;

import co.ata.epicpsi.EpicPsi;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class EpicPsiGroup {
    public static final ItemGroup ITEMS = new ItemGroup(EpicPsi.MODID + ".group"){
        @Override
        public ItemStack createIcon(){
            return new ItemStack(ModItems.PSI_SPEAR.get());
        }
    };
}
