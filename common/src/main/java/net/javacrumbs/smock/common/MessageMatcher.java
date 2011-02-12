/**
 * Copyright 2009-2010 the original author or authors.
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


import static net.javacrumbs.smock.common.XmlUtil.getEnvelopeSource;
import static net.javacrumbs.smock.common.XmlUtil.isSoap;
import static net.javacrumbs.smock.common.XmlUtil.loadDocument;
import static net.javacrumbs.smock.common.XmlUtil.serialize;

import java.io.IOException;
import java.net.URI;

import javax.xml.transform.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.server.ResponseMatcher;



/**
 * Common matching code.
 * @author Lukas Krecan
 *
 */
public class MessageMatcher implements RequestMatcher, ResponseMatcher{

	protected final Source controlMessage;
	
	private final Log logger = LogFactory.getLog(getClass()); 

	static {
		XMLUnit.setIgnoreWhitespace(true);
	}

	public MessageMatcher(Source controlMessage) {
		this.controlMessage = controlMessage;
	}

	/**
	 * Matches document. If the control document is a SOAP message, whole messages are compared, payloads are compared otherwise.
	 * @param input
	 * @param message
	 */
	protected final void matchInternal(WebServiceMessage input, WebServiceMessage message) {
		Source controlMessage = preprocessControlMessage(input);
		if (isSoapControl(controlMessage))
		{
			Source messageSource = getEnvelopeSource(message);
			compare(controlMessage, messageSource);
		}
		else //payload only
		{
			Source messageSource = message.getPayloadSource();
			compare(controlMessage, messageSource);
		}
	}
	
	public void match(WebServiceMessage request, WebServiceMessage response) throws IOException, AssertionError {
		matchInternal(request, response);
		
	}

	public void match(URI uri, WebServiceMessage request) throws IOException, AssertionError {
		matchInternal(null, request);		
	}
	

	/**
	 * Compares message with control message.
	 * @param controlMessage
	 * @param messageSource 
	 * @throws AssertionError
	 */
	protected final void compare(Source controlMessage, Source messageSource) {
		if (logger.isDebugEnabled())
		{
			logger.debug("Comparing:\n "+serialize(controlMessage)+"\n with:\n"+serialize(messageSource));
		}
		Diff diff = createDiff(controlMessage, messageSource);
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
	protected Source preprocessControlMessage(WebServiceMessage input) {
		return getControlMessage();
	}

	/**
	 * Creates Enhanced Diff. Can be overriden. 
	 * @param controlMessage
	 * @param messageDocument
	 * @return
	 */
	protected Diff createDiff(Source controlMessage, Source messageSource) {
		return new EnhancedDiff(loadDocument(controlMessage), loadDocument(messageSource));
	}

	private boolean isSoapControl(Source controlMessage) {
		return isSoap(controlMessage);
	}

	public final Source getControlMessage() {
		return controlMessage;
	}



}