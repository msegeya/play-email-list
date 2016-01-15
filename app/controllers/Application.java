package controllers;

import models.Address;

import services.AddressService;
import forms.AddressForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import views.html.index;

import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

@org.springframework.stereotype.Controller
public class Application extends Controller{

	final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Autowired
    private AddressService addressService;

    public Result index() {
    	logger.info("Generated a generic homepage.");
        return play.mvc.Controller.ok(index.render(Form.form(AddressForm.class)));
    }

    public Result addAddress() throws Exception {
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

        addressService.addAddress(address);
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
