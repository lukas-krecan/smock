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

package net.javacrumbs.smock.common;

import java.io.IOException;

import org.springframework.ws.FaultAwareWebServiceMessage;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Common template class for interceptor handling.
 * @author Lukas Krecan
 */
public class InterceptingTemplate {
	
	private final ClientInterceptor[] interceptors;
	
    public InterceptingTemplate(ClientInterceptor[] interceptors) {
		this.interceptors = interceptors;
	}

	public void interceptRequest(MessageContext messageContext, WebServiceMessageReceiver messageReceiver) throws Exception {
        int interceptorIndex = -1;
		if (interceptors != null) {
            for (int i = 0; i < interceptors.length; i++) {
                interceptorIndex = i;
                if (!interceptors[i].handleRequest(messageContext)) {
                    break;
                }
            }
        }
		// if an interceptor has set a response, we don't send/receive
        if (!messageContext.hasResponse()) {
        	messageReceiver.receive(messageContext);
        }
        if (messageContext.hasResponse()) {
            if (!hasFault(messageContext.getResponse())) {
                triggerHandleResponse(interceptorIndex, messageContext);
            }
            else {
                triggerHandleFault(interceptorIndex, messageContext);
            }
        }
	}
	
    protected boolean hasFault(WebServiceMessage response) throws IOException {
        if (response instanceof FaultAwareWebServiceMessage) {
            FaultAwareWebServiceMessage faultMessage = (FaultAwareWebServiceMessage) response;
            return faultMessage.hasFault();
        }
        return false;
    }

    /**
     * Trigger handleResponse on the defined ClientInterceptors. Will just invoke said method on all interceptors whose
     * handleRequest invocation returned <code>true</code>, in addition to the last interceptor who returned
     * <code>false</code>.
     *
     * @param interceptorIndex index of last interceptor that was called
     * @param messageContext   the message context, whose request and response are filled
     * @see ClientInterceptor#handleResponse(MessageContext)
     * @see ClientInterceptor#handleFault(MessageContext)
     */
    private void triggerHandleResponse(int interceptorIndex, MessageContext messageContext) {
        if (messageContext.hasResponse() && interceptors != null) {
            for (int i = interceptorIndex; i >= 0; i--) {
                if (!interceptors[i].handleResponse(messageContext)) {
                    break;
                }
            }
        }
    }

    /**
     * Trigger handleFault on the defined ClientInterceptors. Will just invoke said method on all interceptors whose
     * handleRequest invocation returned <code>true</code>, in addition to the last interceptor who returned
     * <code>false</code>.
     *
     * @param interceptorIndex index of last interceptor that was called
     * @param messageContext   the message context, whose request and response are filled
     * @see ClientInterceptor#handleResponse(MessageContext)
     * @see ClientInterceptor#handleFault(MessageContext)
     */
    private void triggerHandleFault(int interceptorIndex, MessageContext messageContext) {
        if (messageContext.hasResponse() && interceptors != null) {
            for (int i = interceptorIndex; i >= 0; i--) {
                if (!interceptors[i].handleFault(messageContext)) {
                    break;
                }
            }
        }
    }
	



}
