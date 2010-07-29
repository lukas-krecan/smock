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

package net.javacrumbs.mock;

import org.junit.Test;
import org.springframework.ws.mock.client.RequestMatcher;
import org.springframework.xml.transform.StringSource;

import static net.javacrumbs.mock.ExtendedWebServiceMock.*;
import static org.junit.Assert.assertNotNull;


public class ExtendedWebServiceMockTest {

	@Test
	public void testMessage()
	{
		RequestMatcher requestMatcher = message(new StringSource("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header /><soapenv:Body><test/></soapenv:Body></soapenv:Envelope>"));
		assertNotNull(requestMatcher);
	}
	
}
