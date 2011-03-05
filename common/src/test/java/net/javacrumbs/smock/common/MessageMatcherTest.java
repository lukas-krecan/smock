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
package net.javacrumbs.smock.common;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.xml.transform.Source;

import org.junit.Test;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringSource;


public class MessageMatcherTest extends AbstractSmockTest{

	
	@Test
	public void testSame()
	{
		Source source = new StringSource(MESSAGE);
		MessageMatcher matcher = new MessageMatcher(source);
		
		SoapMessage request = createMock(SoapMessage.class);
		SoapEnvelope envelope = createMock(SoapEnvelope.class);
		expect(request.getEnvelope()).andReturn(envelope );
		expect(envelope.getSource()).andReturn(new StringSource(MESSAGE));
		replay(request, envelope);
		
		matcher.matchInternal(null, request);
		
		verify(request, envelope);
	}
	@Test(expected=AssertionError.class)
	public void testDifferent()
	{
		Source source = new StringSource(MESSAGE);
		MessageMatcher matcher = new MessageMatcher(source);
		
		SoapMessage request = createMock(SoapMessage.class);
		SoapEnvelope envelope = createMock(SoapEnvelope.class);
		expect(request.getEnvelope()).andReturn(envelope );
		expect(envelope.getSource()).andReturn(new StringSource(MESSAGE2));
		replay(request, envelope);
		
		matcher.matchInternal(null, request);
		
		verify(request, envelope);
	}
	@Test
	public void testPayload()
	{
		Source source = new StringSource(PAYLOAD);
		MessageMatcher matcher = new MessageMatcher(source);
		
		SoapMessage request = createMock(SoapMessage.class);
		expect(request.getPayloadSource()).andReturn(new StringSource(PAYLOAD));
		replay(request);
		
		matcher.matchInternal(null, request);
		
		verify(request);
	}
	@Test(expected=AssertionError.class)
	public void testPayloadDifferent()
	{
		Source source = new StringSource(PAYLOAD);
		MessageMatcher matcher = new MessageMatcher(source);
		
		SoapMessage request = createMock(SoapMessage.class);
		expect(request.getPayloadSource()).andReturn(new StringSource(PAYLOAD2));
		replay(request);
		
		matcher.matchInternal(null, request);
		
		verify(request);
	}
}

