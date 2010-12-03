package net.javacrumbs.calc.server;

import static net.javacrumbs.smock.common.SmockCommon.setTemplateProcessor;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;
import static net.javacrumbs.smock.jaxws.server.SmockServer.createClient;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.smock.common.XsltTemplateProcessor;
import net.javacrumbs.smock.jaxws.server.MockWebServiceClient;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class EndpointTest {
	private MockWebServiceClient client;
	
	static
	{
		setTemplateProcessor(new XsltTemplateProcessor());
	}
	
	@Autowired
	public void setApplicationContex(ApplicationContext applicationContext)
	{
		client = createClient("net.javacrumbs.calc.server");
	}
	
	
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://javacrumbs.net/calc");
	
	@Test
	public void testSimple() throws Exception {
		// simulates request coming to MessageDispatcherServlet
		client.sendRequest(withMessage("request1.xml")).andExpect(noFault());
	}
	
//	@Test
//	public void testCompare() throws Exception {
//		client.sendRequest(withMessage("request1.xml")).andExpect(message("response1.xml"));
//	}
//	@Test
//	public void testValidateResponse() throws Exception {
//		client.sendRequest(withMessage("request1.xml")).andExpect(noFault()).andExpect(validPayload(resource("xsd/calc.xsd")));
//	}
//	@Test
//	public void testAssertXPath() throws Exception {
//		client.sendRequest(withMessage("request1.xml")).andExpect(noFault()).andExpect(xpath("//ns:result",NS_MAP).evaluatesTo(3));
//	}
//
//	@Test
//	public void testError() throws Exception {
//		client.sendRequest(withMessage("request-error.xml")).andExpect(message("response-error.xml"));
//	}
//	@Test
//	public void testErrorMessage() throws Exception {
//		client.sendRequest(withMessage("request-error.xml")).andExpect(clientOrSenderFault("Validation error"));
//	}
//
//	@Test
//	public void testResponseTemplate() throws Exception {
//		client.sendRequest(withMessage("request-context-xslt.xml").withParameter("a",1).withParameter("b", 2)).andExpect(message("response-context-xslt.xml").withParameter("result", 3));
//	}
}