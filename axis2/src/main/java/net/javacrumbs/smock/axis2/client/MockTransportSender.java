package net.javacrumbs.smock.axis2.client;

import java.io.IOException;

import net.javacrumbs.smock.extended.client.connection.MockConnection;
import net.javacrumbs.smock.extended.client.connection.threadlocal.ThreadLocalMockWebServiceServer;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.transport.TransportSender;
import org.apache.axis2.transport.TransportUtils;

public class MockTransportSender extends AbstractHandler implements TransportSender {


	public InvocationResponse invoke(MessageContext msgContext)	throws AxisFault {
		MockConnection activeConnection = ThreadLocalMockWebServiceServer.getActiveConnection();
		TransportUtils.writeMessage(msgContext, activeConnection.getOutputStream());
		try {
			msgContext.setProperty(MessageContext.TRANSPORT_IN, activeConnection.getInputStream());
		} catch (IOException e) {
			throw new AxisFault("Error when processing request", e);
		}
		TransportUtils.setResponseWritten(msgContext, true);
        return InvocationResponse.CONTINUE;
	}

	public void cleanup(MessageContext msgContext) throws AxisFault {
	}

	public void init(ConfigurationContext confContext, TransportOutDescription transportOut) throws AxisFault {
	}

	public void stop() {
	}

	
}
