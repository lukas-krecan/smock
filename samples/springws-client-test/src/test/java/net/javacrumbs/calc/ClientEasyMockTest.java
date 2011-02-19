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
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static net.javacrumbs.smock.easymock.client.SmockEasyMockClient.*;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.validPayload;

import java.io.IOException;
import java.util.List;

import net.javacrumbs.airline.client.AirlineClient;
import net.javacrumbs.airline.model.Flight;
import net.javacrumbs.airline.model.GetFlightsResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.client.core.WebServiceOperations;

public class ClientEasyMockTest {
    
    private AirlineClient airlineClient;
    
    
    private WebServiceOperations webServiceTemplate;
    
    @Before
    public void setUpMocks() throws Exception {
       airlineClient = new AirlineClient();
       webServiceTemplate = createMock(WebServiceOperations.class);
       airlineClient.setWsTemplate(webServiceTemplate);
    }
    
    @After
    public void verifyMocks()
    {
    	verify(webServiceTemplate);
    }

	@Test
	public void testSimple()
	{
		expect(webServiceTemplate.marshalSendAndReceive(anyObject())).andReturn(response("response1.xml", GetFlightsResponse.class));
		replay(webServiceTemplate);
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	@Test
	public void testVerifyRequest()
	{
		expect(webServiceTemplate.marshalSendAndReceive(is(message("request1.xml")))).andReturn(response("response1.xml", GetFlightsResponse.class));
		replay(webServiceTemplate);
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	@Test
	public void testSchema() throws IOException
	{
		expect(webServiceTemplate.marshalSendAndReceive(is(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd"))))).andReturn(response("response1.xml", GetFlightsResponse.class));
		replay(webServiceTemplate);
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
}
