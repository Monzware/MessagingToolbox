package com.monzware.messaging.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;

public class EndpointMessageDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {

		if (element instanceof EndpointMessageEditorInput) {

			EndpointMessageEditorInput input = (EndpointMessageEditorInput) element;
			EndpointReceiverMessage message = input.getMessage();
			
			if(message != null) {
				return new Document(message.getMessageContent());
			} else {
				return new Document();
			}
			
		} else {
			return super.createDocument(element);
		}
	}	
}