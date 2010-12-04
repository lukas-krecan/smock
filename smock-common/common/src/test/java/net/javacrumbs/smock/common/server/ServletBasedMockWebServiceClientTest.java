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

import net.javacrumbs.smock.common.server.test.TestWebService;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static net.javacrumbs.smock.common.server.CommonSmockServer.*;
import static org.junit.Assert.assertNotNull;

public class ServletBasedMockWebServiceClientTest {
	
	@Test
	public void testCxf()
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("cxf-servlet.xml");
		ServletBasedMockWebServiceClient client = new ServletBasedMockWebServiceClient("org.apache.cxf.transport.servlet.CXFServlet", context);
		client.sendRequestTo("/TestWebService", withMessage("request.xml")).andExpect(message("response.xml"));
		
		assertNotNull(TestWebService.getValue());
	}
}
