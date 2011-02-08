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
package net.javacrumbs.calc.server;

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.smock.axis2.server.AbstractSmockServerTest;

import org.junit.Test;

public class SimpleEndpointTest extends AbstractSmockServerTest {
	
	private static final String ENDPOINT_URL = "/axis2/services/CalculatorService";
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://javacrumbs.net/calc");
	
	public SimpleEndpointTest()
	{
		createClient(createConfigurationContextFromResource(resource("file:./src/main/webapp/WEB-INF")));
	}
	
	@Test
	public void testSimple() throws Exception {
		sendRequestTo(ENDPOINT_URL, withMessage("request1.xml")).andExpect(noFault());
	}
	
	@Test
	public void testCompare() throws Exception {
		sendRequestTo(ENDPOINT_URL,withMessage("request1.xml")).andExpect(message("response1.xml"));
	}
	@Test
	public void testValidateResponse() throws Exception {
		sendRequestTo(ENDPOINT_URL,withMessage("request1.xml")).andExpect(noFault()).andExpect(validPayload(resource("xsd/calc.xsd")));
	}
	@Test
	public void testAssertXPath() throws Exception {
		sendRequestTo(ENDPOINT_URL,withMessage("request1.xml")).andExpect(noFault()).andExpect(xpath("//ns:return",NS_MAP).evaluatesTo(3));
	}

	@Test
	public void testError() throws Exception {
		sendRequestTo(ENDPOINT_URL,withMessage("request-error.xml")).andExpect(message("response-error.xml"));
	}
	@Test
	public void testErrorMessage() throws Exception {
		sendRequestTo(ENDPOINT_URL,withMessage("request-error.xml")).andExpect(serverOrReceiverFault("For input string: \"aaa\""));
	}

	@Test
	public void testResponseTemplate() throws Exception {
		sendRequestTo(ENDPOINT_URL,withMessage("request-context-xslt.xml").withParameter("a",1).withParameter("b", 2)).andExpect(message("response-context-xslt.xml").withParameter("result", 3));
	}
}