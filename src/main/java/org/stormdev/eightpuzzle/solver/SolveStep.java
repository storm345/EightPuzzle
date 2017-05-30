package org.stormdev.eightpuzzle.solver;

import java.util.ArrayList;
import java.util.List;

import org.stormdev.eightpuzzle.board.BoardState;
import org.stormdev.eightpuzzle.board.Direction;

public class SolveStep implements Comparable<SolveStep> {
	private List<SolveStep> previous = new ArrayList<>();
	private int stepNumber;
	private BoardState stateOfBoard;
	private Direction appliedDirection = Direction.NONE; //Direction already applied in this step when we get the board state
	
	public SolveStep(BoardState bs){
		this.stepNumber = 1;
		this.stateOfBoard = bs;
	}
	
	public SolveStep(int stepNumber, List<SolveStep> previous, BoardState bs, Direction dirMoved){
		this.stepNumber = stepNumber;
		this.stateOfBoard = bs;
		this.previous = previous;
		this.appliedDirection = dirMoved;
	}
	
	public int getStepNumber(){
		return this.stepNumber;
	}
	
	public BoardState getBoardState(){
		return this.stateOfBoard;
	}
	
	public int countTilesOutOfOrder(){
		return getBoardState().countTilesOutOfOrder();
	}
	
	public List<SolveStep> getPreviousSteps(){
		return this.previous;
	}
	
	public Direction getAppliedDirection(){
		return this.appliedDirection;
	}
	
	public List<SolveStep> getNextSteps(){
		int sn = this.stepNumber + 1;
		List<SolveStep> prevs = new ArrayList<>(getPreviousSteps());
		prevs.add(this);
		List<SolveStep> res = new ArrayList<>();
		for(Direction d:Direction.values()){
			if(d.equals(Direction.NONE)){
				continue;
			}
			BoardState newState = getBoardState().stateIfMove(d);
			boolean alreadyHadThisStep = false;
			for(SolveStep prev:getPreviousSteps()){
				if(prev.getBoardState().isEqualTo(newState)){
					prev.getBoardState().printOut();
					newState.printOut();
					alreadyHadThisStep = true;
					break;
				}
			}
			if(!alreadyHadThisStep){
				res.add(new SolveStep(sn, new ArrayList<>(prevs), newState, d));
			}
		}
		return res;
	}

	@Override
	public int compareTo(SolveStep other) {
		int ourHeuristic = getStepNumber()+countTilesOutOfOrder();
		int otherHeuristic = other.getStepNumber() + other.countTilesOutOfOrder();
		if(ourHeuristic > otherHeuristic){
			return 1;
		}
		else if(ourHeuristic < otherHeuristic){
			return -1;
		}
		return 0;
	}
}
