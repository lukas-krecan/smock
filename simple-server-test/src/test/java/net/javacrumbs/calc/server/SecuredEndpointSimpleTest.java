package net.javacrumbs.calc.server;

import net.javacrumbs.smock.server.AbstractSmockServerTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/secured-spring-ws-servlet.xml"})
public class SecuredEndpointSimpleTest extends AbstractSmockServerTest{

	@Test
	public void testSimple() throws Exception {
		sendRequest(withMessage("request1.xml")).andExpect(noFault());
	}
	
	@Override
	protected ClientInterceptor[] getInterceptors() {
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setSecurementActions("UsernameToken Timestamp");
		securityInterceptor.setSecurementUsername("Bert");
		securityInterceptor.setSecurementPassword("Ernie");
		
		return new ClientInterceptor[]{securityInterceptor};
	}
}