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

import static net.javacrumbs.smock.common.SmockCommon.fromResource;
import static net.javacrumbs.smock.common.server.ServerAssert.deserialize;
import static org.junit.Assert.assertEquals;
import net.javacrumbs.smock.common.server.test.CalcEndpoint;
import net.javacrumbs.smock.common.server.test.PlusRequest;
import net.javacrumbs.smock.common.server.test.PlusResponse;

import org.junit.Test;


public class ServerAssertTest {
	private CalcEndpoint endpoint = new CalcEndpoint();
	
	@Test
	public void testAssert()
	{
		
		
		PlusRequest request = deserialize(fromResource("xml/request1.xml"), PlusRequest.class);
		PlusResponse respense = endpoint.plus(request);
		assertEquals(3, respense.getResult());
	//	ServerAssert.assertMatches(payload(from(document)));
	}
}
