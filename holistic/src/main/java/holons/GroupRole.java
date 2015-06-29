package holons;

import edu.agh.organization.*;
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
public class GroupRole extends Role {
    private String nick;
    
	/** {@inheritDoc}
	 */
    @Override
	public Status live() {
		RequestMessage dm = (RequestMessage) getMessage();
        Algorithms alg = new Algorithms();
		//First send a request
		try {
			switch(dm.requestType){
                case BETWEENESS:
                    alg.betweenessByGroup(getNick());
                    break;
                case CLOSENESS:
                    alg.closenessByGroup(getNick());
                    break;
                case PAGE_RANK:
                    alg.pageRankByGroup(getNick());
                    break;
                case EIGENVECTOR:
                    alg.eigenvectorByGroup(getNick());
                    break;
                case COHESION:
                    alg.cohesionByGroup(getNick());
                    break;
                case DENSITY:
                    alg.densityByGroup(getNick());
                    break;
                case GRAPH:
                    alg.plotGraph();
                    break;
                case MEMBERS:
                    alg.listMembers(getNick());
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
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick the nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

}
