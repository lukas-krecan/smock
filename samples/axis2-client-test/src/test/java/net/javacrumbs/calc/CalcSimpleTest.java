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
import java.rmi.RemoteException;
import java.util.List;

import net.javacrumbs.airline.AirlineClient;
import net.javacrumbs.calc.AirlineServiceStub.Flight;
import net.javacrumbs.smock.axis2.client.AbstractSmockClientTest;

import org.apache.axis2.AxisFault;
import org.junit.After;
import org.junit.Test;
import org.springframework.ws.soap.SoapVersion;

public class CalcSimpleTest extends AbstractSmockClientTest {

    private AirlineClient airlineClient = new AirlineClient("https://localhost:8443/axis2-server-test/services/CalculatorService.CalculatorServiceHttpSoap12Endpoint/");
    
   
	public CalcSimpleTest() {
		createServer(createMessageFactory(SoapVersion.SOAP_11));
	}
	
    @After
    public void verify()
    {
    	super.verify();
    }


	@Test
	public void testSimple() throws RemoteException
	{
		expect(anything()).andRespond(withMessage("response1.xml"));

		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	@Test
	public void testVerifyRequest() throws RemoteException
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
	public void testStrange() throws RemoteException
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
		assertEquals("DUB",flights.get(0).getFrom().getCode().getAirportCode());
	}
	
	@Test
	public void testMultipleTemplate() throws IOException
	{
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights1 = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights1.size());
		assertEquals("DUB",flights1.get(0).getFrom().getCode().getAirportCode());
		
		List<Flight> flights2 = airlineClient.getFlights("JFK", "LAX");
		assertEquals(1, flights2.size());
		assertEquals("JFK",flights2.get(0).getFrom().getCode().getAirportCode());
	}

	@Test
	public void testContext() throws IOException
	{
		expect(message("request-context-xslt.xml").withParameter("from","DUB").withParameter("to", "LAX")).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights1 = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights1.size());
		assertEquals("DUB",flights1.get(0).getFrom().getCode().getAirportCode());
	}
	
	@Test(expected=AxisFault.class)
	public void testException() throws IOException
	{
		expect(anything()).andRespond(withMessage("fault.xml"));
		airlineClient.getFlights("DUB", "LAX");
	}
}
