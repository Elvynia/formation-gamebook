package fr.formation.gamebook.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Step implements Serializable {

	private static final long serialVersionUID = 1L;

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
