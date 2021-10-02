import labyrinth.Labyrinth;
import labyrinth.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Solver {

    public static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    public Labyrinth labyrinth;
    public Position initialPosition;
    public Position finalPosition;
    public ArrayList<Position> openList = new ArrayList<>();
    public ArrayList<Position> closedList = new ArrayList<>();

    // Inicializa o resolvedor com as informações básicas.
    public Solver(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
        this.initialPosition = labyrinth.getPosicaoEntrada();
        this.finalPosition = labyrinth.getPosicaoSaida();
        this.openList.add(labyrinth.getPosicaoEntrada());
    }

    public void solveMaze() {
        // Enquanto a lista de estados em abertos ter alguma posição para ser vista.
        while (!openList.isEmpty()) {
            // Pega a posição com melhor valor de f.
            Position currentPosition = openList.get(0);
            openList.remove(0);

            // Caso a posição sendo vista seja a saida, adiciona no array de itens fechados e coloca como resposta do labirinto.
            // Quebra o laço de repetição.
            if (isExit(currentPosition)) {
                closedList.add(currentPosition);
                labyrinth.setPath(getCorrectPath());
                break;
            }

            // Cria uma array com as possíveis posições a serem vistas, utilizando o DIRECTIONS.
            ArrayList<Position> possibilities = getPositionPossibilities(currentPosition);

            // Para cada posição possivel a ser vista a partir da posição atual.
            for (Position position : possibilities) {
                // Descobre se ela já foi visitada, ou seja, se está na lista aberta ou fechada.
                boolean isInOpenOrClosedList = isInOpenOrClosedList(position);
                // Busca a posição na lista aberta. Se existir pega a posição se não null;
                Position openPosition = getPositionFromOpenListByPosition(position);
                // Busca a posição na lista fechada. Se existir pega a posição se não null;
                Position closedPosition = getPositionFromClosedListByPosition(position);
                // Calcula os valores para a possibilidade.
                // Função H. Utilizado heuristica Manhattan.
                int positionHeuristicValue = calculateManhattanDistance(position);
                // Função G. Custo para chegar até aqui.
                int positionCostValue = currentPosition.getCostValue() + 1;
                // Função F; Valor total de g + h.
                int positionFinalValue = positionHeuristicValue + positionCostValue;

                if (!isInOpenOrClosedList) {
                    // Se não foi vista atualiza com os valores das funções.
                    position.setCostValue(positionCostValue);
                    position.setHeuristicValue(positionHeuristicValue);
                    position.setFinalValue(positionFinalValue);
                    position.setParent(currentPosition);
                    openList.add(position);
                } else if (Objects.nonNull(openPosition)) {
                    // Caso a posição já está na aberta e está sendo revisitada, se o custo final para chegar até ser diferente
                    // da posição que já estava, atualiza os valores.
                    int openPositionFinalValue = openPosition.getFinalValue();
                    if (positionFinalValue < openPositionFinalValue) {
                        openPosition.setCostValue(positionCostValue);
                        openPosition.setHeuristicValue(positionHeuristicValue);
                        openPosition.setFinalValue(positionFinalValue);
                        openPosition.setParent(currentPosition);
                    }
                } else if (Objects.nonNull(closedPosition)) {
                    // Aqui ficou estranho para mim, debbugando o código diversas veses nunca foi encontrado uma posição
                    // na lista fechada que tivesse seu valor maior do que a posição atual.
                    int closedPositionFinalValue = closedPosition.getFinalValue();
                    if (positionFinalValue < closedPositionFinalValue) {
                        closedList.remove(closedPosition);

                        openList.add(position);
                    }
                }
            }

            // Adiciona a posição atual(não as possibilidades) para começar a criar o caminho andado.
            closedList.add(currentPosition);
            // Reordena as posição na lista aberta, garantindo que a melhor possivel fica mais a esquerda.
            reorderOpenList();
        }
    }

    // Faz o calculo da heuristica de Manhattan.
    private int calculateManhattanDistance(Position position) {
        int xValue = Math.abs(position.getX() - this.finalPosition.getX());
        int yValue = Math.abs(position.getY() - this.finalPosition.getY());

        return xValue + yValue;
    }

    // Verifica que a posição é válida, ou seja ela não é obstaculo, é o inicio ou o fim e não está fora do labirinto.
    private boolean isValidPosition(Position position) {
        if (!isInsideLabyrinth(position)) return false;
        boolean isEmptyPosition = labyrinth.getValorPosicao(position) == 0;
        boolean isBeginning = labyrinth.getValorPosicao(position) == 2;
        boolean isExit = labyrinth.getValorPosicao(position) == 3;
        boolean isWalkablePosition = isEmptyPosition || isBeginning || isExit;

        return isWalkablePosition;
    }

    // Verifica se a posição está dentro do labirinto.
    private boolean isInsideLabyrinth(Position position) {
        boolean isPositionXInsideOfMaze = position.getX() >= 0 && position.getX() <= (labyrinth.getDimX() - 1);
        boolean isPositionYInsideOfMaze = position.getY() >= 0 && position.getY() <= (labyrinth.getDimY() - 1);

        return isPositionXInsideOfMaze && isPositionYInsideOfMaze;
    }

    // Verifica se a posição passada é o fim.
    private boolean isExit(Position position) {
        return labyrinth.getValorPosicao(position) == 3;
    }

    // Para cada direção que posso ir, gero o novo filho e atribuo a lista de possibilidades que a posição passada pode ir.
    private ArrayList<Position> getPositionPossibilities(Position position) {
        ArrayList<Position> possibilities = new ArrayList<>();
        for (int[] direction : DIRECTIONS) {
            int newX = position.getX() + direction[0];
            int newY = position.getY() + direction[1];
            Position newPosition = new Position(newX, newY);

            if (isValidPosition(newPosition)) possibilities.add(newPosition);
        }

        return possibilities;
    }

    // Verifica se a posição passada está dentro da lista de aberto e fechados.
    private boolean isInOpenOrClosedList(Position position) {
        Position openPosition = getPositionFromOpenListByPosition(position);
        Position closedPosition = getPositionFromClosedListByPosition(position);
        return Objects.nonNull(openPosition) || Objects.nonNull(closedPosition);
    }

    // Busca posição dentro do array de abertos ou retorna null.
    private Position getPositionFromOpenListByPosition(Position position) {
        return openList.stream()
                .filter(openPosition -> openPosition.getX() == position.getX() && openPosition.getY() == position.getY())
                .findFirst()
                .orElse(null);
    }

    // Busca posição dentro do array de fechados ou retorna null.
    private Position getPositionFromClosedListByPosition(Position position) {
        return closedList.stream()
                .filter(closedPosition -> closedPosition.getX() == position.getX() && closedPosition.getY() == position.getY())
                .findFirst()
                .orElse(null);
    }

    // Reordena as posições do aberto com base no comparador criado na classe Position.
    private void reorderOpenList() {
        Collections.sort(openList);
    }

    // Retira as posições de caminhos sem fim, utilizando o pai de cada posição.
    private ArrayList<Position> getCorrectPath() {
        ArrayList<Position> correctPath = new ArrayList<>();
        Position currentPosition = closedList.get(closedList.size() - 1);
        while (currentPosition != null) {
            correctPath.add(currentPosition);
            Position parent = currentPosition.getParent();
            currentPosition = Objects.nonNull(parent) ? getPositionFromClosedListByPosition(currentPosition.getParent()) : null;
        }

        return correctPath;
    }
}
