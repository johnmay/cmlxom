package org.xmlcml.cml.element.main;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLElement;

/**
 * user-modifiable class supporting atomType. * autogenerated from schema use as
 * a shell which can be edited
 *
 */
public class CMLAtomType extends AbstractAtomType {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;
    /**
     * constructor.
     */
    public CMLAtomType() {
    }

    /**
     * constructor.
     *
     * @param old
     */
    public CMLAtomType(CMLAtomType old) {
        super((AbstractAtomType) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLAtomType(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLAtomType
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLAtomType();

    }
}
