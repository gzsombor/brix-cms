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

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.ItemVisitor;
import javax.jcr.Node;
import javax.jcr.Value;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.Version;

import brix.jcr.api.JcrItem;
import brix.jcr.api.JcrNode;
import brix.jcr.api.JcrNodeIterator;
import brix.jcr.api.JcrProperty;
import brix.jcr.api.JcrPropertyIterator;
import brix.jcr.api.JcrSession;
import brix.jcr.api.JcrSession.Behavior;
import brix.jcr.api.JcrValue;
import brix.jcr.api.JcrVersion;
import brix.jcr.api.JcrVersionHistory;

/**
 * 
 * @author Matej Knopp
 * @author igor.vaynberg
 */
public class NodeWrapper extends ItemWrapper implements JcrNode
{

    public NodeWrapper(Node delegate, JcrSession session)
    {
        super(delegate, session);
    }

    public static JcrNode wrap(Node delegate, JcrSession session)
    {
        if (delegate == null)
        {
            return null;
        }
        else
        {
            Behavior behavior = session.getBehavior();
            if (behavior != null)
            {
                JcrNode node = behavior.wrap(delegate, session);
                if (node != null)
                {
                    return node;
                }
            }
            return new NodeWrapper(delegate, session);
        }
    }

    @Override
    public Node getDelegate()
    {
        return (Node)super.getDelegate();
    }

