package yf.com.gorgecommidemo;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
	public static SharedPreferences getPref(Context con) {
		// return PreferenceManager.getDefaultSharedPreferences(con);
		return con.getSharedPreferences(con.getPackageName(), Context.MODE_MULTI_PROCESS);
	}

	public static boolean getPrefBoolean(Context con, String key) {
		return getPrefBoolean(con, key, false);
	}

	public static boolean getPrefBoolean(Context con, String key, boolean def) {
		return getPref(con).getBoolean(key, def);
	}

	public static int getPrefInt(Context con, String key, int def) {
		return getPref(con).getInt(key, def);
	}

	public static int getPrefInt(Context con, String key) {
		return getPref(con).getInt(key, 0);
	}

	public static String getPrefString(Context con, String key) {
		return getPrefString(con, key, null);
	}

	public static String getPrefString(Context con, String key, String defString) {
		return getPref(con).getString(key, defString);
	}

	public static void setPref(Context con, String key, Object object) {
		SharedPreferences prefs = getPref(con);
		SharedPreferences.Editor editor = prefs.edit();
		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}
		editor.apply();
	}
}
