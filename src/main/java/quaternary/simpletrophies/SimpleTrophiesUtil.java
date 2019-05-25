package quaternary.simpletrophies;

import javax.annotation.Nonnull;

public class SimpleTrophiesUtil {
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T notNullISwear() {
		return null;
	}
	
	public static <T> T swallowError(ExceptionalSupplier<T> t) {
		try {
			return t.get();
		} catch(Exception e) {
			return null;
		}
	}
	
	public interface ExceptionalSupplier<T> {
		T get() throws Exception;
	}
}
