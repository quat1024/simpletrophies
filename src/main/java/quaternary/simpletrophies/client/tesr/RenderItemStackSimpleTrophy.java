package quaternary.simpletrophies.client.tesr;

import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.client.ClientGameEvents;
import quaternary.simpletrophies.common.block.BlockSimpleTrophy;
import quaternary.simpletrophies.common.etc.EnumTrophyVariant;
import quaternary.simpletrophies.common.etc.TrophyHelpers;
import quaternary.simpletrophies.common.item.ItemSimpleTrophy;

public class RenderItemStackSimpleTrophy extends TileEntityItemStackRenderer {
	static final ModelResourceLocation[] baseModels;
	
	static {
		baseModels = new ModelResourceLocation[EnumTrophyVariant.VALUES.length];
		PropertyEnum<EnumTrophyVariant> propVariant = BlockSimpleTrophy.PROP_VARIANT;
		String variantName = propVariant.getName();
		for(int i = 0; i < EnumTrophyVariant.VALUES.length; i++) {
			EnumTrophyVariant var = EnumTrophyVariant.VALUES[i];
			baseModels[i] = new ModelResourceLocation(new ResourceLocation(SimpleTrophies.MODID, "trophy"), variantName + '=' + propVariant.getName(var));
		}
	}
	
	int recursionDepth = 0;
	
	@Override
	public void renderByItem(ItemStack stack) {
		if(!(stack.getItem() instanceof ItemSimpleTrophy)) return;
		
		//Render the base
		int variantID = TrophyHelpers.getDisplayedVariant(stack).ordinal();
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		ModelManager mm = brd.getBlockModelShapes().getModelManager();
		int color = TrophyHelpers.getCombinedColor(stack);
		float red = ((color & 0xFF0000) >> 16) / 255f;
		float green = ((color & 0x00FF00) >> 8) / 255f;
		float blue = (color & 0x0000FF) / 255f;
		brd.getBlockModelRenderer().renderModelBrightnessColor(mm.getModel(baseModels[variantID]), 1f, red, green, blue);
		
		//Render the item
		ItemStack displayedStack = TrophyHelpers.getDisplayedStack(stack);
		
		if(!displayedStack.isEmpty()) {
			float ticks = ClientGameEvents.getPauseAdjustedTicksAndPartialTicks();
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, .55, .5);
			if(!Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(displayedStack, Minecraft.getMinecraft().world, null).isGui3d()) {
				GlStateManager.translate(0, 0.1, 0);
			}
			
			GlStateManager.rotate(ticks * 2.5f, 0, 1, 0);
			GlStateManager.scale(2, 2, 2);
			
			//Fix flickering leaves issue on old Forges
			//Without this line:
			//RenderItem#renderItem is called on this item
			// -> calls setBlurMipmap(false, false) which saves old blur/mipmap values (A)
			// -> finds and calls this TEISR
			// -> -> I call renderItem#renderItem
			// -> -> -> calls setBlurMipmap(false, false) which destroys old values (!!!) (B)
			// -> -> -> item rendering happens
			// -> -> -> calls restoreLastBlurMipmap() which restores the (false, false) saved in A
			// -> calls restoreLastBlurMipmap() which restores (false, false) from B
			//values saved in A have now been overwritten with (false, false) - stateleak!
			//
			//With this line:
			//RenderItem#renderItem is called on this item
			// -> calls setBlurMipmap(false, false) which saves old blur/mipmap values (A)
			// -> finds and calls this TEISR
			// -> -> I call restoreLastBlurMipmap() which restores old values saved in A
			// -> -> I call renderItem#renderItem
			// -> -> -> calls setBlurMipmap(false, false) which saves old blur/mipmap values (B)
			// -> -> -> item rendering happens
			// -> -> -> calls restoreLastBlurMipmap() which restores the old values saved in A (saved again in B)
			// -> calls restoreLastBlurMipmap() (but the saved values match the real values so nothing happens)
			//values saved in A are now preserved - no stateleak
			//
			//Flickering leaves were fixed in https://github.com/MinecraftForge/MinecraftForge/pull/4997
			//But it still stateleaks technically, you just can't see it on leaves.
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
			
			//Too many nested pushmatrixes can cause severe render glitching on my pc.
			//Nobody's going to actually hand out trophies of trophies of trophies of trophies of trophies of trophies anyways.
			//No, that's not a challenge, stop it.
			//And you can't even see it anyways it's so small.
			recursionDepth++;
			
			if(recursionDepth < 5) {
				try {
					Minecraft.getMinecraft().getRenderItem().renderItem(displayedStack, ItemCameraTransforms.TransformType.GROUND);
				} catch(Exception oof) {
					SimpleTrophies.LOG.error("Problem rendering item on a trophy TEISR", oof);
				}
			}
			
			recursionDepth--;
			
			GlStateManager.enableBlend(); //fix a stateleak
			
			GlStateManager.popMatrix();
		}
	}
}