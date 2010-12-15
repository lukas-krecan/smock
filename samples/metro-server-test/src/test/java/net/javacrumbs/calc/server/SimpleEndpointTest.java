package net.javacrumbs.calc.server;

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.smock.http.metro.server.servlet.AbstractSmockServerTest;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/jaxws-servlet.xml"})
public class SimpleEndpointTest extends AbstractSmockServerTest{
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://javacrumbs.net/calc");
	
	@Test
	public void testSimple() throws Exception {
		sendRequestTo("/CalculatorService", withMessage("request1.xml")).andExpect(noFault());
	}
	
	@Test
	public void testCompare() throws Exception {
		sendRequestTo("/CalculatorService",withMessage("request1.xml")).andExpect(message("response1.xml"));
	}
	@Test
	public void testValidateResponse() throws Exception {
		sendRequestTo("/CalculatorService",withMessage("request1.xml")).andExpect(noFault()).andExpect(validPayload(resource("xsd/calc.xsd")));
	}
	@Test
	public void testAssertXPath() throws Exception {
		sendRequestTo("/CalculatorService",withMessage("request1.xml")).andExpect(noFault()).andExpect(xpath("//ns:result",NS_MAP).evaluatesTo(3));
	}

	@Test
	@Ignore
	public void testError() throws Exception {
		sendRequestTo("/CalculatorService",withMessage("request-error.xml")).andExpect(message("response-error.xml"));
	}
	@Test
	@Ignore
	public void testErrorMessage() throws Exception {
		sendRequestTo("/CalculatorService",withMessage("request-error.xml")).andExpect(clientOrSenderFault("Unmarshalling Error: For input string: \"aaa\" "));
	}

	@Test
	public void testResponseTemplate() throws Exception {
		sendRequestTo("/CalculatorService",withMessage("request-context-xslt.xml").withParameter("a",1).withParameter("b", 2)).andExpect(message("response-context-xslt.xml").withParameter("result", 3));
	}
}