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
package net.javacrumbs.airline.client;

import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.javacrumbs.airline.model.Flight;
import net.javacrumbs.airline.model.GetFlightsRequest;
import net.javacrumbs.airline.model.GetFlightsResponse;
import net.javacrumbs.airline.model.ServiceClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceOperations;

public class AirlineClient {
	@Autowired
	private WebServiceOperations wsTemplate;
	
	
	public List<Flight> getFlights(String from, String to)
	{
		GetFlightsRequest request = new GetFlightsRequest();
		request.setFrom(from);
		request.setTo(to);
		request.setDepartureDate(time("2011-02-19"));
		request.setServiceClass(ServiceClass.ECONOMY);
		GetFlightsResponse response = (GetFlightsResponse) wsTemplate.marshalSendAndReceive(request);
		return response.getFlight();
	}
	private XMLGregorianCalendar time(String time) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(time);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}


	public WebServiceOperations getWsTemplate() {
		return wsTemplate;
	}


	public void setWsTemplate(WebServiceOperations wsTemplate) {
		this.wsTemplate = wsTemplate;
	}
}
