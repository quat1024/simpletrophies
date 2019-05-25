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
		
		if(config.hasChanged()) config.save();
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(SimpleTrophies.MODID)) {
			load();
		}
	}
}
