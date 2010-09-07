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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.dom.DOMSource;

import org.custommonkey.xmlunit.Diff;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

class XsltMessageDiffMatcher extends MessageDiffMatcher implements ParametrizableRequestMatcher<WebServiceMessage> {

	private final Map<String, Object> parameters;
	
	XsltMessageDiffMatcher(Document controlMessage) {
		this(controlMessage, Collections.<String, Object>emptyMap());
	}
	
	XsltMessageDiffMatcher(Document controlMessage, Map<String, Object> parameters) {
		super(controlMessage);
		this.parameters = Collections.unmodifiableMap(new HashMap<String, Object>(parameters));
	}
	
	@Override
	Diff createDiff(Document controlMessage, Document requestDocument) {
		XsltUtil xsltUtil = new XsltUtil(parameters);
		if (xsltUtil.isTemplate(controlMessage))
			{
				Document transformedDocument = xsltUtil.transform(controlMessage, new DOMSource());
				return super.createDiff(transformedDocument, requestDocument);
			}
			else
			{
				return super.createDiff(controlMessage, requestDocument);
			}
	}

	public XsltMessageDiffMatcher withParameter(String name, Object value) {
		return withParameters(Collections.singletonMap(name, value));
	}

	public XsltMessageDiffMatcher withParameters(Map<String, Object> additionalParameters) {
		Map<String, Object> newParameters = new HashMap<String, Object>(parameters);
		newParameters.putAll(additionalParameters);
		return new XsltMessageDiffMatcher(getControlMessage(), newParameters);
	}

}
