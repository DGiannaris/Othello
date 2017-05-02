package othello;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The GUI of the project.
 */

class GPanel extends JPanel implements MouseListener {


	private static JLabel score_black,score_white;
	private static Othello board;
	private static Minimax computer;
	private static int counter=0,depth=0,PCbutton= 0,playerButton=0,score=0;
	private Move hint=null;
	private ArrayList<Move> hintmoves;

	private static boolean playerplayed=false ;
 
	public GPanel (Othello board, JLabel score_black, JLabel score_white) {

		
		super();
		this.board = board;
		this.score_black = score_black;
		this.score_white = score_white;
		addMouseListener(this);
		setBackground(Color.cyan);
		repaint();
		hintmoves= new ArrayList();

		//Asks for the turn
		askTurn();
		SetGameDepth();

		//
		computer = new Minimax(depth,PCbutton);

		if(PCbutton==1)
			pcturn();



	}
	
	//The depth the user,chooses
	public static void SetGameDepth()
	{
		String maxDepth = JOptionPane.showInputDialog(null, "Give desired depth : ");		

		try{
			depth= Integer.parseInt(maxDepth);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Number should be given.\nProgram will exit!");
			System.exit(0);
		}
		
		if(depth<=0)
		{
			JOptionPane.showMessageDialog(null, "Number should be >0.\nProgram will exit!");
			System.exit(0);
		}
		
		
		
		
		
	}


	
	public static void askTurn()
	{

		int dialogButton = JOptionPane.showConfirmDialog(null, "Would you like to play first?","Othello", JOptionPane.YES_NO_OPTION);		

		if(dialogButton == JOptionPane.NO_OPTION)
		{
			PCbutton=1;
			playerButton=-1;
		}
		else if (dialogButton == JOptionPane.YES_OPTION)
		{
			PCbutton=-1;
			playerButton=1;
		}
		else if (dialogButton== JOptionPane.CLOSED_OPTION)
		{
			System.exit(0);
		}
	}


