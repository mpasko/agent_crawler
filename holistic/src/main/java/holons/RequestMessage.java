/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package holons;

import org.janusproject.kernel.message.AbstractContentMessage;

/**
 *
 * @author marcin
 */
class RequestMessage extends AbstractContentMessage<RequestType> {
    RequestType requestType = RequestType.MEMBERS;
    Integer id;
    String name;

    @Override
    public RequestType getContent() {
        return requestType;
    }
    
}
