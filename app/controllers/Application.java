package controllers;

import forms.AddressForm;
import models.Address;
import models.TLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.AddressService;
import services.TLDService;
import views.html.index;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for application.
 * <p>
 * Gets all the view and model to talk to each other.
 */

@org.springframework.stereotype.Controller
public class Application extends Controller {

    private final static Logger log = LoggerFactory.getLogger(Application.class);

    private List<TLD> tlds;

    // use injection to set up the addressService for talking to the database.
    @Autowired
    private AddressService addressService;

    @Autowired
    public Application(TLDService tldService) {
        tlds = tldService.getAllTLDs();
    }

    /**
     * Builds the homepage and returns.
     *
     * @return the homepage
     */
    public Result index() {
        log.debug("Generated a homepage.");
        return ok(index.render(Form.form(AddressForm.class)));
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
            return badRequest(index.render(form));
        }

        // create a new address object to contain the information from the form.
        Address address = new Address();
        address.setAddress(form.get().getAddress());
        log.info("Got a new address: '{}'", address.getAddress());

        // validate the address has a TLD.
        String msg = validate(address.getAddress());
        if (msg != null) {
            //address does not have a TLD, return error.
            form.reject("address", msg);
            return badRequest(index.render(form));
        }

        // try to add the address to the DB.
        // will return false if address was in DB, in which case we should inform the user.
        if (!addressService.addAddress(address)) {
            form.reject("address", Play.application().configuration().getString("msg.duplicate"));
            log.info("Address '{}' was already in DB. Notifying user.", address.getAddress());
            return badRequest(index.render(form));
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
        return ok(Json.toJson(af));
    }

    // Validate address has TLD.
    private String validate(String address) {
        log.debug("Validating TLD.");

        // Check for double period.
        if (address.contains("..")) {
            return Play.application().configuration().getString("msg.doublePeriod");
        }

        // try and grab from the '.' to the end
        String[] splitAddress = address.split("\\.");
        if (splitAddress.length <= 1) {
            // if the array length is 1, there is no period.
            return Play.application().configuration().getString("msg.noTLD");
        }

        String domain = splitAddress[splitAddress.length - 1];
        if (domain.contains("@")) {
            // if the last substring contains an @ then the address was like: 'a.b@c'
            return Play.application().configuration().getString("msg.noTLD");
        }

        log.debug("Extracted domain: {} from Address: ", domain, address);
        TLD toValidate = new TLD();
        toValidate.setDomain(domain.toUpperCase());

        // check if the TLD is in the list.
        if (tlds.contains(toValidate)) {
            // TLD was in list. All is well: return null.
            return null;
        }

        // unable to validate TLD, return error.
        log.info("Unable to find domain: {}", domain);
        return String.format(Play.application().configuration().getString("msg.invalidTLD"), domain.toLowerCase());
    }
}

