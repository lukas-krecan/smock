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
import static net.javacrumbs.smock.common.client.ClientTestHelper.response;
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static net.javacrumbs.smock.mockito.client.SmockMockitoClient.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.client.RequestMatchers.validPayload;

import java.io.IOException;
import java.util.List;

import net.javacrumbs.airline.client.AirlineClient;
import net.javacrumbs.airline.model.Flight;
import net.javacrumbs.airline.model.GetFlightsResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.client.core.WebServiceOperations;

public class ClientMockitoTest {
    
    private AirlineClient airlineClient;
    
    
    private WebServiceOperations webServiceTemplate;
    
    @Before
    public void setUpMocks() throws Exception {
       airlineClient = new AirlineClient();
       webServiceTemplate = mock(WebServiceOperations.class);
       airlineClient.setWsTemplate(webServiceTemplate);
    }
    
	@Test
	public void testSimple()
	{
		when(webServiceTemplate.marshalSendAndReceive(anyObject())).thenReturn(response("response1.xml", GetFlightsResponse.class));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	
	@Test
	public void testVerifyRequest()
	{
		when(webServiceTemplate.marshalSendAndReceive(argThat(is(message("request1.xml"))))).thenReturn(response("response1.xml", GetFlightsResponse.class));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
	
	@Test(expected=AssertionError.class)
	public void testFail()
	{
		when(webServiceTemplate.marshalSendAndReceive(argThat(is(message("request2.xml"))))).thenReturn(response("response1.xml", GetFlightsResponse.class));
		airlineClient.getFlights("PRG", "DUB");
	}
	
	@Test
	public void testSchema() throws IOException
	{
		when(webServiceTemplate.marshalSendAndReceive(argThat(is(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd")))))).thenReturn(response("response1.xml", GetFlightsResponse.class));
		
		List<Flight> flights = airlineClient.getFlights("PRG", "DUB");
		assertEquals(1, flights.size());
	}
}
