package forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import javax.persistence.Column;

/**
 * Form to ask user for an email address.
 * <p>
 *     address must pass play's {@link Email} and {@link Required} validations.
 */
public class AddressForm {
    // validate that input is an email.
    @Email
    // validate that the input is non-empty.
    @Required
    private String address;

    public AddressForm() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}
