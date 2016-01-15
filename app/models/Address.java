package models;

import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address{

	@Id
	private String address;

	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
}