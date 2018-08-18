package quaternary.simpletrophies.common.tile;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.simpletrophies.SimpleTrophies;

public class SimpleTrophiesTiles {
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileSimpleTrophy.class, new ResourceLocation(SimpleTrophies.MODID, "trophy"));
	}
}
