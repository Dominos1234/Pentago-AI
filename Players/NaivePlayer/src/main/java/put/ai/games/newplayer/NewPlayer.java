/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.newplayer;

import java.util.ArrayList;
import java.util.List;
import put.ai.games.game.TypicalBoard;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class NewPlayer extends Player {

    private Random random = new Random(0xdeadbeef);
    private int max = 0;
    private Move TheBestMoveEver;
	private int depth = 3;
    
    
    @Override
    public String getName() {
        return "NewPlayer 686554";
    }
    
    
    public ArrayList<Board> getSuccessors(Board b)
	{
    	ArrayList<Board> successors = new ArrayList<>();
    	Board tempBoard = b.clone();
    	for(Move m: tempBoard.getMovesFor(getColor())) {
			tempBoard.doMove(m);
			successors.add(tempBoard);
			tempBoard.undoMove(m);
    	}
    	return successors;
	}
    
    public int getUtility(Board b)
	{
		int utility = 0;
		int streak = 0;			//streak is used to add additional points for more than 2 in a row
								//2 in a row = 1 pt, 3 in a row = 2 pt, etc.
		
		//count horizontal doubles
		for(int i = 0; i < 6; i++)	//go down row
		{
			for(int j = 0; j < 5; j++)	//count doubles
			{
				if(b.getState(i, j) == getColor() && b.getState(i, j+1) == getColor())
					streak++;
				else {
					utility += streak*streak;
					streak = 0;
				}
			}
		}
		
		//count vertical doubles
		for(int i = 0; i < 6; i++)	//go down columns
		{
			for(int j = 0; j < 5; j++)	//count doubles
			{
				if(b.getState(j, i) == getColor() && b.getState(j+1, i) == getColor())
					streak++;
				else {
					utility += streak*streak;
					streak = 0;
				}
			}
		}
		
		//count main diagonal up-left to down-right
		for(int i = 0; i < 5; i++)
		{
			if(b.getState(i,i) == getColor() && b.getState(i+1, i+1) == getColor())
				streak++;
			else {
				utility += streak*streak;
				streak = 0;
			}
		}
		
		//count main diagonal up-right to down-left
		for(int i = 0; i < 5; i++)
		{
			if(b.getState(i,5-i) == getColor() && b.getState(i+1, 4-i) == getColor())
				streak++;
			else {
				utility += streak*streak;
				streak = 0;
			}
				
		}
		
		return utility;
	}

    
    public int bestMove(int depth, Board currentBoard, boolean maximizingPlayer)
	{
		int bestVal = 0;
		Move move = null;
		TheBestMoveEver = null;
		Board tempBoard = currentBoard.clone();
		
		if(depth == 0)
		{
			return getUtility(currentBoard);
		}
		
		
		
		if(maximizingPlayer)
		{
			
			bestVal = Integer.MIN_VALUE;
			ArrayList <Board> successors = getSuccessors(currentBoard);
			for(int i = 0; i < successors.size(); i++) {
				int thisVal = bestMove(depth - 1, successors.get(i), false);
				if(bestVal < thisVal) {
					bestVal = thisVal;
				}

			}
			
			System.out.println("depth: "+depth);
			System.out.println("this.depth: "+this.depth);
			System.out.println(bestVal +" "+ max);
			
			//if depth of one below parent is reached through recursion and the board has better utility than the previous best, make new board next move
			if(bestVal > max && depth == this.depth - 1)
			{
				System.out.println("kupa2");
				max = bestVal;
				TheBestMoveEver = move;
			}
			return bestVal;
		}
		else	//minimizing player
		{
			bestVal = Integer.MAX_VALUE;
			ArrayList <Board> successors = getSuccessors(currentBoard);
			for(int i = 0; i < successors.size(); i++) {
				int thisVal = bestMove(depth - 1, successors.get(i), true);
				if(bestVal > thisVal) 
					bestVal = thisVal;
			}
			return bestVal;
		}
	}
    
    public static void main(String[] args) {}
    

    @Override
    public Move nextMove(Board b) {
    	
    	bestMove(this.depth, b, false);
    	if(TheBestMoveEver==null) {
    		List<Move> moves = b.getMovesFor(getColor());
    		System.out.println("nie działa");
    		return moves.get(random.nextInt(moves.size()));
    	}
        return TheBestMoveEver;
    }
}
