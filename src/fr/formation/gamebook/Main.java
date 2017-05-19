package fr.formation.gamebook;

public class Main {

	public static void main(String[] args) {
		if (args.length > 0 ) {
			new GameBook(args[0]).run();
		} else {
			System.err.println("Usage : Main <path_to_xml_data>");
		}
	}
}
