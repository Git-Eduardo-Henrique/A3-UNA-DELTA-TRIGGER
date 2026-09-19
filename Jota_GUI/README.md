# Delta Trigger — GUI Opções & Créditos V6

Baseada na GUI Polimento V5 e na lógica do V3.1.

## Mudanças desta revisão
- Removido o botão **GRUPO** da tela da Floresta dos Lobos.
- Tela de **Opções** redesenhada no estilo azul/dourado.
- Opções funcionais para:
  - efeitos sonoros;
  - animações/transições;
  - velocidade automática do texto da história (Lenta, Normal, Rápida e Instantânea);
  - janela maximizada;
  - restaurar padrões.
- **Créditos** agora têm uma tela própria, sem JOptionPane padrão.
- Créditos registram a equipe original e a parceria da GUI: **Você + ChatGPT (OpenAI)**.
- Opção de desligar animações realmente remove os efeitos/transições de combate.

## Executar
Abra `src/MainGUI.java` e rode `main()`.

## Revisão V7 - fundo do menu principal
- A tela inicial agora usa `assets/menu_bg.jpg`, a nova arte de Aethoria enviada pelo usuário.
- `forest_bg.png` continua preservado para Floresta dos Lobos e batalhas; apenas o menu principal foi alterado.

## Revisão V8 — Livro + correção dos itens em batalha

- A introdução agora é apresentada como um livro aberto com 4 páginas (2 spreads).
- O texto continua sendo escrito automaticamente respeitando a velocidade escolhida em Opções.
- Botão "Mostrar páginas" completa apenas as páginas atuais.
- "Próxima página" avança no livro e, ao final, segue para a escolha do personagem.
- Corrigido o inventário de batalha: Kael/Lyra/Elyra podem ser clicados como ALVO do item.
- O item continua saindo do inventário do personagem que está realizando o turno, como no V3.1.
- Removido o conflito visual do texto "GRUPO / INVENTÁRIO" no fundo da janela de itens usando um recorte limpo do cenário.
- Projeto recompilado com sucesso após as alterações.
