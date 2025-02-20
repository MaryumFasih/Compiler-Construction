package com.example.demo;

import java.util.*;
class SymbolTable {
    private static final Map<String, String> keys = new HashMap<>();
    private static final char[] ops = {'+', '-', '*', '/', '=', '%'};
    private static final Map<Character, String> delims = new HashMap<>();

    static {
        keys.put("agar", "if");
        keys.put("warna", "else");
        keys.put("jabtak", "while");
        keys.put("baray", "for");
        keys.put("tabdil", "switch");

        keys.put("adad", "integer");
        keys.put("asharia", "float");
        keys.put("harf", "character");
        keys.put("jhanda", "boolean");
        keys.put("lafz", "string");

        keys.put("likho", "print");
        keys.put("lo", "input");
        keys.put("do", "output");
        keys.put("wapis", "return");

        delims.put(';', "semi colon");
        delims.put('(', "open bracket");
        delims.put(')', "close bracket");
        delims.put('{', "open curly bracket");
        delims.put('}', "close curly bracket");
    }

    private static boolean isKey(String word) {
        return keys.containsKey(word);
    }

    private static boolean isOp(char c) {
        for (char op : ops) {
            if (c == op) return true;
        }
        return false;
    }

    private static boolean isDelim(char c) {
        return delims.containsKey(c);
    }

    private static boolean isID(String word) {
        return word.matches("[a-z][a-z0-9]*");
    }

    private static boolean isInt(String word) {
        return word.matches("\\d+");
    }

    private static boolean isFloat(String word) {
        return word.matches("\\d+\\.\\d+");
    }

    private static boolean isStr(String word) {
        return word.startsWith("\"") && word.endsWith("\"");
    }

    public static void tokenize(String code) {
        List<String> toks = new ArrayList<>();
        StringBuilder curTok = new StringBuilder();
        int len = code.length();
        boolean inCmnt = false, inMultiCmnt = false;

        for (int i = 0; i < len; i++) {
            char c = code.charAt(i);

            // Handle single-line comments (~~)
            if (!inCmnt && !inMultiCmnt && i < len - 1 && code.substring(i, i + 2).equals("~~")) {
                inCmnt = true;
                i++;
                continue;
            }
            if (inCmnt && c == '\n') {
                inCmnt = false;
                continue;
            }

            // Handle multi-line comments (-% ... %-)
            if (!inCmnt && !inMultiCmnt && i < len - 1 && code.substring(i, i + 2).equals("-%")) {
                inMultiCmnt = true;
                i++;
                continue;
            }
            if (inMultiCmnt && i < len - 1 && code.substring(i, i + 2).equals("%-")) {
                inMultiCmnt = false;
                i++;
                continue;
            }
            if (inCmnt || inMultiCmnt) continue;

            if (Character.isWhitespace(c)) {
                if (curTok.length() > 0) {
                    toks.add(curTok.toString());
                    curTok.setLength(0);
                }
                continue;
            }

            if (isOp(c) || isDelim(c)) {
                if (curTok.length() > 0) {
                    toks.add(curTok.toString());
                    curTok.setLength(0);
                }
                toks.add(Character.toString(c));
                continue;
            }

            curTok.append(c);
        }

        if (curTok.length() > 0) {
            toks.add(curTok.toString());
        }

        for (String tok : toks) {
            if (isKey(tok)) {
                System.out.println("KEYWORD: " + tok + " (" + keys.get(tok) + ")");
            } else if (isInt(tok)) {
                System.out.println("INTEGER: " + tok);
            } else if (isFloat(tok)) {
                System.out.println("FLOAT: " + tok);
            } else if (isStr(tok)) {
                System.out.println("STRING: " + tok);
            } else if (isID(tok)) {
                System.out.println("IDENTIFIER: " + tok);
            } else if (tok.length() == 1 && isOp(tok.charAt(0))) {
                System.out.println("OPERATOR: " + tok);
            } else if (tok.length() == 1 && isDelim(tok.charAt(0))) {
                System.out.println("DELIMITER: " + tok + " (" + delims.get(tok.charAt(0)) + ")");
            } else {
                System.out.println("UNKNOWN TOKEN: " + tok);
            }
        }
    }

    public static void main(String[] args) {
        String input = "adad x = 5; agar (x > 3) { likho(\"Bara hai!\"); } warna { likho(\"Chota hai!\"); } "
                + "~~ This is a single-line comment\n"
                + "-% This is a multi-line comment %-"
                + "lafz name = \"Blah\"; lo(name); do(\"Salam\");";
        tokenize(input);
    }
}
