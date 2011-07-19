/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.page.dnd;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;

/**
 *
 * @author felixhusse
 */
public class WrappedComponent extends DragAndDropWrapper {
        
    private final DropHandler dropHandler;

    public WrappedComponent(Component content, DropHandler dropHandler) {
        super(content);
        this.dropHandler = dropHandler;
        setDragStartMode(DragStartMode.WRAPPER);
    }

    @Override
    public DropHandler getDropHandler() {
        return dropHandler;
    }
}
