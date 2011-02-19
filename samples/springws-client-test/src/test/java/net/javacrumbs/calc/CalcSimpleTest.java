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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import net.javacrumbs.airline.client.AirlineClient;
import net.javacrumbs.airline.model.Flight;
import net.javacrumbs.smock.springws.client.AbstractSmockClientTest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.soap.client.SoapFaultClientException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcSimpleTest extends AbstractSmockClientTest {
    @Autowired
    private AirlineClient airlineClient;
    
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext)
    {
    	createServer(applicationContext);
    }
   
    @After
    public void verify()
    {
    	super.verify();
    }

	@Test
	public void testSimple()
	{
		expect(anything()).andRespond(withMessage("response1.xml"));

		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	@Test
	public void testVerifyRequest()
	{
		expect(message("request1.xml")).andRespond(withMessage("response1.xml"));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	@Test
	public void testSchema() throws IOException
	{
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response1.xml"));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	@Test
	public void testIgnore() throws IOException
	{
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		
		List<Flight> flights = airlineClient.getFlights("JFK", "LAX");
		assertEquals(2, flights.size());
	}
	@Test
	public void testMultiple() throws IOException
	{
		expect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
		
		
		assertEquals(2, airlineClient.getFlights("JFK", "LAX").size());
		assertEquals(1, airlineClient.getFlights("JFK", "DUB").size());
	}
	@Test
	public void testStrange()
	{
		expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
		expect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
				
		assertEquals(1, airlineClient.getFlights("PRG", "DUB").size());
		assertEquals(2, airlineClient.getFlights("JFK", "LAX").size());
		assertEquals(1, airlineClient.getFlights("PRG", "DUB").size());
	}
	@Test
	public void testTemplate() throws IOException
	{
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights.size());
		assertEquals("DUB",flights.get(0).getFrom().getCode());
	}
	
	@Test
	public void testMultipleTemplate() throws IOException
	{
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights1 = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights1.size());
		assertEquals("DUB",flights1.get(0).getFrom().getCode());
		
		List<Flight> flights2 = airlineClient.getFlights("JFK", "LAX");
		assertEquals(1, flights2.size());
		assertEquals("JFK",flights2.get(0).getFrom().getCode());
	}

	@Test
	public void testContext() throws IOException
	{
		expect(message("request-context-xslt.xml").withParameter("from","DUB").withParameter("to", "LAX")).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights1 = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights1.size());
		assertEquals("DUB",flights1.get(0).getFrom().getCode());
	}
	
	@Test(expected=SoapFaultClientException.class)
	public void testException() throws IOException
	{
		expect(anything()).andRespond(withMessage("fault.xml"));
		airlineClient.getFlights("DUB", "LAX");
	}
}
