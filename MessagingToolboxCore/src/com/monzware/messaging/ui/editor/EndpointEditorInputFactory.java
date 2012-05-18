package com.monzware.messaging.ui.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

/**
 * Factory for saving and restoring a <code>FileEditorInput</code>. The stored
 * representation of a <code>FileEditorInput</code> remembers the full path of
 * the file (that is, <code>IFile.getFullPath</code>).
 * <p>
 * The workbench will automatically create instances of this class as required.
 * It is not intended to be instantiated or subclassed by the client.
 * </p>
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public class EndpointEditorInputFactory implements IElementFactory {
	/**
	 * Factory id. The workbench plug-in registers a factory by this name with
	 * the "org.eclipse.ui.elementFactories" extension point.
	 */
	private static final String ID_FACTORY = "com.monzware.messaging.ui.MessagingToolboxEndpointEditorFactory"; //$NON-NLS-1$

	/**
	 * Tag for the IFile.fullPath of the file resource.
	 */
	private static final String TAG_PATH = "path"; //$NON-NLS-1$

	/**
	 * Creates a new factory.
	 */
	public EndpointEditorInputFactory() {
	}

	/*
	 * (non-Javadoc) Method declared on IElementFactory.
	 */
	public IAdaptable createElement(IMemento memento) {
		// Get the file name.
		//String fileName = memento.getString(TAG_PATH);
		//if (fileName == null) {
			return null;
		//}

		// Get a handle to the IFile...which can be a handle
		// to a resource that does not exist in workspace
		//IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileName));
		//if (file != null) {
		//	return new FileEditorInput(file);
		
		//return null;
	}

	/**
	 * Returns the element factory id for this class.
	 * 
	 * @return the element factory id
	 */
	public static String getFactoryId() {
		return ID_FACTORY;
	}

	/**
	 * Saves the state of the given file editor input into the given memento.
	 * 
	 * @param memento
	 *            the storage area for element state
	 * @param input
	 *            the file editor input
	 */
	public static void saveState(IMemento memento, EndpointEditorInput input) {
		
		//memento.putString(TAG_PATH, file.getFullPath().toString());
	}
}
