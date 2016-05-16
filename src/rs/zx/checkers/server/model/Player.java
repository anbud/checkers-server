package rs.zx.checkers.server.model;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = 231122823906651732L;
	
	private String id;
	private String name;
	
	public Player(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
