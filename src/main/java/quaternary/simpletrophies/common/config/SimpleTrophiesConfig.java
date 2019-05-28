package quaternary.simpletrophies.common.config;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.simpletrophies.SimpleTrophies;
import quaternary.simpletrophies.SimpleTrophiesUtil;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class SimpleTrophiesConfig {
	public static Configuration config;
	public static final int CONFIG_VERSION = 1;
	
	public static boolean TOOLTIP_CREDITS;
	public static boolean SHOW_EARNEDAT;
	
	public static List<NBTTagCompound> CREATIVETAB_TAGS;
	
	public static boolean SKIP_ITEM_BASES;
	public static boolean SKIP_ITEM_ITEMS;
	public static boolean SKIP_BLOCK_ITEMS;
	public static boolean NO_TEISR;
	public static boolean NO_TESR;
	
	public static String DEFAULT_CREATIVETAB_STR = "{TrophyName:\"Add your own trophies here in the config!\",TrophyVariant:\"classic\",TrophyItem:{id:\"minecraft:diamond_axe\",Count:1b,Damage:0s},TrophyColorRed:65,TrophyColorGreen:205,TrophyColorBlue:52}";
	public static NBTTagCompound DEFAULT_CREATIVETAB_TAG = SimpleTrophiesUtil.swallowError(() -> JsonToNBT.getTagFromJson(DEFAULT_CREATIVETAB_STR));
	
	public static void preinit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile(), String.valueOf(CONFIG_VERSION));
		
		load();
	}
	
	private static void load() {
		TOOLTIP_CREDITS = config.getBoolean("tooltipCredits", "client", false, "Display the author of trophy models on their tooltips.");
		
		SHOW_EARNEDAT = config.getBoolean("showEarnedAt", "client", true, "Show the date and time you earned the trophy on the tooltip and on hover.");
		
		String[] tagStrings = config.getStringList("creativeTabTrophies", "client", new String[] { DEFAULT_CREATIVETAB_STR }, "Trophy tags that will be displayed on the Simple Trophies creative tab. Obtain them by right clicking a trophy in the air in creative. One per line, please.\n\n");
		
		CREATIVETAB_TAGS = new ArrayList<>();
		for(String s : tagStrings) {
			try {
				CREATIVETAB_TAGS.add(JsonToNBT.getTagFromJson(s));
			} catch(NBTException e) {
				SimpleTrophies.LOG.error("Can't parse this NBT tag: " + s, e);
			}
		}
		
		SKIP_BLOCK_ITEMS = config.getBoolean("skipBlockItems", "client.perf", false, "Don't show the items on top of trophies placed in the world. Saves on performance.");
		
		SKIP_ITEM_ITEMS = config.getBoolean("skipItemItems", "client.perf", false, "Don't show the items on top of trophies in your inventory and on other GUIs. Saves on performance.");
		
		SKIP_ITEM_BASES = config.getBoolean("skipItemBases", "client.perf", false, "Don't show trophy bases on trophies in your inventory and on other GUIs. Saves on performance.");
		
		NO_TESR = config.getBoolean("noTileEntitySpecialRenderer", "client.perf", false, "Emergency killswitch for the tile entity renderer. Enable in cases of extreme performance issues or client rendering-related crashes.\n(Requires a game restart in some cases.)");
		
		NO_TEISR = config.getBoolean("noTileEntityItemStackRenderer", "client.perf", false, "Emergency killswitch for the in-inventory trophy renderer. Enable in cases of extreme performance issues or client rendering-related crashes.\n(Requires a game restart in some cases.)\nIf this option is enabled, and skipItemBases is not, trophy item bases will render using a 'fast path' that is about as expensive as rendering a grass block item. This fast path is not compatible with the fancy trophy TEISR, to my knowledge.");
		
		if(config.hasChanged()) config.save();
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(SimpleTrophies.MODID)) {
			load();
		}
	}
}
