//Name: Deval Patel
//Date: Jan 17, 2017
//Purpose: Code 2048-Monkey Style
/*
Extra: 
Change pictures of each number money so its clearer to see

Problems: 
KeyListener doesn't work

 */
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Random;
import java.lang.Object;
import java.util.Vector;

public class FinalGame extends Applet implements ActionListener, KeyListener
{
	//sound file
	AudioClip soundFile;
	Random rand = new Random();
	//Color codes for everything
	Color bg = Color.decode("#FFFF66");
	Color bc = Color.decode("#FFCC00");
	Color c1 = Color.decode("#FF9900");
	Color c2 = Color.decode("#FF0000");
	Font title = new Font("Papyrus", Font.BOLD, 26);
	Font text = new Font ("Garamond", Font.BOLD, 16);
	Panel p_card;  //to hold all of the screens
	Panel card1, card2, card3, card4, card5; //the five screens
	CardLayout cdLayout = new CardLayout ();
	//global JLabels
	JLabel scoreTitle; 
	JLabel highScoreTitle;
	JLabel timerCount;
	JLabel prompt;
	JLabel result;
	JLabel resultpic;
	
	//To list top 5 high scores
	JLabel hsName [] = new JLabel[5];
	JLabel hsScore [] = new JLabel[5];


	//Name 
	String name = new String(); 
	//score 
	int score = 0;
	//elapsed time
	static int time = 0;
	//handles counting time per second
	Thread timecount = new Thread ()
	{
		public void run ()
		{
			while (true)
			{
				try
				{
					Thread.sleep (1000);
				}
				catch (Exception e)
				{
				}
				time++;
				//Calculates minutes and seconds and displays it
				if (time % 60 < 10)
					timerCount.setText ("Elapsed Time: "+ time / 60 + ":0" + time % 60);
				else
					timerCount.setText ("Elapsed Time: "+ time / 60 + ":" + time % 60);

			}
		}
	}


	;

	//grid
	int row = 4;
	int col = 4;
	JButton a[] = new JButton [row * col];
	int b[] [] = {{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}};
    String hs [][] = {{" "," "," "," "," "},{"0","0","0","0","0"}};  

	public void init ()
	{
		//Makes keyboard usable!
		addKeyListener (this);
		openHS ("highscore.txt");               
		//soundFile = getAudioClip (getDocumentBase (), "mnh.wav");
		//soundFile.loop ();
		p_card = new Panel ();
		p_card.setLayout (cdLayout);
		screen1 ();
		screen2 ();
		screen3 ();
		screen4 ();
		screen5 ();
		resize (625, 625);
		setLayout (new BorderLayout ());
		add ("Center", p_card);
	}

	public void screen1 ()
	{ //screen 1 is set up. Splash Screen!
		card1 = new Panel ();
		card1.setBackground (bg);       
		JButton next = new JButton ();
		next.setIcon(createImageIcon ("splash.jpg"));
		next.setActionCommand ("s2");
		next.addActionListener (this);
		card1.add (next);
		p_card.add ("1", card1);
	}

	public void screen2 ()
	{ //screen 2 is set up. Instructions Screen!
		card2 = new Panel ();
		card2.setBackground (bg);
		JLabel ins = new JLabel ();
		ins.setIcon(createImageIcon ("ins2.jpg"));
		JScrollPane scroll = new JScrollPane (ins);
		scroll.setPreferredSize(new Dimension (500,550));
		scroll.setBackground(bg);
		JButton next = new JButton ("Play Now!");
		next.setActionCommand ("s3");
		next.setFont (text);
		next.setBackground(c1);
		next.addActionListener (this); 
		next.setPreferredSize(new Dimension (500, 50));
		card2.add (scroll);
		card2.add (next);
		p_card.add ("2", card2);
	}

