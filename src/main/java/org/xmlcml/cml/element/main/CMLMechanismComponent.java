package org.xmlcml.cml.element.main;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLElement;

/**
 * user-modifiable class supporting mechanismComponent. * autogenerated from
 * schema use as a shell which can be edited
 *
 */
public class CMLMechanismComponent extends AbstractMechanismComponent {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;

    /**
     * constructor.
     */
    public CMLMechanismComponent() {
    }

    /**
     * constructor.
     *
     * @param old
     */
    public CMLMechanismComponent(CMLMechanismComponent old) {
        super((AbstractMechanismComponent) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLMechanismComponent(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLMechanismComponent
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLMechanismComponent();

    }
}
