package com.srp.eways.ui.navigation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Eskafi on 8/13/2019.
 */
public class BackStackMember {

    public static BackStackMember create(Fragment fragment) {
        return new BackStackMember(fragment);
    }

    public static BackStackMember create(Fragment fragment, Bundle args) {
        return new BackStackMember(fragment, args);
    }

    private final Fragment fragment;

    private Bundle bundle;

    public BackStackMember(Fragment fragment, Bundle bundle) {
        this.fragment = fragment;
        this.bundle = bundle;
    }

    public BackStackMember(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void putBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof BackStackMember)) {
            return false;
        }

        BackStackMember member = (BackStackMember) obj;

        if (!member.fragment.getClass().equals(fragment.getClass())) {
            return false;
        }

        if (bundle == null) {
            return member.bundle == null;
        } else {
            return member.bundle != null;
        }
    }

}
