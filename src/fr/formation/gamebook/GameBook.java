package fr.formation.gamebook;

import java.io.File;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fr.formation.gamebook.entity.Choice;
import fr.formation.gamebook.entity.Game;
import fr.formation.gamebook.entity.Step;

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
				initialize();
				start();
			} catch (JAXBException e) {
				e.printStackTrace();
				System.err.println("Impossible de charger le fichier '" + this.dataPath
						+ "' comme données de jeu. Vérifiez la syntaxe XML.");
			}
		} else {
			System.err.println("Le fichier '" + this.dataPath
					+ "' n'existe pas ou la lecture n'est pas autorisée.");
		}
	}

	private void start() {
		int currentStep = 0;
		final Scanner scanner = new Scanner(System.in);
		while (currentStep >= 0) {
			final Step step = game.getStep(currentStep);
			System.out.println(step.description);
			System.out.println(step.question);
			if (step.actions != null && !step.actions.isEmpty()) {
				for (int i = 0; i < step.actions.size(); ++i) {
					final Choice choice = step.actions.get(i);
					System.out.println("\t" + i + " - ".concat(choice.description));
				}
				System.out.print("Votre choix : ");
				final int userChoice = scanner.nextInt();
				currentStep = step.actions.get(userChoice).gotostep;
			} else {
				currentStep = -1;
			}
		}
		scanner.close();
	}

	private void initialize() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Game.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		this.game = (Game) unmarshaller.unmarshal(new File(this.dataPath));
	}

}
