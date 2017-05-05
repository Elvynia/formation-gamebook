package fr.formation.gamebook.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Game {

	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	private List<Step> steps;

	@XmlAttribute(name = "title")
	public String title;

	@XmlAttribute
	public String id;

	@XmlAttribute
	public String lang;

	public Step getStep(final int id) {
		Step result = null;
		for (final Step step : this.steps) {
			if (step.id == id) {
				result = step;
				break;
			}
		}
		return result;
	}
}
