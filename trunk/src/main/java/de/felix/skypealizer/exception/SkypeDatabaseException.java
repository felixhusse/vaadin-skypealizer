/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.felix.skypealizer.exception;

/**
 *
 * @author felixhusse
 */
public class SkypeDatabaseException extends Exception {

    /**
     * Creates a new instance of <code>SkypeDatabaseException</code> without detail message.
     */
    public SkypeDatabaseException() {
    }


    /**
     * Constructs an instance of <code>SkypeDatabaseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SkypeDatabaseException(String msg) {
        super(msg);
    }
}
