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

import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.RequestXPathExpectations;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.client.ResponseCreator;
import org.springframework.ws.test.client.ResponseCreators;

/**
 * Superclass that can be extended by Spring WS client test. 
 * @author Lukas Krecan
 *
 */
public abstract class AbstractWebServiceClientTest implements ApplicationContextAware {

	protected  ApplicationContext applicationContext;
	protected  MockWebServiceServer mockWebServiceServer; 
	
	  /**
     * Records an expectation specified by the given {@link RequestMatcher}. Returns a {@link ResponseActions} object
     * that allows for creating the response, or to set up more expectations.
     *
     * @param requestMatcher the request matcher expected
     * @return the response actions
     */
    public ResponseActions expect(RequestMatcher requestMatcher) {
         return mockWebServiceServer.expect(requestMatcher);
    }

    /**
     * Verifies that all expectations were met.
     *
     * @throws AssertionError in case of unmet expectations
     */
    public void verify() {
    	mockWebServiceServer.verify();
    }
	
    /**
     * Respond with the given {@link javax.xml.transform.Source} XML as payload response.
     *
     * @param payload the response payload
     * @return the response callback
     */
    public  ResponseCreator withPayload(Source payload) {
        return ResponseCreators.withPayload(payload);
    }

    /**
     * Respond with the given {@link org.springframework.core.io.Resource} XML as payload response.
     *
     * @param payload the response payload
     * @return the response callback
     */
    public  ResponseCreator withPayload(Resource payload) throws IOException {
    	return ResponseCreators.withPayload(payload);
    }

    /**
     * Respond with an error.
     *
     * @param errorMessage the error message
     * @return the response callback
     * @see org.springframework.ws.transport.WebServiceConnection#hasError()
     * @see org.springframework.ws.transport.WebServiceConnection#getErrorMessage()
     */
    public  ResponseCreator withError(String errorMessage) {
    	return ResponseCreators.withError(errorMessage);
    }

    /**
     * Respond with an {@link java.io.IOException}.
     *
     * @param ioException the exception to be thrown
     * @return the response callback
     */
    public  ResponseCreator withException(IOException ioException) {
        return ResponseCreators.withException(ioException);
    }

    /**
     * Respond with an {@link RuntimeException}.
     *
     * @param ex the runtime exception to be thrown
     * @return the response callback
     */
    public  ResponseCreator withException(RuntimeException ex) {
        return ResponseCreators.withException(ex);
    }

    /**
     * Respond with a {@code MustUnderstand} fault.
     *
     * @param faultStringOrReason the SOAP 1.1 fault string or SOAP 1.2 reason text
     * @param locale              the language of faultStringOrReason. Optional for SOAP 1.1
     * @see SoapBody#addMustUnderstandFault(String, java.util.Locale)
     */
    public  ResponseCreator withMustUnderstandFault(final String faultStringOrReason, final Locale locale) {
        return ResponseCreators.withMustUnderstandFault(faultStringOrReason, locale);
    }

    /**
     * Respond with a {@code Client} (SOAP 1.1) or {@code Sender} (SOAP 1.2) fault.
     *
     * @param faultStringOrReason the SOAP 1.1 fault string or SOAP 1.2 reason text
     * @param locale              the language of faultStringOrReason. Optional for SOAP 1.1
     * @see org.springframework.ws.soap.SoapBody#addClientOrSenderFault(String, Locale)
     */
    public  ResponseCreator withClientOrSenderFault(final String faultStringOrReason, final Locale locale) {
        return ResponseCreators.withClientOrSenderFault(faultStringOrReason, locale);
    }

    /**
     * Respond with a {@code Server} (SOAP 1.1) or {@code Receiver} (SOAP 1.2) fault.
     *
     * @param faultStringOrReason the SOAP 1.1 fault string or SOAP 1.2 reason text
     * @param locale              the language of faultStringOrReason. Optional for SOAP 1.1
     * @see org.springframework.ws.soap.SoapBody#addServerOrReceiverFault(String, Locale)
     */
    public  ResponseCreator withServerOrReceiverFault(final String faultStringOrReason, final Locale locale) {
    	return ResponseCreators.withServerOrReceiverFault(faultStringOrReason, locale);
    }

