/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.felix.skypealizer.page.dnd.DropLayout;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.navigator7.Page;

/**
 *
 * @author felixhusse
 */
@Page(uriName="drag")
public class DragDropPage extends VerticalLayout {



    public DragDropPage() {
        DropLayout layout = new DropLayout(true);
        layout.setSizeUndefined();
        layout.setHeight("100px");

        // Use these styles to hide irrelevant drag hints
        layout.addStyleName("no-vertical-drag-hints");
        // layout.addStyleName("no-horizontal-drag-hints");
        // layout.addStyleName("no-box-drag-hints");

        for (Component component : createComponents("TopDrop")) {
            layout.addComponent(component);
        }

        DropLayout layout2 = new DropLayout(true);
        layout2.setSizeUndefined();
        layout2.setHeight("100px");
        layout2.addStyleName("no-vertical-drag-hints");
        for (Component component : createComponents("BottomDrop")) {
            layout2.addComponent(component);
        }


        addComponent(layout);
        addComponent(layout2);
    }

    public List<Component> createComponents(String prefix) {
        List<Component> buttons = new ArrayList<Component>();

        HorizontalLayout layout1 = new HorizontalLayout();
        for (int i = 0 ; i < 2; i++) {
            Button button = new Button(prefix + " " + i);
            layout1.addComponent(button);
        }

        HorizontalLayout layout2 = new HorizontalLayout();
        for (int i = 2 ; i < 4; i++) {
            Button button = new Button(prefix + " " + i);
            layout2.addComponent(button);
        }
        buttons.add(layout1);
        buttons.add(layout2);

        return buttons;
    }


}
