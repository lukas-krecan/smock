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

import static net.javacrumbs.smock.common.SmockCommon.resource;
import static net.javacrumbs.smock.springws.client.SmockClient.message;
import static net.javacrumbs.smock.springws.client.SmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;
import static org.springframework.ws.test.client.RequestMatchers.validPayload;

import java.io.IOException;
import java.util.List;

import net.javacrumbs.airline.client.AirlineClient;
import net.javacrumbs.airline.model.Flight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.test.client.MockWebServiceServer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class ClientTest {
    @Autowired
    private AirlineClient airlineClient;
    
    private MockWebServiceServer mockServer;
    
    //inject mock control
    @Autowired
    private WebServiceTemplate webServiceTemplate;
    
    @Before
    public void setUpMocks() throws Exception {
        mockServer = MockWebServiceServer.createServer(webServiceTemplate);
    }
    
    @After
    public void verify()
    {
    	mockServer.verify();
    }

	@Test
	public void testSimple()
	{
		mockServer.expect(anything()).andRespond(withMessage("response1.xml"));

		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	@Test
	public void testVerifyRequest()
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
	public void testStrange()
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
		assertEquals("DUB",flights.get(0).getFrom().getCode());
	}
	
	@Test
	public void testMultipleTemplate() throws IOException
	{
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		mockServer.expect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))).andRespond(withMessage("response-template.xml"));
		
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
		mockServer.expect(message("request-context-xslt.xml").withParameter("from","DUB").withParameter("to", "LAX")).andRespond(withMessage("response-template.xml"));
		
		List<Flight> flights1 = airlineClient.getFlights("DUB", "LAX");
		assertEquals(1, flights1.size());
		assertEquals("DUB",flights1.get(0).getFrom().getCode());
	}
	
	@Test(expected=SoapFaultClientException.class)
	public void testException() throws IOException
	{
		mockServer.expect(anything()).andRespond(withMessage("fault.xml"));
		airlineClient.getFlights("DUB", "LAX");
	}
}
