// ============================================================
// ARQUIVO: EquipamentoService.java
// Cuida da troca de equipamentos entre o inventario do grupo e
// o personagem. Ao equipar algo novo, o equipamento antigo volta
// para o inventario.
// ============================================================

package util;

import model.*;

public final class EquipamentoService {

    // Construtor privado: a classe so tem metodos estaticos.
    private EquipamentoService() {}

    // Equipa um equipamento em um personagem. Retorna false se
    // algum dado for invalido ou se o personagem nao puder usar.
    public static boolean equipar(Grupo grupo, Personagem personagem, Equipamento equipamento) {
        // Confere se os objetos existem.
        if (grupo == null || personagem == null || equipamento == null) return false;
        // O equipamento precisa estar no inventario do grupo.
        if (!grupo.getInventario().possuiEquipamento(equipamento)) return false;
        // O personagem precisa poder usar esse tipo/classe de equipamento.
        if (!personagem.podeEquipar(equipamento)) return false;

        // Guarda o equipamento que ja estava no mesmo "slot".
        Equipamento antigo = personagem.equipamentoDoTipo(equipamento.getTipo());
        // Tira o novo do inventario e devolve o antigo para o inventario.
        grupo.getInventario().removerEquipamento(equipamento);
        if (antigo != null) grupo.getInventario().adicionarEquipamento(antigo);
        // Equipa o novo equipamento no personagem.
        personagem.equiparDireto(equipamento);
        return true;
    }
}

