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

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.AccessDeniedException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;
import javax.jcr.lock.LockManager;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.observation.ObservationManager;
import javax.jcr.query.QueryManager;
import javax.jcr.version.Version;
import javax.jcr.version.VersionManager;

import org.xml.sax.ContentHandler;

import brix.jcr.base.BrixSession;

class WorkspaceWrapper extends BaseWrapper<Workspace> implements Workspace
{

    private WorkspaceWrapper(Workspace delegate, EventHandlerSessionWrapper session)
    {
        super(delegate, session);
    }

    public static WorkspaceWrapper wrap(Workspace delegate, EventHandlerSessionWrapper session)
    {
        if (delegate == null)
        {
            return null;
        }
        else
        {
            return new WorkspaceWrapper(delegate, session);
        }
    }

    public void clone(String srcWorkspace, String srcAbsPath, String destAbsPath,
            boolean removeExisting) throws RepositoryException
    {
        getActionHandler().beforeWorkspaceClone(srcWorkspace, srcAbsPath, destAbsPath);
        getDelegate().clone(srcWorkspace, srcAbsPath, destAbsPath, removeExisting);
        getActionHandler().afterWorkspaceClone(srcWorkspace, srcAbsPath, destAbsPath);
    }

    public void copy(String srcAbsPath, String destAbsPath) throws RepositoryException
    {
        getActionHandler().beforeWorkspaceCopy(srcAbsPath, destAbsPath);
        getDelegate().copy(srcAbsPath, destAbsPath);
        getActionHandler().afterWorkspaceCopy(srcAbsPath, destAbsPath);
    }

    public void copy(String srcWorkspace, String srcAbsPath, String destAbsPath)
            throws RepositoryException
    {
        getActionHandler().beforeWorkspaceCopy(srcWorkspace, srcAbsPath, destAbsPath);
        getDelegate().copy(srcWorkspace, srcAbsPath, destAbsPath);
        getActionHandler().afterWorkspaceCopy(srcWorkspace, srcAbsPath, destAbsPath);
    }

    public String[] getAccessibleWorkspaceNames() throws RepositoryException
    {
        return getDelegate().getAccessibleWorkspaceNames();
    }

    public ContentHandler getImportContentHandler(String parentAbsPath, int uuidBehavior)
            throws RepositoryException
    {
        return getDelegate().getImportContentHandler(parentAbsPath, uuidBehavior);
    }

    public String getName()
    {
        return getDelegate().getName();
    }

    public NamespaceRegistry getNamespaceRegistry() throws RepositoryException
    {
        return getDelegate().getNamespaceRegistry();
    }

    public NodeTypeManager getNodeTypeManager() throws RepositoryException
    {
        return getDelegate().getNodeTypeManager();
    }

    public ObservationManager getObservationManager() throws RepositoryException
    {
        return getDelegate().getObservationManager();
    }

    public QueryManager getQueryManager() throws RepositoryException
    {
        return QueryManagerWrapper.wrap(getDelegate().getQueryManager(), getSessionWrapper());
    }

    public BrixSession getSession()
    {
        return getSessionWrapper();
    }

    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior)
            throws IOException, RepositoryException
    {
        getActionHandler().beforeWorkspaceImportXML(parentAbsPath);
        getDelegate().importXML(parentAbsPath, in, uuidBehavior);
        getActionHandler().afterWorkspaceImportXML(parentAbsPath);
    }

    public void move(String srcAbsPath, String destAbsPath) throws RepositoryException
    {
        getActionHandler().beforeWorkspaceMove(srcAbsPath, destAbsPath);
        getDelegate().move(srcAbsPath, destAbsPath);
        getActionHandler().afterWorkspaceMove(srcAbsPath, destAbsPath);
    }

    /** @deprecated */
    @Deprecated
    public void restore(Version[] versions, boolean removeExisting) throws RepositoryException
    {
        getDelegate().restore(versions, removeExisting);
    }

    public void createWorkspace(String name) throws AccessDeniedException,
            UnsupportedRepositoryOperationException, RepositoryException
    {
        getActionHandler().beforeCreateWorkspace(name, null);
        getDelegate().createWorkspace(name);
        getActionHandler().afterCreateWorkspace(name, null);
    }

    public void createWorkspace(String name, String srcWorkspace) throws AccessDeniedException,
            UnsupportedRepositoryOperationException, NoSuchWorkspaceException, RepositoryException
    {
        getActionHandler().beforeCreateWorkspace(name, srcWorkspace);
        getDelegate().createWorkspace(name, srcWorkspace);
        getActionHandler().afterCreateWorkspace(name, srcWorkspace);

    }

    public void deleteWorkspace(String name) throws AccessDeniedException,
            UnsupportedRepositoryOperationException, NoSuchWorkspaceException, RepositoryException
    {
        getActionHandler().beforeDeleteWorkspace(name);
        getDelegate().deleteWorkspace(name);
        getActionHandler().afterDeleteWorkspace(name);

    }

    public LockManager getLockManager() throws UnsupportedRepositoryOperationException,
            RepositoryException
    {
        return getDelegate().getLockManager();
    }

    public VersionManager getVersionManager() throws UnsupportedRepositoryOperationException,
            RepositoryException
    {
        return getDelegate().getVersionManager();
    }
}
