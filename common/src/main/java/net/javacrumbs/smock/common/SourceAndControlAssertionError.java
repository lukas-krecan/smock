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

package net.javacrumbs.smock.common;

import static org.springframework.util.Assert.notNull;

import javax.xml.transform.Source;

/**
 * Subclass of {@link AssertionError} that also contains a message and control
 * message {@link Source} for more context.
 * 
 * @author Lukas Krecan
 * @author Arjen Poutsma
 * 
 */
public class SourceAndControlAssertionError extends AssertionError {
	private static final long serialVersionUID = 6499060400685231092L;

	private static final String NEW_LINE = System.getProperty("line.separator");

	private final String controlLabel;

	private final Source controlSource;
	
	private final String messageLabel;
	
	private final Source messageSource;
	
	/**
	 * Creates assertion error.
	 * @param message
	 * @param messageLabel
	 * @param messageSource
	 * @param controlLabel
	 * @param controlSource
	 */
	public SourceAndControlAssertionError(String message, String messageLabel, Source messageSource, String controlLabel, Source controlSource) {
		super(message);
		notNull(messageLabel, "messageLabel has to be set");
		notNull(messageSource, "messageSource has to be set");
		notNull(controlLabel, "controlLabel has to be set");
		notNull(controlSource, "controlSource has to be set");
		this.controlLabel = controlLabel;
		this.controlSource = controlSource;
		this.messageLabel = messageLabel;
		this.messageSource = messageSource;
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.getMessage());
		appendMessage(builder, messageSource, messageLabel);
		appendMessage(builder, controlSource, controlLabel);
		return builder.toString();
	}

	private void appendMessage(StringBuilder builder, Source source, String label) {
		String string = getMessageString(source);
		if (string != null) {
			builder.append(NEW_LINE);
			builder.append(label);
			builder.append(": ");
			builder.append(string);
		}
	}

	private String getMessageString(Source source) {
		if (source != null) {
			return XmlUtil.serialize(source);
		}
		return null;
	}


	public String getControlLabel() {
		return controlLabel;
	}

	public Source getControlSource() {
		return controlSource;
	}

}