	//When the AI plays.
	public void pcturn() {

		board.ComputeScor();
    	score_black.setText("Black : "+Integer.toString(board.getCounter(1)));
		score_white.setText("White : "+Integer.toString(board.getCounter(0)));
		
		
		if(board.isTerminal())
		{
			showWinner();

		}
		//If the AI doesn't have a move,the player plays again
		else if (board.possibleMoves(PCbutton).size()==0 && board.possibleMoves(playerButton).size()!=0) {
			JOptionPane.showMessageDialog(null, "Computer does not have any moves its your turn again!","Reversi",JOptionPane.INFORMATION_MESSAGE);
			counter++;
			
		}
		else
		{
			
			try {
				

			Move bestMove = computer.MiniMax(board);
			board.set(bestMove.getRow(), bestMove.getCol(), counter++);
			

			}
			catch(Exception e)
			{
				
			}
			
			if (board.isTerminal()) 
			{

				showWinner();
			}
			//If the player doesn't have a move the AI plays again
			else if (board.possibleMoves(playerButton).size()==0 && board.possibleMoves(PCbutton).size()!=0) {
				repaint();
				
				JOptionPane.showMessageDialog(null, "You do not have any moves its computer's turn again!","Reversi",JOptionPane.INFORMATION_MESSAGE);
				counter++;
				
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
					
						pcturn();
						repaint();
					}
				});


			}
			
		}	
	
		
		board.ComputeScor();
		score_black.setText("Black : "+Integer.toString(board.getCounter(1)));
		score_white.setText("White : "+Integer.toString(board.getCounter(0)));

	
	}
	
	
	


	//Calculates the pieces count
	public static int discDiff(int winner,int loser)
	{
		return board.getCounter(winner)-board.getCounter(loser);

	}

	//The game gets labeled corresponding to the piece difference between the players
	public  void  showWinner()
	{
		repaint();
		board.ComputeScor();
    	score_black.setText("Black : "+Integer.toString(board.getCounter(1)));
		score_white.setText("White : "+Integer.toString(board.getCounter(0)));
		
		
		if(board.getCounter(0)>board.getCounter(1))
		{
			score=discDiff(0,1);

			if(score>=1 && score<=10 )
				JOptionPane.showMessageDialog(null, "White wins by "+score+" discs"+"\nA CLOSE GAME");
			else if (score>10 && score <=24)
				JOptionPane.showMessageDialog(null, "White wins by "+score+" discs"+"\nA HOT GAME");
			else if (score>24 && score <=38)
				JOptionPane.showMessageDialog(null, "White wins by "+score+" discs"+"\nA FIGHT GAME");
			else if (score>38 && score <=52)
				JOptionPane.showMessageDialog(null, "White wins by "+score+" discs"+"\nA WALKAWAY GAME");
			else if (score>52 && score <=64)
				JOptionPane.showMessageDialog(null, "White wins by "+score+" discs"+"\nA PERFECT GAME");
		}
		else if(board.getCounter(0)<board.getCounter(1))
		{

			score=discDiff(1,0);
			if(score>=1 && score<=10 )
				JOptionPane.showMessageDialog(null, "Black wins by "+score+" discs"+"\nA CLOSE GAME");
			else if (score>10 && score <=24)
				JOptionPane.showMessageDialog(null, "Black wins by "+score+" discs"+"\nA HOT GAME");
			else if (score>24 && score <=38)
				JOptionPane.showMessageDialog(null, "Black wins by "+score+" discs"+"\nA FIGHT GAME");
			else if (score>38 && score <=52)
				JOptionPane.showMessageDialog(null, "Black wins by "+score+" discs"+"\nA WALKAWAY GAME");
			else if (score>52 && score <=64)
				JOptionPane.showMessageDialog(null, "Black wins by "+score+" discs"+"\nA PERFECT GAME");

		}
		else if(board.getCounter(0)==board.getCounter(1))
		{
			JOptionPane.showMessageDialog(null, "Draw");

		}	


	}

	
	//Clearing the board,for the game to start again
	public void clear() {
		counter=0;
		score_black.setText("Black : 2");
		score_white.setText("White : 2");

		askTurn();
		SetGameDepth();

		computer = new Minimax(depth,PCbutton);
		board.initializeGame();

		//AI's turn
		if(PCbutton==1)
			pcturn();
	}

	public void drawPanel(Graphics g) {
		//Drawing the rectangles of the board
		for (int i = 1 ; i < 8 ; i++) {
			g.drawLine(i * MYGUI.Square_Length, 0, i * MYGUI.Square_Length, MYGUI.Height);
		}
		g.drawLine(MYGUI.Width, 0, MYGUI.Width, MYGUI.Height);
		for (int i = 1 ; i < 8 ; i++) {
			g.drawLine(0, i * MYGUI.Square_Length, MYGUI.Width, i * MYGUI.Square_Length);
		}
		g.drawLine(0, MYGUI.Height, MYGUI.Width, MYGUI.Height);

		//The buttons
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++)
				switch (board.get(i,j)) {
				case -1:  
					g.setColor(Color.white);
					g.fillOval(1 + j * MYGUI.Square_Length, 1 + i * MYGUI.Square_Length,MYGUI.Square_Length-1, MYGUI.Square_Length-1);
					break;
				case 1:  
					g.setColor(Color.black);
					g.fillOval(1 + j * MYGUI.Square_Length, 1 + i * MYGUI.Square_Length, MYGUI.Square_Length-1, MYGUI.Square_Length-1);
					break;

				}

		while (hintmoves.size()!=0)
		{


			for(int i =0;i<hintmoves.size();i++)
			{

				g.setColor(Color.darkGray);
				g.drawOval(hintmoves.get(i).getCol() *MYGUI.Square_Length+3, hintmoves.get(i).getRow() *MYGUI.Square_Length+3, MYGUI.Square_Length-6, MYGUI.Square_Length-6);
				hintmoves.remove(i);  

			}

		}
	}

	public void setHint(int i,int j)
	{
		hint =new Move(i,j);
		hintmoves.add(hint);
	}

	public static int getcounter(){
		return counter;
	}
	
	public static int getPC(){
		return PCbutton;
	}
	
	public static void  setCounter(int count){
		counter=count;
	}
	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawPanel(g);

	}

	//Our width and height
	public Dimension getPreferredSize() {
		return new Dimension(MYGUI.Width,MYGUI.Height);
	}
  
  
	public void mouseClicked(MouseEvent e) {
		
		
		hint = null;
		
		//The clicked rectangle
		int i = e.getY()/MYGUI.Square_Length;
		int j =e.getX()/MYGUI.Square_Length;
		int player ;
		
		
		boolean flag=false;

		//To catch some bugs
           if(j==8)
              { j=7;}
           
           
		if (board.get(i,j) ==0)
		{
         
			//Which ones turn is it?
			if(counter%2==0)
				player=1 ;
			else
				player=-1;

			
			ArrayList<Point> points = board.possibleMoves(player);

			//If the one playing has moves
			if(!points.isEmpty()){
				for(Point move : points){
					if(move.getRow()==i && move.getCol()==j){
						flag=true ;
						break ;
					}
				}

				if(flag)
				{
					
					board.saveBoard(board);
	
					
					board.set(i, j, counter++);
					
					repaint();

					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {	
							Cursor savedCursor = getCursor();
							
							try {
								Thread.sleep(500);
								} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							pcturn();
						
							repaint();
							setCursor(savedCursor);		
						}
					});
					
					}
				//clicking a move that doent exist
				else 
				{

					JOptionPane.showMessageDialog(this, "You can not make this move!","Othello",JOptionPane.ERROR_MESSAGE);

				}
			}

		}
		//If the move is taken
		else 
		{ 
			JOptionPane.showMessageDialog(this, "There is already a coin here ","Othello",JOptionPane.ERROR_MESSAGE);

		}

		board.ComputeScor();

		score_black.setText("Black : "+Integer.toString(board.getCounter(1)));
		score_white.setText("White : "+Integer.toString(board.getCounter(0)));

	}




	private void paintImmediately(GPanel gPanel) {
		// TODO Auto-generated method stub

	}
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub


	}



}// class GPanel


