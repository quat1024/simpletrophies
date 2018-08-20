package quaternary.simpletrophies.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.client.ClientGameEvents;
import quaternary.simpletrophies.common.etc.TrophyHelpers;
import quaternary.simpletrophies.common.item.ItemSimpleTrophy;

public class RenderItemStackSimpleTrophy extends TileEntityItemStackRenderer {
	//todo hardcode less
	static final ModelResourceLocation baseMrl = new ModelResourceLocation(new ResourceLocation(SimpleTrophies.MODID, "trophy"), "normal");
	
	int recursionDepth = 0;
	
	@Override
	public void renderByItem(ItemStack stack) {
		//Render the base
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		ModelManager mm = brd.getBlockModelShapes().getModelManager();
		int color = TrophyHelpers.getCombinedColor(stack);
		float red = ((color & 0xFF0000) >> 16) / 255f;
		float green = ((color & 0x00FF00) >> 8) / 255f;
		float blue = (color & 0x0000FF) / 255f;
		brd.getBlockModelRenderer().renderModelBrightnessColor(mm.getModel(baseMrl), 1f, red, green, blue);
		
		//Render the item
		ItemStack displayedStack = TrophyHelpers.getDisplayedStack(stack);
		
		if(!displayedStack.isEmpty()) {
			float ticks = ClientGameEvents.getPauseAdjustedTicksAndPartialTicks();
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, .5, .5);
			if(displayedStack.getItem() instanceof ItemBlock) GlStateManager.translate(0, -0.1, 0);
			
			GlStateManager.rotate(ticks * 2.5f, 0, 1, 0);
			GlStateManager.scale(2, 2, 2);
			
			recursionDepth++;
			
			//Too many nested pushmatrixes can cause severe render glitching on my pc.
			//Nobody's going to actually hand out trophies of trophies of trophies of trophies of trophies of trophies anyways.
			//No, that's not a challenge, stop it.
			//And you can't even see it anyways it's so small.
			if(recursionDepth < 5) {
				try {
					Minecraft.getMinecraft().getRenderItem().renderItem(displayedStack, ItemCameraTransforms.TransformType.GROUND);
				} catch(Exception oof) {
					oof.printStackTrace();
				}
			}
			
			recursionDepth--;
			
			GlStateManager.enableBlend(); //fix a stateleak
			
			GlStateManager.popMatrix();
		}
	}
}