    /**
     * Respond with a {@code VersionMismatch} fault.
     *
     * @param faultStringOrReason the SOAP 1.1 fault string or SOAP 1.2 reason text
     * @param locale              the language of faultStringOrReason. Optional for SOAP 1.1
     * @see org.springframework.ws.soap.SoapBody#addVersionMismatchFault(String, Locale)
     */
    public  ResponseCreator withVersionMismatchFault(final String faultStringOrReason, final Locale locale) {
    	return ResponseCreators.withVersionMismatchFault(faultStringOrReason, locale);
    }
    
    /**
    * Expects any request.
    *
    * @return the request matcher
    */
   public  RequestMatcher anything() {
       return RequestMatchers.anything();
   }

   /**
    * Expects the given {@link javax.xml.transform.Source} XML payload.
    *
    * @param payload the XML payload
    * @return the request matcher
    */
   public  RequestMatcher payload(Source payload) {
	   return RequestMatchers.payload(payload);
   }

   /**
    * Expects the given {@link org.springframework.core.io.Resource} XML payload.
    *
    * @param payload the XML payload
    * @return the request matcher
    */
   public  RequestMatcher payload(Resource payload) throws IOException {
	   return RequestMatchers.payload(payload);
   }

   /**
    * Expects the payload to validate against the given XSD schema(s).
    *
    * @param schema         the schema
    * @param furtherSchemas further schemas, if necessary
    * @return the request matcher
    */
   public  RequestMatcher validPayload(Resource schema, Resource... furtherSchemas) throws IOException {
	   return RequestMatchers.validPayload(schema, furtherSchemas);
   }

   /**
    * Expects the given XPath expression to (not) exist or be evaluated to a value.
    *
    * @param xpathExpression the XPath expression
    * @return the XPath expectations, to be further configured
    */
   public  RequestXPathExpectations xpath(String xpathExpression) {
	   return RequestMatchers.xpath(xpathExpression);
   }

   /**
    * Expects the given XPath expression to (not) exist or be evaluated to a value.
    *
    * @param xpathExpression  the XPath expression
    * @param namespaceMapping the namespaces
    * @return the XPath expectations, to be further configured
    */
   public  RequestXPathExpectations xpath(String xpathExpression, Map<String, String> namespaceMapping) {
	   return RequestMatchers.xpath(xpathExpression, namespaceMapping);
   }

   /**
    * Expects the given SOAP header in the outgoing message.
    *
    * @param soapHeaderName the qualified name of the SOAP header to expect
    * @return the request matcher
    */
   public  RequestMatcher soapHeader(QName soapHeaderName) {
	   return RequestMatchers.soapHeader(soapHeaderName);
   }

   /**
    * Expects a connection to the given URI.
    *
    * @param uri the String uri expected to connect to
    * @return the request matcher
    */
   public  RequestMatcher connectionTo(String uri) {
	   return RequestMatchers.connectionTo(uri);
   }

   /**
    * Expects a connection to the given URI.
    *
    * @param uri the String uri expected to connect to
    * @return the request matcher
    */
   public  RequestMatcher connectionTo(URI uri) {
	   return RequestMatchers.connectionTo(uri);
   }
   

	public MockWebServiceServer createServer(ApplicationContext applicationContext) {
		return MockWebServiceServer.createServer(applicationContext);
	}

	public final ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public final void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.mockWebServiceServer = createServer(applicationContext);
	}


	public MockWebServiceServer getMockWebServiceServer() {
		return mockWebServiceServer;
	}

	public void setMockWebServiceServer(MockWebServiceServer mocWebServiceServer) {
		this.mockWebServiceServer = mocWebServiceServer;
	}

}
