/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package holons;

import edu.agh.organization.DateProviderOrganization;
import edu.agh.organization.RequesterRole;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.crio.core.GroupAddress;
import org.janusproject.kernel.status.Status;

/**
 *
 * @author marcin
 */
public class GroupHolon extends Agent{
    
    
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

    void commandBetweenessById(int argumentValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandClosenessById(int argumentValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandEigenvectorById(int argumentValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandPageRankById(int argumentValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void plotGraph() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listUsers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listFriendsOf(int argumentValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listGroups(int argumentValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listAllGroups() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listMembers(String nick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void plotGraphByGroup(String nick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void betweenessByGroup(String nick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void closenessByGroup(String nick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void eigenvectorByGroup(String nick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void pageRankByGroup(String nick) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void densityByAllGroup() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void cohesionByAllGroup() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandBetweenessForAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandClosenessForAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandEigenvectorForAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void commandPageRankForAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
