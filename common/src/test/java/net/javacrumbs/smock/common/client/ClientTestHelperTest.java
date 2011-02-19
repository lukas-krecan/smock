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

package net.javacrumbs.smock.common.client;

import static net.javacrumbs.smock.common.client.ClientTestHelper.response;
import static net.javacrumbs.smock.common.client.ClientTestHelper.serialize;
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reportMatcher;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.javacrumbs.smock.common.client.test.Calc;
import net.javacrumbs.smock.common.server.test.PlusResponse;

import org.easymock.IArgumentMatcher;
import org.junit.Test;
import org.springframework.ws.client.core.WebServiceOperations;
import org.springframework.ws.test.client.RequestMatcher;


public class ClientTestHelperTest {

	@Test
	public void example()
	{
		Calc calc = new Calc();
		WebServiceOperations wsTemplate = createMock(WebServiceOperations.class);
		calc.setWsTemplate(wsTemplate);
		expect(wsTemplate.marshalSendAndReceive(is(message("xml/request2.xml")))).andReturn(response("xml/response1.xml", PlusResponse.class));
		
		replay(wsTemplate);
		
		assertEquals(3, calc.plus(1, 2));
		
		verify(wsTemplate);
	}
	@Test(expected=AssertionError.class)
	public void exampleInvalid()
	{
		Calc calc = new Calc();
		WebServiceOperations wsTemplate = createMock(WebServiceOperations.class);
		calc.setWsTemplate(wsTemplate);
		expect(wsTemplate.marshalSendAndReceive(is(message("xml/valid-message.xml")))).andReturn(response("xml/response1.xml", PlusResponse.class));
		
		replay(wsTemplate);
		
		calc.plus(1, 2);
		
	}

	private <T> T is(final RequestMatcher matcher)
	{
		reportMatcher(new IArgumentMatcher() {
			public boolean matches(Object argument) {
				try {
					matcher.match(null, serialize(argument));
				} catch (IOException e) {
					throw new IllegalArgumentException("Can not match the request.",e);
				} 
				return true;
			}
			
			public void appendTo(StringBuffer buffer) {
								
			}
		});
		return null;
	}


}
