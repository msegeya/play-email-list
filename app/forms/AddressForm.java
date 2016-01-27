package forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.validation.Constraints.Required;
import play.i18n.Messages;

import java.util.regex.Pattern;

/**
 * Form to ask user for an email address.
 * <p>
 * address must pass play's{@link Required} validations.
 */
public class AddressForm {
    private static final Logger log = LoggerFactory.getLogger(AddressForm.class);
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_LOCAL_LENGTH = 64;
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
     * Requires that result of split is at least length 2.
     * Then requires that the last substring from the split does not contain '@'.
     * <p>
     * Does not check Top Level Domain validity.
     *
     * @return Null if String is an email, or message detailing why String is not an email.
     */
    public String validate() {
        log.debug("Validating TLD.");

        // verify address meets regex, stolen from Play's email validation, but removed length constraints since they were not helpful
        // and are enforced below.
        final Pattern regex = Pattern.compile(
                        "\\b[a-zA-Z0-9.!#$%&\'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)*\\b");
        if (!regex.matcher(address).matches()) {
            return "msg.addressNotMatch";
        }
        // check the overall length of the address.
        if (address.length() > MAX_EMAIL_LENGTH) {
            log.debug("Overall address is too long. Length: {}", address.length());
            return Messages.get("msg.addressTooLongOverall", address.length());
        }

        //split the address on the @
        String[] splitAddress = address.split("@");
        //check 'local' part for length.
        if (splitAddress[0].length() > MAX_LOCAL_LENGTH) {
            log.debug("Local part of address is too long. Length: {}", splitAddress[0].length());
            return Messages.get("msg.addressTooLongLocal", splitAddress[0], splitAddress[0].length());
        }

        // Check for double period.
        if (address.contains("..")) {
            return "msg.doublePeriod";
        }

        // try and grab from the '.' to the end
        splitAddress = address.split("\\.");
        if (splitAddress.length <= 1) {
            // if the array length must be greater than 1, there is no period.
            return "msg.noTLD";
        }

        String domain = splitAddress[splitAddress.length - 1];
        if (domain.contains("@")) {
            // if the last substring contains an @ then the address was like: 'a.b@c'
            return "msg.noTLD";
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
