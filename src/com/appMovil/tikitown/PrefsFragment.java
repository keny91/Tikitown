package com.appMovil.tikitown;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	}
}

