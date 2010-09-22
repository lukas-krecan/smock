/*
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

import java.io.IOException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Diff that ignores "${IGNORE}" placeholder and is able to correctly compare namespace prefixes in the attribute values.
 * @author Lukas Krecan
 *
 */
public final class EnhancedDiff extends Diff {
	public EnhancedDiff(Document controlDoc, Document testDoc) {
		super(controlDoc, testDoc);
	}

	EnhancedDiff(String control, String test) throws SAXException, IOException {
		super(control, test);
	}

	public int differenceFound(Difference difference) {
		//ignore dissimilarities
		if (difference.isRecoverable())
		{
			return RETURN_ACCEPT_DIFFERENCE;
		}
		if ("${IGNORE}".equals(difference.getControlNodeDetail().getValue())) {
			return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		} 
		else if (isDifferenceOnlyInAttributeValuePrefix(difference)) 
		{
			return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		}
		else
		{
			return super.differenceFound(difference);
		}

	}

	protected final boolean isDifferenceOnlyInAttributeValuePrefix(Difference difference) {
		Node testNode = difference.getTestNodeDetail().getNode();
		if (testNode==null)
		{
			return false;
		}
		String testNodeValue = testNode.getNodeValue();
		
		Node controlNode = difference.getControlNodeDetail().getNode();
		String controlNodeValue = controlNode.getNodeValue();
		
		if (isAttr(testNode) && isAttr(controlNode) && (hasNsPrefix(testNodeValue) || hasNsPrefix(controlNodeValue)))
		{
			String testValueNsResolved = resolveNamespaces(testNode, testNodeValue);
			String controlValueNsResolved = resolveNamespaces(controlNode, controlNodeValue);
			
			return testValueNsResolved.equals(controlValueNsResolved);
			
		}
		return false;
	}

	/**
	 * Replaces namespace prefixes with their URLs.
	 * @param node
	 * @param value
	 * @return
	 */
	private String resolveNamespaces(Node node, String value) {
		int prefixLength = value.indexOf(':');
		if (prefixLength>=0)
		{
			String prefix = value.substring(0, prefixLength);
			String nsUri = node.lookupNamespaceURI(prefix);
			if (nsUri==null)//prefix not resolved, let's use prefix instead
			{
				nsUri = prefix;
			}
			return nsUri+value.substring(prefixLength);
		}
		else
		{
			String nsUri = node.lookupNamespaceURI(null);
			return nsUri+":"+value;
		}
	}

	private boolean hasNsPrefix(String testNodeValue) {
		return testNodeValue.contains(":");
	}

	private boolean isAttr(Node testNode) {
		return testNode instanceof Attr;
	}
}