import static org.fest.assertions.Assertions.assertThat;

import configs.AppConfig;

import models.Address;

import services.AddressService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

@ContextConfiguration(classes={AppConfig.class, TestDataConfig.class})
public class ServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private AddressService barService;

    // Tests that an Address object can be created and used to store a string. 
    // also used as a helper method to test data storage service. 
    @Test
    public void createBar() {
        Address bar = new Address();
        bar.setAddress("foo");
        barService.addAddress(bar);
        assertThat(bar.getAddress()).isEqualTo("foo");
    }

    // Tests that when an object is added to the data service the data service grows in length. 
    @Test
    public void getBars() {
        List<Address> bars = barService.getAllAddresses();
        int ds = bars.size();
        createBar();
        bars = barService.getAllAddresses();
        assertThat(bars.size()).isEqualTo(ds+1);
    }

}