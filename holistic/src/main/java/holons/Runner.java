/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package holons;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;

/**
 *
 * @author marcin
 */
public class Runner {
    
    public static void start(){
        SocietyHolon head = new SocietyHolon();
        Kernel kernel = Kernels.get();
        kernel.launchLightAgent(head);
    }
}