public class MYGUI extends JFrame  implements ActionListener {

	JLabel score_black, score_white;
	JEditorPane editorPane;
	static JMenuItem hint,repaint;
	protected static final int Square_Length = 70; // length in pixel of a square 
	protected static final int  Width = 8 * Square_Length; // Width
	protected static final int  Height = 8 * Square_Length; // height

	protected Othello oth;
	protected static GPanel gpanel;

	public MYGUI(){
		//This will create the title you see in the upper left of the window   
		setTitle("Othello"); 
		drawFrame();

		score_white=new JLabel("White : 2"); // the game starts with 2 white pieces
		score_white.setForeground(Color.red);
		score_white.setFont(new Font("Dialog", Font.BOLD, 16));
		score_black=new JLabel("Black : 2"); // the game starts with 2 black pieces
		score_black.setForeground(Color.black);
		score_black.setFont(new Font("Dialog", Font.BOLD, 16));

		oth =new Othello();
		gpanel = new GPanel(oth, score_black, score_white);


		JPanel status = new JPanel();
		status.setLayout(new BorderLayout());

		status.add(score_white, BorderLayout.EAST);
		status.add(score_black, BorderLayout.WEST);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gpanel, status);
		splitPane.setOneTouchExpandable(false);
		getContentPane().add(splitPane);
		pack();

		
		setResizable(false);
		
		setVisible(true); 
		setLocationRelativeTo(null);

	}

	public void drawFrame() {

		//frame size
		setBounds(400,400 , 400, 400);
		//classic windows x,on the upper right corner
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Menu();

	}

	public void Menu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(buildMenu());
		menuBar.add(settingsMenu());

		setJMenuBar(menuBar);	
	}
	
	protected JMenu settingsMenu() {
		JMenu settings = new JMenu("Settings");
		JMenuItem undo = new JMenuItem("Undo");
		hint = new JMenuItem("Hint");

		
			// undo button
				undo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						if(!(oth.getCounter(1)==2 && oth.getCounter(0)==2))
						{ 	
							oth.Undo();
						
							if(gpanel.getPC()==1)
								gpanel.setCounter(1);
							else if (gpanel.getPC()==-1)
								gpanel.setCounter(0);
							
							score_black.setText("Black : "+Integer.toString(oth.getCounter(1)));
							score_white.setText("White : "+Integer.toString(oth.getCounter(0)));
							
							repaint();
						}
					}});

		
				//hint button
				hint.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						ArrayList<Point> moves;
						if (GPanel.getcounter()%2==0)
							moves=oth.possibleMoves(1);//white
						else
							moves=oth.possibleMoves(-1);//black


						for(int i=0;i<moves.size();i++)
						{

							gpanel.setHint(moves.get(i).getRow(), moves.get(i).getCol());
							repaint();
						}

					}
				});
				
				settings.add(hint);
				settings.addSeparator();
				settings.add(undo);
		
		
		
		return settings;
	}

	protected JMenu buildMenu() {
		JMenu game = new JMenu("Game");
		
		JMenuItem New = new JMenuItem("New Game");
		JMenuItem Depth = new JMenuItem("Set Game Depth");
		JMenuItem quit = new JMenuItem("Quit");
		

		
		
		
		// new button
		New.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gpanel.clear();
				repaint();
			}});

		//quit
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?","Othello", JOptionPane.YES_NO_OPTION);		

				if(dialogButton == JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}	


			}});

		// depth button
		Depth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.showConfirmDialog(null, "The game will start again with the desired depth.\nAre you sure you want to continue?","Othello", JOptionPane.YES_NO_OPTION);		

				if(dialogButton == JOptionPane.YES_OPTION)
				{
					gpanel.clear();
					repaint();
				}

			}});
		


		game.add(New);	
		game.addSeparator();
		game.add(Depth);
		game.addSeparator();
		game.add(quit);

	
		
		return game;

	}

	public void actionPerformed(ActionEvent arg0) { 	}


	public static void main (String []args){
		MYGUI tab = new MYGUI();
	}

}
