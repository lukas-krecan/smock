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

package net.javacrumbs.smock.client;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.smock.common.AbstractMessageCreator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.client.ResponseCreator;
import org.w3c.dom.Document;

/**
 * Creates response from a {@link Document}. If it's a SOAP message a response is created as it is (including SOAP faults), if it
 * contains only a payload, it's wrapped in a SOAP envelope.
 * @author Lukas Krecan
 *
 */
public class MessageResponseCreator extends AbstractMessageCreator implements ResponseCreator<WebServiceMessage> {
	public MessageResponseCreator(Document response) {
		super(response);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	public final WebServiceMessage createResponse(URI uri, WebServiceMessage request, WebServiceMessageFactory<? extends WebServiceMessage> messageFactory) throws IOException
	{
			Document response = preprocessResponse(uri, request, messageFactory);
			return createMessageInternal(response, messageFactory);
	}
	
	/**
	 * Pre-processes response and returns it as a {@link Document}. Can be overriden.
	 * @param uri
	 * @param request
	 * @param messageFactory
	 * @return
	 */
	protected Document preprocessResponse(URI uri, WebServiceMessage request, WebServiceMessageFactory<? extends WebServiceMessage> messageFactory) {
		return getSourceDocument();
	}


}
