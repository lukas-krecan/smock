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

import javax.xml.transform.Source;

import net.javacrumbs.smock.common.MessageHelper;

public class ServerAssert {
	private static MessageHelper messageHelper = new MessageHelper();

	public static  <T> T deserialize(Source messageSource, Class<T> targetClass)
	{
		try {
			return messageHelper.deserialize(messageSource, targetClass);
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not deserialize message.",e);
		}
	}
	
	public static MessageHelper getMessageHelper() {
		return messageHelper;
	}

	public static void setMessageHelper(MessageHelper messageHelper) {
		ServerAssert.messageHelper = messageHelper;
	}
	
	
	
}
