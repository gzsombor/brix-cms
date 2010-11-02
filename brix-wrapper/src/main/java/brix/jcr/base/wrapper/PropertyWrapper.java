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

package brix.jcr.base.wrapper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.ItemNotFoundException;
import javax.jcr.ItemVisitor;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.version.VersionException;

class PropertyWrapper extends ItemWrapper implements Property
{

    private PropertyWrapper(Property delegate, EventHandlerSessionWrapper session)
    {
        super(delegate, session);
    }

    public static PropertyWrapper wrap(Property delegate, EventHandlerSessionWrapper session)
    {
        if (delegate == null)
        {
            return null;
        }
        else
        {
            return new PropertyWrapper(delegate, session);
        }
    }

    @Override
    public Property getDelegate()
    {
        return (Property)super.getDelegate();
    }

    public boolean getBoolean() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getBoolean(unwrap(this));
    }

    public Calendar getDate() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getDate(unwrap(this));
    }

    public PropertyDefinition getDefinition() throws RepositoryException
    {
        return getDelegate().getDefinition();
    }

    public double getDouble() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getDouble(unwrap(this));
    }

    public long getLength() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getLength(unwrap(this));
    }

    public long[] getLengths() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getLengths(unwrap(this));
    }

    public long getLong() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getLong(unwrap(this));
    }

    public Node getNode() throws RepositoryException
    {
        Node node = getSessionWrapper().getValueFilter().getNode(unwrap(this));
        return NodeWrapper.wrap(node, getSessionWrapper());
    }

    /** @deprecated */
    @Deprecated
  public InputStream getStream() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getStream(unwrap(this));
    }

    public String getString() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getString(unwrap(this));
    }

    public int getType() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getType(unwrap(this));
    }

    public Value getValue() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getValue(unwrap(this));
    }

    public Value[] getValues() throws RepositoryException
    {
        return getSessionWrapper().getValueFilter().getValues(unwrap(this));
    }

    private void beforeValueSet(Object value) throws RepositoryException
    {
        if (value == null)
        {
            getActionHandler().beforePropertyRemove(getParent(), getName());
        }
        else
        {
            getActionHandler().beforePropertySet(getParent(), getName());
        }
    }

    private void afterValueSet(Object value) throws RepositoryException
    {
        if (value == null)
        {
            getActionHandler().afterPropertyRemove(getParent(), getName());
        }
        else
        {
            getActionHandler().afterPropertySet(this);
        }
    }

    public void setValue(Value value) throws RepositoryException
    {
        beforeValueSet(value);
        getSessionWrapper().getValueFilter().setValue(unwrap(getParent()), getName(), value, null);
        afterValueSet(value);
    }

    public void setValue(Value[] values) throws RepositoryException
    {
        beforeValueSet(values);
        getSessionWrapper().getValueFilter().setValue(unwrap(getParent()), getName(), values, null);
        afterValueSet(values);
    }

    public void setValue(String value) throws RepositoryException
    {
        Value v = value != null ? getSession().getValueFactory().createValue(value) : null;
        setValue(v);
    }

    public void setValue(String[] values) throws RepositoryException
    {
        Value[] v = new Value[values.length];
        for (int i = 0; i < values.length; ++i)
        {
            v[i] = getSession().getValueFactory().createValue(values[i]);
        }
        setValue(v);
    }

    /** @deprecated */
    @Deprecated
   public void setValue(InputStream value) throws RepositoryException
    {
        Value v = value != null ? getSession().getValueFactory().createValue(value) : null;
        setValue(v);
    }

    public void setValue(long value) throws RepositoryException
    {
        Value v = getSession().getValueFactory().createValue(value);
        setValue(v);
    }

    public void setValue(double value) throws RepositoryException
    {
        Value v = getSession().getValueFactory().createValue(value);
        setValue(v);
    }

    public void setValue(Calendar value) throws RepositoryException
    {
        Value v = value != null ? getSession().getValueFactory().createValue(value) : null;
        setValue(v);
    }

    public void setValue(boolean value) throws RepositoryException
    {
        Value v = getSession().getValueFactory().createValue(value);
        setValue(v);
    }

    public void setValue(Node value) throws RepositoryException
    {
        Value v = value != null ? getSession().getValueFactory().createValue(unwrap(value)) : null;
        setValue(v);
    }

    public void accept(ItemVisitor visitor) throws RepositoryException
    {
        visitor.visit(this);
    }

    public boolean isNode()
    {
        return false;
    }

    private String name = null;

    @Override
    public String getName() throws RepositoryException
    {
        if (name == null)
        {
            name = super.getName();
        }
        return name;
    }


    private Node parent = null;

    @Override
    public Node getParent() throws RepositoryException
    {
        if (parent == null)
        {
            parent = super.getParent();
        }
        return parent;
    }

    @Override
    public void remove() throws RepositoryException
    {
        Node parent = getParent();
        String name = getName();
        getActionHandler().beforePropertyRemove(parent, name);
        super.remove();
        getActionHandler().afterPropertyRemove(parent, name);
    }

    public Binary getBinary() throws ValueFormatException, RepositoryException
    {
        return getSessionWrapper().getValueFilter().getBinary(unwrap(this));
    }

    public BigDecimal getDecimal() throws ValueFormatException, RepositoryException
    {
        return getSessionWrapper().getValueFilter().getDecimal(unwrap(this));
    }

    public Property getProperty() throws ItemNotFoundException, ValueFormatException,
            RepositoryException
    {
        return PropertyWrapper.wrap(getDelegate().getProperty(), getSessionWrapper());
    }

    public boolean isMultiple() throws RepositoryException
    {
        return getDelegate().isMultiple();
    }

    public void setValue(Binary value) throws ValueFormatException, VersionException,
            LockException, ConstraintViolationException, RepositoryException
    {
        Value v = value != null ? getSession().getValueFactory().createValue(unwrap(value)) : null;
        setValue(v);


    }

    public void setValue(BigDecimal value) throws ValueFormatException, VersionException,
            LockException, ConstraintViolationException, RepositoryException
    {
        Value v = value != null ? getSession().getValueFactory().createValue(unwrap(value)) : null;
        setValue(v);


    }
}
