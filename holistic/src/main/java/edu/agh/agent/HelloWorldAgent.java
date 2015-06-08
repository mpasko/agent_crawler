/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.agent;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.agent.Holon;
/**
 *
 * @author marcin
 */
public class HelloWorldAgent extends Agent {
  public Status live() {
    Holon h = this;
    //h.
      //TODO
      // http://www.janus-project.org/Agent_Message_Tutorial
    print("hello world!\n");
    return null;
  }
}
