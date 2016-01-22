package services;

import configs.AppConfig;
import models.Address;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@ContextConfiguration(classes = {
                AppConfig.class, config.TestDataConfig.class
})
public class ServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Autowired
    private AddressService barService;

    // Tests that an Address object can be created and used to store a string.
    // also used as a helper method to test data storage service.
    @Test
    public void createBar() {
        Address bar = new Address();
        bar.setAddress("bar");
        barService.addAddress(bar);
        assertThat(bar.getAddress()).isEqualTo("bar");
    }

    // Tests that when an object is added to the data service the data service grows in length.
    @Test
    public void getBars() {
        List<Address> bars = barService.getAllAddresses();
        int ds = bars.size();
        createBar();
        bars = barService.getAllAddresses();
        assertThat(bars.size()).isEqualTo(ds + 1);
    }

}
