import labyrinth.Labyrinth;

public class Start {
    public static void main(String[] args) {
        Labyrinth labyrinth = new Labyrinth(10, 10, 30, false);
        BruteForceSolver bruteForceSolver = new BruteForceSolver(labyrinth);
        Solver solver = new Solver(labyrinth);

        bruteForceSolver.findPathByBruteForce();
        labyrinth.print();
        solver.solveMaze();
        labyrinth.print();
    }
}
