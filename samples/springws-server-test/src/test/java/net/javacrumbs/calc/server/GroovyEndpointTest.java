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
import static org.springframework.ws.test.server.MockWebServiceClient.createClient;

import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.smock.common.groovy.GroovyTemplateProcessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml"})
public class GroovyEndpointTest {
	
	private MockWebServiceClient wsMockClient;

	static
	{
		setTemplateProcessor(new GroovyTemplateProcessor());
	}
	
	@Autowired
	public void setApplicationContext(ApplicationContext context)
	{
		wsMockClient = createClient(context);
	}

	

	@Test
	public void testResponseTemplate() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 1);
		params.put("b", 2);
		wsMockClient.sendRequest(withMessage("request-context-groovy.xml").withParameters(params)).andExpect(message("response-context-groovy.xml"));
	}
}