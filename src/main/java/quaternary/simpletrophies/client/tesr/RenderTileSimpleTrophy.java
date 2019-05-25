package quaternary.simpletrophies.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.client.ClientGameEvents;
import quaternary.simpletrophies.common.config.SimpleTrophiesConfig;
import quaternary.simpletrophies.common.etc.DateHelpers;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

public class RenderTileSimpleTrophy extends TileEntitySpecialRenderer<TileSimpleTrophy> {	
	@Override
	public void render(TileSimpleTrophy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(te == null) return;
		
		ItemStack displayedStack = te.displayedStack;
		
		if(!displayedStack.isEmpty()) {
			float ticks = ClientGameEvents.getPauseAdjustedTicksAndPartialTicks();
			
			//spread out animations a little bit.
			//...Used to use an actually pretty good hash function here, but I like the way this one makes
			//lines of trophies on the ground make a little wave. Wooo!
			ticks += (te.getPos().getX() ^ te.getPos().getZ()) * 30;
			
			GlStateManager.pushMatrix();
			
			GlStateManager.translate(x + .5, y + .6 + Math.sin(ticks / 25f) / 7f, z + .5);
			
			if(!Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(displayedStack, getWorld(), null).isGui3d()) {
				GlStateManager.translate(0, 0.2, 0);
			}
			
			GlStateManager.rotate((ticks * 2.5f) % 360, 0, 1, 0);
			GlStateManager.scale(1.6, 1.6, 1.6);
			try {
				
				Minecraft.getMinecraft().getRenderItem().renderItem(displayedStack, ItemCameraTransforms.TransformType.GROUND);
			} catch(Exception oof) {
				SimpleTrophies.LOG.error("Problem rendering item on a trophy TESR", oof);
			}
			
			GlStateManager.enableBlend(); //fix a stateleak in renderitem >.>
			
			GlStateManager.popMatrix();
		}
		
		RayTraceResult hit = rendererDispatcher.cameraHitResult;
		if(hit != null && te.getPos().equals(hit.getBlockPos())) {
			setLightmapDisabled(true);
			
			if(SimpleTrophiesConfig.SHOW_EARNEDAT && te.earnedTime != 0) {
				String formattedTime = DateHelpers.epochToString(te.earnedTime);
				drawNameplate(te, formattedTime, x, y + 0.3, z, 12);
			}
			
			String name = te.getLocalizedName();
			if (!name.isEmpty()) {
				drawNameplate(te, name, x, y, z, 12);
			}
			
			setLightmapDisabled(false);
		}
	}
}
