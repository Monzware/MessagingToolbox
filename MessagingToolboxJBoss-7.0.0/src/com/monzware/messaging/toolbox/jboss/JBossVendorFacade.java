package com.monzware.messaging.toolbox.jboss;

import java.util.Collection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.monzware.messaging.toolbox.vendor.VendorFacade;

public class JBossVendorFacade implements VendorFacade {

	@Override
	public InitialContext getInitialContext() {

		Hashtable<String, String> environment = new Hashtable<String, String>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		environment.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		environment.put(Context.PROVIDER_URL, "jnp://localhost:9199");

		try {
			InitialContext initialContext = new InitialContext(environment);

			// Queue queue = (Queue) initialContext.lookup("/queue/DLQ");
			return initialContext;
		} catch (NamingException e) {
			return null;
		}

	}

	@Override
	public Collection<String> getJNDIEndpoints() {
		// TODO Auto-generated method stub
		return null;
	}

}
