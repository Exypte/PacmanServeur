package Model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

import Command.PositionAgent;


public class Maze implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 
	 * Les differentes directions possibles pour les actions et les orientations des agents
	 */
	public static int NORTH=0;
	public static int SOUTH=1;
	public static int EAST=2;
	public static int WEST=3;
	public static int STOP=4;
	
	private int size_x;
	private int size_y;
	
	private boolean isScarred;
	/** 
	 * Les elements du labyrinthe
	 */
	private boolean walls[][];
	private boolean food[][];
	private boolean capsules[][];

	/** 
	 * Les positions initiales des agents
	 */
	private ArrayList<PositionAgent> pacman_start;
	private ArrayList<PositionAgent> ghosts_start;

	
	public Maze(String filename) throws Exception
	{
		try{
			System.out.println("Layout file is "+filename);
			//Lecture du fichier pour determiner la taille du labyrinthe
			InputStream ips=new FileInputStream(filename); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			int nbX=0;
			int nbY=0;
			while ((ligne=br.readLine())!=null)
			{
				ligne=ligne.trim();
				if (nbX==0) {nbX=ligne.length();}
				else if (nbX!=ligne.length()) throw new Exception("Wrong Input Format: all lines must have the same size");
				nbY++;
			}			
			br.close(); 
			System.out.println("### Size of maze is "+nbX+";"+nbY);
			
			//Initialisation du labyrinthe
			size_x=nbX;
			size_y=nbY;
			walls=new boolean[size_x][size_y];
			food=new boolean[size_x][size_y];
			capsules=new boolean[size_x][size_y];
			
			pacman_start=new ArrayList<PositionAgent>();
			ghosts_start =new ArrayList<PositionAgent>();
			
			//Lecture du fichier pour mettre a jour le labyrinthe
			 ips=new FileInputStream(filename); 
			 ipsr=new InputStreamReader(ips);
			 br=new BufferedReader(ipsr);
			 int y=0;
			while ((ligne=br.readLine())!=null)
			{
				ligne=ligne.trim();

				for(int x=0;x<ligne.length();x++)
				{
					if (ligne.charAt(x)=='%') walls[x][y]=true; else walls[x][y]=false;
					if (ligne.charAt(x)=='.') food[x][y]=true; else food[x][y]=false;
					if (ligne.charAt(x)=='o') capsules[x][y]=true; else capsules[x][y]=false;
					if (ligne.charAt(x)=='P') {pacman_start.add(new PositionAgent(x,y,Maze.NORTH));}
					if (ligne.charAt(x)=='G') {ghosts_start.add(new PositionAgent(x,y,Maze.NORTH));}
				}
				y++;
			}			
			br.close(); 
			
			if (pacman_start.size()==0)throw new Exception("Wrong input format: must specify a Pacman start");
			
			//On verifie que le labyrinthe est clos			
			for(int x=0;x<size_x;x++) if (!walls[x][0]) throw new Exception("Wrong input format: the maze must be closed");
			for(int x=0;x<size_x;x++) if (!walls[x][size_y-1]) throw new Exception("Wrong input format: the maze must be closed");
			for(y=0;y<size_y;y++) if (!walls[0][y]) throw new Exception("Wrong input format: the maze must be closed");
			for(y=0;y<size_y;y++) if (!walls[size_x-1][y]) throw new Exception("Wrong input format: the maze must be closed");
			System.out.println("### Maze loaded.");
			
		}		
		catch (Exception e){
			e.printStackTrace();
			throw new Exception("Probleme a la lecture du fichier: "+e.getMessage());
		}
	}
	
	/**
	 * Renvoie la taille X du labyrtinhe
	 */
	public int getSizeX() {return(size_x);}

	/**
	 * Renvoie la taille Y du labyrinthe
	 */
	public int getSizeY() {return(size_y);}
	
	/**
	 * Permet de savoir si il y a un mur
	 */
	public boolean isWall(int x,int y) 
	{
		assert((x>=0) && (x<size_x));
		assert((y>=0) && (y<size_y));
		return(walls[x][y]);
	}
	
	/**
	 * Permet de savoir si il y a de la nourriture
	 */
	public boolean isFood(int x,int y) 
	{
		assert((x>=0) && (x<size_x));
		assert((y>=0) && (y<size_y));
		return(food[x][y]);
	}

	public void setFood(int x,int y,boolean b) {
		food[x][y]=b;
	}
	
	
	/**
	 * Permet de savoir si il y a une capsule
	 */
	public boolean isCapsule(int x,int y) 
	{
		assert((x>=0) && (x<size_x));
		assert((y>=0) && (y<size_y));
		return(capsules[x][y]);
	}
	
	public void setCapsule(int x,int y,boolean b) {
		capsules[x][y]=b;
	}
	
	/**
	 * Renvoie le nombre de pacmans
	 * @return
	 */
	public int getInitNumberOfPacmans() 
	{
		return(pacman_start.size());	
	}
	
	/**
	 * Renvoie le nombre de fantomes
	 * @return
	 */
	public int getInitNumberOfGhosts() 
	{
		return(ghosts_start.size());
	}
	
	
	public ArrayList<PositionAgent> getPacman_start() {
		return pacman_start;
	}

	public void setPacman_start(ArrayList<PositionAgent> pacman_start) {
		this.pacman_start = pacman_start;
	}

	public ArrayList<PositionAgent> getGhosts_start() {
		return ghosts_start;
	}

	public void setGhosts_start(ArrayList<PositionAgent> ghosts_start) {
		this.ghosts_start = ghosts_start;
	}

	public boolean isScarred() {
		return isScarred;
	}

	public void setScarred(boolean isScarred) {
		this.isScarred = isScarred;
	}
	
	
}


