package it.polimi.dei.provafinale.carcassonne.model;

/**
 * Enumeration to represent side and entity types, being:
 * <ul>
 * 	<li><b>C</b>: City</li>
 * 	<li><b>S</b>: Road</li>
 * 	<li><b>N</b>: Nothing</li>
 * </ul>
 * */
public enum EntityType {
	C("C"), S("S"), N(" ");
	
	private String representation;
	
	EntityType(String representation){
		this.representation = representation;
	}
	
	public String getRepresentation(){
		return representation;
	}
}
