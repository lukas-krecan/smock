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
import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;

import java.rmi.RemoteException;
import java.util.List;

import net.javacrumbs.airline.AirlineClient;
import net.javacrumbs.calc.AirlineServiceStub.Flight;
import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;



public class CalcInterceptorTest {

    private AirlineClient airlineClient;
    
    private MockWebServiceServer mockServer;
    
    
    @Before
    public void setUpMocks() throws Exception {
        mockServer = createServer(new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
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
}
