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

import static net.javacrumbs.smock.springws.client.SmockClient.createServer;
import static net.javacrumbs.smock.springws.client.SmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.springframework.ws.test.client.RequestMatchers.anything;

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
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.test.client.MockWebServiceServer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:client-config.xml"})
public class CalcInterceptorTest {
    @Autowired
    private AirlineClient airlineClient;
    
    private MockWebServiceServer mockServer;
    
    //inject mock control
    @Autowired
    private WebServiceTemplate webServiceTemplate;
    
    @Before
    public void setUpMocks() throws Exception {
        mockServer = createServer(webServiceTemplate, new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
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
}
