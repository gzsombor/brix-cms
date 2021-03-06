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

package brix.plugin.menu.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.lang.Objects;

import brix.plugin.menu.Menu.Entry;
import brix.web.tree.TreeNode;

public class MenuTreeNode implements TreeNode
{
	private final Entry entry;

	public Entry getEntry()
	{
		return entry;
	}

	public MenuTreeNode(Entry entry)
	{
		if (entry == null)
		{
			throw new IllegalArgumentException("Argument 'entry' may not be null.");
		}
		this.entry = entry;
	}

	public void detach()
	{
	}

	public List<? extends TreeNode> getChildren()
	{
		List<MenuTreeNode> children = new ArrayList<MenuTreeNode>();
		for (Entry e : entry.getChildren())
		{
			children.add(new MenuTreeNode(e));
		}
		return children;
	}

	public boolean isLeaf()
	{
		return entry.getChildren().isEmpty();
	}

	@Override
	public String toString()
	{
		return entry.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		else if (obj instanceof MenuTreeNode == false)
		{
			return false;
		}
		return Objects.equal(entry, ((MenuTreeNode) obj).entry);
	}

	@Override
	public int hashCode()
	{
		return entry.hashCode();
	}
}
