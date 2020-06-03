package it.polito.tdp.crimes.model;

public class Arco {
	private String of1;
	private String of2;
	private int peso;
	public Arco(String of1, String of2, int peso) {
		super();
		this.of1 = of1;
		this.of2 = of2;
		this.peso = peso;
	}
	public String getOf1() {
		return of1;
	}
	public void setOf1(String of1) {
		this.of1 = of1;
	}
	public String getOf2() {
		return of2;
	}
	public void setOf2(String of2) {
		this.of2 = of2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((of1 == null) ? 0 : of1.hashCode());
		result = prime * result + ((of2 == null) ? 0 : of2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		if (of1 == null) {
			if (other.of1 != null)
				return false;
		} else if (!of1.equals(other.of1))
			return false;
		if (of2 == null) {
			if (other.of2 != null)
				return false;
		} else if (!of2.equals(other.of2))
			return false;
		return true;
	}
	
	
}
