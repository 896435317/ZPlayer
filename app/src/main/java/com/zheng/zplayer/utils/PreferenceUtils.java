package com.zheng.zplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.zheng.zplayer.application.ZPlayerApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public final class PreferenceUtils {

	/** 清空数据 */
	public static void reset(final Context ctx) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
		edit.clear();
		edit.commit();
	}

	public static String getString(String key, String defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).getString(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).getLong(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).getFloat(key, defValue);
	}

	public static void put(String key, String value) {
		putString(key, value);
	}

	public static void put(String key, int value) {
		putInt(key, value);
	}

	public static void put(String key, float value) {
		putFloat(key, value);
	}

	public static void put(String key, boolean value) {
		putBoolean(key, value);
	}

	public static void putFloat(String key, float value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
	}

	public static int getInt(String key, int defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).getInt(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).getBoolean(key, defValue);
	}

	public static void putStringProcess(String key, String value) {
		SharedPreferences sharedPreferences = ZPlayerApplication.getContext().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStringProcess(String key, String defValue) {
		SharedPreferences sharedPreferences = ZPlayerApplication.getContext().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
		return sharedPreferences.getString(key, defValue);
	}

	public static boolean hasString(String key) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
		return sharedPreferences.contains(key);
	}

	public static void putString(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putLong(String key, long value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void putBoolean(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putInt(String key, int value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void remove(String... keys) {
		if (keys != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext());
			Editor editor = sharedPreferences.edit();
			for (String key : keys) {
				editor.remove(key);
			}
			editor.commit();
		}
	}
	public static void putBean(String key, Object obj) {
		if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(obj);
				String string64 = new String(Base64.encode(baos.toByteArray(),
						0));
				Editor editor = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).edit();
				editor.putString(key, string64).commit();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			throw new IllegalArgumentException(
					"the obj must implement Serializble");
		}

	}

	public static Object getBean(String key) {
		Object obj = null;
		try {
			String base64 = PreferenceManager.getDefaultSharedPreferences(ZPlayerApplication.getContext()).getString(key, "");
			if (base64.equals("")) {
				return null;
			}
			byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
