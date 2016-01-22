
import java.util.HashMap;

import org.junit.*;
import static org.mockito.Mockito.*;

import play.mvc.Http;
import play.mvc.Result;

import static play.test.Helpers.BAD_REQUEST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import play.test.WithApplication;


import static org.fest.assertions.Assertions.*;

/**
 * Simple (JUnit) tests that can call all parts of a play app. If you are interested in mocking a whole application, see the wiki
 * for more details.
 */
public class ApplicationTest extends WithApplication {

    @Before
    public void setUp() throws Exception {
        Http.Context context = mock(Http.Context.class);
        Http.Context.current.set(context);
    }

    @Test
    public void testAddAddressAddDuplicate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "c@c");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {

                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("Address already exists in the list.");
            }
        });
    }

    @Test
    public void testAddAddressEmpty(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("This field is required");
            }
        });
    }

    @Test
    public void testAddAddressNotAddress(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "blah<>!@#$&%*^()'");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("Valid email required");
            }
        });
    }
}
