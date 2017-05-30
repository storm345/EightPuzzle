package org.stormdev.eightpuzzle.board;

import org.stormdev.eightpuzzle.solver.SolveStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardState implements Cloneable {
	private Integer[][] grid = new Integer[3][3]; //[Rows][Columns]
	
	private void initGrid(){
		//Init grid
		for(int y=0;y<3;y++){
			int base = y*3;
			for(int x=0;x<3;x++){
				if(x == 2 && y == 2){
					grid[y][x] = -1;
				}
				else {
					grid[y][x] = base+x+1;
				}
			}
		}
	}
	
	public BoardState clone(){
		Integer[][] dest = new Integer[3][3];
		for(int y=0;y<3;y++){
			for(int x=0;x<3;x++){
				dest[y][x] = grid[y][x];
			}
		}
		return new BoardState(dest);
	}
	
	public BoardState(){
		initGrid();
	}
	
	public void shuffle(){
		List<Direction> steps = new ArrayList<>();
		for(int i=0;i<50;i++){
			Direction d = Direction.random();
			if(move(d)){
				steps.add(d);
			}
		}
	}
	
	public BoardState(Integer[][] grid){
		this.grid = grid;
	}
	
	public int getValue(int x, int y){
		return this.grid[y][x];
	}
	
	public int getIndex(int x, int y){
		return 3*y + x;
	}
	
	public int getRow(int index){
		return (int) Math.floor(index/3.0d);
	}
	
	public int getCol(int index){
		return index - (getRow(index) * 3);
	}
	
	public int getValue(int index){
		return getValue(getCol(index), getRow(index));
	}
	
	public void setValue(int x, int y, int value){
		this.grid[y][x] = value;
	}
	
	public void setValue(int index, int value){
		setValue(getCol(index), getRow(index), value);
	}
	
	public int indexOfHole(){
		for(int i=0;i<9;i++){
			if(getValue(i) == -1){
				return i;
			}
		}
		return -1;
	}
	
	public boolean moveHoleTo(int index){
		int holeIndex = indexOfHole();
		int holeY = getRow(holeIndex);
		int holeX = getCol(holeIndex);
		int nY = getRow(index);
		int nX = getCol(index);
		int modX = nX - holeX;
		int modY = nY - holeY;
		
		if(Math.abs(modX) > 1 || Math.abs(modY) > 1 || (Math.abs(modX) > 0 && Math.abs(modY) > 0) 
				|| nY < 0 || nX < 0 || nY > 2 || nX > 2){ //Illegal move
			return false;
		}
		
		int currentNVal = getValue(nX, nY);
		setValue(nX, nY, -1);
		setValue(holeX, holeY, currentNVal);
		return true;
	}
	
	public boolean move(Direction dir){
		int holeIndex = indexOfHole();
		int holeY = getRow(holeIndex);
		int holeX = getCol(holeIndex);
		int nX = holeX - dir.getModX();
		int nY = holeY - dir.getModY();
		return moveHoleTo(getIndex(nX, nY));
	}
	
	public BoardState stateIfMove(Direction dir){
		BoardState bs = clone();
		bs.move(dir);
		return bs;
	}
	
	public boolean isComplete(){
		return countTilesOutOfOrder() < 1;
	}
	
	public int countTilesOutOfOrder(){
		int count = 0;
		for(int y=0;y<3;y++){
			for(int x=0;x<3;x++){
				if(getValue(x, y) == -1){
					continue;
				}
				if(getIndex(x, y)+1 != getValue(x, y)){
					count++;
				}
			}
		}
		return count;
	}
	
	public void printOut(){
		for(int y=0;y<3;y++){
			StringBuilder row = new StringBuilder();
			Integer[] columns = grid[y];
			for(int x=0;x<3;x++){
				Integer value = columns[x];
				if(value == -1){
					row.append("   ");
				}
				else {
					row.append(" "+value+" ");
				}
			}
			System.out.println(row);
		}
	}
	
	public boolean isEqualTo(BoardState other){
	    for(int index=0;index<9;index++){
	        if((int)this.getValue(index) != (int)other.getValue(index)){
                return false;
            }
        }
        return true;
	}
	
	public List<BoardState> getAllOtherMoves(){
		List<BoardState> res = new ArrayList<>();
		for(Direction dir:Direction.values()){
			BoardState other = stateIfMove(dir);
			if(other.isEqualTo(this)){
				continue;
			}
			res.add(other);
		}
		return res;
	}
	
	private List<SolveStep> possibleStates = new ArrayList<SolveStep>();
	public List<Direction> movesToSolve(){
		SolveStep self = new SolveStep(this);
		possibleStates.add(self);
		
		boolean solved = this.isComplete();
		SolveStep solution = self;
		while(!solved && !possibleStates.isEmpty()){
			Collections.sort(possibleStates);
			SolveStep next = possibleStates.remove(0);
			if(next == null){
				break;
			}
			if(next.getBoardState().isComplete()){
				solved = true;
				solution = next;
				break;
			}
			possibleStates.addAll(next.getNextSteps());
		}
		
		if(!solved){
			return new ArrayList<>();
		}
		
		List<Direction> steps = new ArrayList<>();
		for(SolveStep ss:solution.getPreviousSteps()){
			if(ss.getAppliedDirection().equals(Direction.NONE)){
				continue;
			}
			steps.add(ss.getAppliedDirection());
		}
		steps.add(solution.getAppliedDirection());
		return steps;
	}
}
