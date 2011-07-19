/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page.dnd;

import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.terminal.gwt.client.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import java.util.Iterator;

/**
 *
 * @author felixhusse
 */
public class ReorderDropHandler implements DropHandler {

    private AbstractOrderedLayout layout;

    public ReorderDropHandler(AbstractOrderedLayout layout) {
        this.layout = layout;
    }

    @Override
    public void drop(DragAndDropEvent event) {
        Transferable transferable = event.getTransferable();
        Component sourceComponent = transferable.getSourceComponent();
        if (sourceComponent instanceof WrappedComponent) {
            TargetDetails dropTargetData = event.getTargetDetails();
            DropTarget target = dropTargetData.getTarget();

            // find the location where to move the dragged component
            boolean sourceWasAfterTarget = true;
            int index = 0;
            Iterator<Component> componentIterator = layout
                    .getComponentIterator();
            Component next = null;
            while (next != target && componentIterator.hasNext()) {
                next = componentIterator.next();
                if (next != sourceComponent) {
                    index++;
                } else {
                    sourceWasAfterTarget = false;
                }
            }
            if (next == null || next != target) {
                // component not found - if dragging from another layout
                return;
            }

            // drop on top of target?
            if (dropTargetData.getData("horizontalLocation").equals(
                    HorizontalDropLocation.CENTER.toString())) {
                if (sourceWasAfterTarget) {
                    index--;
                }
            }

            // drop before the target?
            else if (dropTargetData.getData("horizontalLocation").equals(
                    HorizontalDropLocation.LEFT.toString())) {
                index--;
                if (index < 0) {
                    index = 0;
                }
            }

            // move component within the layout
            layout.removeComponent(sourceComponent);
            layout.addComponent(sourceComponent, index);
        }
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return new Not(SourceIsTarget.get());
    }

}
