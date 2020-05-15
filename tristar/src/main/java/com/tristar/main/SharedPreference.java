package com.tristar.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressWarnings("ALL")
public class SharedPreference {

	public static final String PREFS_NAME = "AOP_PREFS";
	public static final String PREFS_KEY = "AOP_PREFS_String";

	public SharedPreference() {
		super();
	}

	@SuppressLint("CommitPrefEdits")
	public void save(Context contextt, String text) {
		SharedPreferences settings;
		Editor editor;

		settings = contextt.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE); // 1
		editor = settings.edit();

		editor.putString(PREFS_KEY, text);

		editor.commit();
	}

	public String getValue(Context contextt) {
		SharedPreferences settings;
		String text;

		settings = contextt.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		text = settings.getString(PREFS_KEY, null);
		return text;
	}

	public String getValuecompany(Context contextt) {
		SharedPreferences settings;
		String text;

		settings = contextt.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		text = settings.getString(PREFS_KEY, null);
		return text;
	}

	public String getValuepassword(Context contextt) {
		SharedPreferences settings;
		String text;

		settings = contextt.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		text = settings.getString(PREFS_KEY, null);
		return text;
	}

	@SuppressLint("CommitPrefEdits")
	public void clearSharedPreference(Context contextt) {
		SharedPreferences settings;
		Editor editor;

		settings = contextt.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = settings.edit();

		editor.clear();
		editor.commit();
	}

	@SuppressLint("CommitPrefEdits")
	public void removeValue(Context contextt) {
		SharedPreferences settings;
		Editor editor;

		settings = contextt.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = settings.edit();

		editor.remove(PREFS_KEY);
		editor.commit();
	}
}
