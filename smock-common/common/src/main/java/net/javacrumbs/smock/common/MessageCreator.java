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
import java.net.URI;

import javax.xml.transform.dom.DOMSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.client.ResponseCreator;
import org.springframework.ws.test.server.RequestCreator;
import org.w3c.dom.Document;
import static net.javacrumbs.smock.common.XmlUtil.*;

/**
 * Common class that is able to create a message for both client and server.
 * @author Lukas Krecan
 *
 */
public class MessageCreator implements ResponseCreator, RequestCreator{

	private final Document sourceDocument;
	
	protected final Log logger = LogFactory.getLog(getClass());

	public MessageCreator(Document sourceDocument) {
		this.sourceDocument = sourceDocument;
	}
	

	/**
	 * Creates a response. If source document is a SOAP message a response is created as it is (including SOAP faults), if it
	 * contains only a payload, it's wrapped in a SOAP envelope.
	 * @param response
	 * @param messageFactory
	 * @return
	 * @throws IOException
	 */
	protected final WebServiceMessage createMessage(URI uri, WebServiceMessage input, WebServiceMessageFactory messageFactory) throws IOException {
		Document source = preprocessSource(uri, input, messageFactory);
		WebServiceMessage result;
		if (isSoap(source))
		{
			result = messageFactory.createWebServiceMessage(getDocumentAsStream(source));
		}
		else
		{
			WebServiceMessage webServiceMessage = messageFactory.createWebServiceMessage();
			doTransform(new DOMSource(source), webServiceMessage.getPayloadResult());
			result = webServiceMessage;
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Generated message: "+serialize(getEnvelopeSource(result)));
		}
		return result;
	}
	
	public WebServiceMessage createResponse(URI uri, WebServiceMessage request, WebServiceMessageFactory messageFactory) throws IOException {
		return createMessage(uri, request, messageFactory);
	}

	public WebServiceMessage createRequest(WebServiceMessageFactory messageFactory) throws IOException {
		return createMessage(null, null, messageFactory);
	}

	/**
	 * To be overriden by subclasses.
	 * @param uri
	 * @param input
	 * @param messageFactory
	 * @return
	 */
	protected Document preprocessSource(URI uri, WebServiceMessage input, WebServiceMessageFactory messageFactory) {
		return getSourceDocument();
	}

	public final Document getSourceDocument() {
		return sourceDocument;
	}

}