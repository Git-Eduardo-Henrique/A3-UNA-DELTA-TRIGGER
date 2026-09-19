# Delta Trigger - Tutorial V3.1

Projeto Java 8 em modo console.

## Fluxo da demo

Inicio -> historia -> escolha do lider -> Floresta dos Lobos -> 5 waves -> Fenrok -> level up -> Eldoria -> Taverna -> Elyra nivel 2 -> entrada da Caverna 1.

## Waves

1. 3 Lobinhos
2. 3 Lobinhos
3. 2 Lobos
4. 3 Lobos
5. Fenrok

## Regras importantes

- Kael e Lyra ja iniciam com arma e armadura equipadas.
- Elyra entra no nivel 2 e com seus equipamentos iniciais equipados.
- Inimigos escolhem aleatoriamente entre personagens vivos.
- Personagens e inimigos derrotados nao agem nem podem ser alvo.
- Entradas invalidas nao consomem turno.
- 0 volta somente onde o menu oferece essa opcao.
- HP e Mana respeitam os limites maximos.
- Itens nao sao consumidos quando nao podem ser usados.
- Recompensas de batalha vao para o inventario do lider.
- O Manto da Pelagem Alfa e as Botas da Pelagem Alfa sao equipamentos e podem permanecer no inventario.
- Save e feito somente na Pousada e fica criptografado em save.txt.
- Textos narrativos principais ficam em data/textos.txt.
- Cores da interface sao padronizadas.

## Compilar

Na pasta do projeto:

javac -encoding UTF-8 -d out src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/util/*.java

## Executar

java -cp out Main

## Base de cenarios

A classe abstrata Cenario possui as especializacoes Cidade, Campo e Caverna. Essa estrutura sera reutilizada nas proximas areas, mantendo heranca, abstracao e polimorfismo sem duplicar a base de cada cenario.

ETAPA 5
- Batalha redesenhada: somente o protagonista do turno aparece no painel esquerdo.
- Waves 1-2 usam cenário 1; waves 3-4 usam cenário 2; Fenrok usa cenário 3.
- Inimigos ficam abaixo do cenário e a arena suporta múltiplos alvos.
- Botão ITEM abre consumíveis durante a batalha, permite escolher alvo e só consome quando tem efeito.
- Próxima etapa iniciada: base para Caverna dos Slimes permanece após Eldoria.

## VERSÃO GUI COMPLETA PARA TESTE
- Conteúdo V3.1 preservado como base: história, escolha do líder, 5 waves da Floresta, Fenrok, level-up, Eldoria, Taverna, Elyra nível 2, Pousada/save, Loja, itens e equipamentos.
- Batalha GUI com personagem do turno à esquerda, cenário por fase e 2–5 inimigos abaixo do cenário.
- Itens utilizáveis em batalha sem consumo quando não têm efeito.
- Habilidades GUI para Kael, Lyra e Elyra.
- Distribuição de pontos de atributo após Fenrok.
- Continuação jogável nova após o limite do V3.1: Caverna dos Slimes com 3 ondas, Rei Slime e encerramento da versão jogável.
