package net.javacrumbs.calc.server;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(serviceName="CalculatorService", targetNamespace="http://javacrumbs.net/calc")
public class CalcEndpoint {

	private static final String NS = "http://javacrumbs.net/calc";

	@WebMethod
	@WebResult(name="result",targetNamespace=NS)
	public long plus(@WebParam(name="a",targetNamespace=NS) long a, @WebParam(name="b",targetNamespace=NS) long b)
	{
		if (a+b>Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException("Values are too big.");
		}
		return a+b;
	}
}
