package de.cpg_gilching.informatik11.gamelobby.spiele.osmos;

public class Vektor {
	
	public double x, y;
	
	public Vektor(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vektor() {
		this(0, 0);
	}
	
	public Vektor add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vektor add(Vektor anderer) {
		this.x += anderer.x;
		this.y += anderer.y;
		return this;
	}
	
	public Vektor kopiere(Vektor ziel) {
		this.x = ziel.x;
		this.y = ziel.y;
		return this;
	}
	
	public Vektor klonen() {
		return new Vektor(x, y);
	}
	
	@Override
	public int hashCode() {
		final int prime = 20149;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vektor))
			return false;
		
		Vektor anderer = (Vektor) obj;
		
		return (x == anderer.x && y == anderer.y);
	}
	
}
