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

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.smock.common.XsltTemplateProcessor;
import net.javacrumbs.smock.springws.server.AbstractSmockServerTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml"})
public class SimpleEndpointTest extends AbstractSmockServerTest{
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://www.springframework.org/spring-ws/samples/airline/schemas/types");
	
	public SimpleEndpointTest() {
		setTemplateProcessor(new XsltTemplateProcessor());
	}

	@Test
	public void testSimple() throws Exception {
		sendRequest(withMessage("request1.xml")).andExpect(noFault());
	}
	
	@Test
	public void testCompare() throws Exception {
		sendRequest(withMessage("request1.xml")).andExpect(message("response1.xml"));
	}
	@Test
	public void testValidateResponse() throws Exception {
		sendRequest(withMessage("request1.xml")).andExpect(noFault()).andExpect(validPayload(resource("xsd/messages.xsd"),resource("xsd/types.xsd")));
	}
	@Test
	public void testAssertXPath() throws Exception {
		sendRequest(withMessage("request1.xml")).andExpect(noFault()).andExpect(xpath("//ns:from/ns:code",NS_MAP).evaluatesTo("PRG"));
	}

	@Test
	public void testError() throws Exception {
		sendRequest(withMessage("request-error.xml")).andExpect(message("response-error.xml"));
	}
	@Test
	public void testErrorMessage() throws Exception {
		sendRequest(withMessage("request-error.xml")).andExpect(clientOrSenderFault("Departure and destination airport has to differ."));
	}

	@Test
	public void testResponseTemplate() throws Exception {
		sendRequest(withMessage("request-context-xslt.xml").withParameter("serviceClass","first")).andExpect(message("response-context-xslt.xml").withParameter("serviceClass", "first"));
	}
}