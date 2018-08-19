package quaternary.simpletrophies.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.common.block.SimpleTrophiesBlocks;
import quaternary.simpletrophies.common.item.ItemSimpleTrophy;
import quaternary.simpletrophies.common.item.SimpleTrophiesItems;
import quaternary.simpletrophies.common.tile.SimpleTrophiesTiles;

@Mod.EventBusSubscriber(modid = SimpleTrophies.MODID)
public class CommonGameEvents {
	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> e) {
		SimpleTrophiesBlocks.registerBlocks(e.getRegistry());
		SimpleTrophiesTiles.registerTileEntities();
	}
	
	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> e) {
		SimpleTrophiesItems.registerItems(e.getRegistry());
	}
}
