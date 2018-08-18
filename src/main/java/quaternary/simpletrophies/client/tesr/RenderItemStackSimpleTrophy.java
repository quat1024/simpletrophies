package quaternary.simpletrophies.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import quaternary.simpletrophies.common.item.ItemSimpleTrophy;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

public class RenderItemStackSimpleTrophy extends TileEntityItemStackRenderer {
	public RenderItemStackSimpleTrophy(RenderTileSimpleTrophy render) {
		this.render = render;
		secretTile = new TileSimpleTrophy();
	}
	
	private final RenderTileSimpleTrophy render;
	private TileSimpleTrophy secretTile; //Shhhh!!!!
	
	@Override
	public void renderByItem(ItemStack stack) {
		ItemSimpleTrophy.populateTileNBTFromStack(stack, secretTile);
		render.render(secretTile, 0, 0, 0, Minecraft.getMinecraft().getRenderPartialTicks(), 0, 0);
	}
}
