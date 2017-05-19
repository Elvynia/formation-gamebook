package fr.formation.gamebook.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Step implements Comparable<Step>, Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String content;

	private String question;

	private List<Choice> choices;

	/**
	 * @return the id
	 */
	@XmlElement
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the content
	 */
	@XmlElement(name = "description")
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the question
	 */
	@XmlElement
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the choices
	 */
	@XmlElementWrapper(name = "actions")
	@XmlElement(name = "choice")
	public List<Choice> getChoices() {
		return choices;
	}

	/**
	 * @param choices
	 *            the choices to set
	 */
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	@Override
	public int compareTo(Step step) {
		return this.id.compareTo(step.id);
	}

}
