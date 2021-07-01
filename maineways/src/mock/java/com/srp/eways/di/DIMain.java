package com.srp.eways.di;

import com.srp.eways.repository.local.ICache;
import com.srp.eways.repository.local.PreferenceCache;

/**
 * Created by ErfanG on 3/2/2020.
 */
public class DIMain extends DIMainCommon {

    public static ICache getPreferenceCache(){
        return PreferenceCache.getInstance();
    }
}
