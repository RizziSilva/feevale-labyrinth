package labyrinth;

public class Position implements Comparable<Position>{

	private int x;
	private int y;
	private int finalValue;
	private int heuristicValue;
	private int costValue;
	private Position parent;

	public Position(){
		x = -1;
		y = -1;
		finalValue = 0;
		heuristicValue = 0;
		costValue = 0;
	}

	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}

	public boolean comparaCom(Position p){
		if (p != null && p.getX() == x & p.getY() == y)
			return true;
		else 
			return false;
	}

	public boolean comparaCom(int x, int y){
		if (this.x == x & this.y == y)
			return true;
		else 
			return false;
	}

	@Override
	public int compareTo(Position p) {
		if (finalValue < p.finalValue)
			return -1;
		else if(finalValue > p.finalValue)
			return 1;
		else return -1;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setFinalValue(int finalValue) {
		this.finalValue = finalValue;
	}

	public int getFinalValue() {
		return finalValue;
	}

	public void setCostValue(int costValue) {
		this.costValue = costValue;
	}

	public int getCostValue() {
		return costValue;
	}

	public void setHeuristicValue(int heuristicValue) {
		this.heuristicValue = heuristicValue;
	}

	public int getHeuristicValue() {
		return heuristicValue;
	}

	public void setParent(Position parent) {
		this.parent = parent;
	}

	public Position getParent() {
		return parent;
	}

	public String toString(){
		return new String("(" + x + "," + y + ")");
	}
}