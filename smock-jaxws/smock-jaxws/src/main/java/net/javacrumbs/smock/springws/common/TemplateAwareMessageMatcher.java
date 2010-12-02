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
import net.javacrumbs.smock.common.UniversalTemplateAwareMessageMatcher;
import net.javacrumbs.smock.springws.client.ParametrizableRequestMatcher;
import net.javacrumbs.smock.springws.server.ParametrizableResponseMatcher;

import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

/**
 * {@link MessageMatcher} that processes template before comparison.
 * @author Lukas Krecan
 *
 */
public class TemplateAwareMessageMatcher implements ParametrizableResponseMatcher, ParametrizableRequestMatcher {

	private final UniversalTemplateAwareMessageMatcher messageMatcher;
	
	public TemplateAwareMessageMatcher(Document controlMessage, Map<String, Object> parameters, TemplateProcessor templateProcessor) {
		this.messageMatcher = new UniversalTemplateAwareMessageMatcher(controlMessage, parameters, templateProcessor);
	}

	public TemplateAwareMessageMatcher withParameter(String name, Object value) {
		messageMatcher.withParameter(name, value);
		return this;
	}

	public TemplateAwareMessageMatcher withParameters(Map<String, Object> parameters) {
		messageMatcher.withParameters(parameters);
		return this;
	}

	public void match(WebServiceMessage request, WebServiceMessage response) throws IOException, AssertionError {
		messageMatcher.match(request, response);
		
	}

	public void match(URI uri, WebServiceMessage request) throws IOException, AssertionError {
		messageMatcher.match(null, request);		
	}
	
	


}
