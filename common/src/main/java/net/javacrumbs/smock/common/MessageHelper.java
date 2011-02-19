package net.javacrumbs.smock.common;

import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


public class MessageHelper {
	
	private DefaultMethodEndpointAdapter adapter;
	
	private final WebServiceMessageFactory messageFactory;
	
	public MessageHelper(WebServiceMessageFactory messageFactory) {
		 Assert.notNull(messageFactory, "Message factory can not be null");
		 adapter = new DefaultMethodEndpointAdapter();
		 this.messageFactory = messageFactory;
		 try {
			adapter.afterPropertiesSet();
		} catch (Exception e) {
			throw new IllegalStateException("Initialization failed.",e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(WebServiceMessage message, Class<T> targetClass) throws Exception {
		Assert.notNull(message, "message can not be null");
		Assert.notNull(targetClass, "targetClass can not be null");
		MethodParameter parameter = new SmockMethodParameter(targetClass,0); 
		MessageContext messageContext = new DefaultMessageContext(message, messageFactory);
		for (MethodArgumentResolver resolver: adapter.getMethodArgumentResolvers())
		{
			if (resolver.supportsParameter(parameter)){
				Object result = resolver.resolveArgument(messageContext, parameter);
				if (result != null)
				{
					return (T) result;
				}
			}
			
		}
		return null;
	}
	
	public WebServiceMessage serialize(Object data) throws Exception {
		Assert.notNull(data, "data can not be null");
		MethodParameter parameter = new SmockMethodParameter(getTargetClass(data),-1);
		MessageContext messageContext = new DefaultMessageContext(messageFactory);
		for (MethodReturnValueHandler methodReturnValueHandler : adapter.getMethodReturnValueHandlers()) {
            if (methodReturnValueHandler.supportsReturnType(parameter)) {
                methodReturnValueHandler.handleReturnValue(messageContext, parameter, data);
                return messageContext.getResponse();
            }
        }
		return null;
	}

	/**
	 * Find class to be mapped from.
	 * @param data
	 * @return
	 */
	protected Class<? extends Object> getTargetClass(Object data) {
		if (org.w3c.dom.Element.class.isAssignableFrom(data.getClass()))
		{
			return org.w3c.dom.Element.class;
		}
		return data.getClass();
	}

	
	private static class SmockMethodParameter extends MethodParameter
	{
		private final Class<?> parameterType;
		public SmockMethodParameter(Class<?> parameterType, int parameterIndex)
		{
			super(getDummyMethod(), parameterIndex);
			this.parameterType = parameterType;
		}
		
		private static Method getDummyMethod()
		{
			try {
				return SmockMethodParameter.class.getMethod("dummyMethod", String.class);
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException("Should not happen.",e);
			}
		}
		
		@Override
		public Class<?> getParameterType() {
			return parameterType;
		}
		
		@SuppressWarnings("unused")
		@ResponsePayload
		public String dummyMethod(@RequestPayload String param){
			return null;
		}
	}



}
