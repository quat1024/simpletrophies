package quaternary.simpletrophies.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.simpletrophies.common.block.BlockSimpleTrophy;
import quaternary.simpletrophies.common.etc.EnumTrophyVariant;

import javax.annotation.Nullable;

public class TileSimpleTrophy extends TileEntity {
	public ItemStack displayedStack = ItemStack.EMPTY;
	public String displayedName = "";
	public int displayedColorRed = 255;
	public int displayedColorGreen = 255;
	public int displayedColorBlue = 255;
	public EnumTrophyVariant displayedVariant = EnumTrophyVariant.CLASSIC;
	public long earnedTime = 0;
	public boolean showsTooltip = true;
	
	public String getLocalizedName() {
		return I18n.translateToLocal(displayedName);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 6969, getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		readFromNBTInternal(nbt);
		IBlockState hahaYes = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, hahaYes, hahaYes, 3);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		writeToNBTInternal(nbt);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readFromNBTInternal(nbt);
	}
	
	public NBTTagCompound writeToNBTInternal(NBTTagCompound nbt) {
		nbt.setTag(BlockSimpleTrophy.KEY_ITEM, displayedStack.serializeNBT());
		nbt.setString(BlockSimpleTrophy.KEY_NAME, displayedName);
		nbt.setInteger(BlockSimpleTrophy.KEY_COLOR_RED, displayedColorRed);
		nbt.setInteger(BlockSimpleTrophy.KEY_COLOR_GREEN, displayedColorGreen);
		nbt.setInteger(BlockSimpleTrophy.KEY_COLOR_BLUE, displayedColorBlue);
		nbt.setString(BlockSimpleTrophy.KEY_VARIANT, displayedVariant.getName());
		nbt.setLong(BlockSimpleTrophy.KEY_EARNED_AT, earnedTime);
		nbt.setBoolean(BlockSimpleTrophy.KEY_SHOWS_TOOLTIP, showsTooltip);
		
		return nbt;
	}
	
	public void readFromNBTInternal(NBTTagCompound nbt) {
		displayedStack = new ItemStack(nbt.getCompoundTag(BlockSimpleTrophy.KEY_ITEM));
		displayedName = nbt.getString(BlockSimpleTrophy.KEY_NAME);
		displayedColorRed = nbt.getInteger(BlockSimpleTrophy.KEY_COLOR_RED);
		displayedColorGreen = nbt.getInteger(BlockSimpleTrophy.KEY_COLOR_GREEN);
		displayedColorBlue = nbt.getInteger(BlockSimpleTrophy.KEY_COLOR_BLUE);
		displayedVariant = EnumTrophyVariant.fromString(nbt.getString(BlockSimpleTrophy.KEY_VARIANT));
		earnedTime = nbt.getLong(BlockSimpleTrophy.KEY_EARNED_AT);
		showsTooltip = nbt.getBoolean(BlockSimpleTrophy.KEY_SHOWS_TOOLTIP);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos).expand(0, 0.5, 0);
	}
}
