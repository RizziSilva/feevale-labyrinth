import labyrinth.Labyrinth;
import labyrinth.Position;

import java.util.ArrayList;

public class BruteForceSolver {
    public static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    public Labyrinth labyrinth;
    public Position initialPosition;
    public ArrayList<Position> path = new ArrayList<>();
    public ArrayList<Position> positionAlreadyVisited = new ArrayList<>();

    public BruteForceSolver(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
        this.initialPosition = labyrinth.getPosicaoEntrada();
    }

    public void findPathByBruteForce() {
        if (walkInTheMaze(initialPosition)) {
            labyrinth.setPath(path);
        }
    }

    private boolean walkInTheMaze(Position position) {
        // Garante que a posição que será observada é valida. (não está fora do labirinto, não é bloqueio, é fim ou inicio, não foi visitada ainda)
        boolean canWalkTo = isValidPosition(position);

        // Se não for válido não tenta utilizar a posição.
        if (!canWalkTo) return false;

        // Adiciona a posição no caminho da solução e no array de já visitados..
        path.add(position);
        positionAlreadyVisited.add(position);

        // Se for a saida retorna true para quebrar a recursividade.
        if (isExit(position)) return true;

        // Para cada uma das direções possiveis de se ir.
        for (int[] direction : DIRECTIONS) {
            // Criar uma nova posição, com as coordenadas novas, se baseando na posição atual.
            int newX = position.getX() + direction[0];
            int newY = position.getY() + direction[1];
            Position newPosition = new Position(newX, newY);

            // Faz a recursividade novamente, agora utilizando a nova posição criada.
            if (walkInTheMaze(newPosition)) return true;
        }

        // Remove a posição do caminho caso ela não seja uma posição válida(não tenha caminho possivel a partir dela).
        path.remove(path.size() - 1);
        return false;
    }

    private boolean isValidPosition(Position position) {
        if (!isInsideLabyrinth(position)) return false;
        boolean isEmptyPosition = labyrinth.getValorPosicao(position) == 0;
        boolean isBeginning = labyrinth.getValorPosicao(position) == 2;
        boolean isExit = labyrinth.getValorPosicao(position) == 3;
        boolean isWalkablePosition = isEmptyPosition || isBeginning || isExit;
        boolean isNotVisited = isNotVisited(position);

        return isWalkablePosition && isNotVisited;
    }

    private boolean isInsideLabyrinth(Position position) {
        boolean isPositionXInsideOfMaze = position.getX() >= 0 && position.getX() <= (labyrinth.getDimX() - 1);
        boolean isPositionYInsideOfMaze = position.getY() >= 0 && position.getY() <= (labyrinth.getDimY() - 1);

        return isPositionXInsideOfMaze && isPositionYInsideOfMaze;
    }

    private boolean isNotVisited(Position position) {
        return positionAlreadyVisited.stream().noneMatch(positionVisited -> positionVisited.getX() == position.getX() && positionVisited.getY() == position.getY());
    }

    private boolean isExit(Position position) {
        return labyrinth.getValorPosicao(position) == 3;
    }
}
