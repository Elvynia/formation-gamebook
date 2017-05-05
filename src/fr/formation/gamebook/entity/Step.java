package fr.formation.gamebook.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Step {

	@XmlElement
	public int id;

	@XmlElement
	public String description;

	@XmlElement
	public String question;

	@XmlElementWrapper(name = "actions")
	@XmlElement(name = "choice")
	public List<Choice> actions;
}
