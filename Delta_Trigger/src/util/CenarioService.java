package util;

import model.*;
import java.io.*;
import java.util.*;

public final class CenarioService {
    private CenarioService() {}
    public static Caverna criarCaverna(String id, String nome, String descricao, String[][] waves) {
        Caverna c = new Caverna(id, nome, descricao);
        for (String[] w : waves) {
            Wave wave = new Wave(Integer.parseInt(w[0]), w[1]);
            String[] grupos = w[2].split(";");
            for (String grupo : grupos) {
                String[] p = grupo.split(":");
                if (p.length == 2) wave.adicionarInimigo(p[0], Integer.parseInt(p[1]));
            }
            c.adicionarWave(wave);
        }
        return c;
    }

    public static Caverna florestaDosLobos() {
        return criarCaverna("FLORESTA_LOBOS", "Floresta dos Lobos", "Uma floresta densa onde a alcateia protege seu territorio.", new String[][] {
            {"1", "Tres Lobinhos surgiram!", "LOBINHO:3"},
            {"2", "Tres Lobinhos surgiram!", "LOBINHO:3"},
            {"3", "Dois Lobos surgiram!", "LOBO:2"},
            {"4", "Tres Lobos surgiram!", "LOBO:3"},
            {"5", "Fenrok, o Lobo Alfa, apareceu!", "FENROK:1"}
        });
    }

    public static Caverna cavernaDosSlimes() {
        return criarCaverna("CAVERNA_SLIMES", "Caverna dos Slimes", "Uma caverna umida tomada por criaturas gelatinosas.", new String[][] {
            {"1", "Slimes surgiram!", "SLIME:3"},
            {"2", "Mais slimes bloqueiam o caminho!", "SLIME:3"},
            {"3", "Um Slime Guerreiro apareceu!", "SLIME_GUERREIRO:2"},
            {"4", "O caminho para o rei esta livre!", "SLIME:2;SLIME_GUERREIRO:1"},
            {"5", "Gellor, o Rei dos Slimes, apareceu!", "GELLOR:1"}
        });
    }
}
