/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page.dnd;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author felixhusse
 */
public class DropLayout extends CustomComponent {

    private AbstractOrderedLayout layout;
    private boolean horizontal;
    private DropHandler dropHandler;

    public DropLayout(boolean horizontal) {
        this.horizontal = horizontal;
        if (horizontal) {
            layout = new HorizontalLayout();
        } else {
            layout = new VerticalLayout();
        }
        dropHandler = new ReorderDropHandler(layout);

        DragAndDropWrapper pane = new DragAndDropWrapper(layout);
        this.setCompositionRoot(pane);
    }

    @Override
    public void addComponent(Component component) {
        WrappedComponent wrapper = new WrappedComponent(component,
                dropHandler);
        wrapper.setSizeUndefined();
        if (horizontal) {
            component.setHeight("100%");
            wrapper.setHeight("100%");
        } else {
            component.setWidth("100%");
            wrapper.setWidth("100%");
        }
        layout.addComponent(wrapper);
    }
}