    public void addMixin(final String mixinName)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().addMixin(mixinName);
            }
        });
    }

    public JcrNode addNode(final String relPath)
    {
        return executeCallback(new Callback<JcrNode>()
        {
            public JcrNode execute() throws Exception
            {
                return JcrNode.Wrapper.wrap(getDelegate().addNode(relPath), getJcrSession());
            }
        });
    }

    public JcrNode addNode(final String relPath, final String primaryNodeTypeName)
    {
        return executeCallback(new Callback<JcrNode>()
        {
            public JcrNode execute() throws Exception
            {
                return JcrNode.Wrapper.wrap(getDelegate().addNode(relPath, primaryNodeTypeName),
                        getJcrSession());
            }
        });
    }

    public boolean canAddMixin(final String mixinName)
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().canAddMixin(mixinName);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void cancelMerge(final Version version)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().cancelMerge(unwrap(version));
            }
        });
    }

    /** @depreated */
    @Deprecated
    public JcrVersion checkin()
    {
        return executeCallback(new Callback<JcrVersion>()
        {
            public JcrVersion execute() throws Exception
            {
                final Node delegate = getDelegate();
                if (delegate.isNodeType("mix:versionable"))
                {
                    return JcrVersion.Wrapper.wrap(delegate.checkin(), getJcrSession());
                }
                else
                {
                    return null;
                }
            }
        });
    }

    /** @depreated */
    @Deprecated
    @Override
    public void save()
    {
        Behavior behavior = getJcrSession().getBehavior();
        if (behavior != null)
        {
            behavior.nodeSaved(this);
        }
        super.save();
    }

    /** @depreated */
    @Deprecated
    public void checkout()
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                if (getDelegate().isNodeType("mix:versionable"))
                {
                    getDelegate().checkout();
                }
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void doneMerge(final Version version)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().doneMerge(unwrap(version));
            }
        });
    }

    /** @depreated */
    @Deprecated
    public JcrVersion getBaseVersion()
    {
        return executeCallback(new Callback<JcrVersion>()
        {
            public JcrVersion execute() throws Exception
            {
                return JcrVersion.Wrapper.wrap(getDelegate().getBaseVersion(), getJcrSession());
            }
        });
    }

    public String getCorrespondingNodePath(final String workspaceName)
    {
        return executeCallback(new Callback<String>()
        {
            public String execute() throws Exception
            {
                return getDelegate().getCorrespondingNodePath(workspaceName);
            }
        });
    }

    public NodeDefinition getDefinition()
    {
        return executeCallback(new Callback<NodeDefinition>()
        {
            public NodeDefinition execute() throws Exception
            {
                return getDelegate().getDefinition();
            }
        });
    }

    public int getIndex()
    {
        return executeCallback(new Callback<Integer>()
        {
            public Integer execute() throws Exception
            {
                return getDelegate().getIndex();
            }
        });
    }

    /** @depreated */
    @Deprecated
    public Lock getLock()
    {
        return executeCallback(new Callback<Lock>()
        {
            public Lock execute() throws Exception
            {
                return getDelegate().getLock();
            }
        });
    }

    public NodeType[] getMixinNodeTypes()
    {
        return executeCallback(new Callback<NodeType[]>()
        {
            public NodeType[] execute() throws Exception
            {
                return getDelegate().getMixinNodeTypes();
            }
        });
    }

    public JcrNode getNode(final String relPath)
    {
        return executeCallback(new Callback<JcrNode>()
        {
            public JcrNode execute() throws Exception
            {
                return JcrNode.Wrapper.wrap(getDelegate().getNode(relPath), getJcrSession());
            }
        });
    }

    public JcrNodeIterator getNodes()
    {
        return executeCallback(new Callback<JcrNodeIterator>()
        {
            public JcrNodeIterator execute() throws Exception
            {
                return JcrNodeIterator.Wrapper.wrap(getDelegate().getNodes(), getJcrSession());
            }
        });
    }

    public JcrNodeIterator getNodes(final String namePattern)
    {
        return executeCallback(new Callback<JcrNodeIterator>()
        {
            public JcrNodeIterator execute() throws Exception
            {
                return JcrNodeIterator.Wrapper.wrap(getDelegate().getNodes(namePattern),
                        getJcrSession());
            }
        });
    }

    public JcrItem getPrimaryItem()
    {
        return executeCallback(new Callback<JcrItem>()
        {
            public JcrItem execute() throws Exception
            {
                return JcrItem.Wrapper.wrap(getDelegate().getPrimaryItem(), getJcrSession());
            }
        });
    }

    public NodeType getPrimaryNodeType()
    {
        return executeCallback(new Callback<NodeType>()
        {
            public NodeType execute() throws Exception
            {
                return getDelegate().getPrimaryNodeType();
            }
        });
    }

    public JcrPropertyIterator getProperties()
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {
            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getProperties(),
                        getJcrSession());
            }
        });
    }

    public JcrPropertyIterator getProperties(final String namePattern)
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {
            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getProperties(namePattern),
                        getJcrSession());
            }
        });
    }

    public JcrProperty getProperty(final String relPath)
    {
        return executeCallback(new Callback<JcrProperty>()
        {
            public JcrProperty execute() throws Exception
            {
                return JcrProperty.Wrapper
                        .wrap(getDelegate().getProperty(relPath), getJcrSession());
            }
        });
    }

    public JcrPropertyIterator getReferences()
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {
            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getReferences(),
                        getJcrSession());
            }
        });
    }

    /** @depreated */
    @Deprecated
    public String getUUID()
    {
        return executeCallback(new Callback<String>()
        {
            public String execute() throws Exception
            {
                return getDelegate().getUUID();
            }
        });
    }

    /** @depreated */
    @Deprecated
    public JcrVersionHistory getVersionHistory()
    {
        return executeCallback(new Callback<JcrVersionHistory>()
        {
            public JcrVersionHistory execute() throws Exception
            {
                return JcrVersionHistory.Wrapper.wrap(getDelegate().getVersionHistory(),
                        getJcrSession());
            }
        });
    }

    public boolean hasNode(final String relPath)
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().hasNode(relPath);
            }
        });
    }

    public boolean hasNodes()
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().hasNodes();
            }
        });
    }

    public boolean hasProperties()
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().hasProperties();
            }
        });
    }

    public boolean hasProperty(final String relPath)
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().hasProperty(relPath);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public boolean holdsLock()
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().holdsLock();
            }
        });
    }

    public boolean isCheckedOut()
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().isCheckedOut();
            }
        });
    }

    public boolean isLocked()
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().isLocked();
            }
        });
    }

    public boolean isNodeType(final String nodeTypeName)
    {
        return executeCallback(new Callback<Boolean>()
        {
            public Boolean execute() throws Exception
            {
                return getDelegate().isNodeType(nodeTypeName);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public Lock lock(final boolean isDeep, final boolean isSessionScoped)
    {
        return executeCallback(new Callback<Lock>()
        {
            public Lock execute() throws Exception
            {
                return getDelegate().lock(isDeep, isSessionScoped);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public JcrNodeIterator merge(final String srcWorkspace, final boolean bestEffort)
    {
        return executeCallback(new Callback<JcrNodeIterator>()
        {
            public JcrNodeIterator execute() throws Exception
            {
                return JcrNodeIterator.Wrapper.wrap(getDelegate().merge(srcWorkspace, bestEffort),
                        getJcrSession());
            }
        });
    }

    public void orderBefore(final String srcChildRelPath, final String destChildRelPath)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().orderBefore(srcChildRelPath, destChildRelPath);
            }
        });

    }

    public void removeMixin(final String mixinName)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().removeMixin(mixinName);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void restore(final String versionName, final boolean removeExisting)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().restore(versionName, removeExisting);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void restore(final Version version, final boolean removeExisting)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().restore(unwrap(version), removeExisting);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void restore(final Version version, final String relPath, final boolean removeExisting)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().restore(unwrap(version), relPath, removeExisting);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void restoreByLabel(final String versionLabel, final boolean removeExisting)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().restoreByLabel(versionLabel, removeExisting);
            }
        });
    }

    public void setProperty(final String name, final JcrValue value)
    {
    	setPropertyValue(name, value.getValueAsObject());
    }

    public void setProperty(final String name, final Value[] values)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                final Value[] unwrapped = unwrap(values, new Value[values.length]);
                getDelegate().setProperty(name, unwrapped);
            }
        });
    }

    public void setProperty(final String name, final String[] values)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, values);
            }
        });
    }

    public void setProperty(final String name, final String value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, value);
            }
        });
    }

    /** @depreated */
    @Deprecated
    public void setProperty(final String name, final InputStream value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, value);
            }
        });
    }

    public void setProperty(final String name, final boolean value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, value);
            }
        });
    }

    public void setProperty(final String name, final double value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, value);
            }
        });
    }

    public void setProperty(final String name, final long value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, value);
            }
        });
    }

    public void setProperty(final String name, final Calendar value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, value);
            }
        });
    }

    public void setProperty(final String name, final JcrNode value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, unwrap(value.getDelegate()));
            }
        });
    }

	public void setProperty(final String name, final JcrValue[] values) {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
            	// unwrap can't be used here, because JcrValue[] and Value[] is not related to each other
            	Value[] result = new Value[values.length];
            	for (int i = 0; i < values.length; i++) {
            		result[i] = values[i].getDelegate();
            	}
                getDelegate().setProperty(name, result);
            }
        });
	}
    
    

    /** @depreated */
    @Deprecated
    public void unlock()
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().unlock();
            }
        });
    }

    public void update(final String srcWorkspaceName)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().update(srcWorkspaceName);
            }
        });
    }

    @Override
    public String toString()
    {
        return getPath() + " [" + getPrimaryNodeType().getName() + "]";
    }

    /*public void accept(final ItemVisitor visitor)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                visitor.visit(NodeWrapper.this);
            }
        });
    }*/

    public void followLifecycleTransition(final String transition)
    {
        executeCallback(new VoidCallback()
        {

            public void execute() throws Exception
            {
                getDelegate().followLifecycleTransition(transition);
            }
        });

    }

    public String[] getAllowedLifecycleTransistions()
    {
        return executeCallback(new Callback<String[]>()
        {

            public String[] execute() throws Exception
            {
                return getDelegate().getAllowedLifecycleTransistions();
            }
        });
    }

    public String getIdentifier()
    {
        return executeCallback(new Callback<String>()
        {

            public String execute() throws Exception
            {
                return getDelegate().getIdentifier();
            }
        });
    }

    public JcrNodeIterator getNodes(final String[] nameGlobs)
    {
        return executeCallback(new Callback<JcrNodeIterator>()
        {
            public JcrNodeIterator execute() throws Exception
            {
                return JcrNodeIterator.Wrapper.wrap(getDelegate().getNodes(nameGlobs),
                        getJcrSession());
            }
        });
    }

    public JcrPropertyIterator getProperties(final String[] nameGlobs)
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {

            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getProperties(nameGlobs),
                        getJcrSession());
            }
        });
    }

    public JcrPropertyIterator getReferences(final String name)
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {

            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getReferences(name),
                        getJcrSession());
            }
        });
    }

    public JcrNodeIterator getSharedSet()
    {
        return executeCallback(new Callback<JcrNodeIterator>()
        {

            public JcrNodeIterator execute() throws Exception
            {
                return JcrNodeIterator.Wrapper.wrap(getDelegate().getSharedSet(), getJcrSession());
            }
        });
    }

    public JcrPropertyIterator getWeakReferences()
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {

            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getWeakReferences(),getJcrSession());
            }
        });
    }

    public JcrPropertyIterator getWeakReferences(final String name)
    {
        return executeCallback(new Callback<JcrPropertyIterator>()
        {

            public JcrPropertyIterator execute() throws Exception
            {
                return JcrPropertyIterator.Wrapper.wrap(getDelegate().getWeakReferences(name), getJcrSession());
            }
        });
    }

    public void removeShare()
    {
        executeCallback(new VoidCallback()
        {

            public void execute() throws Exception
            {
                getDelegate().removeShare();
            }
        });

    }

    public void removeSharedSet()
    {
        executeCallback(new VoidCallback()
        {

            public void execute() throws Exception
            {
                getDelegate().removeSharedSet();
            }
        });

    }

    public void setPrimaryType(final String nodeTypeName)
    {
        executeCallback(new VoidCallback()
        {

            public void execute() throws Exception
            {
                getDelegate().setPrimaryType(nodeTypeName);
            }
        });

    }

    public void setProperty(final String name, final Binary value)
    {
        executeCallback(new VoidCallback()
        {

            public void execute() throws Exception
            {
                getDelegate().setProperty(name, unwrap(value));
            }
        });
    }

    public void setProperty(final String name, final BigDecimal value)
    {
        executeCallback(new VoidCallback()
        {
            public void execute() throws Exception
            {
                getDelegate().setProperty(name, unwrap(value));
            }
        });
    }
    
    public void setPropertyValue(final String name, final Object value) {
    	if (value instanceof JcrValue) {
    		// recursive call
    		setPropertyValue(name, ((JcrValue) value).getValueAsObject());
    	} else {
    		executeCallback(new VoidCallback() { 
    			public void execute() throws Exception {
    				Node node = getDelegate();
    		    	if (value instanceof String) {
    		    		node.setProperty(name, (String) value);
    		    	} else if (value instanceof Long) {
    		    		node.setProperty(name, (Long) value);
    		    	} else if (value instanceof Boolean) {
    		    		node.setProperty(name, (Boolean) value);
    		    	} else if (value instanceof String[]) {
    		    		node.setProperty(name, (String[]) value);
    		    	} else if (value instanceof Double) {
    		    		node.setProperty(name, (Double) value);
    		    	} else if (value instanceof BigDecimal) {
    		    		node.setProperty(name, (BigDecimal) value);
    		    	} else if (value instanceof Calendar) {
    		    		node.setProperty(name, (Calendar) value);
    		    	} else if (value instanceof Node) {
    		    		node.setProperty(name, (Node) value);
    		    	} else if (value instanceof JcrNode) {
    		    		node.setProperty(name, ((JcrNode) value).getDelegate());
    		    	} else if (value == null) {
    		    		node.setProperty(name, (String) null);
    		    	} else {
    		    		throw new RuntimeException("Unexpected type "+value.getClass()+" value="+value);
    		    	}
    				
    			}
    		});
    	}
    }

}
