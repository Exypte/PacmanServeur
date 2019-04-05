package Model;

import java.util.ArrayList;
import java.util.List;


import View.Observateur;

public abstract class Game implements Runnable,Subject{

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
	
	private  List<Observateur> obs = new ArrayList<Observateur>();

	public abstract void initializeGame();
	public abstract void takeTurn();
	public abstract void gameOver();
	
	/*
	 * Initialise le jeu
	 */
	public void init() {
		this.lapCount = 0;
		
		this.initializeGame();
		
		notifier("Init Simple game");
	}
	
	/*
	 * Effectue un tour de jeu
	 */
	public void step() {
		if( lapCount > maxLapCount ){
			this.gameOver();
			notifier("Game Over !");
		}else {	
			this.takeTurn();
			notifier("Take Turn !");
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

	@Override
	public void enregistrerObservateur(Observateur o) {
		// TODO Auto-generated method stub
		obs.add(o);

	}

	@Override
	public void removeObservateur(Observateur o) {
		// TODO Auto-generated method stub
		obs.remove(o);

	}
	
	@Override
	public void notifier(String text) {
		// TODO Auto-generated method stub
		for(Observateur o: obs) {
			o.actualiser(text);
		}
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
	
	public List<Observateur> getObs() {
		return obs;
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
	
	public void setObs(List<Observateur> obs) {
		this.obs = obs;
	}
	public Object getLock() {
		return lock;
	}
}
