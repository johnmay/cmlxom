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
import nu.xom.Node;
import nu.xom.Nodes;

import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.euclid.Point3;

/**
 * user-modifiable class supporting symmetry. * autogenerated from schema use as
 * a shell which can be edited
 * 
 */
public class CMLSymmetry extends AbstractSymmetry {

	/** namespaced element name.*/
	public final static String NS = C_E+TAG;
	
    /**
     * constructor.
     */
    public CMLSymmetry() {
    }

    /**
     * constructor.
     * 
     * @param old
     */
    public CMLSymmetry(CMLSymmetry old) {
        super((AbstractSymmetry) old);

    }

    /**
     * copy node .
     * 
     * @return Node
     */
    public Element copy() {
        return new CMLSymmetry(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     * 
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLSymmetry
     */
    public CMLSymmetry makeElementInContext(Element parent) {
        return new CMLSymmetry();

    }

    /**
     * create from crystallographic operators. example: "x, y, z", "-x+1/2, -y,
     * z+1/2", "x+1/2, -y+1/2, -z", "-x, y+1/2, -z+1/2", "-x, -y, -z", "x-1/2,
     * y, -z-1/2", "-x-1/2, y-1/2, z", "x, -y-1/2, z-1/2", for spacegroup Pbca
     * 
     * @param xyz
     *            the strings
     */
    public CMLSymmetry(String[] xyz) {
        this();
        for (String s : xyz) {
            CMLTransform3 t = new CMLTransform3(s);
            this.addTransform3(t);
        }
    }

    /**
     * create from list of transforms. does not check any validity (e.g. may not
     * be a group)
     * 
     * @param trList transforms to add
     */
    public CMLSymmetry(List<CMLTransform3> trList) {
        this();
        for (CMLTransform3 tr : trList) {
            this.addTransform3(tr);
        }
    }

    /**
     * create from list of transforms. does not check any validity (e.g. may not
     * be a group)
     * 
     * @param trStringList transforms to add
     * @return the symmetry operations
     */
    public static CMLSymmetry createFromXYZStrings(List<String> trStringList) {
        List<CMLTransform3> trList = new ArrayList<CMLTransform3>();
        for (String trString : trStringList) {
            trList.add(new CMLTransform3(trString));
        }
        return new CMLSymmetry(trList);
    }

    /**
     * creates a new symmetry as the product of two others. combines each
     * element of this with each of sym and elimnates duplicates brute force -
     * multiplying two cubic space groups might take a little time
     * 
     * @param sym
     * @return CMLSymmetry contains CMLTransform for
     *         this.t[i].concatenate(sym.t[j])
     */
    public CMLSymmetry convolute(CMLSymmetry sym) {
        List<CMLTransform3> trList = new ArrayList<CMLTransform3>();
        for (CMLTransform3 tr2 : this.getTransform3Elements()) {
            for (CMLTransform3 tr3 : sym.getTransform3Elements()) {
                CMLTransform3 trNew = tr2.concatenate(tr3);
                if (CMLTransform3.indexOf(trList, trNew, EPS) == -1) {
                    trList.add(trNew);
                }
            }
        }
        return new CMLSymmetry(trList);
    }

    /**
     * are two symmetrys equal. 
     * iterates through all elements in case order varies. 
     * 
     * @param sym to compare
     * @param eps max deviation of elements
     * @return true if equal
     */
    public boolean isEqualTo(CMLSymmetry sym, double eps) {
    	// first check in case they are lexically equivalent
    	boolean equals = false;
//        boolean equals = CMLUtil.equalsCanonically(this, sym, true);
//        if (!equals) {
	        CMLElements<CMLTransform3> thisTrs = this.getTransform3Elements();
	        CMLElements<CMLTransform3> symTrs = sym.getTransform3Elements();
	        if (thisTrs.size() == symTrs.size()) {
	            for (int i = 0; i < thisTrs.size(); i++) {
	            	equals = false;
	                for (int j = 0; j < symTrs.size(); j++) {
	                    if (thisTrs.get(i).isEqualTo(symTrs.get(j), eps)) {
	                        equals = true;
	                        break;
	                    }
	                }
	                if (!equals) {
	                	break;
	                }
	            }
	        }
//        }
        return equals;
    }

    /** normalizes all transform3 children. see
     * CMLTransform.normalizeCrystallographically()
     * 
     */
    public void normalizeCrystallographically() {
        for (CMLTransform3 t : this.getTransform3Elements()) {
            t.normalizeCrystallographically();
        }
    }

    /**
     * gets all symmetry operations that do not have translations. uses
     * CMLTransform3.hasNonZeroTranslationComponent the result may or may not be
     * a group or spacegroup but will (I think) be group generators
     * 
     * @return the symmetry
     */
    public CMLSymmetry getNonTranslations() {
        CMLSymmetry newSymmetry = new CMLSymmetry();
        CMLElements<CMLTransform3> symmetryElements = 
            this.getTransform3Elements();
        for (CMLTransform3 tr : symmetryElements) {
            if (!tr.hasNonZeroTranslationComponent() && !tr.isUnit()) {
                newSymmetry.addTransform3(new CMLTransform3(tr));
            }
        }
        return newSymmetry;
    }

    /** gets all symmetry operations that have pure translations. uses
     * CMLTransform3.isPureTranslation does not include the identity operation
     * the result should provide the centering for the cell.
     * 
     * @return the symmetry
     */
    public List<CMLTransform3> getPureTranslations() {
        List<CMLTransform3> translations = new ArrayList<CMLTransform3>();
        CMLElements<CMLTransform3> symmetryElements = this
                .getTransform3Elements();
        for (CMLTransform3 tr : symmetryElements) {
            if (tr.isPureTranslation()) {
                translations.add(new CMLTransform3(tr));
            }
        }
        return translations;
    }

    /**
     * gets centring from symmetry operators.
     * 
     * @return centering or UNKNOWN
     */
    public CMLCrystal.Centering getCentering() {
        return CMLSymmetry.getCentering(this.getPureTranslations());
    }

    /**
     * matches list of translations into centering type.
     * 
     * @param transformList
     * @return centering
     */
    static CMLCrystal.Centering getCentering(List<CMLTransform3> transformList) {
        CMLCrystal.Centering centering = CMLCrystal.Centering.P;
        if (transformList.size() == 0) {
        } else if (transformList.size() == 1) {
            if (transformList.get(0).isEqualTo(
                    CMLCrystal.Centering.A.translations.get(0))) {
                centering = CMLCrystal.Centering.A;
            } else if (transformList.get(0).isEqualTo(
                    CMLCrystal.Centering.B.translations.get(0))) {
                centering = CMLCrystal.Centering.B;
            } else if (transformList.get(0).isEqualTo(
                    CMLCrystal.Centering.C.translations.get(0))) {
                centering = CMLCrystal.Centering.C;
            } else if (transformList.get(0).isEqualTo(
                    CMLCrystal.Centering.I.translations.get(0))) {
                centering = CMLCrystal.Centering.I;
            } else {
                centering = CMLCrystal.Centering.UNKNOWN;
            }
        } else if (transformList.size() == 2) {
            centering = CMLCrystal.Centering.R;
            for (CMLTransform3 transform : transformList) {
                if (transform.isEqualTo(CMLCrystal.Centering.R.translations
                        .get(0))
                        || transform
                                .isEqualTo(CMLCrystal.Centering.R.translations
                                        .get(1))) {
                } else {
                    centering = CMLCrystal.Centering.UNKNOWN;
                    break;
                }
            }
        } else if (transformList.size() == 3) {
            centering = CMLCrystal.Centering.F;
            for (CMLTransform3 transform : transformList) {
                if (transform.isEqualTo(CMLCrystal.Centering.F.translations
                        .get(0))
                        || transform
                                .isEqualTo(CMLCrystal.Centering.F.translations
                                        .get(1))
                        || transform
                                .isEqualTo(CMLCrystal.Centering.F.translations
                                        .get(2))) {
                } else {
                    centering = CMLCrystal.Centering.UNKNOWN;
                    break;
                }
            }
        }
        return centering;
    }

    /**
     * do the elements form a group. translation is included but not normalized
     * (see isSpaceGroup()) iterates through all n^2 combinations (a*b and b*a)
     * and tests them against group members. therefore O(n^3) and expensive for
     * (say) spacegroups with 192 elements
     * 
     * @return true if group
     */
    public boolean isGroup() {
        this.normalizeCrystallographically();
        CMLElements<CMLTransform3> transform3s = this.getTransform3Elements();
        int i = 0;
        boolean group = false;
        for (CMLTransform3 tr1 : transform3s) {
            int j = 0;
            for (CMLTransform3 tr2 : transform3s) {
                CMLTransform3 tr3 = tr1.concatenate(tr2);
                group = false;
                for (CMLTransform3 tr : transform3s) {
                    if (tr.isEqualTo(tr3, EPS)) {
                        group = true;
                        break;
                    }
                }
                if (!group) {
                    // LOG.debug("operators "+i+" and "+j+" do not
                    // generate a group element: ");
                    break;
                }
                j++;
            }
            i++;
        }
        return group;
    }

    /**
     * do the elements form a spacegroup. operates on a cpoy so does not
     * normalize this translation is normalized to range 0 -> 1-eps iterates
     * through all n^2 combinations (a*b and b*a) and tests them against group
     * members
     * 
     * @return true if group
     */
    public boolean isSpaceGroup() {
        CMLSymmetry symmetryCopy = new CMLSymmetry(this);
        symmetryCopy.normalizeCrystallographically();
        CMLElements<CMLTransform3> transform3s = symmetryCopy
                .getTransform3Elements();
        int i = 0;
        boolean group = false;
        for (CMLTransform3 tr1 : transform3s) {
            int j = 0;
            for (CMLTransform3 tr2 : transform3s) {
                CMLTransform3 tr3 = tr1.concatenate(tr2);
                tr3.normalizeCrystallographically();
                group = false;
                for (CMLTransform3 tr : transform3s) {
                    if (tr.isEqualTo(tr3, EPS)) {
                        group = true;
                        break;
                    }
                }
                if (!group) {
                    // LOG.debug("operators "+i+" and "+j+" do not generate a group element: ");
                    break;
                }
                j++;
            }
            i++;
        }
        return group;
    }

    /**
     * gets count of operations which transform point into itself. applies all
     * opertions to point and count which transform point into itself WITHOUT
     * crystallographic normalisation
     * 
     * @param point
     * @param eps
     *            tolerance for agreement
     * @return the multiplicity
     */
    public int getPointGroupMultiplicity(Point3 point, double eps) {
        int operatorCount = 0;
        CMLElements<CMLTransform3> transforms = this.getTransform3Elements();
        for (CMLTransform3 tr : transforms) {
            Point3 newPoint = tr.transform(point);
            if (newPoint.isEqualTo(point, eps)) {
                operatorCount++;
            }
        }
        return operatorCount;
    }

    /** gets count of operations which transform point into itself. applies all
     * operations to point and count which transform point into itself WITH
     * crystallographic normalisation
     * 
     * @param point
     * @return multiplicity
     */
    public int getSpaceGroupMultiplicity(Point3 point) {
        int operatorCount = 0;
        if (point != null) {
            CMLElements<CMLTransform3> transforms = 
                this.getTransform3Elements();
            for (CMLTransform3 tr : transforms) {
                Point3 newPoint = tr.transform(point);
                if (newPoint.equalsCrystallographically(point)) {
                    operatorCount++;
                }
            }
        }
        return operatorCount;
    }

    /** convenience method to get a single symmetry descendant of an element.
     * 
     * @param element to search under
     * @return the symmetry
     * @throws RuntimeException if 0 or >1 nodes
     */
    //TODO should this really call a RunTimeException?
    public static CMLSymmetry getContainedSymmetry(CMLElement element) throws RuntimeException {
        Nodes symmetryNodes = element.query(".//"+CMLSymmetry.NS, CMLConstants.CML_XPATH);
        if (symmetryNodes.size() == 0) {
            throw new RuntimeException("NO <symmetry> FOUND");
        } else if (symmetryNodes.size() > 1) {
            throw new RuntimeException("TOO MANY <symmetry> FOUND "+symmetryNodes.size());
        }
        return (CMLSymmetry) symmetryNodes.get(0);
    }

    
}
