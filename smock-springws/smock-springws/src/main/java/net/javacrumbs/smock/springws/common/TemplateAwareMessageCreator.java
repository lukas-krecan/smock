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

package net.javacrumbs.smock.springws.common;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import net.javacrumbs.smock.common.TemplateProcessor;
import net.javacrumbs.smock.common.UniversalTemplateAwareMessageCreator;
import net.javacrumbs.smock.springws.client.ParametrizableResponseCreator;
import net.javacrumbs.smock.springws.server.ParametrizableRequestCreator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.w3c.dom.Document;


/**
 * {@link MessageResponseCreator} that preprocesses response using {@link TemplateProcessor}.
 * @author Lukas Krecan
 *
 */
public class TemplateAwareMessageCreator implements ParametrizableResponseCreator, ParametrizableRequestCreator{
	
	private final UniversalTemplateAwareMessageCreator messageCreator;

	public TemplateAwareMessageCreator(Document response, Map<String, Object> parameters, TemplateProcessor templateProcessor) {
		this.messageCreator = new UniversalTemplateAwareMessageCreator(response, parameters, templateProcessor);
	}
	
	public TemplateAwareMessageCreator withParameter(String name, Object value) {
		messageCreator.withParameter(name, value);
		return this;
	}

	public TemplateAwareMessageCreator withParameters(Map<String, Object> additionalParameters) {
		messageCreator.withParameters(additionalParameters);
		return this;
	}

	public WebServiceMessage createResponse(URI uri, WebServiceMessage request, WebServiceMessageFactory messageFactory) throws IOException {
		return messageCreator.createMessage(uri, request, messageFactory);
	}

	public WebServiceMessage createRequest(WebServiceMessageFactory messageFactory) throws IOException {
		return messageCreator.createMessage(null, null, messageFactory);
	}

}
