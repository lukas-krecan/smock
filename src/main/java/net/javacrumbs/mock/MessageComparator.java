package net.javacrumbs.mock;
import java.io.IOException;

import javax.xml.transform.Source;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.mock.client.RequestMatcher;
import org.springframework.ws.mock.client.WebServiceMock;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;

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

/**
 * Adds extra features to {@link WebServiceMock}. 
 */
public abstract class MessageComparator  {
	/**
	 * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public static RequestMatcher message(Resource message) {
		Assert.notNull(message, "'message' must not be null");
		Document document = XmlUtil.getInstance().loadDocument(createResourceSource(message));
		return message(document);
	}	
    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public static RequestMatcher message(Source message) {
        Assert.notNull(message, "'message' must not be null");
       	Document document = XmlUtil.getInstance().loadDocument(message);
		return message(document);
    }	
    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public static RequestMatcher message(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new MessageDiffMatcher(message);
    }	
    
    private static ResourceSource createResourceSource(Resource resource) {
        try {
            return new ResourceSource(resource);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException(resource + " could not be opened", ex);
        }
    }
}
