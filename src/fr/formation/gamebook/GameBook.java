package fr.formation.gamebook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fr.formation.gamebook.entity.Choice;
import fr.formation.gamebook.entity.Game;
import fr.formation.gamebook.entity.Step;

public class GameBook implements Runnable {

	private final Scanner scanner;
	private String dataPath;
	private Game game;

	public GameBook(final String dataPath) {
		this.dataPath = dataPath;
		this.scanner = new Scanner(System.in);
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
			System.err.println("Impossible de démarrer le jeu, fichier introuvable " + "ou accès en lecture manquant.");
		}
	}

	private void initialize() throws JAXBException, FileNotFoundException {
		final JAXBContext context = JAXBContext.newInstance(Game.class);
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		this.game = (Game) unmarshaller.unmarshal(new FileInputStream(this.dataPath));
	}

	private void start() {
		System.out.println("Titre du jeu : " + this.game.getTitle());
		int index = 0;
		while (index >= 0) {
			final Step step = this.game.getStep(index);
			System.out.println(step.getContent());
			System.out.println(step.getQuestion());
			if (step.getChoices().isEmpty()) {
				index = -1;
			} else {
				for (int i = 0; i < step.getChoices().size(); ++i) {
					final Choice choice = step.getChoices().get(i);
					System.out.println("\t" + i + " - " + choice.getContent());
				}
				final int choiceIndex = this.readChoice(step.getChoices().size() - 1);
				index = step.getChoices().get(choiceIndex).getGotostep();
			}
		}
	}

	private int readChoice(int limitMax) {
		int choice = -1;
		boolean choiceOk = false;
		while (!choiceOk) {
			System.out.print("Quel est votre choix ? ");
			try {
				choice = this.scanner.nextInt();
				if (choice >= 0 && choice <= limitMax) {
					choiceOk = true;
				} else {
					System.err.println("Le nombre saisi ne correspond pas à un choix valide, veuillez recommencer.");
				}
			} catch (final InputMismatchException e) {
				System.err.println("Saisie invalide, veuillez recommencer.");
			}
		}
		return choice;
	}

}
