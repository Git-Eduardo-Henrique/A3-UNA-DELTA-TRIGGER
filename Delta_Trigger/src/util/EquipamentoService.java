package util;

import model.*;

public final class EquipamentoService {
    private EquipamentoService() {}

    public static boolean equipar(Grupo grupo, Personagem personagem, Equipamento equipamento) {
        if (grupo == null || personagem == null || equipamento == null) return false;
        if (!grupo.getInventario().possuiEquipamento(equipamento)) return false;
        if (!personagem.podeEquipar(equipamento)) return false;

        Equipamento antigo = personagem.equipamentoDoTipo(equipamento.getTipo());
        grupo.getInventario().removerEquipamento(equipamento);
        if (antigo != null) grupo.getInventario().adicionarEquipamento(antigo);
        personagem.equiparDireto(equipamento);
        return true;
    }
}
