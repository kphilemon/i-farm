package com.example.ifarm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String ACTION_SOWING = "sowing";
    public static final String ACTION_FERTILIZERS = "fertilizers";
    public static final String ACTION_PESTICIDES = "pesticides";
    public static final String ACTION_HARVEST = "harvest";
    public static final String ACTION_SALES = "sales";
    public static final List<String> ACTION_LIST = new ArrayList<>(Arrays.asList(ACTION_SOWING, ACTION_FERTILIZERS, ACTION_PESTICIDES, ACTION_HARVEST, ACTION_SALES));

    public static final String UNIT_KG = "kg";
    public static final String UNIT_G = "g";
    public static final List<String> MASS_UNIT_LIST = new ArrayList<>(Arrays.asList(UNIT_KG, UNIT_G));

    public static final String UNIT_PACK_500 = "pack (500g)";
    public static final String UNIT_PACK_1000 = "pack (1000g)";
    public static final List<String> PACK_UNIT_LIST = new ArrayList<>(Arrays.asList(UNIT_PACK_500, UNIT_PACK_1000));

    public static final String UNIT_L = "l";
    public static final String UNIT_ML = "ml";
    public static final List<String> VOLUME_UNIT_LIST = new ArrayList<>(Arrays.asList(UNIT_L, UNIT_ML));

    public static final String UNIT_TYPE_MASS = "mass";
    public static final String UNIT_TYPE_PACK = "pack";
    public static final String UNIT_TYPE_VOLUME = "volume";

    public static final int STATUS_OK = 1;
    public static final int STATUS_INTERNAL_ERROR = 2;
    public static final int STATUS_INVALID_REQUEST = 3;
}
