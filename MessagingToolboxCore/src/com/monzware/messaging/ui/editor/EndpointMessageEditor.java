package com.monzware.messaging.ui.editor;

import org.eclipse.ui.editors.text.TextEditor;

public class EndpointMessageEditor extends TextEditor {

	public EndpointMessageEditor() {
		super();

		// setSourceViewerConfiguration(new EndpointMessageDocumentProvider());
		setDocumentProvider(new EndpointMessageDocumentProvider());
	}
}