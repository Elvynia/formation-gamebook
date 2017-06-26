package fr.formation.gamebook.model;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class Game {

	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
	private static final Integer DEFAULT_STEP_ID = 0;

	@XmlAttribute
	public String title;

	@XmlAttribute(name = "lang")
	public String language;

	@XmlAttribute
	public String id;

	@XmlElementWrapper
	@XmlElement(name = "step")
	public List<Step> steps;

	private transient Save save;

	/**
	 * Méthode de recherche d'une étape par son identifiant.
	 * 
	 * @param id
	 * l'identifiant recherché.
	 * @return Step l'étape trouvée ou null.
	 */
	public Step getById(final int id) {
		Step result = null;
		for (final Step step : this.steps) {
			if (step.id == id) {
				result = step;
				break;
			}
		}
		return result;
	}

	public void save(final Step lastStep) {
		this.save = new Save();
		this.save.gameId = id;
		this.save.lastStep = lastStep.id;
		this.save.persist();
	}

	public void load(final String savePath) {
		try {
			JAXBContext context = JAXBContext.newInstance(Save.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			this.save = (Save) unmarshaller.unmarshal(new File(savePath));
		} catch (JAXBException e) {
			Game.LOGGER.error(
					"Impossible de charger la sauvegarde '" + savePath + "' : ",
					e);
		}
	}

	public Step getStartStep() {
		Step result = null;
		if (this.save != null) {
			result = this.getById(this.save.lastStep);
		} else {
			result = this.getById(Game.DEFAULT_STEP_ID);
		}
		return result;
	}

	public boolean checkSave() {
		return this.save != null ? this.id.equals(this.save.gameId) : true;
	}
}
