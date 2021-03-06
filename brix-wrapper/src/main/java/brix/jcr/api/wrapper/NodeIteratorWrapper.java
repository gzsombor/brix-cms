/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package brix.jcr.api.wrapper;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RangeIterator;

import brix.jcr.api.JcrNode;
import brix.jcr.api.JcrNodeIterator;
import brix.jcr.api.JcrSession;

/**
 * 
 * @author Matej Knopp
 */
class NodeIteratorWrapper extends RangeIteratorWrapper implements JcrNodeIterator
{

    protected NodeIteratorWrapper(RangeIterator delegate, JcrSession session)
    {
        super(delegate, session);
    }

    public static JcrNodeIterator wrap(NodeIterator delegate, JcrSession session)
    {
        if (delegate == null)
        {
            return null;
        }
        else
        {
            return new NodeIteratorWrapper(delegate, session);
        }
    }

    @Override
    public NodeIterator getDelegate()
    {
        return (NodeIterator)super.getDelegate();
    }

    @Override
    public Object next()
    {
        return JcrNode.Wrapper.wrap((Node)getDelegate().next(), getJcrSession());
    }

    public JcrNode nextNode()
    {
        return JcrNode.Wrapper.wrap(getDelegate().nextNode(), getJcrSession());
    }

}
