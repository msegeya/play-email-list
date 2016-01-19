package forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

/**
 * Form to ask user for an email address.
 */
public class AddressForm {
    // validate that input is an email.
    @play.data.validation.Constraints.Email
    // validate that the input is non-empty.
    @play.data.validation.Constraints.Required
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}
