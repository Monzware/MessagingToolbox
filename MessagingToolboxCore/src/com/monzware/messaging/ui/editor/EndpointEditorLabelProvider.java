package com.monzware.messaging.ui.editor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;

public class EndpointEditorLabelProvider implements ITableLabelProvider {

	private SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	public EndpointEditorLabelProvider() {

	}

	public void addListener(ILabelProviderListener listener) {

	}

	public void dispose() {

	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof EndpointReceiverMessage) {

			EndpointReceiverMessage message = (EndpointReceiverMessage) element;

			switch (columnIndex) {
			case 0:
				return message.getMessageId();

			case 1:

				Calendar sendTime = message.getSendTime();
				if (sendTime != null) {
					return f.format(message.getSendTime().getTime());
				}
				return "-";

			case 2:
				String user = message.getUser();
				return user != null ? user : "";

			case 3:
				return message.getMessageContent();

			default:
				break;
			}

		}

		return "";
	}

}
