package forms;

import play.data.validation.Constraints.Required;

/**
 * Form to ask user for an email address.
 * <p>
 * address must pass play's{@link Required} validations.
 */
public class AddressForm {
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
