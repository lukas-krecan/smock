package net.javacrumbs.calc.server;

import static net.javacrumbs.smock.server.SmockServer.createClient;
import static net.javacrumbs.smock.server.SmockServer.withMessage;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/secured-spring-ws-servlet.xml"})
public class SecuredEndpointTest {

private MockWebServiceClient wsMockClient;
	private ClientInterceptor[] interceptors;
	

	@Autowired
	public void setApplicationContext(ApplicationContext context)
	{
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setSecurementActions("UsernameToken Timestamp");
		securityInterceptor.setSecurementUsername("Bert");
		securityInterceptor.setSecurementPassword("Ernie");
		
		this.interceptors =  new ClientInterceptor[]{securityInterceptor};
		wsMockClient = createClient(context, interceptors);
	}
	@Test
	public void testSimple() throws Exception {
		wsMockClient.sendRequest(withMessage("request1.xml")).andExpect(noFault());
	}
}