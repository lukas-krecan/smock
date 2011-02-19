/*
 * Copyright 2005-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.javacrumbs.airline.server;

import static net.javacrumbs.airline.server.AirlineWebServiceConstants.GET_FLIGHTS_REQUEST;
import static net.javacrumbs.airline.server.AirlineWebServiceConstants.MESSAGES_NAMESPACE;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.javacrumbs.airline.model.Airport;
import net.javacrumbs.airline.model.Flight;
import net.javacrumbs.airline.model.GetFlightsRequest;
import net.javacrumbs.airline.model.GetFlightsResponse;
import net.javacrumbs.airline.model.ServiceClass;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint that handles the Airline Web Service messages using a combination of
 * JAXB2 marshalling and XPath expressions.
 * 
 * @author Arjen Poutsma
 * @author Lukas Krecan
 */
@Endpoint
public class AirlineEndpoint {

	@PayloadRoot(localPart = GET_FLIGHTS_REQUEST, namespace = MESSAGES_NAMESPACE)
	@Namespace(prefix = "m", uri = MESSAGES_NAMESPACE)
	@ResponsePayload
	public GetFlightsResponse getFlights(@RequestPayload GetFlightsRequest request) throws AirlineException{
		String from = request.getFrom();
		String to = request.getTo();
		ServiceClass serviceClass = request.getServiceClass();
		if (from.equals(to)) {
			throw new AirlineException("Departure and destination airport has to differ.");
		}

		GetFlightsResponse response = new GetFlightsResponse();
		Flight flight = new Flight();
		flight.setFrom(airport(from));
		flight.setTo(airport(to));
		flight.setNumber("OK1324");
		flight.setServiceClass(serviceClass);
		flight.setDepartureTime(time("2011-02-19T10:00:00"));
		flight.setArrivalTime(time("2011-02-19T12:00:00"));
		response.getFlight().add(flight);
		return response;

	}

	private XMLGregorianCalendar time(String time) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(time);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	private Airport airport(String code) {
		Airport airport = new Airport();
		airport.setCode(code);
		airport.setCity("City " + code);
		airport.setName("Name " + code);
		return airport;
	}

}
