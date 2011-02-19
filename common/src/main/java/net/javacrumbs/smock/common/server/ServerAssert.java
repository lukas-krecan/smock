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

import net.javacrumbs.smock.common.MessageHelper;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseActions;

public class ServerAssert extends CommonSmockServer{
	
	private static WebServiceMessageFactory messageFactory = createMessageFactory();

	private static MessageHelper messageHelper = new MessageHelper(messageFactory);

	public static  <T> T createRequest(String messageSourceLocation, Class<T> targetClass)
	{
		return createRequest(fromResource(messageSourceLocation), targetClass);
	}
	
	public static  <T> T createRequest(Source messageSource, Class<T> targetClass)
	{
		return createRequest(withMessage(messageSource), targetClass);
	}
	
	public static  <T> T createRequest(RequestCreator requestCreator, Class<T> targetClass)
	{
		try {
			return messageHelper.deserialize(requestCreator.createRequest(messageFactory), targetClass);
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not deserialize message.",e);
		}
	}
	
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
	
	public static MessageHelper getMessageHelper() {
		return messageHelper;
	}

	public static void setMessageHelper(MessageHelper messageHelper) {
		ServerAssert.messageHelper = messageHelper;
	}
	
	public static ResponseActions validate(Object response)
	{
		DefaultMessageContext messageContext = new DefaultMessageContext(messageFactory);
		messageContext.setResponse(serialize(response));
		return validate(messageContext);
	}

	public static ResponseActions validate(Object response, Object request)
	{
		DefaultMessageContext messageContext = new DefaultMessageContext(serialize(request), messageFactory);
		messageContext.setResponse(serialize(response));
		return validate(messageContext);
	}

	public static ResponseActions validate(MessageContext messageContext)
	{
		return new MockWebServiceClientResponseActions(messageContext);
	}
	
	
}
