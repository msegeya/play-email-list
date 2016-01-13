package controllers;

import models.Address;

import services.AddressService;
import services.AddressServiceImpl;
import forms.AddressForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import views.html.index;

import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;


public class Application extends Controller{

	
    private static AddressService addressService = new AddressServiceImpl();

    public static Result index() {
        return play.mvc.Controller.ok(index.render("Play Email List", Form.form(Address.class)));
    }

    public static Result addAddress() {
        // play.data.Form<models.Address> form = play.data.Form.form(models.Address.class).bindFromRequest();
        // models.Address address = form.get();
        // //address.save();
        // return redirect(routes.Application.index());


        Form<Address> form = Form.form(Address.class).bindFromRequest();
        if (form.hasErrors()) {
            return play.mvc.Controller.badRequest(index.render("Play Email List",form));
        }
        Address address = form.get();
        addressService.addAddress(address);
        return redirect(routes.Application.index());
    }
}
