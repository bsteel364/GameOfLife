import javax.swing.JFrame;

public class Board {
	
	public int width;
	public int height;
	public Cell[][] matrix;
	public int age;
	
	/*
	public Board(int w, int h) {
		this.age = 0;
		this.width = w;
		this.height = h;
		this.matrix = new Cell[w][h];
		for(int i = 0; i < w; i++ ) {
			for(int k = 0; k < h; k++ ) {
				this.matrix[i][k] = new Cell(true, i, k);
			}
		}
	}
	*/
	
	public Board(int square) {
		this.age = 0;
		this.width = square;
		this.height = square;
		this.matrix = new Cell[square][square];
		for(int i = 0; i < square; i++ ) {
			for(int k = 0; k < square; k++ ) {
				this.matrix[i][k] = new Cell(true, i, k);
			}
		}
	}
	
	public Object[][] getIntegerMatrix(){
		Integer[][] intMatrix = new Integer[width][height];
		for(int i = 0; i < width; i++ ) {
			for(int k = 0; k < height; k++ ) {
				intMatrix[i][k] = this.matrix[i][k].getIntValue();
			}
		}
		return intMatrix;
	}
	
	
	
	public Cell getCell(int w, int h) {
		return this.matrix[w][h];
	}
	
	public int getCellsAge(int w, int h) {
		return this.matrix[w][h].getAge();
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public boolean coordOutOfBounds(int w, int h) {
		return ((w < 0 || w >= this.width)||(h < 0 || h >= this.height));
	}
	
	public void update() {
		for(int i = 0; i < this.width; i++ ) {
			for(int k = 0; k < this.height; k++ ) {
				applyRules(getCell(i, k));
			//	getCell(i, k).age;
			}
		}
		age++;
	}
	
	public boolean isTouchingAnotherLivingCell(int w, int h) {
		for(int i = w-1; i <= w+1; i++ ) {
			for(int k = h-1; k <= h+1; k++ ) {
				if(!coordOutOfBounds(i, k) && this.matrix[i][k].isAlive() && !(i == w && k == h)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isTouchingInfectedCell(int w, int h) {
		for(int i = w-1; i <= w+1; i++ ) {
			for(int k = h-1; k <= h+1; k++ ) {
				if(!coordOutOfBounds(i, k) && this.matrix[i][k].isInfected() && !(i == w && k == h)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getNumberOfLivingNeighbors(int w, int h) {
		int neighbors = 0;
		for(int i = w-1; i <= w+1; i++ ) {
			for(int k = h-1; k <= h+1; k++ ) {
				if(!coordOutOfBounds(i, k) && this.matrix[i][k].isAlive() && !(i == w && k == h)) {
					neighbors++;
				}
			}
		}
		return neighbors;
	}
	
	
	public void applyRules(Cell cell) {
		rule1(cell);
		rule2(cell);
		rule3(cell);
	//	infectionRule(cell);
	//	testRule(cell);
		//rule3(cell);
	}
	
	//////////////////////////////////////RULES///////////////////////////////////////////////////////
	/* STANDARD RULES OF OG Conway's Game
	Any live cell with fewer than two live neighbors dies, as if by under population.
	Any live cell with two or three live neighbors lives on to the next generation.
	Any live cell with more than three live neighbors dies, as if by overpopulation.
	Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
	*/
	public void infectionRule(Cell cell) {
		if(cell.isAlive() && !cell.isInfected() && isTouchingInfectedCell(cell.getWidth(), cell.getHeight())) {
			cell.infect();
		} else if(cell.isInfected() && cell.getAge() > 3) {
			cell.defect();
			cell.kill();
		}
	}
	
	public void rule1(Cell cell) {
		if(this.getNumberOfLivingNeighbors(cell.getWidth(), cell.getHeight()) < 2){
			cell.kill();
		}
	}
	
	public void rule2(Cell cell) {
		if(this.getNumberOfLivingNeighbors(cell.getWidth(), cell.getHeight()) > 3) {
			cell.kill();
		}
	}
	
	public void rule3(Cell cell) {
		if(cell.isDead() && this.getNumberOfLivingNeighbors(cell.getWidth(), cell.getHeight()) == 3) {
			cell.spawn();
		}
	}
	
	
	
	public void testRule(Cell cell) {
		if(cell.isAlive()) {
			cell.kill();
		} else {
			cell.spawn();
		}
	}
	
	//////////////// === My Recursive Rules === \\\\\\\\\\\\\\\\\\\\\\\\\\
	
	//public void
	

	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void simulate(int maxAge) {
		int currentAge = 0;
		while(currentAge < maxAge) {
			this.update();
			currentAge++;
		}
	}
	
	public void randomlyFillBoard(double precentageChanceOfBirthPerCell, double precentageChanceOfInfection) {
		for(int i = 0; i < width; i++ ) {
			for(int k = 0; k < height; k++ ) {
				double rand = Math.random();
				if(rand <= (precentageChanceOfBirthPerCell/100)) {
					getCell(i, k).spawn();
					double rand2 = Math.random();
					if(rand2 < precentageChanceOfInfection/100) {
						getCell(i, k).infect();
					} 
				} else {
					getCell(i, k).kill();
				}
			}
		}
	}
	
	public void printBoardToConsole() {
		for(int i = 0; i < width; i++ ) {
			for(int k = 0; k < height; k++ ) {
				if(matrix[i][k].isDead()) {
					System.out.print(" ");
				} else {
					System.out.print("*");
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Board b = new Board(9);
		b.randomlyFillBoard(50, 2);
		b.printBoardToConsole();
		b.simulate(4);
		System.out.println("\n\n 100 ages later \n\n");
		b.printBoardToConsole();
		
		
		
	}
	
	
	
}
