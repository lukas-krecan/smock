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

import net.javacrumbs.smock.common.AbstractMessageCreator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.server.RequestCreator;
import org.w3c.dom.Document;

/**
 * Creates message based on payload or whole SOAP message.
 * @author Lukas Krecan
 *
 */
public class MessageRequestCreator extends AbstractMessageCreator implements RequestCreator {
	
	public MessageRequestCreator(Document sourceDocument) {
		super(sourceDocument);
	}

	public WebServiceMessage createRequest(WebServiceMessageFactory messageFactory) throws IOException {
		Document document = preprocessRequest();
		return createMessageInternal(document, messageFactory);
		
	}

	/**
	 * Can be overriden
	 * @return
	 */
	protected Document preprocessRequest() {
		return getSourceDocument();
	}
}
