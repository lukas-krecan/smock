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

package net.javacrumbs.smock.server;

import java.util.Collections;

import javax.xml.transform.Source;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.SoapMessageDispatcher;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.support.MockStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;

import net.javacrumbs.smock.common.SmockCommon;
import net.javacrumbs.smock.common.TemplateAwareMessageCompareMatcher;
import net.javacrumbs.smock.common.TemplateAwareMessageCreator;

public class SmockServer extends SmockCommon {
	//TODO add all variants of methods (Source, Resource, String args)
    /**
     * Create a request with the given {@link Source} XML as payload.
     *
     * @param payload the request payload
     * @return the request creator
     */
    public static ParametrizableRequestCreator withContent(Source content) {
    	
    	return new TemplateAwareMessageCreator(loadDocument(content),Collections.<String, Object>emptyMap(), getTemplateProcessor());
    }
    /**
     * Create a request with the given {@link Source} XML as payload.
     *
     * @param payload the request payload
     * @return the request creator
     */
    public static ParametrizableRequestCreator withMessage(String contentResource) {
    	return withContent(fromResource(contentResource));
    }
    
    public static ParametrizableResponseMatcher message(String messageResource)
    {
    	return message(fromResource(messageResource));
    }
    
	public static ParametrizableResponseMatcher message(Source content) {
		return new TemplateAwareMessageCompareMatcher(loadDocument(content),Collections.<String, Object>emptyMap(), getTemplateProcessor());
	}
	
	
	
	public static MockWebServiceClient createClient(WebServiceMessageReceiver messageReceiver,   WebServiceMessageFactory messageFactory, ClientInterceptor[] interceptors) {
			return MockWebServiceClient.createClient(new InterceptingMessageReceiver(messageReceiver, interceptors), messageFactory);
	}
	
    public static MockWebServiceClient createClient(ApplicationContext applicationContext, ClientInterceptor[] interceptors) {
        Assert.notNull(applicationContext, "'applicationContext' must not be null");

        MockStrategiesHelper strategiesHelper = new MockStrategiesHelper(applicationContext);

        WebServiceMessageReceiver messageReceiver =
                strategiesHelper.getStrategy(WebServiceMessageReceiver.class, SoapMessageDispatcher.class);
        WebServiceMessageFactory messageFactory =
                strategiesHelper.getStrategy(WebServiceMessageFactory.class, SaajSoapMessageFactory.class);
        return createClient(messageReceiver, messageFactory, interceptors);
    }

}
