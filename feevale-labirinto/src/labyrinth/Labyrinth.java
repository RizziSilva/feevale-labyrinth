package labyrinth;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

// 0 = vazio
// 1 = ocupado
// 2 = entrada
// 3 = saída

public class Labyrinth {
	
	private int dimX;
	private int dimY;
	private int labirinto[][];
	private int txObstaculos;
	private ArrayList<Position> path = new ArrayList<>();
	private Position atual;
	private Position saida;

	public Labyrinth(int dimX, int dimY, int txO, boolean d){
		this.dimX = dimX;
		this.dimY = dimY;
		labirinto = new int[dimX][dimY];
		txObstaculos = txO;

		Random gerador = new Random();

		for (int x = 0; x < dimX; x++)
			for (int y = 0; y < dimY; y++){
				if (gerador.nextInt(100) < txObstaculos)
					labirinto[x][y] = 1;
			}

		atual = new Position(gerador.nextInt(dimX), gerador.nextInt(dimY));
		labirinto[atual.getX()][atual.getY()] = 2;
		saida   = new Position(gerador.nextInt(dimX), gerador.nextInt(dimY));
		labirinto[saida.getX()][saida.getY()] = 3;
	}

	public Position getPosicaoEntrada(){
		return atual;
	}

	public Position getPosicaoSaida(){
		return saida;
	}

	public Vector<Position> getExpansao(Position p){
		int x = p.getX();
		int y = p.getY();

		Vector<Position> expansao = new Vector<Position>();

		// Sentido horário
		if (x > 0 && labirinto[x-1][y] != 1)
			expansao.add(new Position(x-1,y));
		if (y < dimY-1  && labirinto[x][y+1] != 1)
			expansao.add(new Position(x,y+1));
		if (x < dimX-1  && labirinto[x+1][y] != 1)
			expansao.add(new Position(x+1,y));
		if (y > 0  && labirinto[x][y-1] != 1)
			expansao.add(new Position(x,y-1));

		return expansao;
	}

	public int getValorPosicao(Position p){
		return labirinto[p.getX()][p.getY()];
	}

	public void print() {
		
		/* Imprime o labyrinth.Labirinto com o caminho */
		System.out.print("      |");
		for (int c = 0; c < dimY; c++)
			System.out.printf("%4d|", c);
		System.out.printf("\n      |");
		for (int c = 0; c < dimY; c++)
			System.out.printf("----|");
		System.out.println();
		for (int x = 0; x < dimX; x++){
			System.out.printf("%6d|", x);
			for (int y = 0; y < dimY; y++){
				if (labirinto[x][y] != 0 ) {
					if (labirinto[x][y] == 1)
						System.out.printf("++++|");
					if (labirinto[x][y] == 2)
						System.out.printf("  E |");
					if (labirinto[x][y] == 3)
						System.out.printf("  S |");
				}
				else {
					boolean solucao = false;		       
					if ( path != null){
						for (int pc=0; pc<path.size(); pc++)
							if (path.get(pc).comparaCom(x,y)){
								solucao = true;
							}
					}
					if (solucao)
						System.out.print("  x |");
					else
						System.out.print("    |");
				}
			}
			System.out.printf("\n      |");
			for (int c = 0; c < dimY; c++)
				System.out.printf("----|");
			System.out.println();
		}
		
		if (path != null) {
			/* Imprime o caminho*/
			for (int c = path.size() - 1; c >= 0; c--) {
				System.out.print(" -> " + path.get(c).toString());
			}
			System.out.println();
		}
	}

	public double getDLR(Position a, Position b){
		int catB = Math.abs(a.getX() - b.getX()); // Cateto B
		int catC = Math.abs(a.getY() - b.getY()); // Cateto B
		double DLR = Math.sqrt((catB * catB) + (catC * catC));
		return DLR;
	}

	public int getDimX(){
		return dimX;
	}

	public int getDimY(){
		return dimY;
	}

	public void setPath(ArrayList<Position> path) {
		this.path = path;
	}

	public ArrayList<Position> getPath() {
		return path;
	}
}