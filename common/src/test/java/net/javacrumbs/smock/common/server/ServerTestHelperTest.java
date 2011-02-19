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

package net.javacrumbs.smock.common.server;

import static net.javacrumbs.smock.common.server.ServerTestHelper.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.server.test.CalcEndpoint;
import net.javacrumbs.smock.common.server.test.PlusRequest;
import net.javacrumbs.smock.common.server.test.PlusResponse;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.server.ResponseMatcher;


public class ServerTestHelperTest {
	private CalcEndpoint endpoint = new CalcEndpoint();
	
	@Test
	public void testAssert()
	{
		PlusRequest request = createRequest("xml/request1.xml", PlusRequest.class);
		PlusResponse response = endpoint.plus(request);
		assertEquals(3, response.getResult());
		validate(response).andExpect(message("xml/response1.xml"));
	//	ServerAssert.assertMatches(payload(from(document)));
	}
	@Test
	public void testAssertCreator()
	{
		PlusRequest request = createRequest(withMessage(fromResource("xml/request1.xml")), PlusRequest.class);
		PlusResponse response = endpoint.plus(request);
		assertEquals(3, response.getResult());
		validate(response).andExpect(message("xml/response1.xml"));
		//	ServerAssert.assertMatches(payload(from(document)));
	}
	@Test
	public void testValidate()
	{
		Source response = fromResource("xml/response1.xml");
		Source request = fromResource("xml/request1.xml");
		validate(response,request).andExpect(new ResponseMatcher() {
			public void match(WebServiceMessage request, WebServiceMessage response) throws IOException, AssertionError {
				assertNotNull(request);
				assertNotNull(response);
			}
		});
	}
	
}
