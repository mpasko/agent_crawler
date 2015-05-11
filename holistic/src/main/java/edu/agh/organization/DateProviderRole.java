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
package edu.agh.organization;

import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.crio.core.Role;
import org.janusproject.kernel.message.MessageException;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

/**
 * A simple role that waits for requests and returns the current date.
 * In this example we include the use a StringMessage with the content "done"
 * to stop the provider so the application ends.
 * 
 * In a normal use of this organization, the {@link DateProviderRole} would never 
 * stop.
 * 
 * @author $Author: srodriguez$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DateProviderRole extends Role {

	/** {@inheritDoc}
	 */
	public Status live() {
		//Get the next available message.
		StringMessage m = (StringMessage) getMessage();
		//Since the agent might have not received any new messages
		//we have to check that the message is not null.
		if(m != null){
			//if the requester role is done.
			if(m.getContent().equals("done")){
				leaveMe();
			}
			try {
                //We replay the request with the current time.
                AgentAddress sender = (AgentAddress)m.getSender();
				sendMessage(RequesterRole.class, sender, new DateMessage());
			}
			catch(AssertionError ae) {
				throw ae;
			}
			catch (MessageException e) {
				e.printStackTrace();
			}
		}

		return StatusFactory.ok(this);
	}

}
