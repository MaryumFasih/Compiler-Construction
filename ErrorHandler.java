package com.example.demo;

import java.util.*;
public class ErrorHandler {
    private String[] errs;
    private int errCnt;

    public ErrorHandler() {
        errs = new String[100]; // Limit errors to 100
        errCnt = 0;
    }

    public void logError(int line, String msg) {
        if (errCnt < 100) {
            errs[errCnt++] = "Error on line " + line + ": " + msg;
        }
    }

    public boolean hasErrors() {
        return errCnt > 0;
    }

    public void printErrors() {
        if (errCnt == 0) {
            System.out.println("No errors found.");
        } else {
            System.out.println("Compilation Errors:");
            for (int i = 0; i < errCnt; i++) {
                System.out.println(errs[i]);
            }
        }
    }

    public void checkBrackets(String code) {
        char[] stk = new char[100];
        int top = -1, line = 1;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '\n') line++;
            else if (c == '(' || c == '{') {
                if (top < 99) stk[++top] = c;
            } else if (c == ')' || c == '}') {
                if (top == -1) logError(line, "Unmatched closing bracket: " + c);
                else {
                    char last = stk[top--];
                    if ((last == '(' && c != ')') || (last == '{' && c != '}')) {
                        logError(line, "Mismatched brackets: " + last + " and " + c);
                    }
                }
            }
        }

        while (top != -1) logError(line, "Unmatched opening bracket: " + stk[top--]);
    }

    public void checkIdentifiers(String line, int lineNum) {
        String[] words = split(line, ' ');
        for (int i = 0; i < words.length; i++) {
            if (!isValidID(words[i])) {
                logError(lineNum, "Invalid identifier: " + words[i]);
            }
        }
    }

    private boolean isValidID(String word) {
        if (word.length() == 0) return false;
        char first = word.charAt(0);
        if (first < 'a' || first > 'z') return false;

        for (int i = 1; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))) {
                return false;
            }
        }
        return true;
    }

    public void checkSemicolons(String line, int lineNum) {
        if (!line.endsWith(";") && !line.startsWith("agar") && !line.startsWith("jabtak")) {
            logError(lineNum, "Missing semicolon.");
        }
    }

    public void analyze(String code) {
        String[] lines = split(code, '\n');
        for (int i = 0; i < lines.length; i++) {
            String ln = trim(lines[i]);
            if (ln.length() == 0 || startsWith(ln, "~~") || startsWith(ln, "-%")) continue;
            checkIdentifiers(ln, i + 1);
            checkSemicolons(ln, i + 1);
        }
        checkBrackets(code);
    }

    private String[] split(String str, char delim) {
        int cnt = 1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == delim) cnt++;
        }

        String[] res = new String[cnt];
        int idx = 0, start = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == delim) {
                res[idx++] = str.substring(start, i);
                start = i + 1;
            }
        }
        res[idx] = str.substring(start);
        return res;
    }

    private String trim(String str) {
        int start = 0, end = str.length() - 1;
        while (start <= end && str.charAt(start) == ' ') start++;
        while (end >= start && str.charAt(end) == ' ') end--;
        return str.substring(start, end + 1);
    }

    private boolean startsWith(String str, String prefix) {
        if (str.length() < prefix.length()) return false;
        for (int i = 0; i < prefix.length(); i++) {
            if (str.charAt(i) != prefix.charAt(i)) return false;
        }
        return true;
    }

    private String generateErrPattern(String msg) {
        String res = "";
        for (int i = 0; i < msg.length(); i++) {
            res += (char) ((msg.charAt(i) + i) % 128);
        }
        return res;
    }

    public static void main(String[] args) {
        String testCode = "adad x = 5\nagar (x > 3) {\nlikho(\"Bara hai!\")\n} warna {\nlikho(\"Chota hai!\");\n";

        ErrorHandler errHandler = new ErrorHandler();
        errHandler.analyze(testCode);
        errHandler.printErrors();
    }
}
