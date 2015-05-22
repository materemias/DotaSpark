package com.examples.dota2tv.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.examples.dota2tv.R;

public class SettingsActivity extends PreferenceActivity {
//	private ActionBar mActionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		mActionBar = getSupportActionBar();
//
//		mActionBar.setHomeButtonEnabled(true);
//		mActionBar.setDisplayHomeAsUpEnabled(true);

		addPreferencesFromResource(R.xml.preferences);

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
