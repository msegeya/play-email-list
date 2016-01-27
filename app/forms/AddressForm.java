package forms;

import models.TLD;

import services.TLDService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;
import play.data.validation.Constraints.Required;

import java.util.regex.Pattern;

/**
 * Form to ask user for an email address.
 * <p>
 * address must pass play's{@link Required} validations.
 */
public class AddressForm {
    private final static Logger log = LoggerFactory.getLogger(AddressForm.class);
    // validate that the input is non-empty.
    @Required
    private String address;

    public AddressForm() {
    }

    /**
     * Validate that a {@link String} is an email address. Used in conjunction with @Required
     * validation on {@link AddressForm}.
     * <p>
     * Checks that string passes initial regex (looks roughly like an email).
     * Checks that the overall length is less than 255.
     * Checks that the local length is less than 65.
     * Requires that the String does not contain '..' then splits string on periods.
     *
     * Does not check Top Level Domain validity.
     *
     * @return Null if String is an email, or message detailing why String is not an email.
     */
    public String validate() {
        log.debug("Validating TLD.");

        // verify address meets regex, stolen from Play's email validation, but removed length constraints since they were not helpful
        // and are enforced below.
        final Pattern
                        regex =
                        java.util.regex.Pattern.compile(
                                        "\\b[a-zA-Z0-9.!#$%&\'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)*\\b");
        if (!regex.matcher(address).matches()) {
            return Play.application().configuration().getString("msg.addressNotMatch");
        }

        //split the address on the @
        String[] splitAddress = address.split("@");

        // check the overall length of the address.
        if (address.length() > 254) {
            log.debug("Overall address is too long. Length: {}", address.length());
            return String.format(Play.application().configuration().getString("msg.addressTooLongOverall"), address.length());
        }

        //check 'local' part for length.
        if (splitAddress[0].length() > 64) {
            log.debug("Local part of address is too long. Length: {}", splitAddress[0].length());
            return String.format(Play.application().configuration().getString("msg.addressTooLongLocal"), splitAddress[0],
                                 splitAddress[0].length());
        }

        // Check for double period.
        if (address.contains("..")) {
            return Play.application().configuration().getString("msg.doublePeriod");
        }
        return null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}
