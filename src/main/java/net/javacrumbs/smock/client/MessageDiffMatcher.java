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

import java.net.URI;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.mock.client.RequestMatcher;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

/**
 * Compares whole messages.
 * @author Lukas Krecan
 *
 */
class MessageDiffMatcher implements RequestMatcher<WebServiceMessage> {

	private final Document controlMessage;
	
	static {
	        XMLUnit.setIgnoreWhitespace(true);
	}
	
	public MessageDiffMatcher(Document controlMessage) {
		this.controlMessage = controlMessage;
	}



	public void match(URI uri, WebServiceMessage request){
		if (isSoapControl())
		{
			Document requestDocument = XmlUtil.getInstance().loadDocument(((SoapMessage)request).getEnvelope().getSource());
			compare(requestDocument);
		}
		else //payload only
		{
			Document requestDocument = XmlUtil.getInstance().loadDocument(request.getPayloadSource());
			compare(requestDocument);
		}
	}

	private void compare(Document requestDocument) throws AssertionError {
		Diff diff = createDiff(controlMessage, requestDocument);
		if (!diff.similar())
		{
			throw new AssertionError("Messages are different, " + diff.toString());
		}
	}



	Diff createDiff(Document controlMessage, Document requestDocument) {
		return new Diff(controlMessage, requestDocument);
	}
	
	boolean isSoapControl() {
		return XmlUtil.getInstance().isSoap(controlMessage);
	}

	Document getControlMessage() {
		return controlMessage;
	}
}
