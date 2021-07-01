package com.srp.eways.util;

public class ShadowUtil {

    private static final int SHADOW_COLOR = 0x14000000; // 8% black opacity

    public static int getShadowInfo(float elevation, float[] shadowInfo) {

        shadowInfo[0] = 0;
        shadowInfo[1] = elevation / 4;
        shadowInfo[2] = elevation / 2;

        shadowInfo[3] = 0;
        shadowInfo[4] = elevation / 4;
        shadowInfo[5] = elevation / 1;

        return SHADOW_COLOR;
    }

}
