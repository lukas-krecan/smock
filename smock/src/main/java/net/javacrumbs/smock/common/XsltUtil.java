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
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.xml.transform.TransformerObjectSupport;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
	 boolean isTemplate(Node context) {
		XPathExpression xPathExpression = XPathExpressionFactory.createXPathExpression("count(/xs:stylesheet)>0", NAMESPACES);
		return xPathExpression.evaluateAsBoolean(context);
	}
	 
	/**
	 * Does XSLT transformation. 
	 * @param template
	 * @param source
	 * @return
	 * @throws TransformerException 
	 */
	Document transform(Document template, Source source) 
	{
		try {
			DOMResult transformedExpectedDocument = new DOMResult();
			transform(template, source, transformedExpectedDocument);
			return (Document) transformedExpectedDocument.getNode();
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
	void transform(Document template, Source source, Result result) throws TransformerException {
		Transformer transformer = getTransformerFactory().newTransformer(new DOMSource(template));
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
