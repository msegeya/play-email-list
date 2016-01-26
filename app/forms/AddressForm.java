package forms;

import models.TLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import play.Play;
import play.data.validation.Constraints.Required;
import services.TLDService;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Form to ask user for an email address.
 * <p>
 * address must pass play's{@link Required} validations.
 */
public class AddressForm{
    // validate that the input is non-empty.
    @Required
    private String address;
    private final static Logger log = LoggerFactory.getLogger(AddressForm.class);

    /**
     * Validate that a {@link String} is an email address. Used in conjunction with @Required
     * validation on {@link AddressForm}.
     * <p>
     * Needs {@link AddressForm#tlds} to be set for the class and contain a list of valid {@link TLD}s.
     * This can be set using {@link TLDService#getAllTLDs()}.
     * <p>
     * Checks that string passes initial regex (looks roughly like an email).
     * Checks that the overall length is less than 255.
     * Checks that the local length is less than 65.
     * Requires that the String does not contain '..' then splits string on periods.
     * Requires that result of split is at least length 2.
     * Then requires that the last substring from the split does not contain '@'.
     * Finally compares that substring against list of {@link TLD}s. If a match is found the string is accepted.
     *
     * @return Null if String is an email, or message detailing why String is not an email.
     */
    public String validate() {
        log.debug("Validating TLD.");

        // verify address meets regex, stolen from Play's email validation, but removed length constraints since they were not helpful
        // and are enforced below.
        final Pattern regex = java.util.regex.Pattern.compile("\\b[a-zA-Z0-9.!#$%&\'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)*\\b");
        if (!regex.matcher(address).matches()) {
            return Play.application().configuration().getString("msg.addressNotMatch");
        }

        //split the address on the @
        String[] splitAddress = address.split("@");

        // check the overall length of the address.
        if(address.length() > 254){
            log.debug("Overall address is too long. Length: {}", address.length());
            return String.format(Play.application().configuration().getString("msg.addressTooLongOverall"), address.length());
        }

        //check 'local' part for length.
        if(splitAddress[0].length() > 64){
            log.debug("Local part of address is too long. Length: {}", splitAddress[0].length());
            return String.format(Play.application().configuration().getString("msg.addressTooLongLocal"), splitAddress[0], splitAddress[0].length());
        }

        // Check for double period.
        if (address.contains("..")) {
            return Play.application().configuration().getString("msg.doublePeriod");
        }
        return null;
    }

    public AddressForm(){
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}
