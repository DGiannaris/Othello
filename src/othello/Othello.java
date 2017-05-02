package othello;


/**
 * Getting a frame of the game, and evaluating it
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


public class Othello {

	public  static final int white = -1;
	public static final int black = 1;
	public static final int empty = 0;
	private final static int numofdir=8;
	private int [][] board = new int[numofdir][numofdir];
	private int [][] undoBoard = new int[numofdir][numofdir];


	//for the interface
	static int [] counter = new int[2]; // 1 = black, 0 = white
	static int [] undoCounter = new int[2]; // 1 = black, 0 = white


	//Searching all the directions
	private  final int[][] directions = new int[][]
			{       {0, -1},	// North.
		{1, -1},	// Northeast.
		{1, 0},		// East.
		{1, 1},		// Southeast.
		{0, 1},		// South.
		{-1, 1},	// Southwest.
		{-1, 0},	// West.
		{-1, -1}};	// Northwest.



		//We need that for the undo button
		//We keep the last move and the player tha did it		
		private Move lastMove,undoLastMove;
		private int lastPlayerPlayed,undoLastPlayerPlayed;


		public Othello() {
			initializeGame();
		}


		//Creates a node with an already existing game frame
		//meaning that some moves have already been done
		public Othello(int [][]board1) {

			for(int i=0; i<numofdir; i++)
			{
				for(int j=0; j<numofdir; j++)
				{
					board[i][j] = board1[i][j];
				}
			}
		}

		//the same game frame as othello
		public Othello(Othello board1) {

			lastMove = board1.lastMove;
			lastPlayerPlayed = board1.lastPlayerPlayed;
			board = new int[8][8];
			for(int i=0; i<numofdir; i++)
			{
				for(int j=0; j<numofdir; j++)
				{
					board[i][j] = board1.board[i][j];
				}
			}

		}


		//Initialliazing vars and baord
		public void initializeGame() {


			lastMove = new Move();
			lastPlayerPlayed = white;

			for (int i = 0 ; i < numofdir ; i++)
				for (int j = 0 ; j < numofdir ; j++)
				{
					board[i][j]=empty;
					undoBoard[i][j]=empty;
				}

			board[3][4]=black;
			board[4][3]=black;
			board[3][3]=white;
			board[4][4]=white;


			undoBoard[3][4]=black;
			undoBoard[4][3]=black;
			undoBoard[3][3]=white;
			undoBoard[4][4]=white;



			counter[0] = 2;
			counter[1] = 2;
			undoCounter[0]=2;
			undoCounter[1]=2;


		}

		//it saves the board n the "undo board" before the next move happens
		public void saveBoard(Othello board)
		{
			//saving the score
			undoCounter[0]=counter[0];
			undoCounter[1]=counter[1];

			//saving the move
			undoLastMove = board.lastMove;

			//saving the last player
			undoLastPlayerPlayed=board.getLastPlayerPlayed();

			//saving the board
			for(int i=0; i<numofdir; i++)
			{
				for(int j=0; j<numofdir; j++)
				{
					undoBoard[i][j] = board.board[i][j];
				}
			}


		}

		public void Undo()
		{

			counter[1]=undoCounter[1];
			counter[0]=undoCounter[0];


			lastMove=undoLastMove;

			lastPlayerPlayed=undoLastPlayerPlayed;
			for(int i=0; i<numofdir; i++)
			{
				for(int j=0; j<numofdir; j++)
				{
					board[i][j] = undoBoard[i][j];
				}
			}


		}



		public int getCounter(int player) {
			return counter[player];
		}

		public void setCounter(int player,int scor) {
			counter[player]=scor;
		}

		public Move getLastMove()
		{
			return lastMove;
		}

		public int getLastPlayerPlayed()
		{
			return lastPlayerPlayed;
		}



		public void setLastMove(Move lastMove)
		{
			this.lastMove.setRow(lastMove.getRow());
			this.lastMove.setCol(lastMove.getCol());
			this.lastMove.setValue(lastMove.getValue());
		}

		public void setLastLetterPlayed(int lastPlayerPlayed)
		{
			this.lastPlayerPlayed = lastPlayerPlayed;
		}




		//we need this to give values from the interface to the othello
		public int get(int i, int j){
			return board[i][j];
		}


		//I tells us which pieces have to change.
		private void findFlips(int x,int y, int player){


			int x1,y2 ;


			boolean search ;

			//The pieces that will change,flip
			ArrayList<Point> flips= new ArrayList<Point>();


			for(int k=0;k<directions.length;k++){

				x1=x+this.directions[k][0];
				y2=y+this.directions[k][1];

				//If the neighbour exists *it may also be out of board
				if(isValidMove(x1,y2)){


					//if the neighbour is the other player we may need to flip
					//so we need to search in the opposing direction, to confirm
					if(board[x1][y2]==-player){

						search = true ;
						flips.add(new Point(x1,y2,-player));

						while(search){

							//in the same direction
							x1+=this.directions[k][0];
							y2+=this.directions[k][1];

							if(this.isValidMove(x1, y2)){

								//if it;s the opponent's
								if(board[x1][y2]==-player){
									//adding to the flips array , and continue searching the same direction
									flips.add(new Point(x1,y2,-player));
								}else if(board[x1][y2]==player){

									flip(flips);
									flips.clear();
									//stop searching because, we found our piece in the end of the other direction, which means
									//its not a move the opponent did to flip my pieces
									search=false ;
								}
								else if (board[x1][y2]==0)
								{
									search = false ;
									flips.clear();
								}

							}
							else
							{//out of board, so no need to flip
								search = false ;
								flips.clear();

							}
						}
					}

				}

			}

		}

		//does the move flip indicates
		private void flip(ArrayList<Point> flips){

			for(Point point : flips){

				board[point.getRow()][point.getCol()]=-point.getPlayer();//bazw to anapodo
			}

		}


		//for the interface to give values
		public void set(int i,int j,int counter){

			if (counter%2==0){
				board[i][j]=black;
				findFlips(i,j,black);
				this.setLastLetterPlayed(black);

			}
			else{

				board[i][j]=white;
				findFlips(i,j,white);
				this.setLastLetterPlayed(white);
			}

			this.setLastMove(new Move(i,j,this.evaluate()));
		}




		//Does a move
		private void makeMove(int row, int col, int player){

			board[row][col]=player;
			findFlips(row,col,player);
			lastMove = new Move(row, col);
			lastPlayerPlayed = player;

		}



		//It decides if a move is lawful
		public boolean isValidMove(int x,int y){

			if(x>=0 && x<numofdir && y>=0 && y<numofdir)
				return true;

			return false; 
		}


		//Finds all the possible moves from the node
		public ArrayList<Point> possibleMoves(int player){

			int x,y,tmpx,tmpy ;
			ArrayList<Point> points = new ArrayList<Point>() ;

			boolean search ;


			for(int x1=0;x1<numofdir;x1++){
				for(int y2=0;y2<numofdir;y2++){

					//if the move is not empty, its already played
					if(board[x1][y2]==0){

						//checking for all directions if the neighbouring pieces are of opposing color
						for(int k=0;k<directions.length;k++){

							x=x1+this.directions[k][0];
							y=y2+this.directions[k][1];

							//if the neighbour exists
							if(isValidMove(x,y)){



								//For the value t be valid it should exist one of my pieces, in the other side
								//to be flipped
								//so we search in the same direction

								if(board[x][y]==-player){


									search=true ;

									tmpx=x ;
									tmpy=y;

									while(search){

										//searching the same diretion, for my pieces
										tmpx+=directions[k][0];
										tmpy+=directions[k][1];

										//check if the box is valid
										if(isValidMove(tmpx,tmpy)){


											//if I find the same color piece as mine
											if(board[tmpx][tmpy]==player){

												//save the move
												points.add(new Point(x1,y2,player));
												//stop the search
												search=false;


												k=directions.length ;
											}

											else if(board[tmpx][tmpy]==empty){
												search = false ;
											}
										}
										//if the box/move is not valid or out of board I stop the search
										else
											search=false ;

									}//while(search)

								}//if(board[x][y]==-player)

							}//if(isValidMove(x,y)){

						}//for(int k=0;k<directions.length;k++){

					}//if(board[x1][y2]==0){

				}//for
			}//for

			return points;
		}


		//Children are the possible moves
		public Collection<Othello> getChildren(int player) {



			//Getting all the possible moves at this moment
			ArrayList<Point> possiblePoints= this.possibleMoves(player);

			//list with the node's children
			ArrayList<Othello> children = new ArrayList<Othello>();

			//for every ossible move
			for(Point point : possiblePoints){


				//create a child that represents this specific move
				Othello child = new Othello(this.board);


				child.makeMove(point.getRow(),point.getCol(),point.getPlayer());

				//add the child to the list
				children.add(child);		
			}
			return children;
		}

		public boolean isTerminal() {

			//if in the node there are no moves for the white or theblack that means the node is the final node
			if(this.possibleMoves(1).size()==0 && this.possibleMoves(-1).size()==0){
				//System.out.println("EIMAI EDW STON TERMATIKO KOMVO ");
				return true;
			}

			return false;
		}

		public void ComputeScor(){
			int sumblack=0,sumwhite=0;

			//after the move , we get to see the score
			for (int a = 0 ; a < numofdir ; a++)
				for (int b = 0 ; b < numofdir ; b++)
				{
					{    //white
						if (board[a][b] == -1)
						{    
							sumwhite++;


						}//black
						if (board[a][b] == 1)
						{   
							sumblack++;


						} 

					}
				}

			//setting the score after every move
			setCounter(0,sumwhite);
			setCounter(1,sumblack);


		}

		public int evaluate() {


			int antipalos=0,mobility=0,blackCornerSum=0,whiteCornerSum=0,corners=0
					,coins=0,potMobBlack=0,potMobWhite=0,potentialMobilityValue=0,sum=0
					,whiteregion=0,blackregion=0,RegionValue=0,Block=0;

			//Who has the whites and who the blacks
			antipalos=-lastPlayerPlayed;


			//how many ieces does each have?
			coins = 100 *(counter[1] - counter[0] ) / (counter[1] + counter[0]) ;


			ArrayList<Point> blackmoves=possibleMoves(black);
			ArrayList<Point> whitemoves=possibleMoves(white);


			//Calculating mobility
			if ( blackmoves.size()!=0 &&  whitemoves.size()!=0 )
				mobility =100 * (blackmoves.size() - whitemoves.size()) / (blackmoves.size() + whitemoves.size());



			//potential mobility
			potMobBlack=computeEmptySpaces(white);
			potMobWhite=computeEmptySpaces(black);

			if(potMobBlack+ potMobWhite !=0)
				potentialMobilityValue=100 * (potMobBlack - potMobWhite) / (potMobBlack + potMobWhite);


			//
			//Checking the corners
			if (board[0][0]==-1)
				whiteCornerSum++;
			else if (board[0][0]==1)
				blackCornerSum++;

			if (board[0][7]==-1)
				whiteCornerSum++;
			else if(board[0][7]==1)
				blackCornerSum++;

			if (board[7][0]==-1)
				whiteCornerSum++;
			else if (board[7][0]==1)
				blackCornerSum++;

			if (board[7][7]==-1)
				whiteCornerSum++;
			else if (board[7][7]==1)
				blackCornerSum++;


			//How may corners does each have?
			if(blackCornerSum + whiteCornerSum !=0)
				corners =100 * (blackCornerSum - whiteCornerSum) / (blackCornerSum + whiteCornerSum);




			//the boxes before the corners are evaluated as negative
			if(board[0][1]== -1 || board[1][0]==-1 || board[1][1]==-1){
				if(this.board[0][0]!=-1)
					blackregion++;
			}
			else if(board[0][1]== 1 || board[1][0]==1 || board[1][1]==1){
				if(this.board[0][0]!=1)
					whiteregion++;
			}
			if(board[0][6]==-1 || board[1][6]==-1 || board[1][7]==-1){
				if(this.board[0][7]!=-1)
					blackregion++;	
			}
			else if(board[0][6]==1 || board[1][6]==1 || board[1][7]==1){
				if(this.board[0][7]!=1)
					whiteregion++;
			}
			if(board[6][0]==-1 || board[6][1]==-1 || board[7][1]==-1){
				if(this.board[7][0]!=-1)
					blackregion++;
			}
			else if(board[6][0]==1 || board[6][1]==1 || board[7][1]==1){
				if(this.board[7][0]!=1)
					whiteregion++;
			}
			if(board[6][7]==-1 || board[6][6]==-1 || board[7][6]==-1){
				if(this.board[7][7]!=-1)
					blackregion++;	
			}
			else if(board[6][7]==1 || board[6][6]==1 || board[7][6]==1){
				if(this.board[7][7]!=1)
					whiteregion++;
			}


			//when the blacks have a corner, the value increases so that its  good move for whites

			if(blackregion+whiteregion !=0)
				RegionValue=100*(blackregion-whiteregion)/(blackregion+ whiteregion);



			// Blocking
			ArrayList<Point> Blockingmove=possibleMoves(antipalos);

			if(Blockingmove.size()==0){ 
				if(antipalos==this.black)
					Block=-100;
				else 
					Block =100 ;
			}



			//Total evaluate
			sum=10 * potentialMobilityValue+ 10* coins+ 10 * Block  + 20* RegionValue +
					20 * mobility+ 50* corners;


			return  sum;



		}

		//how many empty boxes exist next to the opponent
		private  int computeEmptySpaces(int antipalos){
			int x,y ;
			ArrayList<Point> emptySpaces = new ArrayList<Point>() ;
			Point point;

			//for every box in the board
			for(int x1=0;x1<numofdir;x1++){
				for(int y2=0;y2<numofdir;y2++){


					if(board[x1][y2]==antipalos){

						//chek all the directions, and if there is an empty box
						for(int k=0;k<directions.length;k++){

							x=x1+this.directions[k][0];
							y=y2+this.directions[k][1];
							//ama einai keni


							if(isValidMove(x,y)){

								if(board[x][y]==0 )
								{

									point= new Point(x,y);


									boolean flag=false ;

									//check if this point exist in the list
									for(Point points : emptySpaces){
										if(point.isEqual(points)){
											flag=true ;
											break ;
										}
									}



									if(!flag)
										emptySpaces.add(point);


								}//if


							}//if valid
						}//for

					}//if

				}//for
			}//for

			return emptySpaces.size();
		}





		//returns which pieces should be flipped 
		//it doesnt flip them
		private ArrayList<Point> findFlips2(int x,int y,int player){


			int x1,y2 ;


			boolean search ;

			//contains the pieces tha wil be flipped
			ArrayList<Point> flips= new ArrayList<Point>();


			for(int k=0;k<directions.length;k++){

				x1=x+this.directions[k][0];
				y2=y+this.directions[k][1];


				if(isValidMove(x1,y2)){

					//if the neigbour is the opponent then it may be a flip case
					//so we need to check the opposing direction,to confirm
					if(board[x1][y2]==-player){

						search = true ;
						flips.add(new Point(x1,y2,-player));

						while(search){


							x1+=this.directions[k][0];
							y2+=this.directions[k][1];

							if(this.isValidMove(x1, y2)){

								//if its the opponents
								if(board[x1][y2]==-player){
									//add to flips and continue searching
									flips.add(new Point(x1,y2,-player));
								}else if(board[x1][y2]==player){

									return flips;
								}

							}else{//out of board
								search = false ;
								flips.clear();

							}
						}
					}

				}

			}
			return flips ;
		}



}



