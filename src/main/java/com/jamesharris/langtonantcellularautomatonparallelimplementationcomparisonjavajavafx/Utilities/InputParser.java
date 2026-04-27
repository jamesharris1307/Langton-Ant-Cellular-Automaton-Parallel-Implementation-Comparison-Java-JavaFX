package com.jamesharris.langtonantcellularautomatonparallelimplementationcomparisonjavajavafx.Utilities;

public class InputParser {
    public static int getInt(String input, int fallback) {
        try { return Integer.parseInt(input); }
        catch (NumberFormatException e) { return fallback; }
    }

    public static long getLong(String input, long fallback) {
        try { return Long.parseLong(input); }
        catch (NumberFormatException e) { return fallback; }
    }
}