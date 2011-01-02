package net.javacrumbs.calc;

import static net.javacrumbs.smock.http.client.connection.SmockClient.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;
import net.javacrumbs.smock.http.client.connection.MockWebServiceServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcInterceptorTest {
    @Autowired
    private CalcService calc;
    
    private MockWebServiceServer mockServer;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Before
    public void setUpMocks() throws Exception {
        mockServer = createServer(applicationContext, new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
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

		long result = calc.plus(1, 2);
		assertEquals(3, result);
	}
}
