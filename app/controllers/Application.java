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

@org.springframework.stereotype.Controller
public class Application extends Controller{

	final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Autowired
    private AddressService addressService;

    public Result index() {
    	logger.debug("Generated a generic homepage.");
        return play.mvc.Controller.ok(index.render(Form.form(AddressForm.class)));
    }

    public Result addAddress() throws Exception {

        Form<AddressForm> form = Form.form(AddressForm.class).bindFromRequest();
        if (form.hasErrors()) {
            return play.mvc.Controller.badRequest(index.render(form));
        }

        AddressForm addressForm = form.get();
        Address address = new Address();
        address.setAddress(addressForm.getAddress());

        addressService.addAddress(address);
        return redirect(routes.Application.index());
    }
}
