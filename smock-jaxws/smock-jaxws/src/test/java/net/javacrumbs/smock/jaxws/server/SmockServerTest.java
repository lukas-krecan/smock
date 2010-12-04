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

package net.javacrumbs.smock.jaxws.server;

import static org.junit.Assert.assertNotNull;
import static net.javacrumbs.smock.jaxws.server.SmockServer.*;

import net.javacrumbs.smock.jaxws.server.test.TestWebService;

import org.junit.After;
import org.junit.Test;


public class SmockServerTest {

	@After
	public void tearDown()
	{
		TestWebService.clean();
	}
	
	@Test
	public void testCreateClient()
	{
		MockWebServiceClient client = createClient("net.javacrumbs.smock.jaxws.server.test");
		assertNotNull(client);
		client.sendRequest("TestWebServiceService", withMessage("request.xml"));
		assertNotNull(TestWebService.getValue());
	}
}
