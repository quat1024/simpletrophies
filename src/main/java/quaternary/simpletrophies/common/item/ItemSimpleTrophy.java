package quaternary.simpletrophies.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.simpletrophies.common.block.BlockSimpleTrophy;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSimpleTrophy extends ItemBlock {
	public ItemSimpleTrophy(BlockSimpleTrophy block) {
		super(block);
	}
	
	public static ItemStack getDisplayedItem(ItemStack trophyStack) {
		if(trophyStack.hasTagCompound() && trophyStack.getTagCompound().hasKey(BlockSimpleTrophy.KEY_ITEM)) {
			return new ItemStack(trophyStack.getTagCompound().getCompoundTag(BlockSimpleTrophy.KEY_ITEM));
		} else return ItemStack.EMPTY;
	}
	
	public static String getName(ItemStack trophyStack) {
		if(trophyStack.hasTagCompound() && trophyStack.getTagCompound().hasKey(BlockSimpleTrophy.KEY_NAME)) {
			return trophyStack.getTagCompound().getString(BlockSimpleTrophy.KEY_NAME);
		} else return "";
	}
	
	public static int getColor(ItemStack trophyStack) {
		if(trophyStack.hasTagCompound() && trophyStack.getTagCompound().hasKey(BlockSimpleTrophy.KEY_COLOR)) {
			return trophyStack.getTagCompound().getInteger(BlockSimpleTrophy.KEY_COLOR);
		} else return 0xFFFFFF;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		//Move vanilla customname stuff (italic) over to my own system
		//This just lets people rename the trophy in an anvil instead of needing to manually NBT hack
		//and have it not show up all... italicy and weird
		if(entity instanceof EntityPlayer && ((EntityPlayer)entity).isCreative()) {
			if(stack.hasDisplayName()) {
				String customName = stack.getDisplayName();
				if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setString(BlockSimpleTrophy.KEY_NAME, customName);
				stack.clearCustomName();
				
				//remove the funky anvil tag too
				if(stack.getTagCompound().hasKey("RepairCost")) stack.getTagCompound().removeTag("RepairCost");
			}
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String trophyName = getName(stack);
		if(trophyName.isEmpty()) return super.getItemStackDisplayName(stack);
		else return trophyName;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		ItemStack displayedStack = getDisplayedItem(stack);
		if(displayedStack.isEmpty()) return;
		else {
			//add the "Displayed" tooltip
			tooltip.add(I18n.translateToLocalFormatted("simple_trophies.misc.tooltip.displaying", displayedStack.getRarity().color + displayedStack.getDisplayName()));
			
			//add additional debugging information
			if(mistake.isAdvanced()) {
				StringBuilder bob = new StringBuilder();
				bob.append("   ");
				bob.append(TextFormatting.DARK_GRAY);
				bob.append(displayedStack.getItem().getRegistryName());
				bob.append(" (#");
				bob.append(Item.getIdFromItem(displayedStack.getItem()));
				bob.append('/');
				bob.append(displayedStack.getItemDamage());
				bob.append(')');
				tooltip.add(bob.toString());
			}
			
			//add the item itself's tooltip. Why not?
			List<String> displayedTooltip = new ArrayList<>();
			displayedStack.getItem().addInformation(displayedStack, world, displayedTooltip, mistake);
			displayedTooltip.forEach(s -> tooltip.add("   " + s));
		}
		
		super.addInformation(stack, world, tooltip, mistake);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		ItemStack displayedItem = getDisplayedItem(stack);
		return displayedItem.isEmpty() ? EnumRarity.COMMON : displayedItem.getRarity();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState, 11)) return false;
		
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.block) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileSimpleTrophy) {
				populateTileNBTFromStack(stack, (TileSimpleTrophy) tile);
			}
			
			this.block.onBlockPlacedBy(world, pos, state, player, stack);
			
			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
			}
		}
		
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		//TODO nuke this LMAO
		ItemStack hahaYes = player.getHeldItem(hand);
		if(hahaYes.hasTagCompound()) {
			System.out.println(hahaYes.getTagCompound());
		}
		return super.onItemRightClick(world, player, hand);
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
			return;
		}
		
		tile.readFromNBTInternal(stack.getTagCompound());
		
		//allow for renaming it in an anvil before placing it I guess?
		if(stack.hasDisplayName()) tile.displayedName = stack.getDisplayName();
	}
}
