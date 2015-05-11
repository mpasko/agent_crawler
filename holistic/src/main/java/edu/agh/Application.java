/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2010-2011 Janus Core Developers
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.agh;

import edu.agh.agent.ProviderAgent;
import edu.agh.agent.RequestingAgent;

import org.janusproject.kernel.agent.KernelAgentFactory;
import org.janusproject.kernel.mmf.KernelAuthority;
import org.janusproject.kernel.mmf.KernelService;
import org.janusproject.kernel.mmf.JanusApplication;
//import org.janusproject.kernel.network.agent.NetworkingKernelAgentFactory;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;
import org.osgi.framework.BundleContext;

/**
 * A basic application implementation.
 * 
 * @author Sebastian Rodriguez &lt;sebastian@sebastianrodriguez.com.ar&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.janus-project.kernel
 * @mavenartifactid osgi-basic-archetype
 * 
 */
public class Application implements JanusApplication {

	private BundleContext context = null;
	private ProviderAgent providerAgent = null;
	private RequestingAgent requestingAgent = null;
	
	public Application(BundleContext context) {
		this.context = context;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.janusproject.kernel.mmf.JanusModule#getName()
	 */
	@Override
	public String getName() {
		return "Date Requesting Application";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.janusproject.kernel.mmf.JanusModule#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Starts a Requester agent that asks for the current time 5 times to a Provider using the DateProviderOrganization";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.janusproject.kernel.mmf.JanusModule#start(org.janusproject.kernel
	 * .mmf.IKernelService)
	 */
	@Override
	public Status start(KernelService kernel) {
		providerAgent = new ProviderAgent();
		requestingAgent = new RequestingAgent();
		kernel.launchHeavyAgent(providerAgent);
		kernel.launchHeavyAgent(requestingAgent);
		return StatusFactory.ok(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.janusproject.kernel.mmf.JanusApplication#getKernelAgentFactory()
	 */
	@Override
	public KernelAgentFactory getKernelAgentFactory() {
		//Use this if you want to enable networking.
		//return new NetworkingKernelAgentFactory(context);
		
		//Use this if you want a stand-alone kernel.
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.janusproject.kernel.mmf.JanusApplication#getKernelAuthority()
	 */
	@Override
	public KernelAuthority getKernelAuthority() {
		// All operations are approved.
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.janusproject.kernel.mmf.JanusApplication#isAutoStartJanusModules()
	 */
	@Override
	public boolean isAutoStartJanusModules() {
		// All modules registered will be started automatically.
		return false;
	}

	/* (non-Javadoc)
	 * @see org.janusproject.kernel.mmf.JanusApplication#isStopOsgiFramework()
	 */
	@Override
	public boolean isStopOsgiFramework() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.janusproject.kernel.mmf.JanusApplication#isKeepKernelAlive()
	 */
	@Override
	public boolean isKeepKernelAlive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.janusproject.kernel.mmf.JanusModule#isRunning()
	 */
	@Override
	public boolean isRunning() {
		if(providerAgent != null && providerAgent.isAlive() ){
			return true;
		}
		
		if( requestingAgent != null && requestingAgent.isAlive()){
			return true;
		}
		providerAgent = null;
		requestingAgent = null;
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.janusproject.kernel.mmf.JanusModule#stop(org.janusproject.kernel.mmf.IKernelService)
	 */
	@Override
	public Status stop(KernelService kernel) {
		return StatusFactory.ok(this);
	}

}
