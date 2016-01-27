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
    @Test
    public void addAddressTest() {
        addressService.addAddress(new Address("address"));
        assertThat(addressService.getAllAddresses().get(0).getAddress()).isEqualTo("address");
    }

    // Tests that when an object is added to the data service the data service grows in length.
    @Test
    public void testAddAddressLonger() {
        List<Address> addressList = addressService.getAllAddresses();
        assertThat(addressList.size()).isEqualTo(0);
        addressService.addAddress(new Address("address"));
        addressList = addressService.getAllAddresses();
        assertThat(addressList.size()).isEqualTo(1);
    }

    // tests that duplicate entries are not added to the database and returned as false.
    @Test
    public void addAddressDuplicateTest() {
        Address a1 = new Address("a");
        Address a2 = new Address("a");
        // ensure that we start with an empty DB.
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
        // first should be true since object is NOT in DB.
        assertThat(addressService.addAddress(a1)).isEqualTo(true);
        // we inserted 1 item, 1 + 0 = 1 in most cases.
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
        // second should be false since object IS in DB.
        assertThat(addressService.addAddress(a2)).isEqualTo(false);
        // the insert should have failed, so make sure the size is the same.
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
    }

    // tests that adding a null object throws exception as expected.
    @Test(expected = NullPointerException.class)
    public void addAddressNullObjectTest() {
        addressService.addAddress(null);
    }

    // tests that adding an address that hasn't been initialized throws exception  as expected.
    @Test(expected = IllegalArgumentException.class)
    public void addAddressNullStringTest() {
        addressService.addAddress(new Address());
    }

    @Test
    public void addAddressEmptyTest() {
        // This should work, since "" is a valid String.
        addressService.addAddress(new Address(""));
    }

    // Tests that deleteAddress can delete items from the DB.
    @Test
    public void deleteAddressTest() {
        Address a = new Address("a");
        Address b = new Address("b");
        // ensure that we start with an empty DB.
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
        assertThat(addressService.addAddress(a)).isEqualTo(true);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
        assertThat(addressService.addAddress(b)).isEqualTo(true);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(2);

        addressService.deleteAddress(a);
        // make sure the right object was deleted.
        assertThat(addressService.getAllAddresses().contains(a)).isEqualTo(false);
        assertThat(addressService.getAllAddresses().contains(b)).isEqualTo(true);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
        addressService.deleteAddress(b);
        // make sure we deleted b.
        assertThat(addressService.getAllAddresses().contains(b)).isEqualTo(false);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
    }

    @Test
    public void deleteAddressNonexistentTest() {
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
        addressService.deleteAddress(new Address("a"));
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteAddressNullTest() {
        addressService.deleteAddress(null);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void deleteAddressNullStringTest() {
        addressService.deleteAddress(new Address());
    }

    @Test
    public void deleteEmptyStringNonexistantTest(){
        addressService.deleteAddress(new Address(""));
    }

    @Test
    public void deleteEmptyStringTest(){
        Address a = new Address("");
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
        addressService.addAddress(a);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
        addressService.deleteAddress(a);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
    }

    // test that getAllAddresses returns an empty list if the DB is empty.
    @Test
    public void getAllAddressesEmptyTest(){
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
    }

    // Test that an added address gets returned by getAllAddresses.
    @Test
    public void getAllAddressesSingleTest(){
        Address a = new Address("a");
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
        addressService.addAddress(a);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
        assertThat(addressService.getAllAddresses().get(0)).isEqualTo(a);
    }

    // Test adding multiple addresses and ensure that they get returned by getAllAddresses.
    @Test
    public void getAllAddressesMultipleTest(){
        Address a = new Address("a");
        Address b = new Address("b");
        assertThat(addressService.getAllAddresses().size()).isEqualTo(0);
        addressService.addAddress(a);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(1);
        assertThat(addressService.getAllAddresses().get(0)).isEqualTo(a);
        addressService.addAddress(b);
        assertThat(addressService.getAllAddresses().size()).isEqualTo(2);
        assertThat(addressService.getAllAddresses().get(1)).isEqualTo(b);
    }
}
