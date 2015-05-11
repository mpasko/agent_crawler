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
package edu.agh.agent;

import edu.agh.organization.DateProviderOrganization;
import edu.agh.organization.RequesterRole;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.crio.core.GroupAddress;
import org.janusproject.kernel.status.Status;

/**
 * An agent that request date information.
 * 
 * @author $Author: srodriguez$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RequestingAgent extends Agent {

	/** {@inheritDoc}
	 */
	@Override
	public Status activate(Object... parameters) {
		Status status = super.activate(parameters);
		GroupAddress ga = getOrCreateGroup(DateProviderOrganization.class);
		requestRole(RequesterRole.class, ga);
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status live() {
		// In the default implementation of the live method, janus uses an
		// activator to schedule the various agent's
		// But you may also override this method to implement other kinds of
		// role scheduling or simply implements agent with adopting an
		// organizational perspective (an agent without role)
		Status status = super.live();
		// do something
		return status;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Status end() {
		print("RequestingAgent end.");
		return super.end();
	}
}
