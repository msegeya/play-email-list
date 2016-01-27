package services;

import static org.fest.assertions.Assertions.assertThat;

import configs.AppConfig;

import models.Address;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

@ContextConfiguration(classes = {
                AppConfig.class, config.TestDataConfig.class
})
public class AddressServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private AddressService addressService;

    // Tests that an Address object can be created and used to store a string.
    // also used as a helper method to test data storage service.
    @Test
    public void addAddressTest() {
        Address addr = new Address("address");
        addressService.addAddress(addr);

        //TODO: change to fetch
        assertThat(addr.getAddress()).isEqualTo("address");
    }

    // Tests that when an object is added to the data service the data service grows in length.
    @Test
    public void testAddAddressLonger() {
        List<Address> addressList = addressService.getAllAddresses();
        int size = addressList.size();
        //TODO addAddressTest();
        addressList = addressService.getAllAddresses();
        assertThat(addressList.size()).isEqualTo(size + 1);
    }

    // tests that duplicate entries are not added to the database and returned as false.
    @Test
    public void addAddressDuplicateTest() {
        Address a1 = new Address("a");
        Address a2 = new Address("a");
        int initialSize = addressService.getAllAddresses().size();
        // first should be true since object is NOT in DB.
        assertThat(addressService.addAddress(a1)).isEqualTo(true);
        //TODO assert size is zero ect.
        int secondSize = addressService.getAllAddresses().size();
        // second should be false since object IS in DB.
        assertThat(addressService.addAddress(a2)).isEqualTo(false);
        int finalSize = addressService.getAllAddresses().size();
        // secondSize should be 1 bigger than initialSize since an item was inserted.
        assertThat(secondSize).isEqualTo(initialSize + 1);
        // finalSize and secondSize should be equal because the second insert should fail.
        assertThat(finalSize).isEqualTo(secondSize);
    }

    // tests that adding a null object throws exception as expected.
    @Test(expected = NullPointerException.class)
    public void addAddressNullObjectTest() {
        Address none = null;
        addressService.addAddress(none);
    }

    // tests that adding an address that hasn't been initialized throws exception  as expected.
    @Test(expected = IllegalArgumentException.class)
    public void addAddressNullStringTest() {
        Address nully = new Address();
        addressService.addAddress(nully);
    }

    @Test
    public void addAddressEmptyTest() {
        Address empty = new Address("");
        addressService.addAddress(empty);
        //// TODO: 1/27/16
    }

    // Tests that deleteAddress can delete items from the DB.
    @Test
    public void deleteAddressTest() {
        Address a = new Address("a");
        Address b = new Address("b");
        int initialSize = addressService.getAllAddresses().size();

        // todo assert more sizes
        assertThat(addressService.addAddress(a)).isEqualTo(true);
        assertThat(addressService.addAddress(b)).isEqualTo(true);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(2);
        addressService.deleteAddress(a);
        addressService.deleteAddress(b);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(initialSize);
    }

    @Test
    public void deleteAddressNonexistentTest() {
        Address a = new Address("a");
        addressService.deleteAddress(a);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteAddressNullTest() {
        Address a = null;
        addressService.deleteAddress(a);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void deleteAddressNullStringTest() {
        Address a = new Address();
        addressService.deleteAddress(a);
    }

    //todo delete of ""

    //todo get all addresses
}
