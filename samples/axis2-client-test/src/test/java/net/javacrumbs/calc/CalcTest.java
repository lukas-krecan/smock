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

import static net.javacrumbs.smock.axis2.client.SmockClient.createServer;
import static net.javacrumbs.smock.common.SmockCommon.createMessageFactory;
import static net.javacrumbs.smock.common.SmockCommon.resource;
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;
import static org.springframework.ws.test.client.RequestMatchers.validPayload;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import net.javacrumbs.airline.AirlineClient;
import net.javacrumbs.calc.AirlineServiceStub.Flight;
import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;

import org.apache.axis2.AxisFault;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.soap.SoapVersion;


public class CalcTest {

    private AirlineClient airlineClient;
    
    private MockWebServiceServer mockServer;
    
    @Before
    public void setUp(){
    	mockServer = createServer(createMessageFactory(SoapVersion.SOAP_11));
    	//client has to be created after createServer was called.
    	airlineClient = new AirlineClient();
    }
    
    @After
    public void verify()
    {
    	mockServer.verify();
    }

	@Test
	public void testSimple() throws RemoteException
	{
		mockServer.expect(anything()).andRespond(withMessage("response1.xml"));

		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	@Test
	public void testVerifyRequest() throws RemoteException
	{
		mockServer.expect(message("request1.xml")).andRespond(withMessage("response1.xml"));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	@Test
	public void testSchema() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response1.xml"));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	@Test
	public void testIgnore() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andExpect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		
		List<Flight> flights = airlineClient.getFlights("JFK", "LAX");
		assertEquals(2, flights.size());
	}
	@Test
	public void testMultiple() throws IOException
	{
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
		
		
		assertEquals(2, airlineClient.getFlights("JFK", "LAX").size());
		assertEquals(1, airlineClient.getFlights("JFK", "DUB").size());
	}
	@Test
	public void testStrange() throws RemoteException
	{
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response2.xml"));
		mockServer.expect(message("request-ignore.xml")).andRespond(withMessage("response1.xml"));
				
		assertEquals(1, airlineClient.getFlights("PRG", "DUB").size());
		assertEquals(2, airlineClient.getFlights("JFK", "LAX").size());
		assertEquals(1, airlineClient.getFlights("PRG", "DUB").size());
	}
	@Test
	public void testTemplate() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights.size());
		assertEquals("DUB",flights.get(0).getFrom().getCode().getAirportCode());
	}
	
	@Test
	public void testMultipleTemplate() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		
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
		mockServer.expect(message("request-context-xslt.xml").withParameter("from","DUB").withParameter("to", "LAX")).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights1 = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights1.size());
		assertEquals("DUB",flights1.get(0).getFrom().getCode().getAirportCode());
	}
	
	@Test(expected=AxisFault.class)
	public void testException() throws IOException
	{
		mockServer.expect(anything()).andRespond(withMessage("fault.xml"));
		airlineClient.getFlights("DUB", "LAX");
	}
}
