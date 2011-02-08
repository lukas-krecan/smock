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
package net.javacrumbs.smock.axis2.server;

import net.javacrumbs.smock.common.server.AbstractCommonSmockServerTest;

import org.apache.axis2.context.ConfigurationContext;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * Creates ServletBasedMockWebServiceClient automatically and provides method for simple use of Smock library.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractSmockServerTest extends AbstractCommonSmockServerTest {
	//TODO finish
	protected  Axis2MockWebServiceClient mockWebServiceClient;
	
	protected Axis2MockWebServiceClient createClient(ConfigurationContext configurationContext, ClientInterceptor[] interceptors) {
		return SmockServer.createClient(configurationContext);
	}
}
