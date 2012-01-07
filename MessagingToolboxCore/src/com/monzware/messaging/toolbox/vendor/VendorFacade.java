package com.monzware.messaging.toolbox.vendor;

import java.util.Collection;

import javax.naming.InitialContext;

public interface VendorFacade {

	public InitialContext getInitialContext();

	public Collection<String> getJNDIEndpoints();

}
