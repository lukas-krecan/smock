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

import static net.javacrumbs.smock.common.SmockCommon.setTemplateProcessor;
import static net.javacrumbs.smock.common.server.CommonSmockServer.message;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;
import static net.javacrumbs.smock.common.server.ServerAssert.deserialize;
import static net.javacrumbs.smock.common.server.ServerAssert.validate;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.calc.model.PlusRequest;
import net.javacrumbs.calc.model.PlusResponse;
import net.javacrumbs.smock.common.groovy.GroovyTemplateProcessor;

import org.junit.Test;

public class EndpointUnitTest {
	
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://javacrumbs.net/calc");
	
	private static CalcEndpoint endpoint = new CalcEndpoint();

	static
	{
		setTemplateProcessor(new GroovyTemplateProcessor());
	}

		
	@Test
	public void testCompare() throws Exception {
		PlusRequest request = deserialize("request1.xml", PlusRequest.class);
		PlusResponse response = endpoint.plus(request);
		validate(response).andExpect(message("response1.xml"));
	}
	@Test
	public void testAssertXPath() throws Exception {
		PlusRequest request = deserialize("request1.xml", PlusRequest.class);
		PlusResponse response = endpoint.plus(request);
		validate(response).andExpect(xpath("//ns:result",NS_MAP).evaluatesTo(3));

	}

	@Test
	public void testResponseTemplate() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 1);
		params.put("b", 2);
		PlusRequest request = deserialize(withMessage("request-context-groovy.xml").withParameters(params), PlusRequest.class);
		PlusResponse response = endpoint.plus(request);
		validate(response, request).andExpect(message("response-context-groovy.xml").withParameter("result", 3));
	}
}