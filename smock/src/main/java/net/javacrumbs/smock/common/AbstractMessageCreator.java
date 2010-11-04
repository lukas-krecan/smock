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

import javax.xml.transform.dom.DOMSource;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.w3c.dom.Document;

public abstract class AbstractMessageCreator {

	private final Document sourceDocument;

	public AbstractMessageCreator(Document sourceDocument) {
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
	protected final WebServiceMessage createMessageInternal(Document response, WebServiceMessageFactory<? extends WebServiceMessage> messageFactory) throws IOException {
		if (XmlUtil.getInstance().isSoap(response))
		{
			return messageFactory.createWebServiceMessage(XmlUtil.getInstance().getResponseAsStream(response));
		}
		else
		{
			WebServiceMessage webServiceMessage = messageFactory.createWebServiceMessage();
			XmlUtil.getInstance().doTransform(new DOMSource(response), webServiceMessage.getPayloadResult());
			return webServiceMessage;
		}
	}

	public final Document getSourceDocument() {
		return sourceDocument;
	}

}