package net.javacrumbs.calc.server;

import static net.javacrumbs.smock.springws.server.SmockServer.message;
import static net.javacrumbs.smock.springws.server.SmockServer.withMessage;
import static org.springframework.ws.test.server.MockWebServiceClient.createClient;

import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.smock.common.groovy.GroovyTemplateProcessor;
import net.javacrumbs.smock.springws.server.SmockServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml"})
public class GroovyEndpointTest {
	
	private MockWebServiceClient wsMockClient;
	
	@Autowired
	public void setApplicationContext(ApplicationContext context)
	{
		SmockServer.setTemplateProcessor(new GroovyTemplateProcessor());
		wsMockClient = createClient(context);
	}

	

	@Test
	public void testResponseTemplate() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 1);
		params.put("b", 2);
		wsMockClient.sendRequest(withMessage("request-context-groovy.xml").withParameters(params)).andExpect(message("response-context-groovy.xml"));
	}
}