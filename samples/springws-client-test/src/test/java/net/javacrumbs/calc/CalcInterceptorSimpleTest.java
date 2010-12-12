package net.javacrumbs.calc;

import static org.junit.Assert.assertEquals;
import net.javacrumbs.smock.springws.client.AbstractSmockClientTest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcInterceptorSimpleTest extends AbstractSmockClientTest{
    @Autowired
    private Calc calc;
    
    
    @After
    public void verify()
    {
    	super.verify();
    }

	@Test
	public void testSimple()
	{
		expect(anything()).andRespond(withMessage("response1.xml"));

		int result = calc.plus(1, 2);
		assertEquals(3, result);
	}
	
	@Override
	protected EndpointInterceptor[] getInterceptors() {
		return new EndpointInterceptor[]{new PayloadLoggingInterceptor()};
	}
}
