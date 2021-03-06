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

package brix.plugin.site;

import javax.jcr.Node;

import brix.Brix;
import brix.jcr.JcrNodeWrapperFactory;
import brix.jcr.api.JcrNode;
import brix.jcr.api.JcrSession;
import brix.plugin.site.folder.FolderNode;

/**
 * Node that can wrap the brix:root/brix:site node
 * 
 * @author Matej Knopp
 */
public class SiteRootNode extends FolderNode 
{
	
	public SiteRootNode(Node delegate, JcrSession session)
	{
		super(delegate, session);
	}

	@Override
	public String getUserVisibleName()
	{
		return "Site";
	}

	public static final JcrNodeWrapperFactory FACTORY = new JcrNodeWrapperFactory()
	{

		@Override
		public boolean canWrap(Brix brix, JcrNode node)
		{
			return node.getPath().equals(SitePlugin.get(brix).getSiteRootPath());
		}

		@Override
		public JcrNode wrap(Brix brix, Node node, JcrSession session)
		{
			return new SiteRootNode(node, session);
		}
	};
}
