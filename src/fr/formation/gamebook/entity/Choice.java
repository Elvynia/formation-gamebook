package fr.formation.gamebook.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Choice implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer gotostep;

	private String content;

	/**
	 * @return the gotostep
	 */
	@XmlAttribute
	public Integer getGotostep() {
		return gotostep;
	}

	/**
	 * @param gotostep
	 *            the gotostep to set
	 */
	public void setGotostep(Integer gotostep) {
		this.gotostep = gotostep;
	}

	/**
	 * @return the content
	 */
	@XmlValue
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

}
