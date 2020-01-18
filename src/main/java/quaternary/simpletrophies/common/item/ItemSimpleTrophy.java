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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.common.block.BlockSimpleTrophy;
import quaternary.simpletrophies.common.config.SimpleTrophiesConfig;
import quaternary.simpletrophies.common.etc.DateHelpers;
import quaternary.simpletrophies.common.etc.EnumTrophyVariant;
import quaternary.simpletrophies.common.etc.TrophyHelpers;
import quaternary.simpletrophies.common.tile.TileSimpleTrophy;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class ItemSimpleTrophy extends ItemBlock {
	public ItemSimpleTrophy(BlockSimpleTrophy block) {
		super(block);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound()); 
		//Add all of the other NBT tags if they don't already exist
		//I am so sorry, I'm really sorry for this code
		NBTTagCompound nbt = stack.getTagCompound();
		assert nbt != null;
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_COLOR_RED)) nbt.setInteger(BlockSimpleTrophy.KEY_COLOR_RED, 255);
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_COLOR_GREEN)) nbt.setInteger(BlockSimpleTrophy.KEY_COLOR_GREEN, 255);
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_COLOR_BLUE)) nbt.setInteger(BlockSimpleTrophy.KEY_COLOR_BLUE, 255);
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_ITEM)) nbt.setTag(BlockSimpleTrophy.KEY_ITEM, ItemStack.EMPTY.serializeNBT());
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_NAME)) nbt.setString(BlockSimpleTrophy.KEY_NAME, "");
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_VARIANT)) nbt.setString(BlockSimpleTrophy.KEY_VARIANT, "classic");
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_EARNED_AT)) nbt.setLong(BlockSimpleTrophy.KEY_EARNED_AT, DateHelpers.now());
		if(!nbt.hasKey(BlockSimpleTrophy.KEY_SHOWS_TOOLTIP)) nbt.setBoolean(BlockSimpleTrophy.KEY_SHOWS_TOOLTIP, true);
		
		if(entity instanceof EntityPlayer && ((EntityPlayer)entity).isCreative()) {
			//Move vanilla customname stuff (italic) over to my own system
			//This just lets people rename the trophy in an anvil instead of needing to manually NBT hack
			//and have it not show up all... italicy and weird
			if(stack.hasDisplayName()) {
				String customName = stack.getDisplayName();
				nbt.setString(BlockSimpleTrophy.KEY_NAME, customName.equals("<CLEAR>") ? "" : customName);
				stack.clearCustomName();
				
				//remove the funky anvil tag too
				if(nbt.hasKey("RepairCost")) nbt.removeTag("RepairCost");
			}
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String trophyName = TrophyHelpers.getDisplayedName(stack);
		if(trophyName.isEmpty()) return super.getItemStackDisplayName(stack);
		else return I18n.translateToLocal(trophyName);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		ItemStack displayedStack = TrophyHelpers.getDisplayedStack(stack);
		if(!displayedStack.isEmpty()) {
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

			if(!SimpleTrophiesConfig.NO_TOOLTIP && TrophyHelpers.showsTooltip(stack)) {
				//add the item itself's tooltip. Why not?
				//(In 2020, I found some reasons why not)
				try {
					List<String> displayedTooltip = new ArrayList<>();
					displayedStack.getItem().addInformation(displayedStack, world, displayedTooltip, mistake);
					displayedTooltip.forEach(s -> tooltip.add("   " + s));
				} catch (Exception fartNoise) {
					//Some items are kinda wack with their tooltips and crash. Not much I can do.
					//See: https://github.com/quat1024/simpletrophies/issues/6
				}
			}
		}
		
		EnumTrophyVariant trophyVariant = TrophyHelpers.getDisplayedVariant(stack);
		if(mistake.isAdvanced()) {
			tooltip.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("simple_trophies.misc.modelName", trophyVariant.blockstateVariant));
		}
		
		long time = TrophyHelpers.getEarnTime(stack);
		if(SimpleTrophiesConfig.SHOW_EARNEDAT && time != 0) {
			tooltip.add(I18n.translateToLocalFormatted("simple_trophies.misc.earnedAt", DateHelpers.epochToString(time)));
		}
		
		if(SimpleTrophiesConfig.TOOLTIP_CREDITS) {
			tooltip.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("simple_trophies.misc.modelBy", trophyVariant.author));
		}
		
		super.addInformation(stack, world, tooltip, mistake);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		ItemStack displayedItem = TrophyHelpers.getDisplayedStack(stack);
		return displayedItem.isEmpty() ? EnumRarity.COMMON : displayedItem.getRarity();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState, 11)) return false;
		
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.block) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileSimpleTrophy) {
				TrophyHelpers.populateTileNBTFromStack(stack, (TileSimpleTrophy) tile);
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
		if(player.isCreative()) {
			ItemStack held = player.getHeldItem(hand);
			if(held.hasTagCompound() && world.isRemote) {
				NBTTagCompound cmp = held.getTagCompound().copy();
				//Remove earned time since authors aren't likely to want that
				cmp.removeTag(BlockSimpleTrophy.KEY_EARNED_AT);
				String str = cmp.toString();
				
				LogManager.getLogger(SimpleTrophies.NAME).info(str);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(str), null);
				player.sendStatusMessage(new TextComponentTranslation("simple_trophies.misc.copied"), true);
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, held);
		}
		
		return super.onItemRightClick(world, player, hand);
	}
}
