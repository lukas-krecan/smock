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


import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.server.ResponseMatcher;
import org.w3c.dom.Document;

/**
 * Common matching code.
 * @author Lukas Krecan
 *
 */
public class MessageCompareMatcher implements RequestMatcher, ResponseMatcher{

	protected final Document controlMessage;
	
	private final Log logger = LogFactory.getLog(getClass()); 

	static {
		XMLUnit.setIgnoreWhitespace(true);
	}

	public MessageCompareMatcher(Document controlMessage) {
		this.controlMessage = controlMessage;
	}

	protected final void matchInternal(WebServiceMessage input, WebServiceMessage message) {
		Document controlMessage = preprocessControlMessage(input);
		if (isSoapControl(controlMessage))
		{
			Document messageDocument = XmlUtil.getInstance().loadDocument(((SoapMessage)message).getEnvelope().getSource());
			compare(controlMessage, messageDocument);
		}
		else //payload only
		{
			Document messageDocument = XmlUtil.getInstance().loadDocument(message.getPayloadSource());
			compare(controlMessage, messageDocument);
		}
	}
	
	/*
	 * RequestMatcher
	 * @see org.springframework.ws.test.client.RequestMatcher#match(java.net.URI, org.springframework.ws.WebServiceMessage)
	 */
	public void match(URI uri, WebServiceMessage request)  {
		matchInternal(null, request);		
	}
	
	/*
	 * Response matcher.
	 * @see org.springframework.ws.test.server.ResponseMatcher#match(org.springframework.ws.WebServiceMessage, org.springframework.ws.WebServiceMessage)
	 */
	public void match(WebServiceMessage request, WebServiceMessage response) {
		matchInternal(request, response);
	}

	/**
	 * Compares document with control document.
	 * @param messageDocument
	 * @param messageDocument 
	 * @throws AssertionError
	 */
	protected final void compare(Document controlMessage, Document messageDocument) {
		if (logger.isDebugEnabled())
		{
			logger.debug("Comparing:\n "+XmlUtil.getInstance().serialize(controlMessage)+"\n with:\n"+XmlUtil.getInstance().serialize(messageDocument));
		}
		Diff diff = createDiff(controlMessage, messageDocument);
		if (!diff.similar())
		{
			throw new AssertionError("Messages are different, " + diff.toString());
		}
	}
	
	/**
	 * Does control message pre-processing. Can be overriden.
	 * @param input 
	 * @param controlMessage
	 * @return
	 */
	protected Document preprocessControlMessage(WebServiceMessage input) {
		return getControlMessage();
	}

	/**
	 * Creates Enhanced Diff. Can be overriden. 
	 * @param controlMessage
	 * @param messageDocument
	 * @return
	 */
	protected Diff createDiff(Document controlMessage, Document messageDocument) {
		return new EnhancedDiff(controlMessage, messageDocument);
	}

	private boolean isSoapControl(Document controlMessage) {
		return XmlUtil.getInstance().isSoap(controlMessage);
	}

	public final Document getControlMessage() {
		return controlMessage;
	}

}