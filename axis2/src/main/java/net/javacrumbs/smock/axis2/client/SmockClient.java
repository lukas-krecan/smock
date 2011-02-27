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

package net.javacrumbs.smock.axis2.client;

import java.util.HashMap;

import net.javacrumbs.smock.common.client.CommonSmockClient;
import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.extended.client.connection.threadlocal.ThreadLocalMockWebServiceServer;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.ListenerManager;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapVersion;

public class SmockClient extends CommonSmockClient {
	static
	{
		bootstrap();
	}
	public static void bootstrap() {
		try {
			ConfigurationContext configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
			HashMap<String, TransportOutDescription> transportsOut = configurationContext.getAxisConfiguration().getTransportsOut();
			for (TransportOutDescription tod: transportsOut.values())
			{
				setSender(tod);
			}
			ListenerManager.defaultConfigurationContext = configurationContext;
		} catch (AxisFault e) {
			throw new IllegalStateException("Can not set ListenerManager.defaultConfigurationContext.",e);
		}
	}
	private static void setSender(TransportOutDescription transportOutDescription)
	{
		if (transportOutDescription!=null)
		{
			transportOutDescription.setSender(new MockTransportSender());
		}
	}
	
	public static MockWebServiceServer createServer(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors)
	{
		return new ThreadLocalMockWebServiceServer(messageFactory, interceptors);
	}
	public static MockWebServiceServer createServer(WebServiceMessageFactory messageFactory)
	{
		return createServer(messageFactory, null);
	}
	public static MockWebServiceServer createServer(EndpointInterceptor[] interceptors)
	{
		return createServer(createMessageFactory(), interceptors);
	}
	public static MockWebServiceServer createServer()
	{
		return createServer(createMessageFactory(), null);
	}
	public static WebServiceMessageFactory createMessageFactory()
    {
    	return createMessageFactory(SoapVersion.SOAP_12);
    }
}
