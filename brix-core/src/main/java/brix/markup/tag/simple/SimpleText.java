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

package brix.markup.tag.simple;

import brix.markup.tag.Text;

/**
 * Simple implementation of the {@link Text} interface
 * 
 * @author Matej Knopp
 */
public class SimpleText implements Text
{
	private final String text;
	
	public SimpleText(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}

}
