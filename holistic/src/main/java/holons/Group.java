/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package holons;

import org.janusproject.kernel.crio.core.CRIOContext;
import org.janusproject.kernel.crio.core.Organization;

/**
 *
 * @author marcin
 */
public class Group extends Organization{
    public Group(CRIOContext context) {
        super(context);
        addRole(UserRole.class);
    }
}
