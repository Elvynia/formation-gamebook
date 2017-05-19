package fr.formation.gamebook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fr.formation.gamebook.entity.Game;

public class GameBook implements Runnable {

	private String dataPath;
	private Game game;

	public GameBook(final String dataPath) {
		this.dataPath = dataPath;
	}

	@Override
	public void run() {
		final File dataFile = new File(this.dataPath);
		if (dataFile.exists() && dataFile.canRead()) {
			try {
				this.initialize();
				this.start();
			} catch (JAXBException | FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Impossible de démarrer le jeu, fichier introuvable "
					+ "ou accès en lecture manquant.");
		}
	}

	private void initialize() throws JAXBException, FileNotFoundException {
		final JAXBContext context = JAXBContext.newInstance(Game.class);
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		this.game = (Game) unmarshaller.unmarshal(new FileInputStream(this.dataPath));
	}

	private void start() {
		System.out.println("Titre du jeu : " + this.game.getTitle());
	}

}
