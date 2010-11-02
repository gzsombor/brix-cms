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

import brix.jcr.base.action.AbstractActionHandler;

class BaseWrapper<T>
{

	private final T delegate;
	private final EventHandlerSessionWrapper session;

	public BaseWrapper(T delegate, EventHandlerSessionWrapper session)
	{
		this.delegate = delegate;
		this.session = session;
	}

	public T getDelegate()
	{
		return delegate;
	}

	public EventHandlerSessionWrapper getSessionWrapper()
	{
		return session;
	}

	Integer hashCode;

	@Override
	public int hashCode()
	{
		return getDelegate().hashCode();
		// optimization, is it really needed? used in equals...
//		if (hashCode == null)
//		{
//			hashCode = getDelegate().hashCode();
//		}
//		return hashCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj instanceof BaseWrapper)
		{
			return false;
		}
		BaseWrapper wrapper = (BaseWrapper) obj;

		// optimization - check for hash code (if we know it)
		if (hashCode != null && wrapper.hashCode != null && hashCode != wrapper.hashCode())
		{
			return false;
		}
		else
		{
			return getDelegate() == wrapper.getDelegate() || getDelegate().equals(wrapper.getDelegate());
		}
	}

	@SuppressWarnings("unchecked")
	protected <TYPE> TYPE unwrap(TYPE wrapper)
	{
		while (wrapper instanceof BaseWrapper)
		{
			wrapper = (TYPE) ((BaseWrapper) wrapper).getDelegate();
		}
		return wrapper;
	}

	public <TYPE> TYPE[] unwrap(TYPE original[], TYPE newArray[])
	{
		for (int i = 0; i < original.length; ++i)
		{
			newArray[i] = unwrap(original[i]);
		}
		return newArray;
	}

	public AbstractActionHandler getActionHandler()
	{
		return getSessionWrapper().getActionHandler();
	}
}
