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
import java.util.Map;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.w3c.dom.Document;

//TODO support XPath to SOAP Envelope?
public class XsltMessageResponseCreator extends MessageResponseCreator {
	private final XsltUtil xsltUtil;

	XsltMessageResponseCreator(Document response, Map<String, Object> parameters) {
		super(response);
		xsltUtil = new XsltUtil(parameters);
	}

	@Override
	public WebServiceMessage createResponse(URI uri, WebServiceMessage request,
			WebServiceMessageFactory<? extends WebServiceMessage> messageFactory)
			throws IOException {
		Document responseDocument = getResponse();
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

}
