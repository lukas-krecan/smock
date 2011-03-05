/*
 * Copyright 2005-2010 the original author or authors.
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

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import net.javacrumbs.airline.model.Flight;
import net.javacrumbs.airline.model.GetFlightsRequest;
import net.javacrumbs.airline.model.GetFlightsResponse;
import net.javacrumbs.airline.model.ServiceClass;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.xml.transform.TransformerHelper;
import org.w3c.dom.Document;

/**
 * Test that does not use any help from the framework.
 * @author Lukas Krecan
 *
 */
public class VanillaTest {

	private AirlineEndpoint endpoint = new AirlineEndpoint();
	
	@Test
	public void testGetFlights() throws AirlineException, DatatypeConfigurationException
	{
		GetFlightsRequest request = new GetFlightsRequest();
		request.setFrom("PRG");
		request.setTo("DUB");
		request.setServiceClass(ServiceClass.BUSINESS);
		request.setDepartureDate(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2011, 02, 23, 0));
		//more setter calls could be here
		
		GetFlightsResponse response = endpoint.getFlights(request);
		
		List<Flight> flights = response.getFlight();
		assertEquals(1, flights.size());
		assertEquals(ServiceClass.BUSINESS, flights.get(0).getServiceClass());
		//more assertions here
		
	}
	
	@Test
	public void testGetFlightsXml() throws AirlineException, DatatypeConfigurationException, TransformerException
	{
		GetFlightsRequest request = JAXB.unmarshal(getStream("request1.xml"), GetFlightsRequest.class);
		
		GetFlightsResponse response = endpoint.getFlights(request);
		
		DOMResult domResponse = new DOMResult();
		JAXB.marshal(response, domResponse);
		
		XMLUnit.setIgnoreWhitespace(true);
		XMLAssert.assertXMLEqual(getDocument("response1.xml"), (Document)domResponse.getNode());
		
	}

	private InputStream getStream(String name) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	}
	private Document getDocument(String name) throws TransformerException {
		
		DOMResult result = new DOMResult();
		new TransformerHelper().transform(new StreamSource(getStream(name)), result);
		return (Document)result.getNode();
		
	}
	
	
}
