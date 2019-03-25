package Model;

import View.Observateur;

public interface Subject {
	 public void enregistrerObservateur(Observateur o);
	 public void removeObservateur(Observateur o);
	 public void notifier(String text);
}
