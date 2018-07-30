package org.oversky.code.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private List<Table> tables = new ArrayList<Table>();

	public void addTable(Table table){
		tables.add(table);
	}
	
	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
}
