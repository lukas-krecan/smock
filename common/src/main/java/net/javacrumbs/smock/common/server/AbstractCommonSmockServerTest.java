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
package net.javacrumbs.smock.common.server;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.TemplateProcessor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.w3c.dom.Document;

/**
 * Extends {@link AbstractCommonWebServiceServerTest} and adds Smock specific methods.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractCommonSmockServerTest extends AbstractCommonWebServiceServerTest {
	/**
	 * Sets {@link TemplateProcessor} used by Smock.
	 * @param templateProcessor
	 */
	public void setTemplateProcessor(TemplateProcessor templateProcessor) {
		CommonSmockServer.setTemplateProcessor(templateProcessor);
	}

	/**
	 * Sets the resource loader to be used. Be aware that it sets a static variable so it will be used by other tests too. 
	 * @param resourceLoader
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		CommonSmockServer.setResourceLoader(resourceLoader);
	}
	/**
	 * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
	 * @param location Location of the resource 
	 */
	public  Source fromResource(String location)
	{
		return CommonSmockServer.fromResource(location);
	}
	/**
	 * Loads resource using resourceLoader set by {@link #setResourceLoader(ResourceLoader)}.
	 * @param location Location of the resource 
	 */
	public Resource resource(String location)
	{
		return CommonSmockServer.resource(location);
	}
	/**
	 * Create a request with the given {@link Resource} as content. If the content is payload it will be wrapped into a SOAP, 
	 * if the content contains the whole message, it will be used as it is. Supports templates that will be resolved by {@link TemplateProcessor}.
	 *
	 * @param payload the request payload
	 * @return the request creator
	 */
	public  ParametrizableRequestCreator withMessage(String messageResource) {
		return CommonSmockServer.withMessage(messageResource);
	}
	/**
	 * Create a request with the given {@link Resource} as content. If the content is payload it will be wrapped into a SOAP, 
	 * if the content contains the whole message, it will be used as it is. Supports templates that will be resolved by {@link TemplateProcessor}.
	 *
	 * @param payload the request payload
	 * @return the request creator
	 */
	public  ParametrizableRequestCreator withMessage(Resource messageResource) {
		return CommonSmockServer.withMessage(messageResource);
	}
    /**
     * Create a request with the given {@link Source} XML as content. If the content is payload it will be wrapped into a SOAP, 
	 * if the content contains the whole message, it will be used as it is. Supports templates that will be resolved by {@link TemplateProcessor}.
     *
     * @param message 
     * @return the request creator
     */
	public  ParametrizableRequestCreator withMessage(Source message) {
		return CommonSmockServer.withMessage(message);
	}
    /**
     * Create a request with the given {@link Document} XML as content. If the content is payload it will be wrapped into a SOAP, 
	 * if the content contains the whole message, it will be used as it is. Supports templates that will be resolved by {@link TemplateProcessor}.
     *
     * @param message 
     * @return the request creator
     */
	public  ParametrizableRequestCreator withMessage(Document message) {
		return CommonSmockServer.withMessage(message);
	}
    /**
     * Expects the given message. If the message contains only payload, only the payloads will be compared. If it contains whole message, 
     * whole messages will be compared. Control message can be preprocessed by {@link TemplateProcessor}. 
     *
     * @param message 
     * @return the request creator
     */
	public  ParametrizableResponseMatcher message(String messageResource)
	{
		return CommonSmockServer.message(messageResource);
	}
    /**
     * Expects the given message. If the message contains only payload, only the payloads will be compared. If it contains whole message, 
     * whole messages will be compared. Control message can be preprocessed by {@link TemplateProcessor}. 
     *
     * @param message 
     * @return the request creator
     */
	public  ParametrizableResponseMatcher message(Resource messageResource)
	{
		return CommonSmockServer.message(messageResource);
	}
	/**
	 * Expects the given message. If the message contains only payload, only the payloads will be compared. If it contains whole message, 
     * whole messages will be compared. Control message can be preprocessed by {@link TemplateProcessor}. 
	 * @param content
	 * @return
	 */
	public  ParametrizableResponseMatcher message(Source content) {
		return CommonSmockServer.message(content);
	}
	/**
     * Expects the given message. If the message contains only payload, only the payloads will be compared. If it contains whole message, 
     * whole messages will be compared. Control message can be preprocessed by {@link TemplateProcessor}. 
	 * @param message
	 * @return
	 */
	public  ParametrizableResponseMatcher message(Document message) {
		return CommonSmockServer.message(message);
	}


}
