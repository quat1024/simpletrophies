package quaternary.simpletrophies.common.etc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import quaternary.simpletrophies.common.block.BlockSimpleTrophy;
import quaternary.simpletrophies.common.item.SimpleTrophiesItems;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

import javax.annotation.Nullable;

public class TrophyHelpers {
	public static int getCombinedColor(ItemStack stack) {
		if(stack.hasTagCompound()) return getCombinedColor(stack.getTagCompound());
		else return 0xFFFFFF;
	}
	
	public static int getCombinedColor(TileSimpleTrophy tile) {
		return (tile.displayedColorRed << 16) | (tile.displayedColorGreen << 8) | tile.displayedColorBlue;
	}
	
	public static int getCombinedColor(NBTTagCompound nbt) {
		int red = nbt.hasKey(BlockSimpleTrophy.KEY_COLOR_RED) ? nbt.getInteger(BlockSimpleTrophy.KEY_COLOR_RED) : 255;
		int green = nbt.hasKey(BlockSimpleTrophy.KEY_COLOR_GREEN) ? nbt.getInteger(BlockSimpleTrophy.KEY_COLOR_GREEN) : 255;
		int blue = nbt.hasKey(BlockSimpleTrophy.KEY_COLOR_BLUE) ? nbt.getInteger(BlockSimpleTrophy.KEY_COLOR_BLUE) : 255;
		red = MathHelper.clamp(red, 0, 255);
		green = MathHelper.clamp(green, 0, 255);
		blue = MathHelper.clamp(blue, 0, 255);
		return (red << 16) | (green << 8) | blue;
	}
	
	public static ItemStack getDisplayedStack(ItemStack stack) {
		if(stack.hasTagCompound()) return getDisplayedStack(stack.getTagCompound());
		else return ItemStack.EMPTY;
	}
	
	public static ItemStack getDisplayedStack(NBTTagCompound nbt) {
		if(nbt.hasKey(BlockSimpleTrophy.KEY_ITEM)) return new ItemStack(nbt.getCompoundTag(BlockSimpleTrophy.KEY_ITEM));
		else return ItemStack.EMPTY;
	}
	
	public static String getDisplayedName(ItemStack stack) {
		if(stack.hasTagCompound()) return getDisplayedName(stack.getTagCompound());
		else return "";
	}
	
	public static String getDisplayedName(NBTTagCompound nbt) {
		if(nbt.hasKey(BlockSimpleTrophy.KEY_NAME)) return nbt.getString(BlockSimpleTrophy.KEY_NAME);
		else return "";
	}
	
	public static EnumTrophyVariant getDisplayedVariant(ItemStack stack) {
		if(stack.hasTagCompound()) return getDisplayedVariant(stack.getTagCompound());
		else return EnumTrophyVariant.CLASSIC;
	}
	
	public static EnumTrophyVariant getDisplayedVariant(NBTTagCompound nbt) {
		if(nbt.hasKey(BlockSimpleTrophy.KEY_VARIANT)) return EnumTrophyVariant.fromString(nbt.getString(BlockSimpleTrophy.KEY_VARIANT));
		else return EnumTrophyVariant.CLASSIC;
	}
	
	public static long getEarnTime(ItemStack stack) {
		if(stack.hasTagCompound()) return stack.getTagCompound().getLong(BlockSimpleTrophy.KEY_EARNED_AT);
		else return 0;
	}
	
	public static void populateStackNBTFromTile(ItemStack stack, TileSimpleTrophy tile) {
		if(tile.displayedStack.isEmpty() && tile.displayedName.isEmpty()) return;
		
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = stack.getTagCompound();
		assert nbt != null;
		
		nbt.merge(tile.writeToNBTInternal(new NBTTagCompound()));
	}
	
	public static void populateTileNBTFromStack(ItemStack stack, TileSimpleTrophy tile) {
		if(!stack.hasTagCompound()) {
			tile.displayedName = "";
			tile.displayedStack = ItemStack.EMPTY;
			tile.displayedColorRed = 255;
			tile.displayedColorGreen = 255;
			tile.displayedColorBlue = 255;
			tile.displayedVariant = EnumTrophyVariant.CLASSIC;
			tile.earnedTime = 0;
		} else tile.readFromNBTInternal(stack.getTagCompound());
	}
	
	public static ItemStack createItemStackFromTile(@Nullable TileSimpleTrophy tile) {
		ItemStack stack = new ItemStack(SimpleTrophiesItems.TROPHY);
		if(tile != null) populateStackNBTFromTile(stack, tile);
		return stack;
	}
}
