package it.polimi.dei.provafinale.carcassonne.model.gamelogic.card;

public class SideConnection {
	private Side side1;
	private Side side2;

	public SideConnection(Side side1, Side side2) {
		this.side1 = side1;
		this.side2 = side2;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SideConnection)) {
			return false;
		}

		SideConnection otherConn = (SideConnection) obj;
		return (side1 == otherConn.side1 && side2 == otherConn.side2)
				|| (side1 == otherConn.side2 && side2 == otherConn.side1);
	}
}
