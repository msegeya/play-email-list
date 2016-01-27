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

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Map<String, Boolean> tlds;

    // use injection to set up the addressService for talking to the database.
    @Autowired
    private AddressService addressService;

    @Autowired
    public Application(TLDService tldService) {
        // loop through all the TLDs stored in the DB and store them in the hashmap.
        tlds = new HashMap<>();
        for (TLD tld : tldService.getAllTLDs()) {
            // ensure that all the TLD keys are in uppercase.
            tlds.put(tld.getDomain().toUpperCase(), true);
        }
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

        // Couldn't validate TLD in form because Form doesn't have access to list of TLDs.
        String msg = validateTLD(form.get().getAddress());
        if (msg != null) {
            log.debug("Bad TLD.");
            form.reject("address", msg);
            return badRequest(index.render(form, listAddress()));
        }
        // create a new address object to contain the information from the form.
        Address address = new Address(form.get().getAddress());

        // try to add the address to the DB.
        // will return false if address was in DB, in which case we should inform the user.
        if (!addressService.addAddress(address)) {
            form.reject("address", "msg.duplicate");
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
        Address addr = new Address(address);
        addressService.deleteAddress(addr);
        return redirect(routes.Application.index());
    }

    /**
     * Validate the Top level domain of an email address
     * <p>
     * Needs {@link Application#tlds} to be set for the class and contain a list of valid {@link TLD}s.
     * This can be set using {@link TLDService#getAllTLDs()}.
     * <p>
     * Input string should have first been validated by {@link AddressForm#validate()}.
     * <p>
     * <p>
     * Finally compares that substring against list of {@link TLD}s. If a match is found the string is accepted.
     *
     * @param address the email address as a String to validate.
     * @return null if the top level domain is valid, or a string detailing why it is not.
     */
    private String validateTLD(String address) {
        // try and grab from the '.' to the end
        // can be liberal with not checking since validation should be preformed by the form.
        String[] splitAddress = address.split("\\.");
        String domain = splitAddress[splitAddress.length - 1];
        log.debug("Extracted domain: {} from Address: ", domain, address);

        // check if the TLD (as an uppercase) is in the list.
        if (tlds.containsKey(domain.toUpperCase())) {
            log.debug("Found the domain.");
            // TLD was in map. All is well: return null.
            return null;
        }

        // unable to validate TLD, return error.
        log.info("Unable to find domain: {}", domain);
        return Messages.get("msg.invalidTLD", domain.toLowerCase());
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

