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
public class UserRole extends Role {
    private int id;

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
                    alg.commandBetweenessById(id);
                    break;
                case CLOSENESS:
                    alg.commandClosenessById(id);
                    break;
                case PAGE_RANK:
                    alg.commandPageRankById(id);
                    break;
                case EIGENVECTOR:
                    alg.commandEigenvectorById(id);
                    break;
                case MEMBERS:
                    alg.listGroups(id);
                case FRIENDS:
                    alg.listFriendsOf(id);
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
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
