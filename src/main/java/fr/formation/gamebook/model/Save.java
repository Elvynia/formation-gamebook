package fr.formation.gamebook.model;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ol>
 * <li>Découper la méthode GameBook.startGame() pour améliorer la lisibilité de
 * la logique du jeu et la maintenabilité du code.</li>
 * <li>Prendre en compte la possibilité pour l'utilisateur de quitter le jeu en
 * cours de partie (au moment de la saisie du choix).</li>
 * <li>Si l'utilisateur quitte en cours de partie, proposer à l'utilisateur de
 * sauvegarder sa partie. Faire un objet Java Save comportant les informations à
 * persister en fichier XML.</li>
 * <li>Prendre en compte la possibilité d'un second argument à l'application
 * (méthode main) afin de permettre à l'utilisateur de reprendre une partie
 * sauvegardée.</li>
 * </ol>
 * 
 * @author Arcanis
 *
 */
@XmlRootElement
public class Save {
	private static final String PERSIST_PATH = "D:/workspaces/java_marseille/gamebook.sav";
	private static final Logger LOGGER = LoggerFactory.getLogger(Save.class);

	@XmlElement
	public Integer lastStep;

	@XmlElement
	public String gameId;

	public void persist() {
		try {
			JAXBContext context = JAXBContext.newInstance(this.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(this, new File(Save.PERSIST_PATH));
		} catch (JAXBException e) {
			Save.LOGGER.error(
					"Impossible de créer le fichier de sauvegarde, problème technique JAXB : ",
					e);
		}
	}
}
