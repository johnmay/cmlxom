// /*======AUTOGENERATED FROM SCHEMA; DO NOT EDIT BELOW THIS LINE ======*/
package org.xmlcml.cml.element.main;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLElement;

/**
 * A container for kpoints.
 *
 *
 * Experimental.
 *
 * user-modifiable class autogenerated from schema if no class exists use as a
 * shell which can be edited the autogeneration software will not overwrite an
 * existing class file
 *
 */
public class CMLKpointList extends AbstractKpointList {

    /**
     * must give simple documentation.
     *
     *
     */

    public CMLKpointList() {
    }

    /**
     * must give simple documentation.
     *
     * @param old
     *            CMLKpointList to copy
     *
     */

    public CMLKpointList(CMLKpointList old) {
        super((AbstractKpointList) old);
    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLKpointList(this);
    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLKpointList
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLKpointList();
    }
}
