package net.javacrumbs.calc;

import static net.javacrumbs.smock.client.SmockClient.createServer;
import static net.javacrumbs.smock.client.SmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.test.client.MockWebServiceServer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcInterceptorTest {
    @Autowired
    private Calc calc;
    
    private MockWebServiceServer mockServer;
    
    //inject mock control
    @Autowired
    private WebServiceTemplate webServiceTemplate;
    
    @Before
    public void setUpMocks() throws Exception {
        mockServer = createServer(webServiceTemplate, new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
    }
    
    @After
    public void verify()
    {
    	mockServer.verify();
    }

	@Test
	public void testSimple()
	{
		mockServer.expect(anything()).andRespond(withMessage("response1.xml"));

		int result = calc.plus(1, 2);
		assertEquals(3, result);
	}
}
