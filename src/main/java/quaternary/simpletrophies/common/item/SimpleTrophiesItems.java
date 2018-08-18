package quaternary.simpletrophies.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.SimpleTrophiesUtil;
import quaternary.simpletrophies.common.block.SimpleTrophiesBlocks;

@GameRegistry.ObjectHolder(SimpleTrophies.MODID)
public class SimpleTrophiesItems {
	@GameRegistry.ObjectHolder(SimpleTrophiesBlocks.RegistryNames.TROPHY)
	public static final ItemBlock TROPHY = SimpleTrophiesUtil.notNullISwear();
	
	public static void registerItems(IForgeRegistry<Item> reg) {
		registerItemBlock(new ItemSimpleTrophy(SimpleTrophiesBlocks.TROPHY), reg);
	}
	
	private static void registerItemBlock(ItemBlock itemBlock, IForgeRegistry<Item> reg) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		reg.register(itemBlock);
	}
}
