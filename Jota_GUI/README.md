# Delta Trigger — GUI Polimento V5

Versão de polimento visual e funcional construída sobre a GUI anterior e seguindo o V3.1 do jogo em console como referência de regras.

## Rodar

No IntelliJ, execute:

`src/MainGUI.java`

Ou pelo terminal, na pasta do projeto:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out MainGUI
```

## Destaques da V5

- Batalhas com feedback visual, transições de wave, vitória, level up e Game Over.
- Mochila com comparação de equipamentos e opção de desequipar.
- Loja gráfica com comprar, vender e equipar.
- Grupo com acesso direto ao inventário de cada personagem.
- Menu com Continuar/Save, Opções e Créditos.
- Mapa de progresso da jornada.
- Diálogo visual estilo RPG.
- Layout mais adaptável a diferentes resoluções.

Veja `GUI_POLIMENTO_V5.txt` para a lista completa.
