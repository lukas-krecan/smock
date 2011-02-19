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

import static net.javacrumbs.smock.springws.server.SmockServer.message;
import static net.javacrumbs.smock.springws.server.SmockServer.withMessage;
import static org.springframework.ws.test.server.MockWebServiceClient.createClient;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.javacrumbs.smock.common.XsltTemplateProcessor;
import net.javacrumbs.smock.springws.server.SmockServer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.ws.test.server.MockWebServiceClient;

@RunWith(Parameterized.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-ws-servlet.xml"})
public class ParametrizedTest {

		
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", AirlineWebServiceConstants.MESSAGES_NAMESPACE);
	private MockWebServiceClient wsMockClient;
	private int a;
	private int b;
	private TestContextManager testContextManager;
	
	@Autowired
	public void setApplicationContext(ApplicationContext context)
	{
		wsMockClient = createClient(context);
	}
	
	@Before
	public void injectDependencies() throws Throwable {
		SmockServer.setTemplateProcessor(new XsltTemplateProcessor());
		this.testContextManager.prepareTestInstance(this);
	}
	
	
	public ParametrizedTest(int a, int b) {
		this.testContextManager = new TestContextManager(getClass());
		this.a = a;
		this.b = b;
	}
	

	@Parameters
	public static Collection<Integer[]> inputData() {
		return Arrays.asList(new Integer[][] { 
				{ 1, 2}, 
				{ 0, 0},
				{-1, 2},
		});
	}
	
	@Test
	public void testAssertTemplate() throws Exception {
		wsMockClient.sendRequest(withMessage("request-context-xslt.xml").withParameter("a",a).withParameter("b", b)).andExpect(message("response-context-xslt.xml").withParameter("result", a+b));
	}
	@Test
	public void testAssertXpath() throws Exception {
		wsMockClient.sendRequest(withMessage("request-context-xslt.xml").withParameter("a",a).withParameter("b", b)).andExpect(xpath("//ns:result",NS_MAP).evaluatesTo(a+b));
	}
}