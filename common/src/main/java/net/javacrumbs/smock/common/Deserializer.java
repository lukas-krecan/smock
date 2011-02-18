package net.javacrumbs.smock.common;

import static net.javacrumbs.smock.common.SmockCommon.withMessageFactory;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;

import java.lang.reflect.Method;

import javax.xml.transform.Source;

import org.springframework.core.MethodParameter;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

public class Deserializer {
	
	private DefaultMethodEndpointAdapter adapter;
	
	private WebServiceMessageFactory messageFactory = withMessageFactory();
	
	public Deserializer() {
		 adapter = new DefaultMethodEndpointAdapter();
		 try {
			adapter.afterPropertiesSet();
		} catch (Exception e) {
			throw new IllegalStateException("Initialization failed.",e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(Source messageSource, Class<T> targetClass) throws Exception {
		WebServiceMessage message = withMessage(messageSource).createRequest(messageFactory);
		MethodParameter parameter = new SmockMethodParameter(targetClass); 
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
	
	private static class SmockMethodParameter extends MethodParameter
	{
		private final Class<?> parameterType;
		public SmockMethodParameter(Class<?> parameterType)
		{
			super(getDummyMethod(), 0);
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
		public void dummyMethod(@RequestPayload String param){
			
		}
	}
}
