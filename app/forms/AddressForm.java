package forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class AddressForm {
    @play.data.validation.Constraints.Email
    @play.data.validation.Constraints.Required
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}