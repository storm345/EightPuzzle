package org.stormdev.eightpuzzle.board;

import org.stormdev.eightpuzzle.main.EightPuzzle;

public enum Direction {
	UP(0, -1), LEFT(-1, 0), DOWN(0, 1), RIGHT(1, 0), NONE(0, 0);
	
	static {
		UP.setOpposite(DOWN);
		DOWN.setOpposite(UP);
		RIGHT.setOpposite(LEFT);
		LEFT.setOpposite(RIGHT);
		NONE.setOpposite(NONE);
	}
	
	private int modX = 0;
	private int modY = 0;
	private Direction opposite;
	private Direction(int modX, int modY){
		this.modX = modX;
		this.modY = modY;
	}
	
	private void setOpposite(Direction d){
		this.opposite = d;
	}
	
	public Direction getOpposite(){
		return this.opposite;
	}
	
	public int getModX(){
		return this.modX;
	}
	
	public int getModY(){
		return this.modY;
	}
	
	public static Direction random(){
		Direction r = values()[EightPuzzle.random.nextInt(values().length)];
		while(r.equals(Direction.NONE)){
			r = values()[EightPuzzle.random.nextInt(values().length)];
		}
		return r;
	}
}
