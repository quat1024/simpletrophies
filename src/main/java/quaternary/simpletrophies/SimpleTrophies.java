package quaternary.simpletrophies;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.simpletrophies.common.config.SimpleTrophiesConfig;
import quaternary.simpletrophies.common.item.SimpleTrophiesItems;

@Mod(
	modid = SimpleTrophies.MODID,
	name = SimpleTrophies.NAME,
	version = SimpleTrophies.VERSION,
	guiFactory = "quaternary.simpletrophies.common.config.Xd"
)
public class SimpleTrophies {
	public static final String MODID = "simple_trophies";
	public static final String NAME = "Simple Trophies";
	public static final String VERSION = "GRADLE:VERSION";
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack createIcon() {
			return trophyFor(SimpleTrophiesConfig.DEFAULT_CREATIVETAB_TAG);
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			SimpleTrophiesConfig.CREATIVETAB_TAGS.forEach(tag -> list.add(trophyFor(tag)));
		}
		
		private ItemStack trophyFor(NBTTagCompound tag) {
			ItemStack stack = new ItemStack(SimpleTrophiesItems.TROPHY);
			stack.setTagCompound(tag.copy());
			return stack;
		}
	};
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		SimpleTrophiesConfig.preinit(e);
	}
}
