package net.javacrumbs.calc;

import static net.javacrumbs.smock.common.SmockCommon.resource;
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;
import static org.springframework.ws.test.client.RequestMatchers.validPayload;

import java.io.IOException;

import javax.xml.ws.soap.SOAPFaultException;

import net.javacrumbs.smock.common.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.common.client.connection.threadlocal.http.ThreadLocalMockWebServiceServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.soap.client.SoapFaultClientException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcTest {
    @Autowired
    private CalcService calc;
    
    private MockWebServiceServer mockServer;
    
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext){
    	mockServer = new ThreadLocalMockWebServiceServer(applicationContext);
    }
    


	@Test
	public void testSimple()
	{
		mockServer.expect(anything()).andRespond(withMessage("response1.xml"));

		long result = calc.plus(1, 2);
		assertEquals(3, result);
	}
	
	@Test
	public void testVerifyRequest()
	{
		mockServer.expect(message("request1.xml")).andRespond(withMessage("response1.xml"));
		
		long result = calc.plus(1, 2);
		assertEquals(3, result);
	}
	@Test
	public void testSchema() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request1.xml")).andRespond(withMessage("response1.xml"));
		
		long result = calc.plus(1, 2);
		assertEquals(3, result);
	}
	@Test
	public void testIgnore() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		
		long result = calc.plus(2, 3);
		assertEquals(5, result);
	}
	@Test
	public void testMultiple() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response3.xml"));
		
		
		assertEquals(5, calc.plus(2, 3));
		assertEquals(8, calc.plus(3, 5));
	}
	@Test
	public void testStrange()
	{
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
				
		assertEquals(3, calc.plus(1, 2));
		assertEquals(5, calc.plus(2, 3));
		assertEquals(3, calc.plus(1, 2));
	}
	@Test
	public void testTemplate() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response-template.xml"));
		
		long result = calc.plus(2, 3);
		assertEquals(5, result);
	}
	
	@Test
	public void testMultipleTemplate() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response-template.xml"));
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response-template.xml"));
		
		assertEquals(5, calc.plus(2, 3));
		assertEquals(8, calc.plus(3, 5));
	}

	@Test
	public void testContext() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd")))
			.andExpect(message("request-context-xslt.xml").withParameter("a",1).withParameter("b", 4))
			.andRespond(withMessage("response-template.xml"));
		
		long result = calc.plus(1, 4);
		assertEquals(5, result);
	}
	
	@Test(expected=SOAPFaultException.class)
	public void testException() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("fault.xml"));
		calc.plus(2, 3);
	}
}
