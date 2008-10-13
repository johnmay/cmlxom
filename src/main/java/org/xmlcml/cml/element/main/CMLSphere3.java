package org.xmlcml.cml.element.main;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLElement;

/**
 * user-modifiable class supporting sphere3. * autogenerated from schema use as
 * a shell which can be edited
 *
 */
public class CMLSphere3 extends AbstractSphere3 {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;

    /**
     * contructor.
     */
    public CMLSphere3() {
    }

    /**
     * contructor.
     *
     * @param old
     */
    public CMLSphere3(CMLSphere3 old) {
        super((AbstractSphere3) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLSphere3(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLSphere3
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLSphere3();

    }
}
