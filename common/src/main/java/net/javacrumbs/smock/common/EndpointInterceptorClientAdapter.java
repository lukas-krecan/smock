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
package net.javacrumbs.smock.common;

import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

/**
 * Converts {@link EndpointInterceptor} to {@link ClientInterceptor}.
 * @author Lukas Krecan
 *
 */
public class EndpointInterceptorClientAdapter implements ClientInterceptor {
	private final EndpointInterceptor interceptor;
	
	public EndpointInterceptorClientAdapter(EndpointInterceptor interceptor) {
		this.interceptor = interceptor;
	}
	
	/**
	 * Wraps all intrceptors in the array.
	 * @param endpointInterceptors
	 * @return
	 */
	public static ClientInterceptor[] wrapEndpointInterceptors(EndpointInterceptor[] endpointInterceptors)
	{
  		 if (endpointInterceptors!=null)
  		 {
  			 ClientInterceptor[] result = new ClientInterceptor[endpointInterceptors.length];
			 for (int i=0; i<endpointInterceptors.length; i++)
			 {
				 result[i]= new EndpointInterceptorClientAdapter(endpointInterceptors[i]);
			 }
			 return result;
  		 }
  		 else
  		 {
  			 return new ClientInterceptor[0];
  		 }
	}

	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		try {
			return interceptor.handleFault(messageContext, null);
		} catch (Exception e) {
			throw new ServerSideInterceptorWebServiceClientException(e);
		}
	}

	public boolean handleRequest(MessageContext messageContext)	throws WebServiceClientException {
		try {
			return interceptor.handleRequest(messageContext, null);
		} catch (Exception e) {
			throw new ServerSideInterceptorWebServiceClientException(e);
		}
	}

	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		try {
			return interceptor.handleResponse(messageContext, null);
		} catch (Exception e) {
			throw new ServerSideInterceptorWebServiceClientException(e);
		}
	}
	
	/**
	 * Exception thrown when an interceptor used by MockWsClient throws an exception.
	 * @author Lukas Krecan
	 *
	 */
	private static final class ServerSideInterceptorWebServiceClientException extends WebServiceClientException
	{
		private static final long serialVersionUID = -1658208004293097418L;

		public ServerSideInterceptorWebServiceClientException(Exception e) {
			super("Exception thrown by interceptors injected by test.", e);
		}
	}

}
