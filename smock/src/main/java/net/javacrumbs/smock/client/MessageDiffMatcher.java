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

import net.javacrumbs.smock.common.EnhancedDiff;
import net.javacrumbs.smock.common.XmlUtil;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.mock.client.RequestMatcher;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

/**
 * Request matcher that compares whole messages. If control document is a SOAP message, 
 * compares whole message, if not, only payloads are compared.
 * @author Lukas Krecan
 *
 */
public class MessageDiffMatcher implements RequestMatcher<WebServiceMessage> {

	static {
		XMLUnit.setIgnoreWhitespace(true);
	}

	private final Document controlMessage;
	
	public MessageDiffMatcher(Document controlMessage) {
		this.controlMessage = controlMessage;
	}

	/**
	 * Checks if control document is a SOAP message. If so, whole message is compared, if not, only payloads are compared.
	 */
	public final void match(URI uri, WebServiceMessage request){
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

	/**
	 * Compares document with control document.
	 * @param requestDocument
	 * @throws AssertionError
	 */
	protected final void compare(Document requestDocument) throws AssertionError {
		Document controlMessage = preprocessControlMessage();
		Diff diff = createDiff(controlMessage, requestDocument);
		if (!diff.similar())
		{
			throw new AssertionError("Messages are different, " + diff.toString());
		}
	}

	/**
	 * Does control message pre-processing. Can be overriden.
	 * @param controlMessage
	 * @return
	 */
	protected Document preprocessControlMessage() {
		return getControlMessage();
	}

	/**
	 * Creates Enhanced Diff. Can be overriden. 
	 * @param controlMessage
	 * @param requestDocument
	 * @return
	 */
	protected Diff createDiff(Document controlMessage, Document requestDocument) {
		return new EnhancedDiff(controlMessage, requestDocument);
	}
	
	private boolean isSoapControl() {
		return XmlUtil.getInstance().isSoap(controlMessage);
	}

	public final Document getControlMessage() {
		return controlMessage;
	}
}
