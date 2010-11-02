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

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.qom.QueryObjectModelFactory;

class QueryManagerWrapper extends BaseWrapper<QueryManager> implements QueryManager
{

    private QueryManagerWrapper(QueryManager delegate, EventHandlerSessionWrapper session)
    {
        super(delegate, session);
    }

    public static QueryManagerWrapper wrap(QueryManager delegate, EventHandlerSessionWrapper session)
    {
        if (delegate == null)
        {
            return null;
        }
        else
        {
            return new QueryManagerWrapper(delegate, session);
        }
    }

    public Query createQuery(String statement, String language) throws RepositoryException
    {
        return QueryWrapper.wrap(getDelegate().createQuery(statement, language),
                getSessionWrapper());
    }

    public Query getQuery(Node node) throws RepositoryException
    {
        return QueryWrapper.wrap(getDelegate().getQuery(unwrap(node)), getSessionWrapper());
    }

    public String[] getSupportedQueryLanguages() throws RepositoryException
    {
        return getDelegate().getSupportedQueryLanguages();
    }

    public QueryObjectModelFactory getQOMFactory()
    {
        return getDelegate().getQOMFactory();
    }

}
