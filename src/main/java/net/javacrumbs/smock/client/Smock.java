package net.javacrumbs.smock.client;
import java.io.IOException;

import javax.xml.transform.Source;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
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
public abstract class Smock  {
	
	private static TemplateProcessor templateProcessor = new XsltTemplateProcessor();
	
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	/**
	 * Expects the given XML message loaded from resource with given name. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 *
	 * @param location of the resource where the message is stored.
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher<WebServiceMessage> message(String location) {
		Assert.notNull(location, "'location' must not be null");
		return message(fromResource(location));
	}	
	/**
	 * Expects the given {@link Resource} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher<WebServiceMessage> message(Resource message) {
		Assert.notNull(message, "'message' must not be null");
		Document document = loadDocument(createResourceSource(message));
		return message(document);
	}	
    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public static ParametrizableRequestMatcher<WebServiceMessage> message(Source message) {
        Assert.notNull(message, "'message' must not be null");
       	Document document = loadDocument(message);
		return message(document);
    }

    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public static ParametrizableRequestMatcher<WebServiceMessage> message(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new TemplateAwareMessageDiffMatcher(message, templateProcessor);
    }	
    
    /**
     * Respond with the given XML loaded from resource as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     *
     * @param loaction of the resource
     * @return the response callback
     */
    public static ParametrizableResponseCreator<WebServiceMessage> withMessage(String location) {
    	Assert.notNull(location, "'location' must not be null");
    	return withMessage(fromResource(location));
    }
    /**
     * Respond with the given {@link Source} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     *
     * @param payload the response message
     * @return the response callback
     */
    public static ParametrizableResponseCreator<WebServiceMessage> withMessage(Source message) {
        Assert.notNull(message, "'message' must not be null");
        return withMessage(loadDocument(message));
    }
    /**
     * Respond with the given {@link Source} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     *
     * @param payload the response message
     * @return the response callback
     */
    public static ParametrizableResponseCreator<WebServiceMessage> withMessage(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new TemplateAwareMessageResponseCreator(message, templateProcessor);
    }
    
	private static Document loadDocument(Source message) {
		return XmlUtil.getInstance().loadDocument(message);
	}	
    
    private static ResourceSource createResourceSource(Resource resource) {
        try {
            return new ResourceSource(resource);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException(resource + " could not be opened", ex);
        }
    }
    
    /**
     * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
     * @param location Location of the resource 
     */
    public static Source fromResource(String location)
    {
    	return createResourceSource(resource(location));
    }
    /**
     * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
     * @param location Location of the resource 
     */
    public static Resource resource(String location)
    {
    	return resourceLoader.getResource(location);
    }
    
    public static TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}
    public static void setTemplateProcessor(TemplateProcessor templateProcessor) {
    	Assert.notNull(templateProcessor, "'templateProcessor' can not be null");
		Smock.templateProcessor = templateProcessor;
	}
    public static ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
    public static void setResourceLoader(ResourceLoader resourceLoader) {
    	Assert.notNull(resourceLoader, "'resourceLoader' can not be null");
		Smock.resourceLoader = resourceLoader;
	}
}
