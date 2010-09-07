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

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.w3c.dom.Document;

//TODO support XPath to SOAP Envelope?
class XsltMessageResponseCreator extends MessageResponseCreator implements ParametrizableResponseCreator<WebServiceMessage>{
	
	private final Map<String, Object> parameters;

	XsltMessageResponseCreator(Document response) {
		this(response, Collections.<String, Object>emptyMap());
	}

	XsltMessageResponseCreator(Document response, Map<String, Object> parameters) {
		super(response);
		this.parameters = Collections.unmodifiableMap(new HashMap<String, Object>(parameters));
	}

	@Override
	public WebServiceMessage createResponse(URI uri, WebServiceMessage request, WebServiceMessageFactory<? extends WebServiceMessage> messageFactory) throws IOException {
		Document responseDocument = getResponse();
		XsltUtil xsltUtil = new XsltUtil(parameters);
		if (xsltUtil.isTemplate(responseDocument))
		{
			Document transformedResponse = xsltUtil.transform(responseDocument, request.getPayloadSource());
			return super.createResponseInternal(transformedResponse, messageFactory);
		}
		else
		{
			return super.createResponseInternal(responseDocument, messageFactory);
		}
	}
	
	public XsltMessageResponseCreator withParameter(String name, Object value) {
		return withParameters(Collections.singletonMap(name, value));
	}

	public XsltMessageResponseCreator withParameters(Map<String, Object> additionalParameters) {
		Map<String, Object> newParameters = new HashMap<String, Object>(parameters);
		newParameters.putAll(additionalParameters);
		return new XsltMessageResponseCreator(getResponse(), newParameters);
	}

}
