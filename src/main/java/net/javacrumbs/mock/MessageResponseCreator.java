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

package net.javacrumbs.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

import javax.xml.transform.dom.DOMSource;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.mock.client.ResponseCreator;
import org.w3c.dom.Document;

class MessageResponseCreator implements ResponseCreator<WebServiceMessage> {
	private static final Charset UTF8_CHARSET = Charset.availableCharsets().get("UTF-8");

	private final Document response;
	
	MessageResponseCreator(Document response) {
		this.response = response;
	}

	public WebServiceMessage createResponse(URI uri, WebServiceMessage request, WebServiceMessageFactory<? extends WebServiceMessage> messageFactory)	throws IOException {
		return createResponseInternal(response, messageFactory);
	}

	WebServiceMessage createResponseInternal(Document response, WebServiceMessageFactory<? extends WebServiceMessage> messageFactory) throws IOException {
		if (XmlUtil.getInstance().isSoap(response))
		{
			return messageFactory.createWebServiceMessage(getResponseAsStream(response));
		}
		else
		{
			WebServiceMessage webServiceMessage = messageFactory.createWebServiceMessage();
			XmlUtil.getInstance().doTransform(new DOMSource(response), webServiceMessage.getPayloadResult());
			return webServiceMessage;
		}
	}
	
	InputStream getResponseAsStream(Document response)
	{
		return new ByteArrayInputStream(XmlUtil.getInstance().serialize(response).getBytes(UTF8_CHARSET));
	}
	
	Document getResponse() {
		return response;
	}



}
