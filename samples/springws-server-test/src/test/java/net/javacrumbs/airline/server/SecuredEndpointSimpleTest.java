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

import net.javacrumbs.smock.springws.server.AbstractSmockServerTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/secured-spring-ws-servlet.xml"})
public class SecuredEndpointSimpleTest extends AbstractSmockServerTest{

	@Test
	public void testSimple() throws Exception {
		sendRequest(withMessage("request1.xml")).andExpect(noFault());
	}
	
	@Override
	protected ClientInterceptor[] getInterceptors() {
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setSecurementActions("UsernameToken Timestamp");
		securityInterceptor.setSecurementUsername("Bert");
		securityInterceptor.setSecurementPassword("Ernie");
		
		return new ClientInterceptor[]{securityInterceptor};
	}
}