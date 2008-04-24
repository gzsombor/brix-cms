package brix.plugin.site.admin;

import java.util.List;

import javax.jcr.ReferentialIntegrityException;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import brix.Brix;
import brix.BrixRequestCycle;
import brix.Path;
import brix.BrixRequestCycle.Locator;
import brix.auth.Action;
import brix.auth.Action.Context;
import brix.auth.impl.NodeActionImpl;
import brix.jcr.api.JcrNode;
import brix.jcr.exception.JcrException;
import brix.plugin.site.SiteNavigationTreeNode;
import brix.plugin.site.SiteNodePlugin;
import brix.plugin.site.SitePlugin;
import brix.web.util.PathLabel;

public class NodeManagerContainerPanel extends NodeManagerPanel
{
    
    private Component editor;

    public NodeManagerContainerPanel(String id, IModel<JcrNode> model)
    {
        super(id, model);

        Path root = new Path(BrixRequestCycle.Locator.getBrix().getWebPath());
        add(new PathLabel("path2", new PropertyModel(this, "node.path"), root)
        {
            @Override
            protected void onPathClicked(Path path)
            {
                JcrNode node = (JcrNode)getNode().getSession().getItem(path.toString());
                selectNode(node);
            }
        });

        add(new Link("makeVersionable")
        {
            @Override
            public void onClick()
            {
                if (!getNode().isNodeType("mix:versionable"))
                {
                    getNode().addMixin("mix:versionable");
                    getNode().save();
                    getNode().checkin();
                }
            }

            @Override
            public boolean isVisible()
            {
                Action action = new NodeActionImpl(Context.ADMINISTRATION,
                        NodeActionImpl.Type.NODE_EDIT, getNode());
                return getNode() != null && getNode().isNodeType("nt:file") &&
                        !getNode().isNodeType("mix:versionable") &&
                        Locator.getBrix().getAuthorizationStrategy().isActionAuthorized(action);
            }
        });

        add(new Link("delete")
        {

            @Override
            public void onClick()
            {
                JcrNode node = getNode();
                JcrNode parent = node.getParent();

                selectNode(parent);
                
                node.remove();

                try
                {                    
                    parent.save();
                    rebuildChildren(parent);                                        
                }
                catch (JcrException e)
                {
                    if (e.getCause() instanceof ReferentialIntegrityException)
                    {
                        parent.getSession().refresh(false);
                        NodeManagerContainerPanel.this.getModel().detach();
                        // parent.refresh(false);
                        getSession()
                                .error(
                                        "Couldn't delete node. Other nodes contain references to this node.");
                        selectNode(getNode());
                    }
                    else
                    {
                        throw e;
                    }
                }
            }

            @Override
            public boolean isVisible()
            {
                Action action = new NodeActionImpl(Context.ADMINISTRATION,
                        NodeActionImpl.Type.NODE_DELETE, getNode());
                Brix brix = BrixRequestCycle.Locator.getBrix();
                String path = getNode().getPath();
                return path.startsWith(brix.getWebPath()) &&
                        path.length() > brix.getWebPath().length() &&
                        Locator.getBrix().getAuthorizationStrategy().isActionAuthorized(action);
            }

        });

        editor = new WebMarkupContainer("editor");
        add(editor);
        setupEditor();
        
        add(new SessionFeedbackPanel("sessionFeedback"));
    }

    private void selectNode(JcrNode node)
    {
        getNavigation().selectNode(new SiteNavigationTreeNode(node));
    }

    private void nodeDeleted(JcrNode node)
    {
        getNavigation().nodeDeleted(new SiteNavigationTreeNode(node));
    }
    
    private void rebuildChildren(JcrNode parent)
    {
        getNavigation().nodeChildrenChanged(new SiteNavigationTreeNode(parent));
    }
    
    private void setupEditor()
    {
        final Component newEditor;

        SiteNodePlugin plugin = SitePlugin.get().getNodePluginForNode(getNode());

        newEditor = plugin.newManageNodePanel("editor", getModel());

        editor.replaceWith(newEditor);
        editor = newEditor;
    }
    
    private static class SessionFeedbackPanel extends FeedbackPanel
    {

        public SessionFeedbackPanel(String id)
        {
            super(id, new Filter());
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean isVisible()
        {
            List messages = (List)getFeedbackMessagesModel().getObject();
            return messages != null && !messages.isEmpty();
        }

        private static class Filter implements IFeedbackMessageFilter
        {
            public boolean accept(FeedbackMessage message)
            {
                return message.getReporter() == null;
            }
        };
    };

}