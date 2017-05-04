package fr.formation.gamebook.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Choice {

	@XmlAttribute
	public int gotostep;
	@XmlValue
	public String description;
}
