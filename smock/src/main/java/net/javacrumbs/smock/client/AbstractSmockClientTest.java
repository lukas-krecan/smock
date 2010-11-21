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

package net.javacrumbs.smock.client;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.TemplateProcessor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;


public abstract class AbstractSmockClientTest extends AbstractWebServiceClientTest {
	/**
	 * Sets {@link TemplateProcessor} used by Smock.
	 * @param templateProcessor
	 */
	public static void setTemplateProcessor(TemplateProcessor templateProcessor) {
		SmockClient.setTemplateProcessor(templateProcessor);
	}
	
	/**
	 * Sets the resource loader to be used. Be aware that it sets a static variable so it will be used by other tests too. 
	 * @param resourceLoader
	 */
	public static void setResourceLoader(ResourceLoader resourceLoader) {
		SmockClient.setResourceLoader(resourceLoader);
	}
	/**
	 * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
	 * @param location Location of the resource 
	 */
	public static Source fromResource(String location)
	{
		return SmockClient.fromResource(location);
	}
	/**
	 * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
	 * @param location Location of the resource 
	 */
	public static Resource resource(String location)
	{
		return SmockClient.resource(location);
	} 

	/** 
	 * Expects the given XML message loaded from resource with given name. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 *
	 * @param location of the resource where the message is stored.
	 * @return the request matcher
	 */
	public  ParametrizableRequestMatcher message(String location) {
		return SmockClient.message(location);
	}	
	/**
	 * Expects the given {@link Resource} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public  ParametrizableRequestMatcher message(Resource message) {
		return SmockClient.message(message);
	}	
	/**
	 * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public  ParametrizableRequestMatcher message(Source message) {
		return SmockClient.message(message);
	}


    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public  ParametrizableRequestMatcher message(Document message) {
    	return SmockClient.message(message);
    }	
    
    /**
     * Respond with the given XML loaded from resource as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     *
     * @param loaction of the resource
     * @return the response callback
     */
    public  ParametrizableResponseCreator withMessage(String location) {
    	return SmockClient.withMessage(location);
    }

    public  ParametrizableResponseCreator withMessage(Resource message) {
    	return SmockClient.withMessage(message);
    }
    /**
     * Respond with the given {@link Source} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     *
     * @param payload the response message
     * @return the response callback
     */
    public  ParametrizableResponseCreator withMessage(Source message) {
    	return SmockClient.withMessage(message);
    }
}
