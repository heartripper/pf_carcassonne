package it.polimi.dei.provafinale.carcassonne.model;


public final class EntityFactory {

	private EntityFactory() {
	}

	/**
	 * Creates a new instance of the Entity implementation corresponding to the
	 * given type.
	 * @param type - the type of the entity to create
	 * @return a new instance of a Entity of the given type.
	 * */
	public static Entity createByType(EntityType type) {
		switch (type) {
		case C:
			return new City();
		case S:
			return new Road();
		default:
			return null;
		}

	}
}
