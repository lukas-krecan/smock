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

import java.io.IOException;

import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.test.server.ResponseMatcher;

/**
 * Response actions implementation.
 * @author Lukas Krecan
 *
 */
public class MockWebServiceClientResponseActions implements ResponseActions {

    private final MessageContext messageContext;

    public MockWebServiceClientResponseActions(MessageContext messageContext) {
        Assert.notNull(messageContext, "'messageContext' must not be null");
        this.messageContext = messageContext;
    }

    public ResponseActions andExpect(ResponseMatcher responseMatcher) {
        WebServiceMessage request = messageContext.getRequest();
        WebServiceMessage response = messageContext.getResponse();
        if (response == null) {
            fail("No response received");
            return null;
        }
        try {
            responseMatcher.match(request, response);
            return this;
        }
        catch (IOException ex) {
            fail(ex.getMessage());
            return null;
        }
    }
}