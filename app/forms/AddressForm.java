package forms;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class AddressForm {
    @Required (message = "Address is required")
    @MaxLength(message = "Address must be less than 26 characters", value = 25)
    @MinLength(message = "Address must be greater than 3 characters", value = 4)
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        address = inputAddress;
    }
}