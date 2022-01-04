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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;

import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLElements;

/**
 * user-modifiable class supporting spectator. * autogenerated from schema use
 * as a shell which can be edited
 *
 */
public class CMLSpectator extends AbstractSpectator implements ReactionComponent {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;

    /**
     * contructor.
     */
    public CMLSpectator() {
    }

    /**
     * contructor.
     *
     * @param old
     */
    public CMLSpectator(CMLSpectator old) {
        super((AbstractSpectator) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Element copy() {
        return new CMLSpectator(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLSpectator
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLSpectator();

    }

//    /**
//     * gets formula for cmlSpectator.
//     *
//     * If cmlSpectator has a child formula, returns it, else returns the formula
//     * on molecules, else null. Holds result in CMLFormula.
//     *
//     * @return the cmlSpectator stoichiometry
//     */
//    public CMLFormula getFormula() {
//        CMLFormula formula = null;
//        CMLMolecule molecule = (CMLMolecule) this
//                .getFirstCMLChild(CMLMolecule.TAG);
//        if (molecule != null) {
//            formula = (CMLFormula) molecule.getFirstCMLChild("formula");
//            if (formula == null) {
//                formula = molecule.calculateFormula(
//                    CMLMolecule.HydrogenControl.USE_EXPLICIT_HYDROGENS);
//            }
//        }
//        return formula;
//    }

    /**
     * merge child molecules.
     *
     * A reaction may initally be created with the same cmlSpectator on both
     * sides (such as pseudo-reactant and pseudo-product). A true cmlSpectator
     * will remain unchanged in most respects (e.g. formalCharge, bondOrders)
     * but could have changes in 2D or 3D coordinates. This routine will merge
     * two such molecules into a normalized form. Thus:
     *
     * <pre>
     *      &lt;cmlSpectator&gt;
     *      &lt;label dictRef=&quot;macie:sideChain&quot; value=&quot;B-Glu147&quot;/&gt;
     *      &lt;label dictRef=&quot;macie:hba&quot; value=&quot;B-Glu147&quot;/&gt;
     *      &lt;molecule id=&quot;0001.stg02.r.5&quot;&gt;
     *      &lt;atomArray&gt;
     *      &lt;atom id=&quot;a16&quot; elementType=&quot;C&quot; x2=&quot;-1.212&quot; y2=&quot;0.6047&quot;/&gt;
     *      &lt;atom id=&quot;a17&quot; elementType=&quot;O&quot; x2=&quot;-1.5775&quot; y2=&quot;1.1841&quot;/&gt;
     *      &lt;atom id=&quot;a18&quot; elementType=&quot;O&quot; x2=&quot;-1.5976&quot; y2=&quot;0.1057&quot;/&gt;
     *      &lt;atom id=&quot;a38&quot; elementType=&quot;R&quot; x2=&quot;-0.4945&quot; y2=&quot;0.5889&quot;&gt;
     *      &lt;label value=&quot;B-Glu147&quot;/&gt;
     *      &lt;/atom&gt;
     *      &lt;atom id=&quot;a19&quot; elementType=&quot;H&quot; x2=&quot;-1.8016038&quot; y2=&quot;-0.15829869&quot;/&gt;
     *      &lt;/atomArray&gt;
     *      &lt;bondArray id=&quot;b6&quot;&gt;
     *      &lt;bond atomRefs2=&quot;a16 a17&quot; order=&quot;D&quot; id=&quot;b2&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a16 a18&quot; order=&quot;S&quot; id=&quot;b3&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a38 a16&quot; order=&quot;S&quot; id=&quot;b4&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a18 a19&quot; order=&quot;S&quot; id=&quot;b6&quot;/&gt;
     *      &lt;/bondArray&gt;
     *      &lt;/molecule&gt;
     *      &lt;molecule id=&quot;0001.stg02.p.5&quot;&gt;
     *      &lt;atomArray&gt;
     *      &lt;atom id=&quot;a17&quot; elementType=&quot;C&quot; x2=&quot;11.6172&quot; y2=&quot;0.5839&quot;/&gt;
     *      &lt;atom id=&quot;a18&quot; elementType=&quot;O&quot; x2=&quot;11.2559&quot; y2=&quot;1.1633&quot;/&gt;
     *      &lt;atom id=&quot;a19&quot; elementType=&quot;O&quot; x2=&quot;11.2358&quot; y2=&quot;0.0849&quot;/&gt;
     *      &lt;atom id=&quot;a38&quot; elementType=&quot;R&quot; x2=&quot;12.3346&quot; y2=&quot;0.5681&quot;&gt;
     *      &lt;label value=&quot;B-Glu147&quot;/&gt;
     *      &lt;/atom&gt;
     *      &lt;atom id=&quot;a20&quot; elementType=&quot;H&quot; x2=&quot;11.033881&quot; y2=&quot;-0.17927846&quot;/&gt;
     *      &lt;/atomArray&gt;
     *      &lt;bondArray id=&quot;b8&quot;&gt;
     *      &lt;bond atomRefs2=&quot;a17 a18&quot; order=&quot;D&quot; id=&quot;b5&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a17 a19&quot; order=&quot;S&quot; id=&quot;b6&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a38 a17&quot; order=&quot;S&quot; id=&quot;b7&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a19 a20&quot; order=&quot;S&quot; id=&quot;b8&quot;/&gt;
     *      &lt;/bondArray&gt;
     *      &lt;/molecule&gt;
     *      &lt;/cmlSpectator&gt;
     *
     * </pre>
     *
     * would be changed to:
     *
     * <pre>
     *      &lt;cmlSpectator&gt;
     *      &lt;label dictRef=&quot;macie:sideChain&quot; value=&quot;B-Glu147&quot;/&gt;
     *      &lt;label dictRef=&quot;macie:hba&quot; value=&quot;B-Glu147&quot;/&gt;
     *      &lt;molecule id=&quot;0001.stg02.r.5&quot;&gt;
     *      &lt;atomArray&gt;
     *      &lt;atom id=&quot;a16&quot; elementType=&quot;C&quot; x2=&quot;-1.212&quot; y2=&quot;0.6047&quot;/&gt;
     *      &lt;atom id=&quot;a17&quot; elementType=&quot;O&quot; x2=&quot;-1.5775&quot; y2=&quot;1.1841&quot;/&gt;
     *      &lt;atom id=&quot;a18&quot; elementType=&quot;O&quot; x2=&quot;-1.5976&quot; y2=&quot;0.1057&quot;/&gt;
     *      &lt;atom id=&quot;a38&quot; elementType=&quot;R&quot; x2=&quot;-0.4945&quot; y2=&quot;0.5889&quot;&gt;
     *      &lt;label value=&quot;B-Glu147&quot;/&gt;
     *      &lt;/atom&gt;
     *      &lt;atom id=&quot;a19&quot; elementType=&quot;H&quot; x2=&quot;-1.8016038&quot; y2=&quot;-0.15829869&quot;/&gt;
     *      &lt;/atomArray&gt;
     *      &lt;bondArray id=&quot;b6&quot;&gt;
     *      &lt;bond atomRefs2=&quot;a16 a17&quot; order=&quot;D&quot; id=&quot;b2&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a16 a18&quot; order=&quot;S&quot; id=&quot;b3&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a38 a16&quot; order=&quot;S&quot; id=&quot;b4&quot;/&gt;
     *      &lt;bond atomRefs2=&quot;a18 a19&quot; order=&quot;S&quot; id=&quot;b6&quot;/&gt;
     *      &lt;/bondArray&gt;
     *      &lt;/molecule&gt;
     *      &lt;molecule dx2=&quot;12.82&quot; dy2=&quot;-0.02&quot;/&gt;
     *      &lt;/cmlSpectator&gt;
     * </pre>
     *
     * note that the atomIds must match, but that the bondIds need not and are
     * replaced by the first set of bondIds.
     *
     * The method requires exactly two child molecules, with identical atom and
     * bond information (number, attributes/children, but not order). Bonds are
     * identified by their atomRefs, not their bondIDs.
     *
     * @throws RuntimeException
     *             molecules cannot be matched
     */
    public void mergeChildMolecules() {
        Elements molecules = this.getChildCMLElements(CMLMolecule.TAG);
        if (molecules.size() != 2) {
            throw new RuntimeException(
                    "mergeChildMolecules requires exactly two child molecules");
        }
        @SuppressWarnings("unused")
        CMLMolecule molecule0 = (CMLMolecule) molecules.get(0);
        @SuppressWarnings("unused")
        CMLMolecule molecule1 = (CMLMolecule) molecules.get(1);
        // molecule1.setIgnoreBondIDs(); this doesnt do anything
//        molecule0.mustEqual(molecule1);
    }

    /**
     * moves any pair of reactant and product with label to be child of
     * cmlSpectator. both reactant and product mut have at least one label
     * identical to the labelRef in the cmlSpectator
     *
     * @param reactProd
     *            reactant or product to be moved
     * @param ref
     *            labelRef (probably from this)
     * @throws RuntimeException
     */
    public void moveLabelledReactantsProducts(Elements reactProd, String ref) {
        boolean matched = false;
        if (reactProd != null) {
            for (int j = 0; j < reactProd.size(); j++) {
                Elements labels = ((CMLElement) reactProd.get(j))
                        .getChildCMLElements(CMLLabel.TAG);
                for (int k = 0; k < labels.size(); k++) {
                    CMLLabel label = (CMLLabel) labels.get(k);
                    if (label.getValue().equals(ref)) {
                        CMLMolecule molecule = (CMLMolecule) ((CMLElement) reactProd
                                .get(j)).getFirstCMLChild(CMLMolecule.TAG);
                        try {
                            reactProd.get(j).detach();
                            this.appendChild(molecule);
                            matched = true;
                        } catch (NullPointerException e) {
                            throw new RuntimeException("BUG " + e);
                        }
                        break;
                    }
                }
                if (matched) {
                    break;
                }
            }
        }
        if (!matched) {
            // Cannot find cmlSpectator "+ref+" for
            // "+((CMLReaction)cmlSpectator.getParentNode().getParentNode()).getId());
        }
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
     * gets descendant reactionComponents. only for interface compatibility
     *
     * @return always empty list
     */
    public List<ReactionComponent> getReactionComponentDescendants() {
        return new ArrayList<ReactionComponent>();
    }

    /**
     * gets child reactionComponents. only for interface compatibility
     *
     * @return always empty list
     */
    public List<ReactionComponent> getReactionComponentChildren() {
        return new ArrayList<ReactionComponent>();
    }

	public CMLMolecule getMolecule() {
		Nodes molecules = this.query("./*[local-name()='"+CMLMolecule.TAG+"']");
		return (molecules.size() == 1) ? (CMLMolecule) molecules.get(0) : null;
	}

   public void addAmount(AbstractAmount amount) {
        amount.detach();
        this.appendChild(amount);
    }

   public CMLElements<CMLAmount> getAmountElements() {
        Elements elements = this.getChildElements("amount", CMLConstants.CML_NS);
        return new CMLElements<CMLAmount>(elements);
    }
}
