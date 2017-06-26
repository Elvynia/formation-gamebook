package fr.formation.gamebook;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.formation.gamebook.model.Choice;
import fr.formation.gamebook.model.Game;
import fr.formation.gamebook.model.Step;

public class GameBook implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GameBook.class);
	private static final String MSG_ERR_INPUT = "Saisie invalide, veuillez saisir le nombre correspondant à votre choix.";
	private static final String MSG_EXIT_INGAME = "('exit' pour quitter la partie) ";
	private static final String OPT_EXIT_VALUE = "exit";

	public static void main(final String[] args) {
		String dataPath = null;
		String savePath = null;
		if (args.length > 0) {
			dataPath = args[0];
		}
		if (args.length > 1) {
			savePath = args[1];
		}
		new GameBook(dataPath, savePath).run();
	}

	/*
	 * Séparation entre code statique et code lié à une instance.
	 */

	private String dataPath;
	private String savePath;
	private Game game;
	private Scanner scanner;
	private Step currentStep;

	public GameBook(String dataPath, String savePath) {
		this.dataPath = dataPath;
		this.savePath = savePath;
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void run() {
		if (this.checkPath()) {
			this.game = this.parseData();
			this.parseSave();
			if (this.game != null) {
				GameBook.LOGGER
						.debug("Nom du jeu paramétré : " + this.game.title);
				GameBook.LOGGER.debug(
						"Nombre d'étapes du jeu : " + this.game.steps.size());
				this.startGame();
				while (this.continueGame()) {
					this.displayGame();
					this.interactGame();
				}
				// Vérifier si la partie a été quitté en cours
				if (this.currentStep != null) {
					// Le jeu est terminé, afficher la description de la
					// dernière
					// étape.
					GameBook.LOGGER.info(this.currentStep.description);
				}
				GameBook.LOGGER
						.info("Jeu terminé, fermeture de l'application.");
				this.scanner.close();
			}
		} else {
			this.showUsage();
		}
	}

	/**
	 * Demande d'une saisie utilisateur pour effectuer une association avec le
	 * choix de l'étape courante. Puis mise à jour de l'étape courante avec la
	 * nouvelle valeur provenant de l'attribut gotostep du choix.
	 */
	private void interactGame() {
		final String strExit = GameBook.MSG_EXIT_INGAME;
		boolean valid = false;
		String strChoice = "";
		while (!valid) {
			GameBook.LOGGER.info("Quel est votre choix " + strExit + " ? ");
			try {
				strChoice = scanner.next();
				final int choiceIndex = Integer.parseInt(strChoice);
				if (choiceIndex >= 0
						&& choiceIndex < this.currentStep.actions.size()) {
					final Choice choice = this.currentStep.actions
							.get(choiceIndex);
					this.currentStep = this.game.getById(choice.gotostep);
					valid = true;
				} else {
					GameBook.LOGGER.error(GameBook.MSG_ERR_INPUT);
				}
			} catch (final NumberFormatException e) {
				if (!strExit.isEmpty() && strChoice
						.equalsIgnoreCase(GameBook.OPT_EXIT_VALUE)) {
					this.confirmSave();
					this.currentStep = null;
					valid = true;
				} else {
					GameBook.LOGGER.trace("Mauvaise saisie utilisateur : ", e);
					GameBook.LOGGER.error(GameBook.MSG_ERR_INPUT);
				}
			}
		}
	}

	private void confirmSave() {
		GameBook.LOGGER
				.info("Voulez-vous sauvegarder la partie en cours ? [N/y]");
		boolean save = false;
		try {
			final String input = this.scanner.next("[yYnN]");
			if (input.equalsIgnoreCase("y")) {
				save = true;
			}
		} catch (final NoSuchElementException e) {
			GameBook.LOGGER.trace(
					"Mauvaise réponse utilisateur sur le choix de sauvaegarder.",
					e);
			// Ne pas sauvgarder, save est déjà à false.
		}
		if (save) {
			this.game.save(this.currentStep);
		}
	}

	/**
	 * Afficher l'état courant du jeu (le contenu de l'étape courante et de ses
	 * choix).
	 */
	private void displayGame() {
		GameBook.LOGGER.info(this.currentStep.description);
		GameBook.LOGGER.info(this.currentStep.question);
		for (int i = 0; i < this.currentStep.actions.size(); ++i) {
			Choice choice = this.currentStep.actions.get(i);
			GameBook.LOGGER.info("\t" + i + " - " + choice.content);
		}
	}

	/**
	 * @return boolean vrai si l'étape courante possède au moins une action (un
	 * choix).
	 */
	private boolean continueGame() {
		return this.currentStep == null ? false
				: this.currentStep.actions.size() > 0;
	}

	/**
	 * Initialise la valeur de l'étape courante pour commencer le jeu.
	 */
	private void startGame() {
		// Vérification de l'association entre Game et Save.
		if (this.game.checkSave()) {
			// Démarrage du jeu au premier step d'identifiant 0.
			this.currentStep = this.game.getStartStep();
		} else {
			GameBook.LOGGER.error("Impossible de lancer le jeu, "
					+ "la sauvegarde ne correspond pas au jeu lancé.");
		}
	}

	private Game parseData() {
		Game game = null;
		try {
			final JAXBContext context = JAXBContext.newInstance(Game.class);
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			game = (Game) unmarshaller.unmarshal(new File(this.dataPath));
			GameBook.LOGGER.debug(
					"Analyse et transformation du fichier de données en objets Java réussie.");
		} catch (JAXBException e) {
			GameBook.LOGGER.error("Impossible de démarrer un contexte JAXB.",
					e);
		}
		return game;
	}

	/**
	 * Déclanche le chargement de la sauvegarde si le paramètre correspondant
	 * est présent.
	 */
	private void parseSave() {
		if (this.savePath != null) {
			this.game.load(this.savePath);
		}
	}

	private void showUsage() {
		GameBook.LOGGER.info("Usage : gamebook <path_to_xml_data>");
	}

	/**
	 * Vérification des paramètres donnés à l'application.
	 * 
	 * @return boolean vrai si les paramètres sont techniquement corrects.
	 */
	private boolean checkPath() {
		return this.checkDataPath() & this.checkSavePath();
	}

	private boolean checkDataPath() {
		final String errorPrefix = "Impossible de lancer le jeu, ";
		if (this.dataPath == null || this.dataPath.isEmpty()) {
			GameBook.LOGGER.error(errorPrefix
					+ "chemin vers un fichier de donées XML manquant.");
			return false;
		}
		final File dataFile = new File(this.dataPath);
		if (!dataFile.exists()) {
			GameBook.LOGGER.error(
					errorPrefix + "aucun fichier correspondant au chemin '{}'.",
					this.dataPath);
			return false;
		}
		if (!dataFile.canRead()) {
			GameBook.LOGGER.error(
					errorPrefix
							+ "accès en lecture manquant sur le fichier '{}'",
					this.dataPath);
			return false;
		}
		GameBook.LOGGER.debug("Data path is checked and valid.");
		return true;
	}

	private boolean checkSavePath() {
		if (this.savePath != null) {
			final File saveFile = new File(this.savePath);
			if (!saveFile.exists() || !saveFile.canRead()) {
				GameBook.LOGGER.error(
						"Le fichier '{}' n'est pas un fichier de "
								+ "sauvegarde valide (inexistant ou droits manquants).",
								this.savePath);
				return false;
			}
		}
		return true;
	}
}
