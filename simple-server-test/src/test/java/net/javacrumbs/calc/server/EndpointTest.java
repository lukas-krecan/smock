package net.javacrumbs.calc.server;

import static net.javacrumbs.smock.server.SmockServer.*;
import static org.springframework.ws.test.server.MockWebServiceClient.*;
import static org.springframework.ws.test.server.ResponseMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(SpringJUnit4ClassRunner.class)
// your endpoint configuration + Default helper config
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml"})
public class EndpointTest {

	
	private MockWebServiceClient wsMockClient;
	
	@Autowired
	public void setApplicationContext(ApplicationContext context)
	{
		wsMockClient = createClient(context);
	}

	@Test
	public void testSimple() throws Exception {
		// simulates request coming to MessageDispatcherServlet
		wsMockClient.sendMessage(withContent("request1.xml")).andExpect(anything());
	}
	
	@Test
	public void testCompare() throws Exception {
		wsMockClient.sendMessage(withContent("request1.xml")).andExpect(payload(fromResource("response1.xml")));
	}
//	@Test
//	public void testValidateResponse() throws Exception {
//		wsMockClient.sendMessage(withContent("request1.xml")).andExpect();
//		// simulates request coming to MessageDispatcherServlet
//		MessageContext message = helper.receiveMessage("request1.xml");
//		//validate response
//		helper.createMessageValidator(message.getResponse()).assertNotSoapFault().validate("xsd/calc.xsd").compare("response1.xml");
//				
//	}
//	@Test
//	public void testAssertXPath() throws Exception {
//		// simulates request coming to MessageDispatcherServlet
//		MessageContext message = helper.receiveMessage("request1.xml");
//		//validate response
//		helper.createMessageValidator(message.getResponse()).assertNotSoapFault().validate("xsd/calc.xsd").addNamespaceMapping("ns", "http://javacrumbs.net/calc").assertXPath("//ns:result=3");
//	}
//
	@Test
	public void testError() throws Exception {
		wsMockClient.sendMessage(withContent("request-error.xml")).andExpect(message("response-error.xml"));
	}
//	@Test
//	public void testErrorMessage() throws Exception {
//		// simulates request coming to MessageDispatcherServlet
//		MessageContext message = helper.receiveMessage("request-error.xml");
//		// compare response with control response
//		helper.createMessageValidator(message.getResponse()).assertSoapFault().assertFaultStringOrReason("Validation error").assertContain("'aaa' is not a valid value for 'integer'").assertContainElement("faultstring");
//	}
//
//	@Test
//	public void testResponseTemplate() throws Exception {
//		WsTestContextHolder.getTestContext().setAttribute("a", 1);
//		WsTestContextHolder.getTestContext().setAttribute("b", 2);
//		WsTestContextHolder.getTestContext().setAttribute("result", 3);
//		// simulates request coming to MessageDispatcherServlet
//		MessageContext message = helper.receiveMessage("request-context-xslt.xml");
//		// compare response with control response
//		helper.createMessageValidator( message.getResponse()).assertNotSoapFault().compare("response-context-xslt.xml");
//		
//		WsTestContextHolder.getTestContext().clear();
//	}
//	
//	@Test
//	public void testAssertXPathContext() throws Exception {
//		WsTestContextHolder.getTestContext().setAttribute("a", 1);
//		WsTestContextHolder.getTestContext().setAttribute("b", 2);
//		WsTestContextHolder.getTestContext().setAttribute("result", 3);
//		// simulates request coming to MessageDispatcherServlet
//		MessageContext message = helper.receiveMessage("request-context-xslt.xml");
//		// evaluate XPath
//		helper.createMessageValidator(message.getResponse()).assertNotSoapFault().validate("xsd/calc.xsd").addNamespaceMapping("ns", "http://javacrumbs.net/calc").assertXPath("//ns:result=$context.result");
//		
//		WsTestContextHolder.getTestContext().clear();
//	}
//	
//	@Test
//	public void testRequestTemplate() throws Exception {
//		WsTestContextHolder.getTestContext().setAttribute("a", 1);
//		WsTestContextHolder.getTestContext().setAttribute("b", 2);
//		// simulates request coming to MessageDispatcherServlet
//		MessageContext message = helper.receiveMessage("request-context-xslt.xml");
//		// compare response with control response
//		helper.createMessageValidator(message.getResponse()).assertNotSoapFault().compare("response1.xml");
//	}
}