package org.xmlcml.cml.element.main;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Node;

import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.cml.element.lite.CMLAtom;
import org.xmlcml.cml.element.lite.CMLMolecule;
import org.xmlcml.molutil.ChemicalElement;

/**
 * user-modifiable class supporting basisSet. * autogenerated from schema use as
 * a shell which can be edited
 *
 */
public class CMLBasisSet extends AbstractBasisSet {

    /**
     * common basis sets. currently only minimal
     *
     * @author pmr
     *
     */
    public enum Basis {
        /** valence only */
        MINIMAL("minimal", "H s else s+3p");
        /** value used for comparison with document */
        public String value;

        /** description */
        public String desc;

        private Basis(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    /** molecule to which this set applies */
    CMLMolecule molecule = null;

    /** basis to which this set applies */
    Basis basis = null;

    /** atomic orbital coefficients. */
    CMLEigen coefficients = null;

    /**
     * constructor.
     */
    public CMLBasisSet() {
    }

    /**
     * constructor.
     *
     * @param old
     */
    public CMLBasisSet(CMLBasisSet old) {
        super((AbstractBasisSet) old);

    }

    /**
     * copy node .
     *
     * @return Node
     */
    public Node copy() {
        return new CMLBasisSet(this);

    }

    /**
     * create new instance in context of parent, overridable by subclasses.
     *
     * @param parent
     *            parent of element to be constructed (ignored by default)
     * @return CMLBasisSet
     */
    public CMLElement makeElementInContext(Element parent) {
        return new CMLBasisSet();

    }

    /**
     * constructor.
     *
     * @param basis
     *            to use
     * @param molecule
     *            to use
     */
    public CMLBasisSet(Basis basis, CMLMolecule molecule) {
        this.basis = basis;
        this.molecule = molecule;
    }

    /**
     * get all ABFs that contain a given l value.
     *
     * @param l
     *            the principal quantum number
     * @return all ABFs that have this value of l
     */
    public List<CMLAtomicBasisFunction> getABFsByL(int l) {
        List<CMLAtomicBasisFunction> abfList = new ArrayList<CMLAtomicBasisFunction>();
        CMLElements<CMLAtomicBasisFunction> abChildList = this
                .getAtomicBasisFunctionElements();
        for (CMLAtomicBasisFunction abf : abChildList) {
            if (abf.getLAttribute() != null && abf.getL() == l) {
                abfList.add(abf);
            }
        }
        return abfList;
    }

    /**
     * get all ABFs that contain a given m value.
     *
     * @param m
     *            quantum number
     * @return all ABFs that have this value of m
     */
    public List<CMLAtomicBasisFunction> getABFsByM(int m) {
        List<CMLAtomicBasisFunction> abfList = new ArrayList<CMLAtomicBasisFunction>();
        CMLElements<CMLAtomicBasisFunction> abChildList = this
                .getAtomicBasisFunctionElements();
        for (CMLAtomicBasisFunction abf : abChildList) {
            if (abf.getMAttribute() != null && abf.getM() == m) {
                abfList.add(abf);
            }
        }
        return abfList;
    }

    /**
     * get all ABFs that contain a given n value.
     *
     * @param n
     *            quantum number
     * @return all ABFs that have this value of n
     */
    public List<CMLAtomicBasisFunction> getABFsByN(int n) {
        List<CMLAtomicBasisFunction> abfList = new ArrayList<CMLAtomicBasisFunction>();
        CMLElements<CMLAtomicBasisFunction> abChildList = this
                .getAtomicBasisFunctionElements();
        for (CMLAtomicBasisFunction abf : abChildList) {
            if (abf.getNAttribute() != null && abf.getN() == n) {
                abfList.add(abf);
            }
        }
        return abfList;
    }

    /**
     * get all ABFs that contain a given lm value.
     *
     * @param lm
     *            quantum number
     * @return all ABFs that have this value of lm (empty if lm is null)
     */
    public List<CMLAtomicBasisFunction> getABFsByLM(String lm) {
        List<CMLAtomicBasisFunction> abfList = new ArrayList<CMLAtomicBasisFunction>();
        CMLElements<CMLAtomicBasisFunction> abChildList = this
                .getAtomicBasisFunctionElements();
        for (CMLAtomicBasisFunction abf : abChildList) {
            if (lm.equals(abf.getLm())) {
                abfList.add(abf);
            }
        }
        return abfList;
    }

    /**
     * get all ABFs that contain a given symbol.
     *
     * @param symbol
     * @return all ABFs that have this symbol (empty if symbol is null)
     */
    public List<CMLAtomicBasisFunction> getABFsBySymbol(String symbol) {
        List<CMLAtomicBasisFunction> abfList = new ArrayList<CMLAtomicBasisFunction>();
        if (symbol != null) {
            CMLElements<CMLAtomicBasisFunction> abChildList = this
                    .getAtomicBasisFunctionElements();
            for (CMLAtomicBasisFunction abf : abChildList) {
                if (symbol.equals(abf.getSymbol())) {
                    abfList.add(abf);
                }
            }
        }
        return abfList;
    }

    /**
     * sets molecule. required if atomic orbitals are required for each atom
     *
     * @param mol
     *            molecule corresponding to
     */
    public void setMolecule(CMLMolecule mol) {
        this.molecule = mol;
    }

    /**
     * sets basis. required if calculating electrons
     *
     * @param basis
     */
    public void setBasis(Basis basis) {
        this.basis = basis;
    }

    /**
     * set atomic orbital coefficients.
     *
     * @param coefficients
     * @throws CMLException
     *             wrong size
     */
    public void setMolecularOrbitalCoefficients(CMLEigen coefficients)
            {
        if (this.getAtomicBasisFunctionElements().size() != coefficients
                .getSize()) {
            throw new RuntimeException("Number of orbitals ("
                    + this.getAtomicBasisFunctionElements().size()
                    + ") inconsistent with size of matrix ("
                    + coefficients.getSize() + S_RBRAK);
        }
        this.coefficients = coefficients;
    }

    /**
     * create basis set. requires molecule to have been set
     *
     * @return basisSet of null if molecule not set
     */
    CMLBasisSet createBasisSet() {
        CMLBasisSet basisSet = null;
        if (molecule != null && basis != null) {
            basisSet = new CMLBasisSet();
            List<CMLAtom> atoms = molecule.getAtoms();
            if (basis.equals(Basis.MINIMAL)) {
                for (CMLAtom atom : atoms) {
                    List<CMLAtomicBasisFunction> abfList = CMLAtomicBasisFunction
                            .getABFList(atom, basis);
                    for (CMLAtomicBasisFunction abf : abfList) {
                        basisSet.addAtomicBasisFunction(abf);
                    }
                }
            } else {
                throw new RuntimeException("Unsupported basis set " + basis.value);
            }
        }
        return basisSet;
    }

    /**
     * generates the number of electrons. requires molecule to have been set
     * uses the basis set to count the electrons. Thus a minimal basis set for
     * the first 2 rows will count just the valence electrond (H=1, Li=1, ...
     * F=7) and adjust for overall charge.
     *
     * @throws CMLException
     *             unsupported basis
     * @return electron count or 0 if molecule or basis not set
     */
    public int getElectronCount() {
        int count = 0;
        if (molecule != null && basis != null) {
            List<CMLAtom> atoms = molecule.getAtoms();
            for (CMLAtom atom : atoms) {
                if (basis.equals(Basis.MINIMAL)) {
                    ChemicalElement element = atom.getChemicalElement();
                    int nValence = element.getValenceElectrons();
                    count += nValence;
                } else {
                    throw new RuntimeException("Basis set not supported: " + basis);
                }
            }
            if (molecule.getFormalChargeAttribute() != null) {
                count -= molecule.getFormalCharge();
            }
        }
        return count;
    }

    /**
     * simple string representation. concatenates basis and then lists ABFs
     *
     * @return string
     */
    public String getString() {
        StringBuffer sb = new StringBuffer("basis: " + basis + "\n");
        CMLElements<CMLAtomicBasisFunction> abfList = this
                .getAtomicBasisFunctionElements();
        for (int i = 0; i < abfList.size(); i++) {
            sb.append(abfList.get(i).getString());
            if (i < abfList.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
