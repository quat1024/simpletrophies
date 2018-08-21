package quaternary.simpletrophies.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.client.ClientGameEvents;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

public class RenderTileSimpleTrophy extends TileEntitySpecialRenderer<TileSimpleTrophy> {	
	@Override
	public void render(TileSimpleTrophy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack displayedStack = te.displayedStack;
		
		if(!displayedStack.isEmpty()) {
			float ticks = ClientGameEvents.getPauseAdjustedTicksAndPartialTicks();
			
			//spread out animations a little bit.
			//...Used to use an actually pretty good hash function here, but I like the way this one makes
			//lines of trophies on the ground make a little wave. Wooo!
			ticks += (te.getPos().getX() ^ te.getPos().getZ()) * 30;
			
			GlStateManager.pushMatrix();
			
			GlStateManager.translate(x + .5, y + .7 + Math.sin(ticks / 25f) / 7f, z + .5);
			if(displayedStack.getItem() instanceof ItemBlock) GlStateManager.translate(0, -0.1, 0);
			
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
		
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
