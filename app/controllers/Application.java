package controllers;

import models.Address;

import forms.AddressForm;

import java.util.ArrayList;
import java.util.List;

import models.TLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;
import play.Play;

import services.AddressService;
import services.TLDService;

import views.html.index;

/**
 * Controller for application.
 * <p>
 * Gets all the view and model to talk to each other.
 */

@org.springframework.stereotype.Controller
public class Application extends Controller {

    final Logger log = LoggerFactory.getLogger(Controller.class);

    private List<TLD> tlds;

    // use injection to set up the addressService for talking to the database.
    @Autowired
    private AddressService addressService;

    @Autowired
    private TLDService tldService;

    /**
     * Builds the homepage and returns.
     *
     * @return the homepage
     */
    public Result index() {
        log.debug("Generated a homepage.");
        return play.mvc.Controller.ok(index.render(Form.form(AddressForm.class)));
    }

    /**
     * Talks to both the form and the service to try and add a new address.
     * <p>
     * Extracts information from the form. If the form is blank it displays an error.
     * <p>
     * Passes information from the form to the data service to store. if the data service storage already contains the information,
     * it catches the exception and displays the error message.
     *
     * @return the result to be displayed to the user.
     */
    public Result addAddress() {
        log.debug("Collecting new input... ");

        Form<AddressForm> form = Form.form(AddressForm.class).bindFromRequest();

        // check the form for errors.
        if (form.hasErrors()) {
            log.debug("Bad input.");
            return play.mvc.Controller.badRequest(index.render(form));
        }

        // create a new address object to contain the information from the form.
        Address address = new Address();
        address.setAddress(form.get().getAddress());
        log.info("Got a new address: '{}'", address.getAddress());

        // validate the address has a TLD.
        String msg = validate(address.getAddress());
        if(msg != null){
            //address does not have a TLD, return error.
            form.reject("address", msg);
            return play.mvc.Controller.badRequest(index.render(form));
        }

        // try to store the new address in the database.
        try {
            addressService.addAddress(address);
        } catch (DataIntegrityViolationException e) {
            // this will get thrown if the address already exists in the DB.
            // if this happens, notify the user that the address was in the DB.
            form.reject("address", Play.application().configuration().getString("msg.duplicate"));
            log.info("Address '{}' was already in DB. Notifying user.", address.getAddress());
            return play.mvc.Controller.badRequest(index.render(form));
        }
        // Display the homepage.
        return redirect(routes.Application.index());
    }

    /**
     * Builds a json list of the addresses in the DB.
     */
    public Result listAddress() {
        // Get the addresses from the DB.
        List<Address> al = addressService.getAllAddresses();
        log.info("Returning list of stored addresses. Length: {}", al.size());
        List<AddressForm> af = new ArrayList<AddressForm>();

        // loop through them, wrap in forms, store in a list.
        for (Address i : al) {
            AddressForm aform = new AddressForm();
            aform.setAddress(i.getAddress());
            af.add(aform);
        }

        // convert the form list to json and return.
        return play.mvc.Controller.ok(Json.toJson(af));
    }

    //Validate address has TLD.
    private String validate(String address){
        log.debug("Validating TLD.");
        if(tlds == null){
            log.debug("tlds was null, getting all tlds.");
            tlds = tldService.getAllTLDs();
        }
        //try and grab from the '.' to the end
        String[] splitAddress = address.split("\\.");

        if(splitAddress.length <= 1){
            return "Address does not have valid domain i.e. '.com'";
        }

        String domain = splitAddress[splitAddress.length - 1];

        if(domain.contains("@")){
            return "Address does not have valid domain i.e. '.com'";
        }

        log.debug("Extracted domain: {} from Address: ", domain, address);
        TLD toValidate = new TLD();
        toValidate.setDomain(domain.toUpperCase());

        // check if the TLD is in the list.
        if (tlds.contains(toValidate)){
            return null;
        }
        else{
            log.info("Unable to find domain: {}", domain);
            return "Domain '" + domain.toLowerCase() + "' is not a valid Top Level Domain.";
        }
    }
}
