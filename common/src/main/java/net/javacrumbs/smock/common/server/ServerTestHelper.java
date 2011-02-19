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

package net.javacrumbs.smock.common.server;

import static org.springframework.ws.test.support.AssertionErrors.fail;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.MessageCreator;
import net.javacrumbs.smock.common.MessageHelper;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.test.server.ResponseMatcher;

/**
 * Helper class that simplifies WS server unit tests.  
 * @author Lukas Krecan
 *
 */
public class ServerTestHelper extends CommonSmockServer{
	
	private static WebServiceMessageFactory messageFactory = createMessageFactory();

	private static MessageHelper messageHelper = new MessageHelper(messageFactory);

	/**
	 * Deserializes message loaded from messageLocation to the targetClass.
	 * @param <T>
	 * @param messageSourceLocation
	 * @param targetClass
	 * @return
	 */
	public static  <T> T createRequest(String messageLocation, Class<T> targetClass)
	{
		return createRequest(fromResource(messageLocation), targetClass);
	}
	
	/**
	 * Deserializes message loaded from messageSource to the targetClass.
	 * @param <T>
	 * @param messageSource
	 * @param targetClass
	 * @return
	 */
	public static  <T> T createRequest(Source messageSource, Class<T> targetClass)
	{
		return createRequest(withMessage(messageSource), targetClass);
	}
	
	/**
	 * Deserializes message created by the {@link MessageCreator} to the targetClass.
	 * @param <T>
	 * @param requestCreator
	 * @param targetClass
	 * @return
	 */
	public static  <T> T createRequest(RequestCreator requestCreator, Class<T> targetClass)
	{
		try {
			return messageHelper.deserialize(requestCreator.createRequest(messageFactory), targetClass);
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not deserialize message.",e);
		}
	}
	
	/**
	 * Serilalizes object to {@link WebServiceMessage}.
	 * @param object
	 * @return
	 */
	public static WebServiceMessage serialize(Object object)
	{
		try {
			WebServiceMessage message = messageHelper.serialize(object);
			if (message==null) fail("Can not serialize object "+object);
			return message;
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not serialize object.",e);
		}
	}
	
	/**
	 * Creates validator based on the response.
	 * @param response
	 * @return
	 */
	public static ResponseActions validate(Object response)
	{
		DefaultMessageContext messageContext = new DefaultMessageContext(messageFactory);
		messageContext.setResponse(serialize(response));
		return validate(messageContext);
	}
	/**
	 * Creates validator based on the response and request. Use this method if your {@link ResponseMatcher}s need to read data from request.
	 * @param response
	 * @param request
	 * @return
	 */
	public static ResponseActions validate(Object response, Object request)
	{
		DefaultMessageContext messageContext = new DefaultMessageContext(serialize(request), messageFactory);
		messageContext.setResponse(serialize(response));
		return validate(messageContext);
	}

	/**
	 * Creates validator based on the {@link MessageContext}.
	 * @param messageContext
	 * @return
	 */
	public static ResponseActions validate(MessageContext messageContext)
	{
		return new MockWebServiceClientResponseActions(messageContext);
	}
	
	public static MessageHelper getMessageHelper() {
		return messageHelper;
	}

	public static void setMessageHelper(MessageHelper messageHelper) {
		ServerTestHelper.messageHelper = messageHelper;
	}
	
	
}
