package controllers;

import models.Address;

import services.AddressService;
import forms.AddressForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import views.html.index;

import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
* Controller for application. 
* <p> 
* Gets all the view and model to talk to eachother. 
*/

@org.springframework.stereotype.Controller
public class Application extends Controller{

	final Logger logger = LoggerFactory.getLogger(Controller.class);

	// use injection to set up the addressService for talking to the database. 
	@Autowired
    private AddressService addressService;

	/**
	* Builds the homepage and returns.
	* @return the homepage
	*/
    public Result index() {
    	logger.info("Generated a generic homepage.");
        return play.mvc.Controller.ok(index.render(Form.form(AddressForm.class)));
    }

    /**
    *	Talks to both the form and the service to try and add a new address. 
    * <p>
    * Extracts information from the form. If the form is blank it diplays an error. 
    * <p>
    *  Passes information from the form to the data service to store.
    * if the data service storage already contains the information, it catches 
    * the exception and displays the error message. 
	*
    * @return the result to be displayed to the user. 
    */
    public Result addAddress(){
    	logger.info("Collecting new result... ");
        Form<AddressForm> form = Form.form(AddressForm.class).bindFromRequest();
        if (form.hasErrors()) {
        	logger.info("Bad input on result.");
            return play.mvc.Controller.badRequest(index.render(form));
        }

        AddressForm addressForm = form.get();
        Address address = new Address();
        address.setAddress(addressForm.getAddress());

        logger.info("Got a new address: {}", address.getAddress());

        try{
        	addressService.addAddress(address);
        }catch (DataIntegrityViolationException e){
        	form.reject("address","Address already exists in the list.");
        	return play.mvc.Controller.badRequest(index.render(form));
        }
        return redirect(routes.Application.index());
    }

    public Result listAddress() {
       List <Address> al = addressService.getAllAddresses();
       List <AddressForm> af = new ArrayList<AddressForm>();

       for (Address i : al) {
           AddressForm aform = new AddressForm();
           aform.setAddress(i.getAddress());
           af.add(aform);
       }
        return play.mvc.Controller.ok(Json.toJson(af));
    }
}
