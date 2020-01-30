package put.ai.games.newplayer;

import java.util.*;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class NewPlayer extends Player {

    private Integer max = 0;
    private Move TheBestMoveEver;
    private int depth = 2;
    private long time;

    @Override
    public String getName() {
        return "";
    }
    
    public int bestMove(int depth, Board parent_board, boolean maximizingPlayer)
    {
        int bestVal = 0;
        Move move = null;
        if(depth == 0) {
            return getUtility(parent_board);
        }

        
        Board tempBoard = parent_board.clone();
        List<Move> moves = tempBoard.getMovesFor(getColor());
        
        for(Move m: moves){
        	tempBoard.doMove(m);
            if (tempBoard.getWinner(getColor()) == getColor()){
            	TheBestMoveEver = m;
                return Integer.MAX_VALUE;
            }
            tempBoard.undoMove(m);
        }


        if(maximizingPlayer){ // max
            bestVal = Integer.MIN_VALUE;
            for(Move m : moves) {
            	tempBoard.doMove(m);
                int thisVal = bestMove(depth - 1, tempBoard, false);
                tempBoard.undoMove(m);
                if(bestVal < thisVal) {
                    bestVal = thisVal;
                    move = m;
                    }
                }
            }
            if(bestVal > max && depth == this.depth - 1) {
                max = bestVal;
                TheBestMoveEver = move;
            }

        else{ // min
            bestVal = Integer.MAX_VALUE;
            for(Move m : moves){
            	tempBoard.doMove(m);
                int thisVal = bestMove(depth - 1, tempBoard , true);
                tempBoard.undoMove(m);
                if(bestVal > thisVal){
                    bestVal = thisVal;
                }
            }
        }

        return bestVal;
    }


    public int getUtility(Board board) {
        Integer size = board.getSize();
        int utility = 0;
        int streak = 0;

        if (board.getWinner(this.getColor()) == this.getColor()){
            return Integer.MAX_VALUE;
        }


        for(int i = 0; i < size; i++){
            for(int j = 0; j < size-1; j++){
                if(board.getState(i,j) == this.getColor() && board.getState(i,j+1) == this.getColor()){
                    utility += streak + 1;
                    streak++;
                }
                else streak = 0;
            }
        }
        streak = 0;


        for(int i = 0; i < size; i++){
            for(int j = 0; j < size-1; j++){
                if(board.getState(j, i) == this.getColor() && board.getState(j+1, i) == this.getColor()) {
                    utility += streak + 1;
                    streak++;
                }
                else streak = 0;
            }
        }
        streak = 0;


        for(int i = 0; i < size-1; i++) {
            if(board.getState(i,i) == this.getColor() && board.getState(i+1, i+1) == this.getColor()) {
                utility += streak + 1;
                streak++;
            }
            else streak = 0;
        }
        streak = 0;

        for(int i = 0; i < size-1; i++) {
            if(board.getState(i, size-1-i) == this.getColor() && board.getState(i+1,size-2-i) == this.getColor()) {
                utility += streak + 1;
                streak++;
            }
            else
                streak = 0;
        }

        return utility;
    }
    
    
    public static void main(String[] args) {}

    @Override
    public Move nextMove(Board b) {
        Board clone = b.clone();
        time = System.currentTimeMillis();
        if(b.getMovesFor(getColor()).size()<200)
        	this.depth=3;
        if(b.getMovesFor(getColor()).size()<140)
        	this.depth=4;
        if(b.getMovesFor(getColor()).size()==280) {
        	TheBestMoveEver = b.getMovesFor(getColor()).get(58);
        	return TheBestMoveEver;
        }
        TheBestMoveEver = null;
        max = 0;
        bestMove(this.depth, clone, false);
        System.out.println(TheBestMoveEver);
        clone.doMove(TheBestMoveEver);
        //System.out.println(getUtility(clone));;
        return TheBestMoveEver;
    }
}
