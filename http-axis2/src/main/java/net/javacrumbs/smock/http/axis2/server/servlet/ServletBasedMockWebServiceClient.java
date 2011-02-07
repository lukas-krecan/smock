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
package net.javacrumbs.smock.http.axis2.server.servlet;

import net.javacrumbs.smock.http.server.servlet.CommonServletBasedMockWebServiceClient;

import org.apache.axis2.transport.http.AxisServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * Creates connection to CXF servlet.
 * @author Lukas Krecan
 */
public class ServletBasedMockWebServiceClient extends CommonServletBasedMockWebServiceClient {
	public ServletBasedMockWebServiceClient(String serviceRepositoryPath, ApplicationContext applicationContext, ClientInterceptor[] clientInterceptors) {
		super(AxisServlet.class, applicationContext, clientInterceptors, null, serviceRepositoryPath);
	}
	
	public ServletBasedMockWebServiceClient(String serviceRepositoryPath) {
		this(serviceRepositoryPath, null, null);
	}

}
