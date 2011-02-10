package net.javacrumbs.smock.axis2.client;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.transport.TransportSender;

public class MockWebServiceServer extends AbstractHandler implements TransportSender {


	public InvocationResponse invoke(MessageContext msgContext)
			throws AxisFault {
		return null;
	}

	public void cleanup(MessageContext msgContext) throws AxisFault {
	}

	public void init(ConfigurationContext confContext, TransportOutDescription transportOut) throws AxisFault {
	}

	public void stop() {
	}

	
}
