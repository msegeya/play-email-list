package forms;

import models.TLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import services.TLDService;

import java.util.List;

/**
 * Form to ask user for an email address.
 */
public class AddressForm {

    final Logger log = LoggerFactory.getLogger(AddressForm.class);

    @Autowired
    private TLDService tldService;

    List<TLD> tlds;

    // validate that input is an email.
    @Email
    // validate that the input is non-empty.
    @Required
    private String address;

    public AddressForm(){
    }

    //Validate address has TLD.
    public String validate(){
        log.error("validate");
        if(tldService == null){
            log.error("tlds was null");
            tlds = tldService.getAllTLDs();
        }
//        //try and grab from the '.' to the end
//        String[] splitAddress = address.split("\\.");
//        String domain = splitAddress[splitAddress.length - 1];
//
//        // check if the TLD is in the list.
//        if(tlds.contains(domain.toUpperCase())){
            return null;
//        }
//        else return "Domain " + domain.toLowerCase() + " is not a valid Top Level Domain.";
    }




    public String getAddress() {
        return address;
    }

    public void setAddress(String inputAddress) {
        log.error("setting address");
        address = inputAddress;
    }
}
