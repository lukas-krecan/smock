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

package net.javacrumbs.smock.http.metro.server.servlet;

import net.javacrumbs.smock.http.server.servlet.AbstractHttpSmockServerTest;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * Creates ServletBasedMockWebServiceClient automatically and provides method for simple use of Smock library.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractSmockServerTest extends AbstractHttpSmockServerTest {
	
	@Override
	protected ServletBasedMockWebServiceClient createClient(ApplicationContext applicationContext, ClientInterceptor[] interceptors) {
		return SmockServer.createClient(applicationContext, interceptors);
	}
	
	
}
