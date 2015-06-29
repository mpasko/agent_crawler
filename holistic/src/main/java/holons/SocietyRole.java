package holons;

import edu.agh.organization.*;
import java.util.HashMap;
import java.util.Map;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.crio.core.Role;
import org.janusproject.kernel.message.MessageException;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

/**
 * @author $Author: srodriguez$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SocietyRole extends Role {

	private Map<String, AgentAddress> mapGroups = new HashMap<String, AgentAddress>();
    private Map<Integer, AgentAddress> mapUsers = new HashMap<Integer, AgentAddress>();

	/** {@inheritDoc}
	 */
    @Override
	public Status live() {
		RequestMessage dm = (RequestMessage) getMessage();
        Algorithms alg = new Algorithms();
        AgentAddress addr;
		//First send a request
		try {
			switch(dm.requestType){
                case BETWEENESS:
                case CLOSENESS:
                case PAGE_RANK:
                case EIGENVECTOR:
                case FRIENDS:
                    addr = mapUsers.get(dm.id);
                    sendMessage(UserRole.class, addr, dm);
                    break;
                case COHESION:
                case DENSITY:
                case GRAPH:
                case MEMBERS:
                    addr = mapGroups.get(dm.name);
                    sendMessage(UserRole.class, addr, dm);
                    break;
                default:
            }
		}
		catch(AssertionError ae) {
			ae.printStackTrace();
            return StatusFactory.error(this, ae.getMessage());
		}
		catch (MessageException e) {
			e.printStackTrace();
            return StatusFactory.error(this, e.getMessage());
		}
		
		return StatusFactory.ok(this);
	}

    /**
     * @return the mapGroups
     */
    public Map<String, AgentAddress> getMapGroups() {
        return mapGroups;
    }

    /**
     * @param mapGroups the mapGroups to set
     */
    public void setMapGroups(Map<String, AgentAddress> mapGroups) {
        this.mapGroups = mapGroups;
    }

    /**
     * @return the mapUsers
     */
    public Map<Integer, AgentAddress> getMapUsers() {
        return mapUsers;
    }

    /**
     * @param mapUsers the mapUsers to set
     */
    public void setMapUsers(Map<Integer, AgentAddress> mapUsers) {
        this.mapUsers = mapUsers;
    }

}
