/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

/**
 * @author ncrypted
 */
public class Utils {

    public static String toUserId(int id) {
        return String.format("%06d", id);
    }

    public static int parseId(String input) {
        int id = 0;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException exc) {
        }
        if (id <= 0 || String.valueOf(id).length() > 6) {
            return -1;
        }
        return id;
    }

    public static long parseMoney(String input) {
        long money = 0;
        try {
            money = Long.parseLong(input);
        } catch (NumberFormatException exc) {
        }
        if (money <= 0) {
            return -1;
        }
        return money;
    }
}
