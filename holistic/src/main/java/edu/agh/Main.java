/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh;

import edu.agh.agent.HelloWorldAgent;
import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;
/**
 *
 * @author marcin
 */
public class Main {
    public static void main(String[] args) {
		HelloWorldAgent a = new HelloWorldAgent();
		HelloWorldAgent b = new HelloWorldAgent();
		Kernel k = Kernels.get();
		k.launchLightAgent(a);
		k.launchLightAgent(b);
	}
}
