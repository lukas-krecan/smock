package net.javacrumbs.airline.server;

import java.util.Calendar;

import net.javacrumbs.airilne.AirlineServiceSkeletonInterface;

import org.springframework.www.spring_ws.samples.airline.schemas.messages.GetFlightsRequest;
import org.springframework.www.spring_ws.samples.airline.schemas.messages.GetFlightsResponse;
import org.springframework.www.spring_ws.samples.airline.schemas.types.Airport;
import org.springframework.www.spring_ws.samples.airline.schemas.types.AirportCode;
import org.springframework.www.spring_ws.samples.airline.schemas.types.Flight;
import org.springframework.www.spring_ws.samples.airline.schemas.types.FlightNumber;
import org.springframework.www.spring_ws.samples.airline.schemas.types.ServiceClass;

public class AirlineEndpoint implements AirlineServiceSkeletonInterface {

	@Override
	public GetFlightsResponse getFlights(GetFlightsRequest request) {
		String from = request.getFrom().getAirportCode();
		String to = request.getTo().getAirportCode();
		ServiceClass serviceClass = request.getServiceClass();
		if (from.equals(to)) {
			throw new IllegalArgumentException("Departure and destination airport has to differ.");
		}

		GetFlightsResponse response = new GetFlightsResponse();
		Flight flight = new Flight();
		flight.setFrom(airport(from));
		flight.setTo(airport(to));
		flight.setNumber(flightNumber("OK1324"));
		flight.setServiceClass(serviceClass);
		flight.setDepartureTime(Calendar.getInstance());
		flight.setArrivalTime(Calendar.getInstance());
		response.addFlight(flight);
		return response;
	}
	
	private FlightNumber flightNumber(String number) {
		FlightNumber flightNumber = new FlightNumber();
		flightNumber.setFlightNumber(number);
		return flightNumber;
	}



	private Airport airport(String code) {
		Airport airport = new Airport();
		AirportCode airportCode = new AirportCode();
		airportCode.setAirportCode(code);
		airport.setCode(airportCode);
		airport.setCity("City " + code);
		airport.setName("Name " + code);
		return airport;
	}

}
