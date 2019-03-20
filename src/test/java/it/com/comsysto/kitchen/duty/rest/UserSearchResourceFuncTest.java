package it.com.comsysto.kitchen.duty.rest;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.comsysto.kitchen.duty.rest.UserSearchResource;
import com.comsysto.kitchen.duty.rest.UserSearchResourceModel;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

public class UserSearchResourceFuncTest {

    String baseUrl;

    @Before
    public void setup() {
        baseUrl = System.getProperty("baseurl");
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testSearchUser__unAuthorized() {
        String resourceUrl = baseUrl + "/rest/kitchenduty/1.0/user/search?query=adm";
        String response = httpGet(resourceUrl);
        assertNotNull("should not be null", response);
        assertEquals("should contain unauthorized message",
            toJSON("{'message':'Client must be authenticated to access this resource.','status-code':401}"),
            response);
    }
    @Test
    public void testSearchUser__authorized() {
        String resourceUrl = baseUrl + "/rest/kitchenduty/1.0/user/search?query=adm";
        String response = httpGet(resourceUrl, "admin", "admin");
        assertNotNull("should not be null", response);
        assertEquals("should contain admin",
            toJSON("[{'text':'admin','id':'admin'}]"),
            response);
    }

    private String toJSON(String text) {
        return text.replaceAll("'", "\"");
    }

    private String httpGet(String url) {
        return _httpGet(url, null);
    }

    private String httpGet(String url, String username, String password) {
        ClientConfig config = new ClientConfig();
        BasicAuthSecurityHandler basicAuthSecHandler = new BasicAuthSecurityHandler();
        basicAuthSecHandler.setUserName(username);
        basicAuthSecHandler.setPassword(password);
        config.handlers(basicAuthSecHandler);
        return _httpGet(url, config);
    }

    private String _httpGet(String url, ClientConfig config) {
        RestClient client = new RestClient();
        if (config != null) {
            client = new RestClient(config);
        }
        Resource resource = client.resource(url);
        return resource
            .header("Accept", "application/json;q=1.0")
            .get()
            .getEntity(String.class);
    }
}
