package quaternary.simpletrophies.common.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import quaternary.simpletrophies.SimpleTrophies;

import java.util.Set;
import java.util.stream.Collectors;

public class Xd implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {
		
	}
	
	@Override
	public boolean hasConfigGui() {
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiConfig(parentScreen, SimpleTrophiesConfig.config.getCategoryNames().stream().filter(name -> !SimpleTrophiesConfig.config.getCategory(name).isChild()).map(name -> new ConfigElement(SimpleTrophiesConfig.config.getCategory(name).setLanguageKey(SimpleTrophies.MODID + ".config." + name))).collect(Collectors.toList()), SimpleTrophies.MODID, false, false, SimpleTrophies.NAME);
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
}
