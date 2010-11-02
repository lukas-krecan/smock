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

import java.io.IOException;

import javax.xml.transform.dom.DOMSource;

import net.javacrumbs.smock.common.XmlUtil;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.server.RequestCreator;
import org.w3c.dom.Document;

/**
 * Creates message based on payload or whole SOAP message.
 * @author Lukas Krecan
 *
 */
public class MessageRequestCreator implements RequestCreator {

	private final Document sourceDocument;
	
	public MessageRequestCreator(Document sourceDocument) {
		this.sourceDocument = sourceDocument;
	}

	public WebServiceMessage createRequest(WebServiceMessageFactory messageFactory) throws IOException {
		Document document = preprocessRequest();
		return createRequestInternal(document, messageFactory);
		
	}

	/**
	 * Can be overriden
	 * @return
	 */
	protected Document preprocessRequest() {
		return sourceDocument;
	}

	protected final WebServiceMessage createRequestInternal(Document document, WebServiceMessageFactory messageFactory)
			throws IOException {
		if (XmlUtil.getInstance().isSoap(document))
		{
			return messageFactory.createWebServiceMessage(XmlUtil.getInstance().getResponseAsStream(document));
		}
		else
		{
			WebServiceMessage webServiceMessage = messageFactory.createWebServiceMessage();
			XmlUtil.getInstance().doTransform(new DOMSource(document), webServiceMessage.getPayloadResult());
			return webServiceMessage;
		}
	}

	Document getSourceDocument() {
		return sourceDocument;
	}

}
