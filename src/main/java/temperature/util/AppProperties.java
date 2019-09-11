package temperature.util;

import java.util.ArrayList;
import java.util.List;

public final class AppProperties {

	public static final List<String> countriesList;
	static {
		countriesList = new ArrayList<>();
		countriesList.add("CZ");
		countriesList.add("IT");
		countriesList.add("DE");		
	}
	
	public static final int PRECISION = 10; // 1 decimal
	
	public static int taskDelay = 0;
	public static int  taskPeriod = 60;
	
	public static final String LOCALE_CZ = "CZ";
	public static final String LOCALE_EN = "EN";
	
	public static final String LOADER_PKG_NAME = "temperature.fileloader";
	public static final String FILE_LOADER_NAME = "choose";
	
	public static final String TASK_PKG_NAME = "temperature.manage.calculate";
	public static final String TASK_NAME = "avg";
	
	
	private static String sysLocale;
	
	public static void setLocale(String locale) {
		sysLocale = locale;
	}
	
	public static String getLocale() {
		return sysLocale;
	}
	
}
