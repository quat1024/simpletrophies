package quaternary.simpletrophies.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.simpletrophies.common.etc.TrophyHelpers;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

import javax.annotation.Nullable;

public class BlockSimpleTrophy extends Block {
	//basically a bunch of stuff uses these keys so might as well slap them here
	public static final String KEY_NAME = "TrophyName";
	public static final String KEY_ITEM = "TrophyItem";
	public static final String KEY_COLOR_RED = "TrophyColorRed";
	public static final String KEY_COLOR_GREEN = "TrophyColorGreen";
	public static final String KEY_COLOR_BLUE = "TrophyColorBlue";
	
	public BlockSimpleTrophy() {
		super(Material.ROCK, MapColor.GOLD);
		setHardness(2f);
		setResistance(1f);
	}
	
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 6/16d, 1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSimpleTrophy();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!player.isCreative()) return false;
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileSimpleTrophy) {
			TileSimpleTrophy trophy = (TileSimpleTrophy) tile;			
			int averageColor = getAverageDyeColorHeldByPlayer(player);
			if(averageColor == -1) {
				trophy.displayedStack = player.getHeldItem(hand).copy();
			} else {
				trophy.displayedColorRed = (averageColor & 0xFF0000) >> 16;
				trophy.displayedColorGreen = (averageColor & 0x00FF00) >> 8;
				trophy.displayedColorBlue = averageColor & 0x0000FF;
			}
			
			IBlockState hahaYes = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, hahaYes, hahaYes, 2);
			trophy.markDirty();
			return true;
		}
		
		return false;
	}
	
	private static int getAverageDyeColorHeldByPlayer(EntityPlayer player) {
		int color = -1;
		ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
		if(main.getItem() instanceof ItemDye && MathHelper.clamp(main.getMetadata(), 0, 15) == main.getMetadata()) {
			color = ItemDye.DYE_COLORS[main.getMetadata()];
		}
		
		ItemStack off = player.getHeldItem(EnumHand.OFF_HAND);
		if(off.getItem() instanceof ItemDye && MathHelper.clamp(off.getMetadata(), 0, 15) == off.getMetadata()) {
			int color2 = ItemDye.DYE_COLORS[off.getMetadata()];
			int red = (((color & 0xFF0000) >> 16) + ((color2 & 0xFF0000) >> 16)) / 2;
			int green = (((color & 0x00FF00) >> 8) + ((color2 & 0x00FF00) >> 8)) / 2;
			int blue = ((color & 0x0000FF) + (color2 & 0x0000FF)) / 2;
			color = (red << 16) | (green << 8) | blue;
		}
		
		return color;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileSimpleTrophy) {
			spawnAsEntity(world, pos, TrophyHelpers.createItemStackFromTile((TileSimpleTrophy) tile));
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}
	
	@Override
	public int getLightOpacity(IBlockState state) {
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		TileEntity tile = world.getTileEntity(pos);
		return TrophyHelpers.createItemStackFromTile(tile instanceof TileSimpleTrophy ? ((TileSimpleTrophy)tile) : null);
	}
}
