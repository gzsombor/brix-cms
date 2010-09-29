package brix.plugin.menu.editor.cell;

import brix.plugin.menu.Menu;
import brix.plugin.menu.editor.ReferenceColumnPanel;
import brix.plugin.site.picker.reference.ReferenceEditorConfiguration;
import brix.web.reference.Reference;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.Arrays;

/**
 * A Panel that is used to change the type of a MenuType to any of the possible options
 *
 * Created by IntelliJ IDEA.
 * User: korbinianbachl
 * Date: 08.09.2010
 * Time: 20:55:51
 */
public abstract class SwitcherCellPanel extends Panel {


    IModel<Menu.ChildEntry.MenuType> typeModel;
    IModel<Reference> referenceModel;
    IModel<String> labelOrCodeModel;
    ReferenceEditorConfiguration conf;
    WebMarkupContainer container;

    /**
     * 
     * @param id ComponentID
     * @param typeModel Model of the MenuType
     * @param referenceModel Model of the Reference (backwards compatible)
     * @param labelOrCodeModel Model of the "Label" or "Code" String
     * @param conf  ReferenceEditorConfiguration for ReferenceEditor
     */
    public SwitcherCellPanel(String id,
                             IModel<Menu.ChildEntry.MenuType> typeModel,
                             IModel<Reference> referenceModel,
                             IModel<String> labelOrCodeModel,
                             ReferenceEditorConfiguration conf) {
        super(id);
        this.typeModel = typeModel;
        this.referenceModel = referenceModel;
        this.labelOrCodeModel = labelOrCodeModel;
        this.conf = conf;


        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.add(getEditPanel());
        add(container);


        Form form = new Form("form");
        DropDownChoice choice = new DropDownChoice<Menu.ChildEntry.MenuType>("typeChoice", typeModel, Arrays.asList(Menu.ChildEntry.MenuType.values()));
        choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                container.addOrReplace(getEditPanel());
                target.addComponent(container);
            }
        });

        form.add(choice);
        add(form);

    }

    /**
     * derives the right editPanel
     *
     * @return  a Component that is to be attached
     */
    private Component getEditPanel() {
        String id = "editPanel";
        Component returnComponent;

        if (typeModel.getObject() == Menu.ChildEntry.MenuType.REFERENCE) {

            returnComponent = new ReferenceColumnPanel(id, referenceModel) {
                @Override
                public ReferenceEditorConfiguration getConfiguration() {
                    return conf;
                }

                @Override
                protected boolean isEditing() {
                    return SwitcherCellPanel.this.isEditing();
                }
            };
        } else {
            returnComponent = new AjaxEditableMultiLineLabel<String>(id,labelOrCodeModel);             
        }
        return returnComponent;
    }


    abstract boolean isEditing();


}