package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;

import configs.AppConfig;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;

/**
 * Simple (JUnit) tests that can call all parts of a play app. If you are interested in mocking a whole application, see the wiki
 * for more details.
 */
@ContextConfiguration(classes = {
                AppConfig.class, config.TestDataConfig.class
})
public class ApplicationTest extends WithApplication {

    @Autowired
    Application controller;

    @Before
    public void setUp() throws Exception {
        Http.Context context = mock(Http.Context.class);
        Http.Context.current.set(context);
    }

    @Test
    public void testAddAddressAddDuplicate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "c@c.com");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("Address already exists in the list.");
            }
        });
    }

    @Test
    public void testAddAddressEmpty() {
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
    public void testAddAddressIllegalCharacters() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "blah<>!@#$&%*^()'");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("Please enter a valid email.");
            }
        });
    }

    @Test
    public void testAddAddressLocal64() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "local64aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@a.com");

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
    public void testAddAddressOAL254() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address",
                "oal254@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com");

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
    public void testAddAddressLocal65() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address", "local65aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@a.com");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("Max length is 64 characters.");
            }
        });
    }

    @Test
    public void testAddAddressOAL255() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Address",
                "oal255@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com");

        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.addAddress(), fakeRequest().withFormUrlEncodedBody(map));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("Max length is 254 characters.");
            }
        });
    }

}
