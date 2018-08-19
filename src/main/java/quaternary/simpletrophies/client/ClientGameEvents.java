package quaternary.simpletrophies.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.client.tesr.RenderItemStackSimpleTrophy;
import quaternary.simpletrophies.client.tesr.RenderTileSimpleTrophy;
import quaternary.simpletrophies.common.item.SimpleTrophiesItems;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

@Mod.EventBusSubscriber(modid = SimpleTrophies.MODID, value = Side.CLIENT)
public class ClientGameEvents {
	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		setSimpleItemModel(SimpleTrophiesItems.TROPHY);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileSimpleTrophy.class, new RenderTileSimpleTrophy());
		SimpleTrophiesItems.TROPHY.setTileEntityItemStackRenderer(new RenderItemStackSimpleTrophy());
	}
	
	private static void setSimpleItemModel(Item e) {
		ModelLoader.setCustomModelResourceLocation(e, 0, new ModelResourceLocation(e.getRegistryName(), "inventory"));
	}
	
	private static long ticksInGame = 0;
	private static boolean paused = false;
	private static float lastNonPausedPartialTicks = 0;
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent e) {
		if(e.phase == TickEvent.Phase.END) {
			Minecraft mc = Minecraft.getMinecraft();
			GuiScreen ui = mc.currentScreen;
			//same method mc uses to determine if the game is paused
			if(mc.isSingleplayer() && ui != null && ui.doesGuiPauseGame() && mc.getIntegratedServer() != null && !mc.getIntegratedServer().getPublic()) {
				paused = true;
			} else {
				ticksInGame++;
				paused = false;
			}
		}
	}
	
	public static long getTicksInGame() {
		return ticksInGame;
	}
	
	/** Doesn't return a changing partialTicks value when the game is paused, to prevent "jitter" behavior */
	public static float getPauseAdjustedPartialTicks() {
		//honestly should just prolly AT that shit in Minecraft.java, i mean
		if(paused) return lastNonPausedPartialTicks;
		else {
			return lastNonPausedPartialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		}
	}
	
	public static float getPauseAdjustedTicksAndPartialTicks() {
		return getTicksInGame() + getPauseAdjustedPartialTicks();
	}
}
