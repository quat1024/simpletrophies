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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.client.ClientGameEvents;
import quaternary.simpletrophies.common.item.ItemSimpleTrophy;

public class RenderItemStackSimpleTrophy extends TileEntityItemStackRenderer {
	//todo hardcode less
	static final ModelResourceLocation baseMrl = new ModelResourceLocation(new ResourceLocation(SimpleTrophies.MODID, "trophy"), "normal");
	
	@Override
	public void renderByItem(ItemStack stack) {
		//Render the base
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		ModelManager mm = brd.getBlockModelShapes().getModelManager();
		brd.getBlockModelRenderer().renderModelBrightnessColor(mm.getModel(baseMrl), 1f, 1f, 1f, 1f);
		
		//Render the item
		ItemStack displayedStack = ItemSimpleTrophy.getDisplayedItem(stack);
		
		if(!displayedStack.isEmpty()) {
			float ticks = ClientGameEvents.getPauseAdjustedTicksAndPartialTicks();
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, .6 + Math.sin(ticks / 25f) / 7f, .5);
			GlStateManager.rotate(ticks * 2.5f, 0, 1, 0);
			GlStateManager.scale(1.2, 1.2, 1.2);
			
			try {
				Minecraft.getMinecraft().getRenderItem().renderItem(displayedStack, ItemCameraTransforms.TransformType.GROUND);
			} catch(Exception oof) {
				oof.printStackTrace();
			}
			
			GlStateManager.enableBlend(); //fix a stateleak
			
			GlStateManager.popMatrix();
		}
	}
}
