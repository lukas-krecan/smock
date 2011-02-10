/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.calc;

import static net.javacrumbs.smock.http.client.connection.SmockClient.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;
import static org.springframework.ws.test.client.RequestMatchers.validPayload;

import java.io.IOException;

import javax.xml.ws.soap.SOAPFaultException;

import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcTest {
    @Autowired
    private CalcService calc;
    
    private MockWebServiceServer mockServer;
    
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext){
    	mockServer = createServer(applicationContext);
    }
    
    @After
    public void tearDown()
    {
    	mockServer.verify();
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
