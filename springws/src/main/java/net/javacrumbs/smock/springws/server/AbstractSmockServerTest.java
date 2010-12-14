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

package net.javacrumbs.smock.springws.server;

import net.javacrumbs.smock.common.server.AbstractCommonSmockServerTest;
import net.javacrumbs.smock.common.server.SmockMockWebServiceClient;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Extends {@link AbstractWebServiceServerTest} and adds Smock specific methods.
 * @author Lukas Krecan
 *
 */
public class AbstractSmockServerTest extends AbstractCommonSmockServerTest {
	
	/**
	 * Creates a {@code MockWebServiceClient} instance based on the given {@link WebServiceMessageReceiver} and {@link
     * WebServiceMessageFactory}. Supports interceptors that can be applied on the outgoing message.
	 */
	@Override
	public SmockMockWebServiceClient createClient(ApplicationContext applicationContext) {
		return new SpringWsSmockMockWebServiceClient(SmockServer.createClient(applicationContext, getInterceptors()));
	}

	/**
	 * To be overriden by a subclass that needs to set interceptors.
	 * @return
	 */
	protected ClientInterceptor[] getInterceptors() {
		return null;
	}
}
