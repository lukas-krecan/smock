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
package net.javacrumbs.smock.springws.server;

import net.javacrumbs.smock.common.server.CommonSmockServer;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.SoapMessageDispatcher;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.support.MockStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Adds extra functionality to Spring WS server test support. 
 * @author Lukas Krecan
 *
 */
public class SmockServer extends CommonSmockServer {

    /**
     * Creates a {@code MockWebServiceClient} instance based on the given {@link WebServiceMessageReceiver} and {@link
     * WebServiceMessageFactory}. Supports interceptors that can be applied on the outgoing message.
     *
     * @param messageReceiver the message receiver, typically a {@link SoapMessageDispatcher}
     * @param messageFactory  the message factory
     * @return the created client
     */
	public static MockWebServiceClient createClient(WebServiceMessageReceiver messageReceiver,   WebServiceMessageFactory messageFactory, ClientInterceptor[] interceptors) {
			return MockWebServiceClient.createClient(new InterceptingMessageReceiver(messageReceiver, interceptors), messageFactory);
	}
	

    /**
     * Creates a {@code MockWebServiceClient} instance based on the given {@link WebServiceMessageReceiver} and {@link
     * WebServiceMessageFactory}. Supports interceptors that can be applied on the outgoing message.
     *
     * @param messageReceiver the message receiver, typically a {@link SoapMessageDispatcher}
     * @param messageFactory  the message factory
     * @return the created client
     */
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
