package controllers;

import models.Address;
import models.TLD;

import services.AddressService;
import services.TLDService;

import views.html.index;

import forms.AddressForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Controller for application.
 * <p>
 * Gets all the view and model to talk to each other.
 * Requires an {@link AddressService} to store and lookup {@link Address} objects
 * and a {@link TLDService} to lookup {@link TLD} objects in the database.
 */

@org.springframework.stereotype.Controller
public class Application extends Controller {

    private final static Logger log = LoggerFactory.getLogger(Application.class);
    private final List<TLD> tlds;

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
        return ok(index.render(Form.form(AddressForm.class), listAddress()));
    }

    /**
     * Talks to both the {@link AddressForm} and the {@link AddressService} to try and add a new {@link Address}.
     * <p>
     * Extracts information from the form and validates as an email address.  If the form does not contain an email address, it displays an error.
     * <p>
     * Passes information from the form to the address data service to store. If the data service already contains the information it displays an error.
     *
     * @return the resulting page to be displayed to the user.
     */
    public Result addAddress() {
        log.debug("Collecting new input... ");

        Form<AddressForm> form = Form.form(AddressForm.class).bindFromRequest();

        // check the form for errors.
        if (form.hasErrors()) {
            log.debug("Bad input.");
            return badRequest(index.render(form, listAddress()));
        }
        log.debug("Got new input: '{}'", form.get().getAddress());

        String msg = validateTLD(form.get().getAddress());
        if (msg != null) {
            log.debug("Bad TLD.");
            form.reject("address", msg);
            return badRequest(index.render(form, listAddress()));
        }
        // create a new address object to contain the information from the form.
        Address address = new Address();
        address.setAddress(form.get().getAddress());

        // try to add the address to the DB.
        // will return false if address was in DB, in which case we should inform the user.
        if (!addressService.addAddress(address)) {
            form.reject("address", Play.application().configuration().getString("msg.duplicate"));
            log.info("Address '{}' was already in DB. Notifying user.", address.getAddress());
            return badRequest(index.render(form, listAddress()));
        }

        // Display the homepage.
        return redirect(routes.Application.index());
    }

    /**
     * Remove an address from the database.
     *
     * @param address The address to be removed.
     * @return Redirects to a redraw of the homepage.
     */
    public Result deleteAddress(String address) {
        log.info("Deleting address: {}", address);
        Address addr = new Address();
        addr.setAddress(address);
        addressService.deleteAddress(addr);
        return redirect(routes.Application.index());
    }

    private String validateTLD(String address) {
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

        log.debug("Checking the domain.");
        // check if the TLD is in the list.
        if (tlds.contains(toValidate)) {
            log.debug("Found the domain.");
            // TLD was in list. All is well: return null.
            return null;
        }

        // unable to validate TLD, return error.
        log.info("Unable to find domain: {}", domain);
        return String.format(Play.application().configuration().getString("msg.invalidTLD"), domain.toLowerCase());
    }

    /**
     * Builds a list of the addresses in the DB. Used to display addresses in DB to user in index generation.
     */
    private List<Address> listAddress() {
        // Get the addresses from the DB.
        List<Address> al = addressService.getAllAddresses();
        log.info("Returning list of stored addresses. Length: {}", al.size());
        return al;
    }
}

