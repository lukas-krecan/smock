package net.javacrumbs.smock.springws.client;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.javacrumbs.smock.common.client.CommonSmockClient;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.support.MockStrategiesHelper;

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

/**
 * Adds extra features to Spring WS client test support.
 * @author Lukas Krecan
 */
public abstract class SmockClient extends CommonSmockClient {
			
	/**
     * Creates a {@code MockWebServiceServer} instance based on the given {@link WebServiceTemplate}.
     * Supports interceptors that will be applied on the incomming message. Please note that acctually the interceptoes will
     * be added to the {@link ClientInterceptor} set on the client side. it's an ugly hack, but that's the only way to do it 
     * without reimplementing the whole testing library. I hope it will change in next releases.
     *
     * @param webServiceTemplate the web service template
     * @return the created server
     */
    public static MockWebServiceServer createServer(WebServiceTemplate webServiceTemplate, EndpointInterceptor[] interceptors) {
    	 if (interceptors!=null && interceptors.length>0)
    	 {
    		 List<ClientInterceptor> newInterceptors = new ArrayList<ClientInterceptor>();
    		 if (webServiceTemplate.getInterceptors()!=null)
    		 {
    			 newInterceptors.addAll(Arrays.asList(webServiceTemplate.getInterceptors()));
    		 }
    		 for (EndpointInterceptor interceptor: interceptors)
    		 {
    			 newInterceptors.add(new ClientEndpointInterceptorAdapter(interceptor));
    		 }
    		 webServiceTemplate.setInterceptors(newInterceptors.toArray(new ClientInterceptor[newInterceptors.size()]));
    	 }
         return MockWebServiceServer.createServer(webServiceTemplate);
    }

    /**
     * Creates a {@code MockWebServiceServer} instance based on the given {@link WebServiceGatewaySupport}.
     * Supports interceptors that will be applied on the incomming message. Please note that acctually the interceptoes will
     * be added to the {@link ClientInterceptor} set on the client side. it's an ugly hack, but that's the only way to do it 
     * without reimplementing the whole testing library. I hope it will change in next releases.
     * @param gatewaySupport
     * @param interceptors
     * @return
     */
    public static MockWebServiceServer createServer(WebServiceGatewaySupport gatewaySupport, EndpointInterceptor[] interceptors) {
    	Assert.notNull(gatewaySupport, "'gatewaySupport' must not be null");
        return createServer(gatewaySupport.getWebServiceTemplate(), interceptors);
    }

    /**
     * Creates a {@code MockWebServiceServer} instance based on the given {@link ApplicationContext}.
     * Supports interceptors that will be applied on the incomming message. Please note that acctually the interceptoes will
     * be added to the {@link ClientInterceptor} set on the client side. it's an ugly hack, but that's the only way to do it 
     * without reimplementing the whole testing library. I hope it will change in next releases.
     * @param applicationContext
     * @param interceptors
     * @return
     */
    public static MockWebServiceServer createServer(ApplicationContext applicationContext, EndpointInterceptor[] interceptors) {
    	MockStrategiesHelper strategiesHelper = new MockStrategiesHelper(applicationContext);
        WebServiceTemplate webServiceTemplate = strategiesHelper.getStrategy(WebServiceTemplate.class);
        if (webServiceTemplate != null) {
            return createServer(webServiceTemplate, interceptors);
        }
        WebServiceGatewaySupport gatewaySupport = strategiesHelper.getStrategy(WebServiceGatewaySupport.class);
        if (gatewaySupport != null) {
            return createServer(gatewaySupport, interceptors);
        }
        throw new IllegalArgumentException(
                "Could not find either WebServiceTemplate or WebServiceGatewaySupport in application context");
    }
    



}
