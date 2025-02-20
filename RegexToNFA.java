package com.example.demo;

import java.util.*;

public class RegexToNFA {
    static class State {
        static int idCounter = 0;
        int id;
        List<Transition> transitions = new ArrayList<>();

        public State() {
            this.id = idCounter++;
        }

        public void addTransition(String symbol, State next) {
            transitions.add(new Transition(symbol, next));
        }
    }

    static class Transition {
        String symbol;
        State next;

        public Transition(String symbol, State next) {
            this.symbol = symbol;
            this.next = next;
        }
    }

    static class NFA {
        State start;
        State accept;

        public NFA(State start, State accept) {
            this.start = start;
            this.accept = accept;
        }
    }

    private static final String EPSILON = "ε";

    public static NFA convertToNFA(String regex) {
        String postfix = toPostfix(insertConcatOperators(regex));
        return buildNFAFromPostfix(postfix);
    }

    private static String insertConcatOperators(String regex) {
        StringBuilder result = new StringBuilder();
        int len = regex.length();
        for (int i = 0; i < len; i++) {
            char c = regex.charAt(i);
            result.append(c);
            if (i < len - 1) {
                char next = regex.charAt(i + 1);
                if ((isLiteral(c) || c == '*' || c == ')') && (isLiteral(next) || next == '(')) {
                    result.append('.');
                }
            }
        }
        return result.toString();
    }

    private static boolean isLiteral(char c) {
        return !(c == '(' || c == ')' || c == '*' || c == '|' || c == '.');
    }

    private static String toPostfix(String regex) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (char c : regex.toCharArray()) {
            if (isLiteral(c)) {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    output.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }
        return output.toString();
    }

    private static int precedence(char c) {
        switch (c) {
            case '*':
                return 3;
            case '.':
                return 2;
            case '|':
                return 1;
            default:
                return 0;
        }
    }

    private static NFA buildNFAFromPostfix(String postfix) {
        Stack<NFA> stack = new Stack<>();
        for (char c : postfix.toCharArray()) {
            if (isLiteral(c)) {
                State start = new State();
                State accept = new State();
                start.addTransition(Character.toString(c), accept);
                stack.push(new NFA(start, accept));
            } else if (c == '*') {
                NFA nfa = stack.pop();
                State start = new State();
                State accept = new State();
                start.addTransition(EPSILON, nfa.start);
                start.addTransition(EPSILON, accept);
                nfa.accept.addTransition(EPSILON, nfa.start);
                nfa.accept.addTransition(EPSILON, accept);
                stack.push(new NFA(start, accept));
            } else if (c == '.') {
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                nfa1.accept.addTransition(EPSILON, nfa2.start);
                stack.push(new NFA(nfa1.start, nfa2.accept));
            } else if (c == '|') {
                NFA nfa2 = stack.pop();
                NFA nfa1 = stack.pop();
                State start = new State();
                State accept = new State();
                start.addTransition(EPSILON, nfa1.start);
                start.addTransition(EPSILON, nfa2.start);
                nfa1.accept.addTransition(EPSILON, accept);
                nfa2.accept.addTransition(EPSILON, accept);
                stack.push(new NFA(start, accept));
            }
        }
        return stack.pop();
    }

    public static void printNFA(NFA nfa) {
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(nfa.start);
        visited.add(nfa.start);
        System.out.println("NFA State Transitions:");
        while (!queue.isEmpty()) {
            State state = queue.poll();
            for (Transition trans : state.transitions) {
                System.out.println("State " + state.id + " --" + trans.symbol + "--> State " + trans.next.id);
                if (!visited.contains(trans.next)) {
                    visited.add(trans.next);
                    queue.add(trans.next);
                }
            }
        }
        System.out.println("Start State: " + nfa.start.id);
        System.out.println("Accept State: " + nfa.accept.id);
    }
    static class DFA {
        Set<State> start;
        Set<Set<State>> acceptStates;
        Map<Set<State>, Map<String, Set<State>>> transitions;

        public DFA(Set<State> start, Set<Set<State>> acceptStates, Map<Set<State>, Map<String, Set<State>>> transitions) {
            this.start = start;
            this.acceptStates = acceptStates;
            this.transitions = transitions;
        }
    }

