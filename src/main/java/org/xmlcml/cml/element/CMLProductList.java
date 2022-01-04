/**
 *    Copyright 2011 Peter Murray-Rust et. al.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.xmlcml.cml.element;

import java.util.List;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLElement;

/**
 * user-modifiable class supporting productList. * autogenerated from schema use
 * as a shell which can be edited
 *
 */
public class CMLProductList extends AbstractProductList implements
        ReactionComponent {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;

    /**
     * constructor.
     */
    public CMLProductList() {
    }

    /**
     * constructor.
     *
     * @param old
     */
    public CMLProductList(CMLProductList old) {
        super((AbstractProductList) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Element copy() {
        return new CMLProductList(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLProductList
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLProductList();

    }

    /**
     * get all descendant atoms.
     *
     * @return list of descendant atoms
     */
    public List<CMLAtom> getAtoms() {
        return CMLReaction.getAtoms(this);
    }

    /**
     * get all descendant bonds.
     *
     * @return list of descendant bonds
     */
    public List<CMLBond> getBonds() {
        return CMLReaction.getBonds(this);
    }

    /**
     * get all descendant formulas.
     *
     * @return list of descendant formulas
     */
    public List<CMLFormula> getFormulas() {
        return CMLReaction.getFormulas(this);
    }

    /**
     * get all descendant molecules.
     *
     * @return list of descendant molecules
     */
    public List<CMLMolecule> getMolecules() {
        return CMLReaction.getMolecules(this);
    }

    /**
     * gets descendant reactionComponents. note that this will return all
     * containers as well as contained. thus calling this on: {@code <reaction>
     * <reactantList> <reactant/> </reactantList> </reaction>} will return 2
     * components, reactantList, followed by reactant.
     *
     * @return empty if no components (some components such as CMLProduct will
     *         always return this)
     */
    public List<ReactionComponent> getReactionComponentDescendants() {
        return CMLReaction.getReactionComponentDescendants(this, true);
    }

    /**
     * gets child reactionComponents. note that this will return containers but
     * not their contents. thus calling this on: {@code <reaction> <reactantList>
     * <reactant/> </reactantList> </reaction>} will return 1 components,
     * reactantList.
     *
     * @return empty if no components (some components such as CMLProduct will
     *         always return this)
     */
    public List<ReactionComponent> getReactionComponentChildren() {
        return CMLReaction.getReactionComponentDescendants(this, false);
    }

}
