package pierobon.paiva.lucilio.bluefood.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
	
	private static Locale ptBR = new Locale("pt", "BR");
	
	public static String formatPtBr(final Number target) {
		if (target == null) {
			return null;
		}
		
		String result;
		
		NumberFormat format = NumberFormat.getCurrencyInstance(ptBR);

		result = format.format(target);
		
		return result;
	}

}
