package quaternary.simpletrophies.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.simpletrophies.common.item.ItemSimpleTrophy;
import quaternary.simpletrophies.common.item.SimpleTrophiesItems;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

import javax.annotation.Nullable;

public class BlockSimpleTrophy extends Block {
	//basically a bunch of stuff uses these keys so might as well slap them here
	public static final String KEY_NAME = "TrophyName";
	public static final String KEY_ITEM = "TrophyItem";
	
	public BlockSimpleTrophy() {
		super(Material.ROCK, MapColor.GOLD);
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
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileSimpleTrophy) {
			spawnAsEntity(world, pos, getItemStackFromTile((TileSimpleTrophy) tile));
		}
		
		super.breakBlock(world, pos, state);
	}
	
	public static ItemStack getItemStackFromTile(@Nullable TileSimpleTrophy tile) {
		ItemStack stack = new ItemStack(SimpleTrophiesItems.TROPHY);
		if(tile != null) ItemSimpleTrophy.populateStackNBTFromTile(stack, tile);
		return stack;
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
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileSimpleTrophy) {
			return getItemStackFromTile((TileSimpleTrophy) tile);
		} else return getItemStackFromTile(null);
	}
}
