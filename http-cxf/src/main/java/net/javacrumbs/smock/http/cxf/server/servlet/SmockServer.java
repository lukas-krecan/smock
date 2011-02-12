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

import net.javacrumbs.smock.common.server.CommonSmockServer;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

/**
 * Class to be used by CXF server tests.
 * @author Lukas Krecan
 *
 */
public class SmockServer extends CommonSmockServer {
    
	public static ServletBasedMockWebServiceClient createClient(ApplicationContext applicationContext) {
		return createClient(applicationContext, null);
	}
    public static ServletBasedMockWebServiceClient createClient(ApplicationContext applicationContext, ClientInterceptor[] interceptors) {
        Assert.notNull(applicationContext, "'applicationContext' must not be null");
        return new ServletBasedMockWebServiceClient(applicationContext, interceptors);
    }

}
