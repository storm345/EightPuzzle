package org.stormdev.eightpuzzle.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.stormdev.eightpuzzle.board.BoardState;
import org.stormdev.eightpuzzle.board.Direction;

public class EightPuzzle {
	public static Random random = new Random(); 
	public static BoardState current;
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args){
		current = new BoardState();
		current.shuffle();
		current.printOut();
		gameLoop();
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void gameLoop(){
		System.out.println("Commands: 'u' = up, 'd' = down, 'l' = left, 'r' = right, 's' = solve it!");
		while(!current.isComplete()){
			String in = getInput("Enter a command:");
			if(in.equalsIgnoreCase("u")){
				current.move(Direction.UP);
			}
			else if(in.equalsIgnoreCase("d")){
				current.move(Direction.DOWN);
			}
			else if(in.equalsIgnoreCase("l")){
				current.move(Direction.LEFT);
			}
			else if(in.equalsIgnoreCase("r")){
				current.move(Direction.RIGHT);
			}
			else if(in.equalsIgnoreCase("s")){
				System.out.println("Calculating solution...");
				List<Direction> moves = current.movesToSolve();
				if(moves.size() < 1){
					System.out.println("Unable to solve!");
					continue;
				}
				
				for(Direction d:moves){
					System.out.println(d.name());
					current.move(d);
					current.printOut();
					System.out.println("----------------");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				System.out.println("That took "+moves.size()+" steps!");
				break;
			}
			current.printOut();
		}
		System.out.println("You win!");
	}
	
	public static String getInput(String prompt){
		System.out.println(prompt);
		String in = "";
		try {
			in = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	
}
