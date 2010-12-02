package net.javacrumbs.calc.server;

import net.javacrumbs.calc.model.PlusRequest;
import net.javacrumbs.calc.model.PlusResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

@Endpoint
public class CalcEndpoint {

	@PayloadRoot(localPart = "plusRequest",  namespace = "http://javacrumbs.net/calc")
	public PlusResponse plus(PlusRequest plus)
	{
		if ((long)plus.getA()+(long)plus.getB()>Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException("Values are too big.");
		}
		PlusResponse result = new PlusResponse();
		result.setResult(plus.getA() + plus.getB());
		return result;
	}
}
