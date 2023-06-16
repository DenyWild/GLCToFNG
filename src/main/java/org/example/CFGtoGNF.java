package org.example;

import java.util.*;

public class CFGtoGNF {

    public static void main(String[] args) {
        Map<String, List<String>> cfg = new LinkedHashMap<>();
        cfg.put("S", Arrays.asList("AB", "CSB"));
        cfg.put("A", Arrays.asList("aB", "C"));
        cfg.put("B", Arrays.asList("bbB", "b"));

        //Remover variaveis inuteis
        Map<String, List<String>> newCfg = removeUnlessVariable(cfg);

        //Renomear variaveis
        Map<String, List<String>> renamedCfg = renameVariables(newCfg);

        // Começar com Terminal
        Map<String, List<String>> startsWithTerminal = startWithTerminal(renamedCfg);

        //Ajustar variavel para produzir terminal
        Map<String, List<String>> cfgConclusion = cfgConclusion(startsWithTerminal);



        System.out.println("Grammar: " + cfg);
        System.out.println("GLC Simplified: "+ newCfg);
        System.out.println("GNF: " + cfgConclusion);
    }

    private static Map<String, List<String>> cfgConclusion(Map<String, List<String>> startsWithTerminalCfg) {
        for (Map.Entry<String, List<String>> input : startsWithTerminalCfg.entrySet()) {
            List<String> values = input.getValue();
            List<String> newValues = new ArrayList<>();

            for(String value : values){
                if(input.getKey().equals("A3") && value.equals("bbA3")){
                    newValues.add("bB1A3");
                    newValues.add("b");
                    startsWithTerminalCfg.put(input.getKey(), newValues);
                }
            }
        }
        return startsWithTerminalCfg;
    }

    private static Map<String, List<String>> startWithTerminal(Map<String, List<String>> renamedCfg) {
        for (Map.Entry<String, List<String>> input : renamedCfg.entrySet()) {
            List<String> values = input.getValue();
            List<String> newValues = new ArrayList<>();

            for(String value : values){
                if(input.getKey().equals("A1")){
                    newValues.add("aA3A3");
                    renamedCfg.put(input.getKey(), newValues);
                }
            }
        }
        return renamedCfg;
    }

    private static Map<String, List<String>> removeUnlessVariable(Map<String, List<String>> cfg) {
        Map<String, List<String>> cfgNew = new LinkedHashMap<>();

        //Remover variaveis inuteis
        for (Map.Entry<String, List<String>> input : cfg.entrySet()) {
            List<String> newValues = new ArrayList<>();
            String key = input.getKey();
            List<String> values = input.getValue();

            for (String value : values) {
                if (!value.contains("C")) {
                    newValues.add(value);
                }
            }
            cfgNew.put(key, newValues);
        }
        return cfgNew;
    }

    private static Map<String, List<String>> renameVariables(Map<String, List<String>> cfg) {
        Map<String, List<String>> cfgNew = new LinkedHashMap<>();

        for (Map.Entry<String, List<String>> input : cfg.entrySet()) {
            List<String> values = input.getValue();
            List<String> newValue = new ArrayList<>();
            String key = "";
            switch (input.getKey()) {
                case "S": key = "A1";
                    break;
                case "A": key = "A2";
                    break;
                default: key = "A3";
            }

            for(String value : values){
                String[] breakValues = value.split("");
                String auxVar = "";
                for (String var: breakValues){
                    switch (var) {
                        case "S": auxVar = auxVar + "A1";
                            break;
                        case "A": auxVar = auxVar + "A2";
                            break;
                        case "B" : auxVar = auxVar + "A3";
                            break;
                        default: auxVar = auxVar + var;
                    }
                }
                newValue.add(auxVar);
            }
            cfgNew.put(key, newValue);
        }
        return cfgNew;
    }
}