package com.ilona.lists;

public class Item {
	private long id;
	private String name;
	private boolean checked;
	
	public Item(long id, String name, int checked){
		this.id=id;
		this.name=name;
		if(checked==1){
			this.checked=true;
		}else if(checked==0){
			this.checked=false;
		}
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String toString(){
		return name;
	}

}
