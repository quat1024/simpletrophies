package quaternary.simpletrophies.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import quaternary.simpletrophies.client.ClientGameEvents;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

public class RenderTileSimpleTrophy extends TileEntitySpecialRenderer<TileSimpleTrophy> {
	public RenderTileSimpleTrophy(boolean isTeisr) {
		this.isTeisr = isTeisr;
	}
	
	private final boolean isTeisr;
	
	@Override
	public void render(TileSimpleTrophy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack displayedStack = te.displayedStack;
		
		float ticks = ClientGameEvents.getTicksInGame() + partialTicks;
		
		if(!isTeisr) {
			//spread out their animations a little bit.
			ticks += MathHelper.hash(MathHelper.hash(te.getPos().hashCode())) % 150000;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + .5, y + .6 + Math.sin(ticks / 30f) / 6f, z + .5);
		GlStateManager.rotate(ticks * 2.5f, 0, 1, 0);
		GlStateManager.scale(1.2, 1.2, 1.2);
		
		if(!isTeisr) RenderHelper.enableStandardItemLighting();
		try {
			Minecraft.getMinecraft().getRenderItem().renderItem(displayedStack, ItemCameraTransforms.TransformType.GROUND);
		} catch (Exception oof) {
			oof.printStackTrace();
		}
		if(!isTeisr) RenderHelper.disableStandardItemLighting();
		
		GlStateManager.enableBlend(); //fix a stateleak
		
		GlStateManager.popMatrix();
		
		//No need to draw the nameplate on the teisr version
		if(!isTeisr) super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
