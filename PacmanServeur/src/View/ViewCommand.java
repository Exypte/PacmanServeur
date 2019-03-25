package View;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controler.InterfaceControler;
import Model.Maze;
import Model.PacmanGame;

public class ViewCommand implements Observateur {

	private JFrame frame;
	private JButton restart, start, step, pause, parcourire;

	private PacmanGame gameState;
	private InterfaceControler controler;

	public ViewCommand(PacmanGame gameState, InterfaceControler controler) {

		super();
		
		this.gameState = gameState;
		this.gameState.enregistrerObservateur(this);
		
		this.controler = controler;
		
		windowGenerator();
	}

	public void windowGenerator() {
		
		this.frame = new JFrame();
		this.frame.setTitle("Commands");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(new Dimension(700, 300));
		this.frame.setLocation(1000, 500);

		GridLayout mainGrid = new GridLayout(2, 0);

		this.frame.setLayout(mainGrid);

		GridLayout gridButton = new GridLayout(0, 4);
		JPanel panel = new JPanel(gridButton);

		Icon icon_restart = new ImageIcon("icons/icon_restart.png");
		Icon icon_step = new ImageIcon("icons/icon_step.png");
		Icon icon_run = new ImageIcon("icons/icon_run.png");
		Icon icon_pause = new ImageIcon("icons/icon_pause.png");

		this.restart = new JButton(icon_restart);
		this.start = new JButton(icon_run);
		this.step = new JButton(icon_step);
		this.pause = new JButton(icon_pause);

		this.pause.setEnabled(false);
		this.step.setEnabled(false);
		this.start.setEnabled(false);

		panel.add(restart);
		panel.add(start);
		panel.add(step);
		panel.add(pause);

		GridLayout belowGrid = new GridLayout(0, 2);
		JPanel panelBelow = new JPanel(belowGrid);

		//Create the label.
		JLabel sliderLabel = new JLabel("Number of turns per second", JLabel.CENTER);

		JSlider sliderLaps= new JSlider(JSlider.HORIZONTAL,1,10,1);
		sliderLaps.setMajorTickSpacing(1);
		sliderLaps.setMinorTickSpacing(1);
		sliderLaps.setPaintTicks(true);
		sliderLaps.setPaintLabels(true);


		JLabel turnNumber= new JLabel("Turn : 0", JLabel.CENTER);

		JPanel jPanelParcourire = new JPanel();
		parcourire = new JButton("Parcourire Map");
		jPanelParcourire.add(parcourire);

		panelBelow.add(sliderLabel);
		panelBelow.add(turnNumber);
		panelBelow.add(sliderLaps);
		panelBelow.add(jPanelParcourire);


		frame.add(panel);
		frame.add(panelBelow);

		/*
		 * Listener
		 */
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				controler.restart();
				getStart().setEnabled(true);
				getStep().setEnabled(true);
				getPause().setEnabled(false);
			}
		});

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controler.start();
				
				getPause().setEnabled(true);
				getRestart().setEnabled(true);
				getStart().setEnabled(false);
				getStep().setEnabled(false);
			}
		});

		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controler.step();
			}
		});

		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controler.pause();
				getStart().setEnabled(true);
				getStep().setEnabled(true);
				getPause().setEnabled(false);
			}
		});

		sliderLaps.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				gameState.setTime(1000/(sliderLaps.getValue()));
			}
		});

		// action listener g�rant la modification de la map
		parcourire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controler.pause();
				// Cr�ation du file chooser et de son r�pertoire par d�faut
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("layouts/"));
				// Permet d'afficher une popup demandant � l'utilisateur quel fichier choisir
				// Une fois le fichier choisi on modifie le maze du panel graphique affichant la
				// map
				if (fileChooser.showOpenDialog(frame) == 0) {
					try {
						//mapName=fileChooser.getSelectedFile().getPath();
						//gameState.setLabyrinth(new Maze(fileChooser.getSelectedFile().getPath()));
						gameState.setMapName(fileChooser.getSelectedFile().getPath());
						controler.restart();
						getStart().setEnabled(true);
						getStep().setEnabled(true);
						getPause().setEnabled(false);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				}
			}
		});
	}
	
	@Override
	public void actualiser(String text) {
		//System.out.println(text);
	}
	
	public void showWindow() {
		this.frame.setVisible(true);
	}
	
	public void hideWindow() {
		this.frame.setVisible(false);
	}
	
	public void destroyWindow() {
		this.frame.dispose();
	}
	
	/*
	 * Getters
	 */
	
	public JButton getRestart() {
		return restart;
	}

	public JButton getStart() {
		return start;
	}

	public JButton getStep() {
		return step;
	}

	public JButton getPause() {
		return pause;
	}

	public JButton getParcourire() {
		return parcourire;
	}
	
	/*
	 * Setters
	 */
	
	public void setRestart(JButton restart) {
		this.restart = restart;
	}

	public void setStart(JButton start) {
		this.start = start;
	}

	public void setStep(JButton step) {
		this.step = step;
	}

	public void setPause(JButton pause) {
		this.pause = pause;
	}

	public void setParcourire(JButton parcourire) {
		this.parcourire = parcourire;
	}
}

