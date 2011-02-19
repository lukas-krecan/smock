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

package net.javacrumbs.smock.easymock.client;

import static org.easymock.EasyMock.reportMatcher;
import net.javacrumbs.smock.common.client.ClientTestHelper;

import org.springframework.ws.test.client.RequestMatcher;

public class SmockEasyMockClient extends ClientTestHelper{
	

	/**
	 * Matches a RequestMatcher.
	 * @param <T>
	 * @param matcher
	 * @return
	 */
	public static <T> T is(final RequestMatcher matcher)
	{
		reportMatcher(new SmockArgumentMatcher(matcher));
		return null;
	}
}
