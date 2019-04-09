package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Game implements Runnable{

	/* Compteur de nombre de tours */
	private int lapCount;
	/* Nombre de tours maximun d'une partie */
	private int maxLapCount = 100;
	/* Flag pour savoir si le jeu est en pause */
	private boolean isRunning;
	/* Thread de la classe game*/
	private Thread thread;
	/* Temps d'arrêt après chaque tour du jeu*/
	private long time = 500;
	
	private final Object lock = new Object();

	public abstract void initializeGame();
	public abstract void takeTurn();
	public abstract void gameOver();
	
	/*
	 * Initialise le jeu
	 */
	public void init() {
		this.lapCount = 0;
		
		this.initializeGame();
		
	}
	
	/*
	 * Effectue un tour de jeu
	 */
	public void step() {
		if( lapCount > maxLapCount ){
			this.gameOver();
		}else {	
			this.takeTurn();
		}
	}
	
	/*
	 * Lance le jeu jusqu'au nombre maximun de tour
	 */
	public void run() {
		while(this.isRunning && (this.lapCount <= this.maxLapCount)) {
			
			this.step();
			this.lapCount++;
			
			try {
				synchronized (lock) {
					lock.notify();
					Thread.sleep(time);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Met le jeu en marche
	 */
	public void start() {
		this.isRunning = true;
	}
	
	/*
	 * Met le jeu en pause
	 */
	public void stop() {
		this.isRunning = false;
	}

	/*
	 * Lance un thread
	 */
	public void launch() {
		thread = new Thread(this);
		thread.start();
	}
	/*
	 * Getters
	 */
	public int getLapCount() {
		return lapCount;
	}
	
	public int getMaxLapCount() {
		return maxLapCount;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public long getTime() {
		return time;
	}
	
	
	/*
	 * Setters
	 */
	public void setLapCount(int lapCount) {
		this.lapCount = lapCount;
	}
	
	public void setMaxLapCount(int maxLapCount) {
		this.maxLapCount = maxLapCount;
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public Object getLock() {
		return lock;
	}
}