	public void screen3 ()
	{ //screen 3 is set up. Game Screen!
		timecount.start();
		card3 = new Panel ();
		card3.setBackground (bg);
		//title
		JLabel title = new JLabel ();
		//title.setIcon(createImageIcon ("title.jpg"));
		title.setText("2048");
		//Add to North Panel
		Panel north = new Panel ();
		north.add (title);

		//Score
	    scoreTitle = new JLabel (name+"Score:"+score);
		scoreTitle.setForeground(c2);
		scoreTitle.setFont(text);

		//Panel for Score
		Panel s = new Panel ();
		s.add(scoreTitle);

		//HighScore
		highScoreTitle = new JLabel ("High Score: " + hs[1][0]);
		highScoreTitle.setForeground(c2);
		highScoreTitle.setFont (text);

		//Panel for HighScore
		Panel hs = new Panel ();
		hs.add(highScoreTitle);

		//Timer 
		timerCount = new JLabel ("Elapsed Time: "+time / 60 + ":0" + time % 60);
		timerCount.setFont (text);
		timerCount.setForeground(c2);
		Panel tt = new Panel ();
		tt.add(timerCount);

		//Ins Prompt
		prompt = new JLabel ("Slide the numbers until 2048!",SwingConstants.CENTER);
		prompt.setForeground(c2);
		prompt.setBackground(bg);
		prompt.setFont (text);

		//Panel for all score, high score, prompt
		Panel labels = new Panel (new GridLayout (4,1));
		labels.add(s);
		labels.add(hs);
		labels.add(tt);
		labels.add(prompt);

		//Panel to hold the Labels GridLayout
		Panel e = new Panel (new GridLayout (2,1));
		e.add(labels);
		//e.setPreferredSize(new Dimension (200,400));

		//Instructions Button
		JButton ins = new JButton ("Instructions");
		ins.setActionCommand("toIns");
		ins.addActionListener(this);
		ins.setBackground(c1);
		ins.setFont(text);

		//Reset button
		JButton reset = new JButton ("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		reset.setBackground(c1);
		reset.setFont(text);

		//Open Button
		JButton open = new JButton ("Open");
		open.setActionCommand("open");
		open.setFont(text);
		open.setBackground(c1);
		open.addActionListener(this);

		//Save Button
		JButton save = new JButton ("Save");
		save.setActionCommand("save");
		save.setFont(text);
		save.setBackground(c1);
		save.addActionListener(this);
		
		//HighScore Button
		JButton toScores = new JButton ("High Scores");
		toScores.setActionCommand("toHS");
		toScores.setFont(text);
		toScores.setBackground(c1);
		toScores.addActionListener(this);

		//panel for buttons 
		Panel buttons = new Panel (new GridLayout (5,1));
		buttons.add(ins);
		buttons.add(reset);
		buttons.add(open);
		buttons.add(save);
		buttons.add(toScores);

		//Add buttons to east
		e.add(buttons);
		//Arrow Direction keys to determine slide direction
		//up
		JButton up = new JButton ("Up");
		up.setActionCommand("up");
		up.setBackground(c1);
		up.setFont(text);
		up.addActionListener(this);

		//down
		JButton down = new JButton ("Down");
		down.setActionCommand("down");
		down.setBackground(c1);
		down.setFont(text);
		down.addActionListener(this);

		//right
		JButton right = new JButton ("Right");
		right.setBackground(c1);
		right.setFont(text);
		right.setActionCommand("right");
		right.addActionListener(this);

		//left
		JButton left = new JButton ("Left");
		left.setBackground(c1);
		left.setFont(text);
		left.setActionCommand("left");
		left.addActionListener(this);

		//5 blank buttons
		JButton b1 = new JButton ("");
		b1.setEnabled(false);
		b1.setBackground(bg);
		b1.setBorderPainted(false);

		JButton b2 = new JButton ("");
		b2.setEnabled(false);
		b2.setBackground(bg);
		b2.setBorderPainted(false);

		JButton b3 = new JButton ("");
		b3.setEnabled(false);
		b3.setBackground(bg);
		b3.setBorderPainted(false);

		JButton b4 = new JButton ("");
		b4.setEnabled(false);
		b4.setBackground(bg);
		b4.setBorderPainted(false);

		JButton b5 = new JButton ("");
		b5.setEnabled(false);
		b5.setBackground(bg);
		b5.setBorderPainted(false);

		//Panel for directional arrow keys
		Panel arrows = new Panel (new GridLayout (3,3));
		arrows.add(b1);
		arrows.add(up);
		arrows.add(b2);
		arrows.add(left);
		arrows.add(b3);
		arrows.add(right);
		arrows.add(b4);
		arrows.add(down);
		arrows.add(b5);

		//Add arrows to south
		Panel south = new Panel ();
		south.add(arrows);

		//Set up grid
		Panel grid = new Panel (new GridLayout (row, col));
		int move = 0;
		for (int i = 0 ; i < row ; i++)
		{
			for (int j = 0 ; j < col ; j++)
			{ 
				a [move] = new JButton (createImageIcon (b [i] [j] + ".png"));
				a [move].setBackground(bc);
				a [move].setPreferredSize (new Dimension (100, 100));
				grid.add (a [move]);
				move++;
			}
		}

		//add grid to Center Panel
		Panel center = new Panel ();
		center.add(grid);

		//make East Panel to hold everything in e (GridLayout)
		Panel east = new Panel ();
		east.add(e);

		//Make the border layout to hold all of the panels
		Panel screen = new Panel(new BorderLayout ());
		screen.add("North", north);
		screen.add("West", center);
		screen.add ("East", east);
		screen.add ("South", south);

		//add to card
		card3.add (screen);     
		p_card.add ("3", card3);
	}


	public void screen4 ()
	{ //screen 4 is set up. Win/Lose Screen!
		card4 = new Panel ();
		card4.setBackground (bg);
		result = new JLabel ("You Lose!",SwingConstants.CENTER);
		result.setFont (title);
		resultpic = new JLabel ("",SwingConstants.CENTER);
		resultpic.setIcon(createImageIcon ("lose.jpg"));
		resultpic.setBackground(bc);
		resultpic.setPreferredSize(new Dimension (300,300));
		JButton playAgain = new JButton ("Play Again!");
		playAgain.setActionCommand ("toGame");
		playAgain.addActionListener (this);
		playAgain.setBackground (bc);
		Panel p = new Panel (new BorderLayout ());
		p.add("North", result);
		p.add("Center", resultpic);
		p.add("South", playAgain);
		card4.add (p);
		p_card.add ("4", card4);
	}
	
	public void screen5 () {
		//screen 5 is set up. High Score Screen!
		card5 = new Panel ();
		card5.setBackground(bg);
		JLabel heading = new JLabel ("High Scores!", SwingConstants.CENTER);
		heading.setFont(new Font("Papyrus", Font.BOLD, 90));
		heading.setForeground(c2);
		heading.setSize(625,150);
		//button for game 
		JButton game = new JButton ("Play Again!");
		game.setActionCommand ("toGame2");
		game.addActionListener (this);
		game.setBackground (bc);
		game.setMaximumSize(new Dimension (300,100));
		
		
		Panel n2 = new Panel (new GridLayout (5,1));
		//set all 5 number and name labels
		for (int i = 0; i<hsName.length;i++) {
			hsName[i]= new JLabel ((i+1)+".   "+hs[0][i]);
			hsName[i].setFont(new Font ("Garamond", Font.BOLD, 72));
			hsName[i].setForeground(c2);
			n2.add(hsName[i]);
		}
		
		Panel n3 = new Panel (new GridLayout (5,1));
		//set all 5 score labels
		for (int i = 0; i<hsName.length;i++) {
			hsScore[i]= new JLabel (hs[1][i]);
			hsScore[i].setFont(new Font ("Garamond", Font.BOLD, 72));
			hsScore[i].setForeground(c2);
			n3.add(hsScore[i]);
		}
		
		Panel scrn = new Panel (new BorderLayout ());
		scrn.add ("North", heading);
		scrn.add("Center", n2);         
		scrn.add("East", n3);
		scrn.add("South", game);
		card5.add(scrn);
		p_card.add("5", card5);
	}

	//Slide directions 

	//Left
	public void left () {
		//add a swap variable so that once one combine happens, the same number which just got combined cannot be combined again
		int swap = 0;
		//loop through the array
		for (int i = 0; i<row; i++) {
			for (int j=col-1;j>0;j--) {
				//if the element to the right of the i'th j-1'th element is 0, shuffle left 
				if (b[i][j-1]== 0 && b[i][j]!=0) {
					b[i][j-1] = b[i][j];
					b[i][j]=0;
				}
				//if the two adjacent numbers are the same and have not been combined yet, combine them
				else if (b[i][j-1] == b[i][j] && b[i][j]!=swap) {
					b[i][j-1] *=2;
					b[i][j]=0;
					//update the swap variable 
					swap = b[i][j-1];
					//update score
					score += b[i][j-1];
				}
			}
			//reset swap
			swap =0;
		}
		//shuffle the rest to the left as much as possible to get rid of the 0's
		shuffleleft ();
		addTile ();
		prompt.setText("Slide the numbers until 2048!");
		redraw ();
	}


	public void shuffleleft () {
		for (int k = 0; k<3; k++) {
			for (int i = 0; i<row; i++) {
				for (int l=col-1;l>0;l--) {
					if (b[i][l-1]== 0 && b[i][l]!=0) {
						b[i][l-1] = b[i][l];
						b[i][l]=0;
					}
				}
			}
		}
	}

	//Right
	public void right () {
		//add a swap variable so that once one combine happens, the same number which just got combined cannot be combined again
		int swap = 0;
		//loop through the array
		for (int i = 0; i<row; i++) {
			for (int j=0;j<col-1;j++) {
				//if the element to the left of the i'th j+1'th element is 0, shuffle right 
				if (b[i][j+1]== 0 && b[i][j]!=0) {
					b[i][j+1] = b[i][j];
					b[i][j]=0;
				}
				//if the two adjacent numbers are the same and have not been combined yet, combine them
				else if (b[i][j+1] == b[i][j] && b[i][j]!=swap) {
					b[i][j+1] *=2;
					b[i][j]=0;
					//update the swap variable 
					swap = b[i][j+1];
					//update score
					score += b[i][j+1];
				}
			}
			//reset swap
			swap =0;
		}
		//shuffle the rest to the left as much as possible to get rid of the 0's
		shuffleright ();
		addTile();
		prompt.setText("Slide the numbers until 2048!");
		redraw ();
	}


	public void shuffleright () {
		for (int k = 0; k<3; k++) {
			for (int i = 0; i<row; i++) {
				for (int l=0;l<col-1;l++) {
					if (b[i][l+1]== 0 && b[i][l]!=0) {
						b[i][l+1] = b[i][l];
						b[i][l]=0;
					}
				}
			}
		}
	}

	//Up
	public void up () {
		//add a swap variable so that once one combine happens, the same number which just got combined cannot be combined again
		int swap = 0;
		//loop through the array
		for (int j = 0; j<col; j++) {
			for (int i=row-1;i>0;i--) {
				//if the element to the left of the i'th j+1'th element is 0, shuffle right 
				if (b[i-1][j]== 0 && b[i][j]!=0) {
					b[i-1][j] = b[i][j];
					b[i][j]=0;
				}
				//if the two adjacent numbers are the same and have not been combined yet, combine them
				else if (b[i-1][j] == b[i][j] && b[i][j]!=swap) {
					b[i-1][j] *=2;
					b[i][j]=0;
					//update the swap variable 
					swap = b[i-1][j];
					//update score
					score += b[i-1][j];
				}
			}
			//reset swap
			swap =0;

		}
		//shuffle the rest to the left as much as possible to get rid of the 0's
		shuffleup ();
		addTile();
		prompt.setText("Slide the numbers until 2048!");
		redraw ();

	}


	public void shuffleup () {
		for (int k = 0; k<3; k++) {
			for (int i = 0; i<col; i++) {
				for (int l=row-1;l>0;l--) {
					if (b[l-1][i]== 0 && b[l][i]!=0) {
						b[l-1][i] = b[l][i];
						b[l][i]=0;
					}
				}
			}
		}
	}

	//Down
	public void down () {
		//add a swap variable so that once one combine happens, the same number which just got combined cannot be combined again
		int swap = 0;
		//loop through the array
		for (int j = 0; j<col; j++) {
			for (int i=0;i<row-1;i++) {
				//if the element to the left of the i'th j+1'th element is 0, shuffle right 
				if (b[i+1][j]== 0 && b[i][j]!=0) {
					b[i+1][j] = b[i][j];
					b[i][j]=0;
				}
				//if the two adjacent numbers are the same and have not been combined yet, combine them
				else if (b[i+1][j] == b[i][j] && b[i][j]!=swap) {
					b[i+1][j] *=2;
					b[i][j]=0;
					//update the swap variable 
					swap = b[i+1][j];
					//update score
					score += b[i+1][j];
				}
			}
			//reset swap
			swap =0;
		}
		//shuffle the rest to the left as much as possible to get rid of the 0's
		shuffledown ();
		addTile();
		prompt.setText("Slide the numbers until 2048!");
		redraw ();
	}


	public void shuffledown () {
		for (int k = 0; k<3; k++) {
			for (int i =0;i<col;i++) {
				for (int l=0;l<row-1;l++) {
					if (b[l+1][i]== 0 && b[l][i]!=0) {
						b[l+1][i] = b[l][i];
						b[l][i]=0;
					}
				}
			}
		}
	}


	//Randomize Tile - Find an empty tile by randomizing, uses recursion to check random tiles until one of them is empty, then sends that to addTile ()
	public String blankTile (int i, int j) {
		if (b[i][j] == 0) {
			String s = Integer.toString(i);
			s+= Integer.toString(j);
			return s;
		}
		else {
			Random rand = new Random();
			int  n = rand.nextInt(4);
			int  n2 = rand.nextInt(4);
			return blankTile (n,n2);        
		}
	}

	//Actually adds a number either 2 or 4 to the random empty tile.
	public void addTile () {
		Random rand = new Random();
		int  n = rand.nextInt(4);
		int  n2 = rand.nextInt(4);
		String s = blankTile (n,n2);
		int i= Integer.parseInt (s.substring(0,1));
		int j = Integer.parseInt (s.substring(1,2));

		//add the random tile 2 or 4 and add it to b[n][n2];
		//4 has a lower rate of appearing, therefore it is only 20% likely to appear.
		int num = rand.nextInt(7);
		if (num==3)
			num = 4;
		else 
			num =2;

		b[i][j] = num;
	}
	//handles the image creation 
	protected static ImageIcon createImageIcon (String path)
	{ //change the red to your class name
		java.net.URL imgURL = FinalGame.class.getResource (path);
		if (imgURL != null)
		{
			return new ImageIcon (imgURL);
		}
		else
		{
			System.err.println ("Couldn't find file: " + path);
			return null;
		}
	}
	//convert integer to string using a method for convenience
	public String toString (int n) {
		String s = Integer.toString(n);
		return s;
	}

	//Linear Search Code
	public boolean linearSearch(int key) 
	{
		for (int j = 0; j <col; j++) {
			for (int i =0; i<row; i++) {
				if(b[i][j] == key) {
					return true;
				}
			}
		}
		return false; 
	}
	//Checks if the game is won!
	public boolean checkWin () {
		if (linearSearch(2048)) {
			result.setText("You Win!");
			resultpic.setIcon(createImageIcon ("win.jpg" ));
			showStatus ("You Win!");
			updateHS ();
			cdLayout.show(p_card, "4");
			return true;
		}
		else
			return false;
	}
	//Checks if the game is over
	public boolean checkLose () {
		//check if there is a zero present or if a swap in any direction is possible. If these are true, then the lose condition is not met.
		//else the game is over because the array is filled and swaps are not possible.
		if (linearSearch (0))
			return false;
		else if (checkRight ())
			return false;
		else if (checkLeft ())
			return false;
		else if (checkUp())
			return false;
		else if (checkDown ())
			return false;
		else {
			result.setText("You Lose...");
			resultpic.setIcon(createImageIcon ("lose.jpg" ));
			showStatus ("You Lose!");
			return true;
		}
	}
	//checks if moving to the right is possible
	public boolean checkRight () {
		//Checks to see if a swap to the Right is possible
		for (int i = 0; i<row; i++) {
			for (int j=0;j<col-1;j++) {
				if (b[i][j+1] == b[i][j]&&b[i][j]!= 0)
					return true;
				else if (b[i][j+1]== 0 && b[i][j]!=0)
					return true;
			}
		}
		return false;
	}
	//checks if moving to the left is possible
	public boolean checkLeft () {
		//Checks to see if a swap to the Left is possible 
		for (int i = 0; i<row; i++) {
			for (int j=col-1;j>0;j--) {
				if (b[i][j-1] == b[i][j]&&b[i][j]!= 0)
					return true;
				else if (b[i][j-1]== 0 && b[i][j]!=0)
					return true;
			}
		}
		return false;
	}
	//checks if moving up is possible
	public boolean checkUp () {
		//Check to see if a swap upwards is possible
		for (int j = 0; j<col; j++) {
			for (int i=row-1;i>0;i--) {
				if (b[i-1][j] == b[i][j]&& b[i][j]!=0)
					return true;
				else if (b[i-1][j]== 0 && b[i][j]!=0)
					return true;
			}
		}
		return false;
	}
	//checks if moving down is possible
	public boolean checkDown () {
		//Check to see if a swap downwards is possible
		for (int j = 0; j<col; j++) {
			for (int i=0;i<row-1;i++) {
				if (b[i+1][j] == b[i][j]&&b[i][j]!=0)
					return true;
				else if (b[i+1][j]== 0 && b[i][j]!=0)
					return true;
			}
		}
		return false; 
	}
	//updates the screen
	public void redraw ()
	{
		int move = 0;
		for (int i = 0 ; i < row ; i++)
		{
			for (int j = 0 ; j < col ; j++)
			{
				a [move].setIcon (createImageIcon (b [i] [j] + ".png"));
				move++;
			}
		}
	}

	//Resets the game
	public void reset () {
		for (int i =0; i <row; i++) {
			for (int j =0; j<col; j++) {
				b[i][j]=0;
			}
		}
		addTile();
		addTile();
		score =0;
		time =0;
		timerCount.setText ("Elapsed Time: "+time / 60 + ":0" + time % 60);
		name = JOptionPane.showInputDialog ("Please enter your name!");
		scoreTitle.setText (name + "'s Score: " + score);
		redraw ();
	}
	public void save (String filename) {
		PrintWriter out;
		try {
			out = new PrintWriter (new FileWriter (filename));
			for (int i = 0 ; i < row ; i++)
			{
				for (int j = 0 ; j < col ; j++)
				{
					out.println (b[i][j]);
				}
			}
			out.close ();
		}

		catch (IOException e) {
			System.out.println ("Error opening file " + e);
		}
	}
	public void open(String filename){
		BufferedReader in;
		try {
			in = new BufferedReader (new FileReader (filename));
			String input = in.readLine ();
			for (int i = 0 ; i < row ; i++)
			{
				for (int j = 0 ; j < col ; j++)
				{
					b[i][j]= Integer.parseInt (input);
					input = in.readLine ();
				}
			}
			in.close ();
		}
		catch (IOException e) {
			System.out.println ("Error opening file " + e);
		}
		redraw();
	}

	public void updateHS () {
		//Find position to insert new high score
		int pos = findPos ();
		if (pos!= -1) {
			for (int i = pos+1; i <5; i++ ) {
				hs [1][i] = hs [1][i-1];
			}
			hs [0][pos] = name;
			hs [1][pos] = toString (score);
			highScoreTitle.setText("High Score: "+ hs[1][0]);
			//set Text in the high score screen
			for (int i =0; i<5;i++){
				hsName[i].setText((i+1)+".   "+hs[0][i]);
				hsScore[i].setText(hs[1][i]);
			}
			storeHS ("highscore.txt");
		}       
	}
	
	public int findPos () {
		int pos = -1;
		//loop through the high scores to see if the current score beats any of them
			for (int i = 0; i <5; i++) {
				int n = Integer.parseInt(hs[1][i]);
				//if score is greater than the top 5, find the position to shuffle down to
				if (score >n ) {
					pos= i;
					return pos;
				}
			}
			return pos;
				
	}

	
	public void storeHS (String filename) {
		//stores high score into the high score file
		PrintWriter out;
		try {
			out = new PrintWriter (new FileWriter (filename));
			for (int i = 0 ; i < 2 ; i++)
			{
				for (int j = 0 ; j < 5 ; j++)
				{
					out.println (hs[i][j]);
				}
			}
			out.close ();
		}
		catch (IOException e) {
			System.out.println ("Error opening file " + e);
		}
	}
	
	public void openHS (String filename) {
		BufferedReader in;
		try {
			in = new BufferedReader (new FileReader (filename));
			String input = in.readLine ();
			for (int i = 0 ; i < 2 ; i++)
			{
				for (int j = 0 ; j < 5 ; j++)
				{
					hs[i][j]= (input);
					input = in.readLine ();
				}
			}
			in.close ();
		}
		catch (IOException e) {
			System.out.println ("Error opening file " + e);
		}
	}
	
	//makes buttons work
	public void actionPerformed (ActionEvent e)
	{ //moves between the screens
		if(e.getActionCommand().equals("save"))
			save("opensave.txt");
		else if (e.getActionCommand().equals("open"))
			open("opensave.txt");
		else if (e.getActionCommand ().equals ("s1"))
			cdLayout.show (p_card, "1");
		else if (e.getActionCommand ().equals ("s2"))
			cdLayout.show (p_card, "2");
		else if (e.getActionCommand ().equals ("toIns"))
			cdLayout.show (p_card, "2");
		else if (e.getActionCommand ().equals ("s3")) {
			cdLayout.show (p_card, "3");
			reset ();
		}
		else if (e.getActionCommand ().equals ("s4"))
			cdLayout.show (p_card, "4");
		else if (e.getActionCommand ().equals ("toGame")) {
			updateHS ();
			cdLayout.show (p_card, "3");
			reset ();
		}
		else if (e.getActionCommand ().equals ("toGame2")) {
			cdLayout.show (p_card, "3");
			reset ();
		}
		else if (e.getActionCommand ().equals ("toHS"))
			cdLayout.show (p_card, "5");
		else if (e.getActionCommand ().equals ("s6"))
			System.exit (0);
		else if (e.getActionCommand ().equals ("reset"))
			reset();
		//AC for movement buttons
		else if (e.getActionCommand ().equals("left")) {
			if (checkLeft ()) 
				left ();                              
			else
				prompt.setText("Cannot slide to the Left!");
		}
		else if (e.getActionCommand ().equals("right")) {
			if (checkRight ()) 
				right ();                
			else
				prompt.setText("Cannot slide to the Right!");
		}
		else if (e.getActionCommand ().equals("up")) {
			if (checkUp()) 
				up ();                         
			else
				prompt.setText("Cannot slide Upwards!");
		}
		else if (e.getActionCommand ().equals("down")) {
			if (checkDown ()) 
				down ();                           
			else
				prompt.setText("Cannot slide Downwards!");
		}
		//done for movement
		else
		{ //code to handle the game
			int n = Integer.parseInt (e.getActionCommand ());
			int x = n / col;
			int y = n % col;
			showStatus ("(" + x + ", " + y + ")");

		}
		//if win or lose is met to go screen 4
		if (checkLose()||checkWin())
			cdLayout.show(p_card, "4");
		//set score text
		scoreTitle.setText(name + "'s Score: "+score);
	}

	//KEY LISTENING CODE ***Not working for some reason. Code is correct...***
	public void keyPressed (KeyEvent e)
	{
	}

	public void keyReleased (KeyEvent e)
	{
		switch (e.getKeyCode ())
		{
		case KeyEvent.VK_LEFT:
		{
			if (checkLeft ()) 
				left ();                              
			else
				prompt.setText("Cannot slide to the Left!");
			break;
		}
		case KeyEvent.VK_RIGHT:
		{                 
			if (checkRight ()) 
				right ();                
			else
				prompt.setText("Cannot slide to the Right!");                      
			break;
		}
		case KeyEvent.VK_UP:
		{
			if (checkUp()) 
				up ();                         
			else
				prompt.setText("Cannot slide Upwards!");
			break;
		}
		case KeyEvent.VK_DOWN:
		{
			if (checkDown ()) 
				down ();                           
			else
				prompt.setText("Cannot slide Downwards!");
			break;
		}
		case KeyEvent.VK_SPACE:
			System.exit(0);
		}
	}


	public void keyTyped (KeyEvent e)
	{
		switch (e.getKeyCode ())
		{
		case KeyEvent.VK_RIGHT:
			break;
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_DOWN:
			break;
		}
	}
}
