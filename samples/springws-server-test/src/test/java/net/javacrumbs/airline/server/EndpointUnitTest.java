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
package net.javacrumbs.airline.server;

import static net.javacrumbs.smock.common.SmockCommon.setTemplateProcessor;
import static net.javacrumbs.smock.common.server.CommonSmockServer.message;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;
import static net.javacrumbs.smock.common.server.ServerAssert.createRequest;
import static net.javacrumbs.smock.common.server.ServerAssert.validate;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.airline.model.GetFlightsRequest;
import net.javacrumbs.airline.model.GetFlightsResponse;
import net.javacrumbs.smock.common.groovy.GroovyTemplateProcessor;

import org.junit.Test;

public class EndpointUnitTest {
	
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://www.springframework.org/spring-ws/samples/airline/schemas/types");
	
	private static AirlineEndpoint endpoint = new AirlineEndpoint();

	static
	{
		setTemplateProcessor(new GroovyTemplateProcessor());
	}

		
	@Test
	public void testCompare() throws Exception {
		GetFlightsRequest request = createRequest("request1.xml", GetFlightsRequest.class);
		GetFlightsResponse response = endpoint.getFlights(request);
		validate(response).andExpect(message("response1.xml"));
	}
	@Test
	public void testAssertXPath() throws Exception {
		GetFlightsRequest request = createRequest("request1.xml", GetFlightsRequest.class);
		GetFlightsResponse response = endpoint.getFlights(request);
		validate(response).andExpect(xpath("//ns:from/ns:code",NS_MAP).evaluatesTo("PRG"));

	}

	@Test
	public void testResponseTemplate() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("from", "DUB");
		params.put("to", "JFK");
		params.put("serviceClass", "economy");
		GetFlightsRequest request = createRequest(withMessage("request-context-groovy.xml").withParameters(params), GetFlightsRequest.class);
		GetFlightsResponse response = endpoint.getFlights(request);
		validate(response, request).andExpect(message("response-context-groovy.xml").withParameter("serviceClass", "economy"));
	}
}