package brix.web.nodepage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.target.component.IPageRequestTarget;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.StringValue;

import brix.exception.BrixException;

public class BrixPageParameters implements Serializable
{

    public BrixPageParameters()
    {

    }

    public BrixPageParameters(BrixPageParameters copy)
    {
        if (copy == null)
        {
            throw new IllegalArgumentException("Copy argument may not be null.");
        }
        if (copy.indexedParameters != null)
            this.indexedParameters = new ArrayList<String>(copy.indexedParameters);

        if (copy.queryStringParameters != null)
            this.queryStringParameters = new ArrayList<Entry>(copy.queryStringParameters);
    }

    private List<String> indexedParameters = null;

    private static class Entry implements Serializable
    {
        private String key;
        private String value;
    };

    private List<Entry> queryStringParameters = null;

    public int getIndexedParamsCount()
    {
        return indexedParameters != null ? indexedParameters.size() : 0;
    }

    public void setIndexedParam(int index, Object object)
    {
        if (indexedParameters == null)
            indexedParameters = new ArrayList<String>(index);

        for (int i = indexedParameters.size(); i <= index; ++i)
        {
            indexedParameters.add(null);
        }

        indexedParameters.set(index, object != null ? object.toString() : null);
    }

    public StringValue getIndexedParam(int index)
    {
        if (indexedParameters != null)
        {
            if (index >= 0 && index < indexedParameters.size())
            {
                return StringValue.valueOf(indexedParameters.get(index));
            }
        }
        return StringValue.valueOf((String)null);
    };

    public void removeIndexedParam(int index)
    {
        if (indexedParameters != null)
        {
            if (index >= 0 && index < indexedParameters.size())
            {
                indexedParameters.remove(index);
            }
        }
    }

    public Set<String> getQueryParamKeys()
    {
        if (queryStringParameters == null || queryStringParameters.isEmpty())
        {
            return Collections.emptySet();
        }
        Set<String> set = new TreeSet<String>();
        for (Entry entry : queryStringParameters)
        {
            set.add(entry.key);
        }
        return Collections.unmodifiableSet(set);
    }

    public StringValue getQueryParam(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Parameter name may not be null.");
        }
        if (queryStringParameters != null)
        {
            for (Entry entry : queryStringParameters)
            {
                if (entry.key.equals(name))
                {
                    return StringValue.valueOf(entry.value);
                }
            }
        }
        return StringValue.valueOf((String)null);
    }

    public List<StringValue> getQueryParams(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Parameter name may not be null.");
        }
        if (queryStringParameters != null)
        {
            List<StringValue> result = new ArrayList<StringValue>();
            for (Entry entry : queryStringParameters)
            {
                if (entry.key.equals(name))
                {
                    result.add(StringValue.valueOf(entry.value));
                }
            }
            return Collections.unmodifiableList(result);
        }
        else
        {
            return Collections.emptyList();
        }
    }

    public void removeQueryParam(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Parameter name may not be null.");
        }
        if (queryStringParameters != null)
        {
            for (Iterator<Entry> i = queryStringParameters.iterator(); i.hasNext();)
            {
                Entry e = i.next();
                if (e.key.equals(name))
                {
                    i.remove();
                }
            }
        }
    }

    public void addQueryParam(String name, Object value)
    {
        addQueryParam(name, value, -1);
    }

    public void addQueryParam(String name, Object value, int index)
    {

        if (name == null)
        {
            throw new IllegalArgumentException("Parameter name may not be null.");
        }

        if (value == null)
        {
            throw new IllegalArgumentException("Parameter value may not be null.");
        }

        if (queryStringParameters == null)
            queryStringParameters = new ArrayList<Entry>(1);
        Entry entry = new Entry();
        entry.key = name;
        entry.value = value.toString();

        if (index == -1)
            queryStringParameters.add(entry);
        else
            queryStringParameters.add(index, entry);
    }

    public void setQueryParam(String name, Object value, int index)
    {
        removeQueryParam(name);

        if (value != null)
        {
            addQueryParam(name, value);
        }
    }

    public void setQueryParam(String name, Object value)
    {
        setQueryParam(name, value, -1);
    }

    public void clearIndexedParams()
    {
        this.indexedParameters = null;
    }

    public void clearQueryParams()
    {
        this.queryStringParameters = null;
    }

    void assign(BrixPageParameters other)
    {
        if (this != other)
        {
            this.indexedParameters = other.indexedParameters;
            this.queryStringParameters = other.queryStringParameters;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj instanceof BrixPageParameters == false)
        {
            return false;
        }

        BrixPageParameters rhs = (BrixPageParameters)obj;
        if (!Objects.equal(indexedParameters, rhs.indexedParameters))
        {
            return false;
        }

        if (queryStringParameters == null || rhs.queryStringParameters == null)
        {
            return rhs.queryStringParameters == queryStringParameters;
        }

        if (queryStringParameters.size() != rhs.queryStringParameters.size())
        {
            return false;
        }

        for (String key : getQueryParamKeys())
        {
            List<StringValue> values1 = getQueryParams(key);
            Set<String> v1 = new TreeSet<String>();
            List<StringValue> values2 = rhs.getQueryParams(key);
            Set<String> v2 = new TreeSet<String>();
            for (StringValue sv : values1)
            {
                v1.add(sv.toString());
            }
            for (StringValue sv : values2)
            {
                v2.add(sv.toString());
            }
            if (v1.equals(v2) == false)
            {
                return false;
            }
        }
        return true;
    }

    private static BrixNodeWebPage getCurrentPage()
    {
        IRequestTarget target = RequestCycle.get().getRequestTarget();
        BrixNodeWebPage page = null;
        if (target != null && target instanceof IPageRequestTarget)
        {
            Page p = ((IPageRequestTarget)target).getPage();
            if (p instanceof BrixNodeWebPage)
            {
                page = (BrixNodeWebPage)p;
            }
        }
        if (page == null)
        {
            throw new BrixException(
                    "Couldn't obtain the BrixNodeWebPage instance from RequestTarget.");
        }
        return page;
    }

    public static BrixPageParameters getCurrent()
    {
        IRequestTarget target = RequestCycle.get().getRequestTarget();
        // this is required for getting current page parameters from page constructor
        // (the actual page instance is not constructed yet. 
        if (target instanceof PageParametersRequestTarget)
        {
            return ((PageParametersRequestTarget)target).getPageParameters();
        }
        else
        {
            return getCurrentPage().getBrixPageParameters();
        }
    }

    public String toCallbackURL()
    {
        IRequestTarget target = new BrixNodeRequestTarget(getCurrentPage(), this);
        return RequestCycle.get().urlFor(target).toString();
    }

}