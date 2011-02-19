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

package net.javacrumbs.smock.common.client;

import static org.springframework.ws.test.support.AssertionErrors.fail;

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.MessageHelper;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.client.ResponseCreator;


public class ClientTestHelper extends CommonSmockClient{
	
	private static WebServiceMessageFactory messageFactory = createMessageFactory();

	private static MessageHelper messageHelper = new MessageHelper(messageFactory);
	
	public static  <T> T response(String messageLocation, Class<T> responseType)
	{
		return response(fromResource(messageLocation), responseType);
	}
	
	public static  <T> T response(Source messageSource, Class<T> responseType)
	{
		return response(withMessage(messageSource), null, responseType);
	}
	
	public static  <T> T response(ResponseCreator requestCreator, WebServiceMessage request, Class<T> responseType)
	{
		try {
			return messageHelper.deserialize(requestCreator.createResponse(null, request, messageFactory), responseType);
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not deserialize message.",e);
		}
	}
	
	/**
	 * Serializes object to {@link WebServiceMessage}.
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


	public static WebServiceMessageFactory getMessageFactory() {
		return messageFactory;
	}


	public static void setMessageFactory(WebServiceMessageFactory messageFactory) {
		ClientTestHelper.messageFactory = messageFactory;
	}


	public static MessageHelper getMessageHelper() {
		return messageHelper;
	}


	public static void setMessageHelper(MessageHelper messageHelper) {
		ClientTestHelper.messageHelper = messageHelper;
	}

}
