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

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;

import org.springframework.ws.soap.SoapVersion;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;

class XmlUtil extends TransformerObjectSupport {
	private static final XmlUtil INSTANCE = new XmlUtil();

	private static final Map<String, String> SOAP_NAMESPACES = new HashMap<String, String>();
	
	static{
		SOAP_NAMESPACES.put("soap11", SoapVersion.SOAP_11.getEnvelopeNamespaceUri());
		SOAP_NAMESPACES.put("soap12", SoapVersion.SOAP_12.getEnvelopeNamespaceUri());
	};
	
	
	
	private XmlUtil()
	{
		
	}
	
	static XmlUtil getInstance() {
		return INSTANCE;
	}

	public Document loadDocument(Source source)
	{
		DOMResult result = new DOMResult();
		try {
			transform(source, result);
			return (Document)result.getNode();
		} catch (TransformerException e) {
			throw new IllegalArgumentException("Can not parse message",e);
		}
	}
	
	boolean isSoap(Document document) {
		for (String prefix: SOAP_NAMESPACES.keySet()) {
			String expression = "/"+prefix+":Envelope";
			XPathExpression xPathExpression = XPathExpressionFactory.createXPathExpression(expression, SOAP_NAMESPACES);
			if (xPathExpression.evaluateAsBoolean(document))
			{
				return true;
			}
		}
		return false;
	}
}
