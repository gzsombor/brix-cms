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

package brix.jcr.api;

import javax.jcr.NodeIterator;

import brix.jcr.api.wrapper.WrapperAccessor;

/**
 * 
 * @author Matej Knopp
 */
public interface JcrNodeIterator 
{

    public static class Wrapper
    {
        public static JcrNodeIterator wrap(NodeIterator delegate, JcrSession session)
        {
            return WrapperAccessor.JcrNodeIteratorWrapper.wrap(delegate, session);
        }
    };

//    public NodeIterator getDelegate();

    public JcrNode nextNode();
    
    public boolean hasNext();
    
    public long getSize();

	public void skip(long i);

}