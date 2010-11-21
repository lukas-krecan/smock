package net.javacrumbs.smock.client;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.SmockCommon;
import net.javacrumbs.smock.common.TemplateAwareMessageCompareMatcher;
import net.javacrumbs.smock.common.TemplateAwareMessageCreator;
import net.javacrumbs.smock.common.TemplateProcessor;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.support.MockStrategiesHelper;
import org.w3c.dom.Document;

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
public abstract class SmockClient extends SmockCommon {
			
	private static final Map<String, Object> EMPTY_MAP = Collections.<String, Object>emptyMap();

	/**
	 * Expects the given XML message loaded from resource with given name. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared. 
	 * Message will be preprocessed by {@link TemplateProcessor}.
	 *
	 * @param location of the resource where the message is stored.
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher message(String location) {
		Assert.notNull(location, "'location' must not be null");
		return message(fromResource(location));
	}	
	/**
	 * Expects the given {@link Resource} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 * Message will be preprocessed by {@link TemplateProcessor}.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher message(Resource message) {
		Assert.notNull(message, "'message' must not be null");
		return message(createResourceSource(message));
	}	
	/**
	 * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 * Message will be preprocessed by {@link TemplateProcessor}.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher message(Source message) {
		Assert.notNull(message, "'message' must not be null");
		return message(loadDocument(message));
	}


    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public static ParametrizableRequestMatcher message(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new TemplateAwareMessageCompareMatcher(message, EMPTY_MAP, getTemplateProcessor());
    }	
    
    /**
     * Respond with the given XML loaded from resource as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param loaction of the resource
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(String location) {
    	Assert.notNull(location, "'location' must not be null");
    	return withMessage(fromResource(location));
    }
    /**
     * Respond with the given XML loaded from resource as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param loaction of the resource
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(Resource message) {
    	Assert.notNull(message, "'message' must not be null");
    	return withMessage(createResourceSource(message));
    }
    /**
     * Respond with the given {@link Source} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param payload the response message
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(Source message) {
        Assert.notNull(message, "'message' must not be null");
        return withMessage(loadDocument(message));
    }
   
    /**
     * Respond with the given {@link Document} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param payload the response message
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new TemplateAwareMessageCreator(message, EMPTY_MAP, getTemplateProcessor());
    }
    

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
