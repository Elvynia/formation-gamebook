package fr.formation.gamebook.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Choice implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	public int gotostep;

	@XmlValue
	public String description;
}
