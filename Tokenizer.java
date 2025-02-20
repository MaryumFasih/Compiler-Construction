package com.example.demo;

import java.util.*;
public class Tokenizer {
    static class DFA {
        private final Set<String> keys;
        private final Set<Character> ops;
        private final Set<Character> puncs;

        public DFA() {
            keys = new HashSet<>(Arrays.asList("adad", "agar", "warna", "likho", "lafz", "lo", "do"));
            ops = new HashSet<>(Arrays.asList('+', '-', '*', '/', '=', '>', '<', '!'));
            puncs = new HashSet<>(Arrays.asList(';', '(', ')', '{', '}'));
        }

        public String getType(String tok) {
            if (keys.contains(tok)) return "KEYWORD";
            if (tok.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return "IDENTIFIER";
            if (tok.matches("[0-9]+")) return "INTEGER";
            if (tok.startsWith("\"") && tok.endsWith("\"")) return "STRING";
            if (tok.length() == 1 && ops.contains(tok.charAt(0))) return "OPERATOR";
            if (tok.length() == 1 && puncs.contains(tok.charAt(0))) {
                return switch (tok.charAt(0)) {
                    case ';' -> "SEMICOLON";
                    case '(' -> "OPEN BRACKET";
                    case ')' -> "CLOSE BRACKET";
                    case '{' -> "OPEN CURLY BRACKET";
                    case '}' -> "CLOSE CURLY BRACKET";
                    default -> "UNKNOWN";
                };
            }
            return "UNKNOWN TOKEN";
        }
    }

    public boolean isValid(int state) {
        return state >= 0 && state <= 10;
    }

    public void resetDFA() {
        System.out.println("DFA reset initiated...");
    }

    public static List<String> tokenize(String input) {
        List<String> toks = new ArrayList<>();
        DFA dfa = new DFA();
        StringBuilder curTok = new StringBuilder();
        boolean inStr = false;

        for (char c : input.toCharArray()) {
            if (c == ' ' || c == '\n') {
                if (curTok.length() > 0) {
                    toks.add("[" + dfa.getType(curTok.toString()) + "]");
                    curTok.setLength(0);
                }
                continue;
            }

            if (c == '"') {
                inStr = !inStr;
                curTok.append(c);
                if (!inStr) {
                    toks.add("[" + dfa.getType(curTok.toString()) + "]");
                    curTok.setLength(0);
                }
                continue;
            }

            if (!inStr && (dfa.ops.contains(c) || dfa.puncs.contains(c))) {
                if (curTok.length() > 0) {
                    toks.add("[" + dfa.getType(curTok.toString()) + "]");
                    curTok.setLength(0);
                }
                toks.add("[" + dfa.getType(String.valueOf(c)) + "]");
                continue;
            }

            curTok.append(c);
        }

        if (curTok.length() > 0) {
            toks.add("[" + dfa.getType(curTok.toString()) + "]");
        }

        return toks;
    }

    public int getStateCnt() {
        return 10;
    }

    public static void main(String[] args) {
        String input = "adad x = 5; agar (x > 3) { likho(\"Bara hai!\"); } warna { likho(\"Chota hai!\"); } "
                + "lafz name = \"Ali\"; lo(name); do(\"Hello\");";
        List<String> toks = tokenize(input);
        for (String tok : toks) {
            System.out.print(tok);
        }
        System.out.println();
    }
}
