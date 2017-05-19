package fr.formation.gamebook.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Game {

	private String title;

	private String lang;

	private String id;

	private List<Step> steps;
	
	public Step getStep(final int id) {
		Step result = null;
		for (final Step step : this.steps) {
			if (step.getId() == id) {
				result = step;
				break;
			}
		}
		return result;
	}

	/**
	 * @return the title
	 */
	@XmlAttribute
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the lang
	 */
	@XmlAttribute
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the id
	 */
	@XmlAttribute
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the steps
	 */
	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	public List<Step> getSteps() {
		return steps;
	}

	/**
	 * @param steps
	 *            the steps to set
	 */
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
