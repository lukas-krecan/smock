package net.javacrumbs.smock.common.client;
import java.util.Collections;
import java.util.Map;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.SmockCommon;
import net.javacrumbs.smock.common.TemplateAwareMessageCreator;
import net.javacrumbs.smock.common.TemplateAwareMessageMatcher;
import net.javacrumbs.smock.common.TemplateProcessor;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
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
 * Adds extra features to Spring WS client test support.
 * @author Lukas Krecan
 */
public abstract class CommonSmockClient extends SmockCommon {
			
	private static final Map<String, Object> EMPTY_MAP = Collections.<String, Object>emptyMap();

	/**
	 * Expects the given XML message loaded from resource with given name. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared. 
	 * Message will be preprocessed by {@link TemplateProcessor}.
	 *
	 * @param location of the resource where the message is stored.
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher message(String location) {
		Assert.notNull(location, "'location' must not be null");
		return message(fromResource(location));
	}	
	/**
	 * Expects the given {@link Resource} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 * Message will be preprocessed by {@link TemplateProcessor}.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher message(Resource message) {
		Assert.notNull(message, "'message' must not be null");
		return message(createResourceSource(message));
	}	
	/**
	 * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
	 * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
	 * Message will be preprocessed by {@link TemplateProcessor}.
	 *
	 * @param message the XML message
	 * @return the request matcher
	 */
	public static ParametrizableRequestMatcher message(Source message) {
		Assert.notNull(message, "'message' must not be null");
		return message(loadDocument(message));
	}


    /**
     * Expects the given {@link Source} XML message. Message can either be whole SOAP message or just a payload.
     * If only payload is passed in, only payloads will be compared, otherwise whole message will be compared.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param message the XML message
     * @return the request matcher
     */
    public static ParametrizableRequestMatcher message(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new TemplateAwareMessageMatcher(message, EMPTY_MAP, getTemplateProcessor());
    }	
    
    /**
     * Respond with the given XML loaded from resource as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param loaction of the resource
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(String location) {
    	Assert.notNull(location, "'location' must not be null");
    	return withMessage(fromResource(location));
    }
    /**
     * Respond with the given XML loaded from resource as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param loaction of the resource
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(Resource message) {
    	Assert.notNull(message, "'message' must not be null");
    	return withMessage(createResourceSource(message));
    }
    /**
     * Respond with the given {@link Source} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param payload the response message
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(Source message) {
        Assert.notNull(message, "'message' must not be null");
        return withMessage(loadDocument(message));
    }
   
    /**
     * Respond with the given {@link Document} XML as response. If message is SOAP, it will be returned as response, if message is payload, 
     * it will be wrapped into a SOAP.
     * Message will be preprocessed by {@link TemplateProcessor}.
     *
     * @param payload the response message
     * @return the response callback
     */
    public static ParametrizableResponseCreator withMessage(Document message) {
    	Assert.notNull(message, "'message' must not be null");
    	return new TemplateAwareMessageCreator(message, EMPTY_MAP, getTemplateProcessor());
    }
}
