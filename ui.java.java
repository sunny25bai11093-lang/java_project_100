package com.vit.smartstudent.util;

import java.util.List;

/**
 * CLI utility to format tabular terminal output, borders, and ANSI badges.
 * Author: Sunny Gupta (25BAI11093)
 */
public class ConsoleFormatter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void printHeader(String title) {
        String border = "=".repeat(70);
        System.out.println(border);
        int pad = Math.max(0, (70 - title.length()) / 2);
        System.out.println(" ".repeat(pad) + title);
        System.out.println(border);
    }

    public static void printSubHeader(String sub) {
        System.out.println("-".repeat(70));
        System.out.println("  " + sub);
        System.out.println("-".repeat(70));
    }

    public static void printRow(List<String> columns, List<Integer> widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            int w = (i < widths.size()) ? widths.get(i) : 15;
            String text = columns.get(i);
            if (text.length() > w) {
                text = text.substring(0, w - 3) + "...";
            }
            sb.append(String.format("%-" + w + "s ", text));
        }
        System.out.println(sb.toString());
    }

    public static String colorize(String text, String colorCode) {
        return colorCode + text + ANSI_RESET;
    }

    public static String getStatusBadge(double marks, double attendance) {
        if (attendance < 75.0) {
            return colorize("[DEBARRED - ATTENDANCE]", ANSI_RED);
        }
        if (marks >= 90.0) {
            return colorize("[DISTINCTION]", ANSI_GREEN);
        } else if (marks >= 75.0) {
            return colorize("[FIRST CLASS]", ANSI_CYAN);
        } else if (marks >= 50.0) {
            return colorize("[PASS]", ANSI_YELLOW);
        } else {
            return colorize("[FAIL]", ANSI_RED);
        }
    }
}