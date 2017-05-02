package othello;


/**
 * This Class represents a point in the game board.
 *
 */
public class Point {

	
	private int row ;
	private int col;
	
	//Who played in this point
	private int player ;

	
	public Point(int row, int col){
		
		this.row=row;
		this.col=col ;
		this.player=player;
		
	}
	public Point(int row,int col,int player){
		
		this.row=row;
		this.col=col ;
		this.player=player;
	}


	
	
	public boolean isEqual(Point point){
		
		if(this.getRow()==point.getRow() && this.getCol()==point.getCol())
			return true ;
		
		return false ;
	}
	
	public boolean isEqualplayer(Point point){
		
		if(isEqual(point)){
			if(this.getPlayer()==point.player)
				return true ;
		}
		
		return false ;
	}
	
	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getCol() {
		return col;
	}


	public void setCol(int col) {
		this.col = col;
	}


	public int getPlayer() {
		return player;
	}


	public void setPlayer(int player) {
		this.player = player;
	}
	
	

}

