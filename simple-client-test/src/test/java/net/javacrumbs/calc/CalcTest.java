package net.javacrumbs.calc;

import static net.javacrumbs.smock.common.SmockCommon.resource;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.javacrumbs.smock.client.AbstractSmockClientTest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.soap.client.SoapFaultClientException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcTest extends AbstractSmockClientTest {
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
	
	@Test
	public void testVerifyRequest()
	{
		expect(message("request1.xml")).andRespond(withMessage("response1.xml"));
		
		int result = calc.plus(1, 2);
		assertEquals(3, result);
	}
	@Test
	public void testSchema() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request1.xml")).andRespond(withMessage("response1.xml"));
		
		int result = calc.plus(1, 2);
		assertEquals(3, result);
	}
	@Test
	public void testIgnore() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		
		int result = calc.plus(2, 3);
		assertEquals(5, result);
	}
	@Test
	public void testMultiple() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response3.xml"));
		
		
		assertEquals(5, calc.plus(2, 3));
		assertEquals(8, calc.plus(3, 5));
	}
	@Test
	public void testStrange()
	{
		expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
		expect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
				
		assertEquals(3, calc.plus(1, 2));
		assertEquals(5, calc.plus(2, 3));
		assertEquals(3, calc.plus(1, 2));
	}
	@Test
	public void testTemplate() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response-template.xml"));
		
		int result = calc.plus(2, 3);
		assertEquals(5, result);
	}
	
	@Test
	public void testMultipleTemplate() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response-template.xml"));
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response-template.xml"));
		
		assertEquals(5, calc.plus(2, 3));
		assertEquals(8, calc.plus(3, 5));
	}

	@Test
	public void testContext() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd")))
			.andExpect(message("request-context-xslt.xml").withParameter("a",1).withParameter("b", 4))
			.andRespond(withMessage("response-template.xml"));
		
		int result = calc.plus(1, 4);
		assertEquals(5, result);
	}
	
	@Test(expected=SoapFaultClientException.class)
	public void testException() throws IOException
	{
		expect(validPayload(resource("xsd/calc.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("fault.xml"));
		calc.plus(2, 3);
	}
}