    public static DFA convertToDFA(NFA nfa) {
        Set<State> startState = epsilonClosure(Collections.singleton(nfa.start));
        Set<Set<State>> dfaStates = new HashSet<>();
        Map<Set<State>, Map<String, Set<State>>> dfaTransitions = new HashMap<>();
        Queue<Set<State>> queue = new LinkedList<>();

        dfaStates.add(startState);
        queue.add(startState);

        while (!queue.isEmpty()) {
            Set<State> currentDFAState = queue.poll();
            dfaTransitions.put(currentDFAState, new HashMap<>());

            Set<String> symbols = new HashSet<>();
            for (State nfaState : currentDFAState) {
                for (Transition trans : nfaState.transitions) {
                    if (!trans.symbol.equals(EPSILON)) {
                        symbols.add(trans.symbol);
                    }
                }
            }

            for (String symbol : symbols) {
                Set<State> nextDFAState = new HashSet<>();
                for (State nfaState : currentDFAState) {
                    for (Transition trans : nfaState.transitions) {
                        if (trans.symbol.equals(symbol)) {
                            nextDFAState.add(trans.next);
                        }
                    }
                }

                Set<State> nextDFAStateEpsilonClosure = epsilonClosure(nextDFAState);
                dfaTransitions.get(currentDFAState).put(symbol, nextDFAStateEpsilonClosure);

                if (!dfaStates.contains(nextDFAStateEpsilonClosure)) {
                    dfaStates.add(nextDFAStateEpsilonClosure);
                    queue.add(nextDFAStateEpsilonClosure);
                }
            }
        }

        Set<Set<State>> acceptStates = new HashSet<>();
        for (Set<State> dfaState : dfaStates) {
            for (State nfaState : dfaState) {
                if (nfaState == nfa.accept) {
                    acceptStates.add(dfaState);
                    break;
                }
            }
        }

        return new DFA(startState, acceptStates, dfaTransitions);
    }
    private static void logConversion(String regex) {
        System.out.println("Converting regex: " + regex);
    }
    private static Set<State> epsilonClosure(Set<State> states) {
        Set<State> closure = new HashSet<>(states);
        Queue<State> queue = new LinkedList<>(states);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            for (Transition trans : currentState.transitions) {
                if (trans.symbol.equals(EPSILON) && !closure.contains(trans.next)) {
                    closure.add(trans.next);
                    queue.add(trans.next);
                }
            }
        }
        return closure;
    }

    public static void printDFA(DFA dfa) {
        System.out.println("DFA State Transitions:");
        for (Map.Entry<Set<State>, Map<String, Set<State>>> entry : dfa.transitions.entrySet()) {
            Set<State> fromState = entry.getKey();
            for (Map.Entry<String, Set<State>> transEntry : entry.getValue().entrySet()) {
                String symbol = transEntry.getKey();
                Set<State> toState = transEntry.getValue();
                System.out.println("State " + stateSetToString(fromState) + " --" + symbol + "--> State " + stateSetToString(toState));
            }
        }
        System.out.println("Start State: " + stateSetToString(dfa.start));
        System.out.println("Accept States: " + dfa.acceptStates.stream().map(RegexToNFA::stateSetToString).toList());
    }

    private static Set<State> getAllStates(NFA nfa) {
        Set<State> states = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(nfa.start);
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (!states.contains(state)) {
                states.add(state);
                for (Transition trans : state.transitions) {
                    queue.add(trans.next);
                }
            }
        }
        return states;
    }
    private static String stateSetToString(Set<State> states) {
        return states.stream().map(s -> Integer.toString(s.id)).reduce((a, b) -> a + "," + b).orElse("");
    }

    public static void main(String[] args) {
        String regex = "(lo|do|likho|jhanda|adad|asharia|harf|lafz)*";

        System.out.println("Input Regex: " + regex);
        NFA nfa = convertToNFA(regex);
        System.out.println("NFA:");
        printNFA(nfa);
        DFA dfa = convertToDFA(nfa);
        System.out.println("\nDFA:");
        printDFA(dfa);
    }
}
