package quaternary.simpletrophies.common.block;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.SimpleTrophiesUtil;

@GameRegistry.ObjectHolder(SimpleTrophies.MODID)
public class SimpleTrophiesBlocks {
	public static final class RegistryNames {
		public static final String TROPHY = "trophy";
	}
	
	@GameRegistry.ObjectHolder(RegistryNames.TROPHY)
	public static final BlockSimpleTrophy TROPHY = SimpleTrophiesUtil.notNullISwear();
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		registerBlock(new BlockSimpleTrophy(), RegistryNames.TROPHY, reg);
	}
	
	private static void registerBlock(Block b, String name, IForgeRegistry<Block> reg) {
		b.setRegistryName(new ResourceLocation(SimpleTrophies.MODID, name));
		b.setTranslationKey(SimpleTrophies.MODID + "." + name);
		reg.register(b);
	}
}
