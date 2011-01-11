package net.javacrumbs.calc.server;

import static net.javacrumbs.smock.common.SmockCommon.resource;
import static net.javacrumbs.smock.common.server.CommonSmockServer.message;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;
import static org.springframework.ws.test.server.ResponseMatchers.clientOrSenderFault;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.smock.http.server.servlet.CommonServletBasedMockWebServiceClient;

import org.apache.axis2.deployment.WarBasedAxisConfigurator;
import org.apache.axis2.transport.http.AxisServlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;

public class EndpointTest {
	private CommonServletBasedMockWebServiceClient client;
	
	@Before
	public void setUp()
	{
		client = createClient();
	}
	
	private CommonServletBasedMockWebServiceClient createClient() {
		return new CommonServletBasedMockWebServiceClient(AxisServlet.class, new GenericApplicationContext(),null, Collections.singletonMap(WarBasedAxisConfigurator.PARAM_AXIS2_REPOSITORY_PATH, "C:/Uziv/ext93337/private/workspace/smock/samples/axis2-server-test/src/main/webapp/WEB-INF/services"));
	}


	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://javacrumbs.net/calc");
	
	@Test
	public void testSimple() throws Exception {
		client.sendRequestTo("/CalculatorService", withMessage("request1.xml")).andExpect(noFault());
	}
	
	@Test
	public void testCompare() throws Exception {
		client.sendRequestTo("/CalculatorService",withMessage("request1.xml")).andExpect(message("response1.xml"));
	}
	@Test
	public void testValidateResponse() throws Exception {
		client.sendRequestTo("/CalculatorService",withMessage("request1.xml")).andExpect(noFault()).andExpect(validPayload(resource("xsd/calc.xsd")));
	}
	@Test
	public void testAssertXPath() throws Exception {
		client.sendRequestTo("/CalculatorService",withMessage("request1.xml")).andExpect(noFault()).andExpect(xpath("//ns:result",NS_MAP).evaluatesTo(3));
	}

	@Test
	public void testError() throws Exception {
		client.sendRequestTo("/CalculatorService",withMessage("request-error.xml")).andExpect(message("response-error.xml"));
	}
	@Test
	public void testErrorMessage() throws Exception {
		client.sendRequestTo("/CalculatorService",withMessage("request-error.xml")).andExpect(clientOrSenderFault("Unmarshalling Error: For input string: \"aaa\" "));
	}

	@Test
	public void testResponseTemplate() throws Exception {
		client.sendRequestTo("/CalculatorService",withMessage("request-context-xslt.xml").withParameter("a",1).withParameter("b", 2)).andExpect(message("response-context-xslt.xml").withParameter("result", 3));
	}
}