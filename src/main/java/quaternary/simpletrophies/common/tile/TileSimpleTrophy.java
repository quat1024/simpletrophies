package quaternary.simpletrophies.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import quaternary.simpletrophies.common.block.BlockSimpleTrophy;

import javax.annotation.Nullable;

public class TileSimpleTrophy extends TileEntity {
	public ItemStack displayedStack = ItemStack.EMPTY;
	public String displayedName = "";
	public int displayedColor = 0xFFFFFF;
	
	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		if(displayedName.isEmpty()) return null;
		else return new TextComponentString(displayedName);
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
		nbt.setInteger(BlockSimpleTrophy.KEY_COLOR, displayedColor);
		return nbt;
	}
	
	public void readFromNBTInternal(NBTTagCompound nbt) {
		displayedStack = new ItemStack(nbt.getCompoundTag(BlockSimpleTrophy.KEY_ITEM));
		displayedName = nbt.getString(BlockSimpleTrophy.KEY_NAME);
		displayedColor = nbt.getInteger(BlockSimpleTrophy.KEY_COLOR);
	}
}
