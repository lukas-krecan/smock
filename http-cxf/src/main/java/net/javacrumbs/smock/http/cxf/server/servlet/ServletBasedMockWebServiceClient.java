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
package net.javacrumbs.smock.http.cxf.server.servlet;

import javax.servlet.http.HttpServlet;

import net.javacrumbs.smock.common.SmockCommon;
import net.javacrumbs.smock.http.server.servlet.CommonServletBasedMockWebServiceClient;
import net.javacrumbs.smock.http.server.servlet.ServletUtils;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * Creates connection to CXF servlet.
 * @author Lukas Krecan
 */
public class ServletBasedMockWebServiceClient extends CommonServletBasedMockWebServiceClient {
	
	public ServletBasedMockWebServiceClient(HttpServlet servlet, WebServiceMessageFactory messageFactory,	ClientInterceptor[] clientInterceptors) {
		super(servlet, messageFactory, clientInterceptors);
	}

	public ServletBasedMockWebServiceClient(ApplicationContext applicationContext, ClientInterceptor[] clientInterceptors) {
		this(ServletUtils.createServlet(CXFServlet.class, applicationContext, null, null), SmockCommon.withMessageFactory(applicationContext), clientInterceptors);
	}

	public ServletBasedMockWebServiceClient(ApplicationContext applicationContext) {
		this(applicationContext, null);
	}

}
