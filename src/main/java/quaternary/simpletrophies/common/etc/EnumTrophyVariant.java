package quaternary.simpletrophies.common.etc;

import net.minecraft.util.IStringSerializable;

public enum EnumTrophyVariant implements IStringSerializable {
	CLASSIC("classic", "quaternary"),
	X00FF00_GRAY("neon", "0x00FF00"),
	X00FF00_GOLD("gold", "0x00FF00");
	
	public final String blockstateVariant;
	public final String author;
	
	//Do not mutate.
	public static final EnumTrophyVariant[] VALUES = values();
	
	EnumTrophyVariant(String blockstateVariant, String author) {
		this.blockstateVariant = blockstateVariant;
		this.author = author;
	}
	
	@Override
	public String getName() {
		return blockstateVariant;
	}
	
	public static EnumTrophyVariant fromString(String name) {
		if(name == null || name.isEmpty()) return CLASSIC;
		
		for(EnumTrophyVariant var : VALUES) {
			if(var.blockstateVariant.equals(name)) return var;
		}
		
		return CLASSIC;
	}
}
