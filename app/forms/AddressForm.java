package forms;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class AddressForm {
    @play.data.validation.Constraints.Required
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}