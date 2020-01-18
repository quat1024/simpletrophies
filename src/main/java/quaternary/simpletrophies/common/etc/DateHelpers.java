package quaternary.simpletrophies.common.etc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

//In the "date and time" sense. This class won't help you get laid. Sorry.
public class DateHelpers {
	public static long now() {
		return Instant.now().atZone(ZoneId.systemDefault()).toEpochSecond();
	}
	
	private static final DateTimeFormatter doot = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US).withZone(ZoneId.systemDefault());
	
	public static String epochToString(long time) {
		if(time == 0) return "?";
		else {
			return doot.format(Instant.ofEpochSecond(time));
		}
	}
}
