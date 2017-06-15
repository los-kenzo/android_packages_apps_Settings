/*
 * Copyright (C) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.loskenzo;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.preference.DropDownPreference;
import android.support.v7.preference.Preference;

import cyanogenmod.preference.CMSecureSettingSwitchPreference;
import cyanogenmod.providers.CMSettings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;


public class NetworkTrafficSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener  {

    private static final String TAG = "NetworkTrafficSettings";

    private DropDownPreference mNetTrafficMode;
    private CMSecureSettingSwitchPreference mNetTrafficAutohide;
    private CMSecureSettingSwitchPreference mNetTrafficHideArrow;
    private NetworkTrafficThresholdSeekBarPreference mNetTrafficAutohideThreshold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.network_traffic_settings);
        final ContentResolver resolver = getActivity().getContentResolver();

        mNetTrafficMode = (DropDownPreference)
                findPreference(CMSettings.Secure.NETWORK_TRAFFIC_MODE);
        mNetTrafficMode.setOnPreferenceChangeListener(this);
        int mode = CMSettings.Secure.getInt(resolver,
                CMSettings.Secure.NETWORK_TRAFFIC_MODE, 0);
        mNetTrafficMode.setValue(String.valueOf(mode));

        mNetTrafficAutohide = (CMSecureSettingSwitchPreference)
                findPreference(CMSettings.Secure.NETWORK_TRAFFIC_AUTOHIDE);
	mNetTrafficHideArrow = (CMSecureSettingSwitchPreference)
                findPreference(CMSettings.Secure.NETWORK_TRAFFIC_HIDEARROW);
	mNetTrafficAutohide.setOnPreferenceChangeListener(this);        
	mNetTrafficHideArrow.setOnPreferenceChangeListener(this);

        mNetTrafficAutohideThreshold = (NetworkTrafficThresholdSeekBarPreference)
                findPreference(CMSettings.Secure.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD);
        int netTrafficAutohideThreshold = CMSettings.Secure.getInt(resolver,
                CMSettings.Secure.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD, 10);
        mNetTrafficAutohideThreshold.setThreshold(netTrafficAutohideThreshold);
        mNetTrafficAutohideThreshold.setOnPreferenceChangeListener(this);

        updateEnabledStates(null, 1, null);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DEVICEINFO;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNetTrafficMode) {
            int intState = Integer.valueOf((String) newValue);
            CMSettings.Secure.putInt(getActivity().getContentResolver(),
                    CMSettings.Secure.NETWORK_TRAFFIC_MODE, intState);
            updateEnabledStates(intState, 1, null);
            return true;
        } else if (preference == mNetTrafficAutohide) {
            updateEnabledStates(null, 1, (Boolean) newValue);
            return true;
        } else if (preference == mNetTrafficHideArrow) {
            updateEnabledStates(null, 2, (Boolean) newValue);
            return true;
        }else if (preference == mNetTrafficAutohideThreshold) {
            int threshold = (Integer) newValue;
            CMSettings.Secure.putInt(getActivity().getContentResolver(),
                    CMSettings.Secure.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD, threshold);
            return true;
        }
        return false;
    }

    private void updateEnabledStates(Integer mode, int z, Boolean autoHide) {
        boolean disabled = mode == null ? "0".equals(mNetTrafficMode.getValue()) : mode == 0;
        boolean autoHideEnabled = autoHide == null ? mNetTrafficAutohide.isChecked() : autoHide;

        mNetTrafficAutohide.setEnabled(!disabled);
	mNetTrafficHideArrow.setEnabled(!disabled);
	if(z!=2)
        mNetTrafficAutohideThreshold.setEnabled(!disabled && autoHideEnabled);
    }

}
