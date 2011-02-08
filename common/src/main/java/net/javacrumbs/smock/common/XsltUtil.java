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
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;

/**
 * XSLT helper class.
 * @author Lukas Krecan
 *
 */
class XsltUtil extends TransformerObjectSupport{
	private static final Map<String, String> NAMESPACES = Collections.singletonMap("xs", "http://www.w3.org/1999/XSL/Transform");
	private final Map<String, Object> parameters; 
	
	
	public XsltUtil(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns true if given message is a XSL Template. 
	 * @param stringSource
	 * @return
	 */
	 boolean isTemplate(Source context) {
		Jaxp13XPathTemplate template = new Jaxp13XPathTemplate();
		template.setNamespaces(NAMESPACES);
		return template.evaluateAsBoolean("count(/xs:stylesheet)>0", context);
	}
	 
	/**
	 * Does XSLT transformation. 
	 * @param template
	 * @param source
	 * @return
	 * @throws TransformerException 
	 */
	Source transform(Source template, Source source) 
	{
		try {
			StringResult transformedExpectedDocument = new StringResult();
			transform(template, source, transformedExpectedDocument);
			return new StringSource(transformedExpectedDocument.toString());
		} catch (TransformerException e) {
			throw new IllegalArgumentException("Transformation error",e);
		}
	}
	/**
	 * Does XSLT transformation. 
	 * @param template
	 * @param source
	 * @return
	 * @throws TransformerException 
	 */
	void transform(Source template, Source source, Result result) throws TransformerException {
		Transformer transformer = getTransformerFactory().newTransformer(template);
		setParameters(transformer);
		transformer.transform(source, result);
	}
	
	/**
	 * Sets parameters to the transformer.
	 * @param transformer
	 */
	private void setParameters(Transformer transformer) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			transformer.setParameter(entry.getKey(), entry.getValue());
		}
	}
}
