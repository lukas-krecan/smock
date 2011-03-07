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
package net.javacrumbs.airline;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.javacrumbs.calc.AirlineServiceStub;
import net.javacrumbs.calc.AirlineServiceStub.AirportCode;
import net.javacrumbs.calc.AirlineServiceStub.Flight;
import net.javacrumbs.calc.AirlineServiceStub.FlightNumber;
import net.javacrumbs.calc.AirlineServiceStub.GetFlightsRequest;
import net.javacrumbs.calc.AirlineServiceStub.GetFlightsResponse;
import net.javacrumbs.calc.AirlineServiceStub.ServiceClass;

import org.apache.axis2.AxisFault;


public class AirlineClient {
	
	private AirlineServiceStub stub;

	public AirlineClient() {
		try {
			stub = new AirlineServiceStub();
		} catch (AxisFault e) {
			throw new IllegalStateException("Can not create stub.",e);
		}
	}
	public AirlineClient(String address) {
		try {
			stub = new AirlineServiceStub(address);
		} catch (AxisFault e) {
			throw new IllegalStateException("Can not create stub.",e);
		}
	}

	public List<Flight> getFlights(String from, String to) throws RemoteException
	{
		GetFlightsRequest request = new GetFlightsRequest();
		request.setFrom(airportCode(from));
		request.setTo(airportCode(to));
		request.setDepartureDate(new Date());
		request.setServiceClass(ServiceClass.economy);
		GetFlightsResponse response = stub.getFlights(request);
		return Arrays.asList(response.getFlight());
	}

	private AirportCode airportCode(String code) {
		AirportCode airportCode = new AirportCode();
		airportCode.setAirportCode(code);
		return airportCode;
	}
}
