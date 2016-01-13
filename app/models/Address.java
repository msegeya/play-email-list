package models;

import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address{

	@Id
    @GeneratedValue
    private int id;

	private String address;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}


	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
}