package forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import javax.persistence.Column;

/**
 * Form to ask user for an email address.
 */
public class AddressForm {
    @Column(unique = true)
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
