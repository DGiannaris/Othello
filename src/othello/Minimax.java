package othello;

import java.util.ArrayList;
import java.util.Random;


/**
 * Implements Minimax search and alpha-beta pruning
 * This way it calculates the it's next move 
 */
public class Minimax{
	
	
    //The depth of the tree of possible moves, given by the user
	private int maxDepth;
	
	//Shows the player that is about to play (White/Black)
	private int player;
	
	
	
	public Minimax(int maxDepth, int player){	
		this.maxDepth = maxDepth;
		this.player = player;
	}

	
	
    //Starts the minimax algorithm
	public Move MiniMax(Othello board){
		
		//Initiallizing alpha & beta 
		int alpha = Integer.MIN_VALUE ;
		int beta = Integer.MAX_VALUE ;
		
        //Black always play first,so they are the max 
        if (player == Othello.black){
    	
        	//Max with a-b pruning
            return  max(new Othello(board), maxDepth,alpha , beta);     
        }
        
        //For the white player
        else{
        	//Min with a-b pruning
             return min(new Othello(board), maxDepth, alpha ,  beta);   
        }
        
	}

	
    
	public Move max(Othello board , int depth, int alpha , int beta){
       
		//If two moves have the same evaluation, pick randomly
		Random r = new Random() ;
		
		
		//If it reaches a final node ,in the evaluation tree, or reach the max depth that the user specified
		//the deth reduces as we go deeper
		if(board.isTerminal() || (depth == 0)){
			
			//lastmove shows the move on this node
			Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
			
			//the move and its corresponding evaluate
			return lastMove;
		}
		
		
        //Node's children
		ArrayList<Othello> children = new ArrayList<Othello>(board.getChildren(Othello.black));
		
		
		//To get the best move everytime
		Move maxMove = new Move(Integer.MIN_VALUE);
		
		
		//If there are no children in this move, then the calculation will be done only for  the node.
		if(children.size()==0)
			children.add(board);
			
		
		for (Othello child : children){
			
			//get the next min move
			Move move = min(child, depth-1 , alpha , beta);
						
			
			if(move.getValue() >= maxMove.getValue()){
				
                if ((move.getValue() == maxMove.getValue())){
                
                	//if we get the same values we pick one randomly
                    if (r.nextInt(2) == 0){
                        maxMove.setRow(child.getLastMove().getRow());
                        maxMove.setCol(child.getLastMove().getCol());
                        maxMove.setValue(move.getValue());
                    }
                }
                else{
                    
                	maxMove.setRow(child.getLastMove().getRow());
                    maxMove.setCol(child.getLastMove().getCol());
                    maxMove.setValue(move.getValue());
                }
			}
			
			
			
			//if the value we get is greater than alpha we should "refresh" its value, since alpha has 
			//the best possible evaluation of the max node
			if(move.getValue() > alpha ){
				alpha = move.getValue() ;
				
			}
			
			
			//if the value the node has >= beta 
			//we dont need to check the rest of the nde children,since we will get beta from the previous min
			//so we do pruning
			if(move.getValue() >= beta ){
			    children=null;
				//ara epistrefetai h move pou exw brei mexri twra
				return move ;
			}
			 
		}
		
		
		//if we dont rune, we get the value from max
		return maxMove ;
		
		
		
	}

    //similar to max
	public Move min(Othello board ,  int depth , int alpha , int beta){
       
		Random r = new Random() ;
		
       
		if(board.isTerminal() || (depth == 0)){
			
			Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
			return lastMove;

		}
		
		
		ArrayList<Othello> children = (ArrayList<Othello>) board.getChildren(Othello.white);
		
		
		//we need the min move so initially we get the biggest possible value
		Move minMove = new Move(Integer.MAX_VALUE);
			
		if(children.size()==0)
			children.add(board);
		
			
		for (Othello child : children){
		
			
			Move move= max(child, depth-1, alpha , beta) ;
			
			//if we find a betetr move
			if(move.getValue() <= minMove.getValue()){
				
                if ((move.getValue() == minMove.getValue())){
                	
                    if (r.nextInt(2) == 0){
                        minMove.setRow(child.getLastMove().getRow());
                        minMove.setCol(child.getLastMove().getCol());
                        minMove.setValue(move.getValue());
                    }
                }
                else{
                        minMove.setRow(child.getLastMove().getRow());
                        minMove.setCol(child.getLastMove().getCol());
                        minMove.setValue(move.getValue());
                }
            }
			
			
			
			if(move.getValue() < beta ){	
				beta = move.getValue() ;
			}
			
			
			//if the value is <= alpha then the above max will do the alpha move for sure,or better.
			//Min though, has something <= alpha so we don't need to check the other children of the node
			if( move.getValue() <= alpha ){	
			    children=null;

				return move ;
			}
			
		}
		
		
		return minMove;
   
	
	}
}
