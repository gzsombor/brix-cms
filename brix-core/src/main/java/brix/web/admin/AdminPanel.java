package brix.web.admin;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import brix.Brix;
import brix.BrixRequestCycle;
import brix.Path;
import brix.BrixRequestCycle.Locator;
import brix.auth.Action;
import brix.auth.WorkspaceAction;
import brix.auth.Action.Context;
import brix.auth.impl.PublishWorkspaceActionImpl;
import brix.auth.impl.WorkspaceActionImpl;
import brix.jcr.api.JcrNode;
import brix.jcr.api.JcrSession;
import brix.web.admin.navigation.Navigation;
import brix.web.admin.navigation.NavigationContainer;
import brix.web.admin.navigation.NavigationPanel;
import brix.web.admin.navigation.NavigationTreeModel;
import brix.web.admin.navigation.NavigationTreeNode;

public class AdminPanel extends Panel<Object> implements NavigationContainer
{

    private Component content;
    private NavigationPanel navigation;
    private String workspace;

    public String getWorkspace()
    {   
        return workspace;
    }

    public void setWorkspace(String workspace)
    {
        this.workspace = workspace;
    }

    public Navigation getNavigation()
    {
        return navigation;
    }

    private void setupNavigation()
    {
        if (navigation != null)
        {
            navigation.remove();
        }
        navigation = new NavigationPanel("navigation", workspace)
        {
            @Override
            protected void onNodeSelected(NavigationTreeNode node)
            {
                AdminPanel.this.onNodeSelected(node);
            }
        };
        add(navigation);

        NavigationTreeModel model = (NavigationTreeModel)navigation.getTree().getModel().getObject();
        TreeNode root = (TreeNode)model.getRoot();
        NavigationTreeNode node;
        if (root.getChildCount() > 0)
        {
            node = (NavigationTreeNode)root.getChildAt(0);
            navigation.selectNode(node);
        }
        else
        {
            node = null;
            onNodeSelected(node);
        }        
    }

    private void onNodeSelected(NavigationTreeNode node)
    {
        if (content != null)
        {
            content.remove();
        }
        if (node != null)
        {
            content = node.newManagePanel("content");
        }
        else
        {
            content = new WebMarkupContainer("content");
        }
        add(content);
    }

    public AdminPanel(String id, String workspace, Path root)
    {
        super(id);

        if (workspace == null) 
        {
            workspace = filterWorkspaces(Locator.getBrix().getVisibleWorkspaces()).get(0);
        }
        
        this.workspace = workspace;

        setupNavigation();

        DropDownChoice ws = new DropDownChoice("workspace", new PropertyModel(this, "workspace"),
                new LoadableDetachableModel()
                {
                    @Override
                    protected Object load()
                    {
                        return filterWorkspaces(Locator.getBrix().getVisibleWorkspaces());
                    }
                }, new IChoiceRenderer()
                {
                    public Object getDisplayValue(Object object)
                    {
                        return ((String)object).replace("^", " - ");
                    }

                    public String getIdValue(Object object, int index)
                    {
                        return "" + index;
                    }
                })
        {
            @Override
            protected boolean wantOnSelectionChangedNotifications()
            {
                return true;
            }

            @Override
            protected void onSelectionChanged(Object newSelection)
            {
                setupNavigation();
            }
        };
        ws.setNullValid(false);
        add(ws);

        add(new PublishLink("publishToStaging", Brix.STATE_STAGING, Brix.STATE_DEVELOPMENT));
        add(new PublishLink("publishToProduction", Brix.STATE_PRODUCTION, Brix.STATE_STAGING));


    }

    private class PublishLink extends Link
    {

        private final String targetState;
        private final String requiredState;

        public PublishLink(String id, String targetState, String requireState)
        {
            super(id);
            this.targetState = targetState;
            this.requiredState = requireState;
        }

        @Override
        public void onClick()
        {
            Locator.getBrix().publish(getWorkspace(), targetState,
                    BrixRequestCycle.Locator.getSessionProvider());
        }

        @Override
        public boolean isVisible()
        {
            Action action = new PublishWorkspaceActionImpl(Context.ADMINISTRATION,
                    AdminPanel.this.workspace, targetState);

            Brix brix = Locator.getBrix();

            return requiredState.equals(brix.getWorkspaceState(getWorkspace())) &&
                    brix.getAuthorizationStrategy().isActionAuthorized(action);
        }

    };

    public JcrNode getNode()
    {
        return (JcrNode)getModelObject();
    }


    protected JcrSession getJcrSession()
    {
        return BrixRequestCycle.Locator.getSession(workspace);
    }


    private List<String> filterWorkspaces(List<String> workspaces)
    {
        List<String> result = new ArrayList<String>(workspaces.size());
        for (String s : workspaces)
        {
            Action action = new WorkspaceActionImpl(Action.Context.ADMINISTRATION,
                    WorkspaceAction.Type.VIEW, s);
            if (Locator.getBrix().getAuthorizationStrategy().isActionAuthorized(action))
            {
                result.add(s);
            }
        }

        return result;
    }
}