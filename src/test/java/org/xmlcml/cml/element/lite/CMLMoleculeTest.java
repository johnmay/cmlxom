package org.xmlcml.cml.element.lite;

import static org.xmlcml.cml.base.BaseTest.parseValidString;
import static org.xmlcml.cml.base.CMLConstants.CML_XMLNS;
import static org.xmlcml.euclid.EuclidConstants.EPS;
import static org.xmlcml.euclid.test.EuclidTestBase.neverThrow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.cml.attribute.IdAttribute;
import org.xmlcml.cml.base.BaseTest;
import org.xmlcml.cml.base.CMLAttribute;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.cml.base.CMLUtil;
import org.xmlcml.cml.base.CMLElement.CoordinateType;
import org.xmlcml.cml.base.CMLElement.FormalChargeControl;
import org.xmlcml.cml.element.main.MoleculeAtomBondBase;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Vector;

/**
 * test CMLMolecule.
 * 
 * @author pmr
 * 
 */
public class CMLMoleculeTest extends MoleculeAtomBondBase {

	/**
	 * setup.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * compare two molecules. ignore whitespace nodes in either.
	 * 
	 * @param mol
	 *            to compare
	 * @param filename
	 *            containing molecule as root element
	 */
	public static void assertEqualsCanonically(CMLMolecule mol, String filename) {
		CMLMolecule mol1 = null;
		try {
			mol1 = (CMLMolecule) new CMLBuilder().build(new File(filename))
					.getRootElement();
		} catch (Exception e) {
			neverThrow(e);
		}
		assertEqualsCanonically(mol, mol1);
	}

	/**
	 * compare two molecules. ignore whitespace nodes in either.
	 * 
	 * @param mol
	 *            to compare
	 * @param mol1
	 *            other molecule
	 */
	public static void assertEqualsCanonically(CMLMolecule mol, CMLMolecule mol1) {
		mol = new CMLMolecule(mol);
		CMLUtil.removeWhitespaceNodes(mol);
		mol1 = new CMLMolecule(mol1);
		CMLUtil.removeWhitespaceNodes(mol1);
		String molS = mol.getCanonicalString();
		String mol1S = mol1.getCanonicalString();
		Assert.assertEquals("MOLECUL equality: ", molS, mol1S);
		BaseTest.assertEqualsCanonically("molecule equality", mol, mol1);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.addAtom(CMLAtom)'
	 */
	@Test
	public void testAddAtom() {
		makeMol1();
		Assert.assertEquals("addAtom", 3, mol1.getAtomCount());
		CMLAtom atom = new CMLAtom("a99");
		mol1.addAtom(atom);
		Assert.assertEquals("addAtom", 4, mol1.getAtomCount());
		Assert.assertEquals("addAtom", "a99", mol1.getAtom(3).getId());
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.appendMolecule(CMLMolecule)'
	 */
	@Test
	public void testAppendMolecule() {
		makeMol1();
		makeMol2();

		CMLMolecule emptyMolecule = new CMLMolecule();
		Assert.assertNotNull("empty molecule", emptyMolecule);
		emptyMolecule.setId("m0");
		emptyMolecule.appendMolecule(mol1);
		Assert.assertEquals("molecule children", 2, emptyMolecule
				.getMoleculeCount());
		emptyMolecule.appendMolecule(mol2);
		Assert.assertEquals("molecule children", 3, emptyMolecule
				.getMoleculeCount());
		Assert.assertEquals("top id", "m0", emptyMolecule.getId());

		CMLElements<CMLMolecule> molecules = emptyMolecule
				.getMoleculeElements();
		CMLMolecule mol0a = molecules.get(0);
		Assert.assertEquals("id ", "m0", mol0a.getId());
		List<CMLAtom> atoms0 = mol0a.getAtoms();
		Assert.assertEquals("atoms ", 0, atoms0.size());
		CMLMolecule mol1a = molecules.get(1);
		Assert.assertEquals("id ", "m1", mol1a.getId());
		List<CMLAtom> atoms1 = mol1a.getAtoms();
		Assert.assertEquals("atoms ", 3, atoms1.size());
		Assert.assertEquals("atom id ", "a1", atoms1.get(0).getId());
		CMLMolecule mol2a = molecules.get(2);
		Assert.assertEquals("id ", "m2", mol2a.getId());
		List<CMLAtom> atoms2 = mol2a.getAtoms();
		Assert.assertEquals("atoms ", 3, atoms2.size());
		Assert.assertEquals("atom id ", "a11", atoms2.get(0).getId());

		// ---

		Assert.assertEquals("molecule children", 0, mol1.getMoleculeCount());
		mol1.appendMolecule(mol2);
		Assert.assertEquals("molecule children", 2, mol1.getMoleculeCount());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.CMLMolecule()'
	 */
	@Test
	public void testCMLMolecule() {
		CMLMolecule molecule = new CMLMolecule();
		Assert.assertNotNull("constructor ", molecule);
		Assert.assertNull("no id attribute", molecule.getIdAttribute());
		Assert.assertEquals("no children", molecule.getChildCount(), 0);
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.CMLMolecule(CMLMolecule)'
	 */
	@Test
	public void testCMLMoleculeCMLMolecule() {
		// copy constructor
		CMLMolecule molecule = new CMLMolecule(xmlMolecule);
		Assert.assertNotNull("constructor ", molecule);

		CMLAttribute idAtt = molecule.getIdAttribute();
		Assert.assertTrue("id class is subclass of CMLAttribute",
				CMLAttribute.class.isAssignableFrom(idAtt.getClass()));
		Assert.assertEquals("id class is StringSTAttribute", IdAttribute.class,
				idAtt.getClass());
		Assert.assertEquals("id value", molecule.getId(), xmlMolecule.getId());

		Assert.assertEquals("Molecule is identical", molecule
				.compareTo(xmlMolecule), 0);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.copy()'
	 */
	@Test
	public void testCopy() {
		Node copy = xmlMolecule.copy();
		Assert.assertEquals("class should be CMLMolecule: ", copy.getClass(),
				CMLMolecule.class);
		CMLMolecule copyMolecule = (CMLMolecule) copy;
		Assert.assertEquals("Molecule is identical", copyMolecule
				.compareTo(xmlMolecule), 0);
	}

	/**
	 * test building from XML* Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.createAndAddAtom(String)'
	 */
	@Test
	public void testCreateAndAddAtomString() {

		// build molecule
		CMLMolecule molecule = new CMLMolecule();
		// add atom - fragile
		CMLAtom atom = new CMLAtom();
		atom.setId("a1");
		molecule.getOrCreateAtomArray().appendChild(atom);
		Assert.assertNotNull("create and add atom", atom);
		Assert.assertNotNull("created id", atom.getId());
		Assert.assertEquals("created id", atom.getId(), "a1");
		CMLAtomArray atomArray = (CMLAtomArray) molecule.getAtomArrayElements()
				.get(0);
		Assert.assertNotNull("added atomArray", atomArray);
		Assert.assertEquals("added atom", atomArray.getAtomElements().get(0),
				atom);

		// add another of same id, should fail
		atom = molecule.getAtomById("a1");
		Assert.assertNotNull("atom should not be null", atom);
		atom = new CMLAtom();
		atom.setId("z1");
		xomMolecule.getOrCreateAtomArray().appendChild(atom);
		List<CMLAtom> atoms = xomMolecule.getAtoms();
		Assert.assertEquals("atom count", 6, atoms.size());
		Assert.assertEquals("atom id", "a1", ((CMLAtom) atoms.get(0)).getId());
		Assert.assertEquals("atom id", "z1", ((CMLAtom) atoms.get(5)).getId());

	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getBond(CMLAtom,
	 * CMLAtom)'
	 */
	@Test
	public void testGetBondCMLAtomCMLAtom() {
		makeMol5a();
		Assert.assertEquals("get bond", 4, mol5a.getBondCount());
		CMLAtom atom1 = mol5a.getAtomById("a1");
		Assert.assertNotNull("get bond", atom1);
		CMLAtom atom3 = mol5a.getAtomById("a3");
		Assert.assertNotNull("get bond", atom3);
		CMLAtom atom4 = mol5a.getAtomById("a4");
		Assert.assertNotNull("get bond", atom4);
		CMLBond bond = mol5a.getBond(atom1, atom3);
		Assert.assertNull("bond should be be null", bond);
		bond = mol5a.getBond(atom1, atom4);
		Assert.assertNotNull("bond should not be null", bond);
		Assert.assertEquals("bond atoms", new String[] { "a1", "a4" }, bond
				.getAtomRefs2());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getBondCount()'
	 */
	@Test
	public void testGetBondCount() {
		makeMol5a();
		Assert.assertEquals("get bond count", 4, mol5a.getBondCount());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getBonds()'
	 */
	@Test
	public void testGetBonds() {
		List<CMLBond> bonds = xmlMolecule.getBonds();
		Assert.assertNotNull("bonds not null", bonds);
		Assert.assertEquals("number of bonds", xmlNbonds, bonds.size());
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.getCalculatedFormalCharge()'
	 */
	@Test
	public void testGetCalculatedFormalCharge() {
		makeMol1();
		int fc = Integer.MIN_VALUE;
		try {
			fc = mol1.getCalculatedFormalCharge(FormalChargeControl.NO_DEFAULT);
		} catch (RuntimeException e) {
			Assert.assertEquals("formal charge ",
					"BUG: (unset attribute: formalCharge)should never throw", e
							.getMessage());
		}
		fc = Integer.MIN_VALUE;
		Assert.assertEquals("formal charge", Integer.MIN_VALUE, fc);
		makeMol5();
		try {
			fc = mol5.getCalculatedFormalCharge(FormalChargeControl.DEFAULT);
		} catch (RuntimeException e) {
			Assert.fail("formal charge should not throw " + e);
		}
		Assert.assertEquals("formal charge", -1, fc);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getCentroid2D()'
	 */
	@Test
	public void testGetCentroid2D() {
		makeMol1();
		Real2 centroid = mol1.calculateCentroid2D();
		Assert.assertNull("centroid 1", centroid);
		makeMol7();
		centroid = mol7.calculateCentroid2D();
		Assert.assertNotNull("centroid 7", centroid);
		Assert.assertEquals("centroid x", 0.4, centroid.getX(), .0001);
		Assert.assertEquals("centroid y", 1.1666, centroid.getY(), .0001);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getMoleculeCount()'
	 */
	@Test
	public void testGetMoleculeCount() {
		makeMol1();
		Assert.assertEquals("molecule count", 0, mol1.getMoleculeCount());
		makeMol8();
		Assert.assertEquals("molecule count", 2, mol8.getMoleculeCount());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getMolecules()'
	 */
	@Test
	public void testGetMolecules() {
		CMLElements<CMLMolecule> molecules = xmlMolecule.getMoleculeElements();
		Assert.assertNotNull("empty child molecules not null", molecules);
		Assert.assertEquals("child molecule count", 0, molecules.size());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getVector2D()'
	 */
	@Test
	public void testGetVector2D() {
		makeMol1();
		Real2Vector vector = mol1.getCoordinates2D();
		Assert.assertNotNull("get vector2d", vector);
		Assert.assertEquals("get vector2d", 0, vector.size());
		makeMol7();
		vector = mol7.getCoordinates2D();
		Assert.assertNotNull("get vector2d", vector);
		Assert.assertEquals("get vector2d", 3, vector.size());
		Real2 p = vector.getReal2(2);
		Assert.assertEquals("vector x", 1.2, p.getX(), EPS);
		Assert.assertEquals("vector y", 2.2, p.getY(), EPS);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.hasCoords()'
	 */
	@Test
	public void testHasCoords() {
		makeMol1();
		Assert.assertTrue("has 3d coords", mol1
				.hasCoordinates(CoordinateType.CARTESIAN));
		Assert.assertFalse("has 2d coords", mol1
				.hasCoordinates(CoordinateType.TWOD));
		makeMol7();
		Assert.assertTrue("has 3d coords", mol1
				.hasCoordinates(CoordinateType.CARTESIAN));
		Assert.assertFalse("has 2d coords", mol1
				.hasCoordinates(CoordinateType.TWOD));
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.multiply2DCoordsBy(double)'
	 */
	@Test
	public void testMultiply2DCoordsBy() {
		makeMol7();
		mol7.multiply2DCoordsBy(10.);
		Assert.assertEquals("scaled atom x", 0.0, mol7.getAtom(0).getX2(), EPS);
		Assert.assertEquals("scaled atom x", 0.0, mol7.getAtom(0).getY2(), EPS);
		Assert
				.assertEquals("scaled atom x", 12.0, mol7.getAtom(2).getX2(),
						EPS);
		Assert
				.assertEquals("scaled atom x", 22.0, mol7.getAtom(2).getY2(),
						EPS);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.renameAtomIDs(List,
	 * List)'
	 */
	@Test
	public void testRenameAtomIDs() {
		List<String> oldList = new ArrayList<String>();
		oldList.add("a1");
		oldList.add("a2");
		oldList.add("a3");
		List<String> newList = new ArrayList<String>();
		newList.add("a01");
		newList.add("a02");
		newList.add("a03");
		List<String> badList = new ArrayList<String>();
		badList.add("a01");
		badList.add("a02");
		makeMol1();

		mol1.renameAtomIDs(oldList, newList);
		Assert.assertEquals("renamed id", "a01", mol1.getAtom(0).getId());
		Assert.assertEquals("renamed id", "a02", mol1.getAtom(1).getId());
		Assert.assertEquals("renamed id", "a03", mol1.getAtom(2).getId());

		mol1.renameAtomIDs(newList, oldList);
		Assert.assertEquals("renamed id", "a1", mol1.getAtom(0).getId());
		Assert.assertEquals("renamed id", "a2", mol1.getAtom(1).getId());
		Assert.assertEquals("renamed id", "a3", mol1.getAtom(2).getId());

		try {
			mol1.renameAtomIDs(oldList, badList);
			Assert.fail("rename should throw exeception");
		} catch (Exception e) {
			Assert.assertEquals("rename IDs should throw",
					"Lists (3/2) must be same length as atomCount (3)", e
							.getMessage());
		}
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.translate2D(Real2)'
	 */
	@Test
	public void testTranslate2D() {
		makeMol7();
		Real2 p = mol7.getAtom(0).getXY2();
		Assert.assertEquals("original point", 0.0, p.getX(), EPS);
		Assert.assertEquals("original point", 0.0, p.getY(), EPS);
		p = mol7.getAtom(2).getXY2();
		Assert.assertEquals("original point", 1.2, p.getX(), EPS);
		Assert.assertEquals("original point", 2.2, p.getY(), EPS);
		mol7.translate2D(new Real2(0.000111, 0.999999));
		p = mol7.getAtom(0).getXY2();
		Assert.assertEquals("moved point", 0.000111, p.getX(), EPS);
		Assert.assertEquals("moved point", 0.999999, p.getY(), EPS);
		p = mol7.getAtom(2).getXY2();
		Assert.assertEquals("moved point", 1.200111, p.getX(), EPS);
		Assert.assertEquals("moved point", 3.199999, p.getY(), EPS);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.unlabelAllAtoms()'
	 */
	@Test
	public void testUnlabelAllAtoms() {
		makeMol5();
		Assert.assertNotNull("label C1", mol5.getAtomByLabel("C1"));
		Assert.assertNull("label C2", mol5.getAtomByLabel("C2"));
		Assert.assertNotNull("label H1a", mol5.getAtomByLabel("H1a"));
		mol5.unlabelAllAtoms();
		Assert.assertNull("label C1", mol5.getAtomByLabel("C1"));
		Assert.assertNull("label C2", mol5.getAtomByLabel("C2"));
		Assert.assertNull("label H1a", mol5.getAtomByLabel("H1a"));
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.getMoleculeAncestor(CMLElement)'
	 * 
	 * @exception Exception
	 */
	@Test
	public void testGetMoleculeAncestor() throws Exception {
		CMLCml cml = (CMLCml) parseValidString("<cml "
				+ CML_XMLNS
				+ ">"
				+ "  <molecule id='m1'>"
				+ "    <name>foo</name>"
				+ "    <atomArray>"
				+ "      <h:p xmlns:h='http://www.w3.org/1999/xhtml'>para</h:p>"
				+ "    </atomArray>" + "  </molecule>" + "</cml>");
		CMLMolecule mol = (CMLMolecule) cml.getChildElements().get(0);
		CMLName name = (CMLName) mol.getChildElements().get(0);
		CMLAtomArray atomArray = (CMLAtomArray) mol.getChildElements().get(1);
		// Element p = atomArray.getChildElements().get(0);
		Element e = CMLMolecule.getMoleculeAncestor(atomArray);
		Assert.assertEquals("equals", e, mol);
		e = CMLMolecule.getMoleculeAncestor(name);
		Assert.assertEquals("equals", e, mol);
		e = CMLMolecule.getMoleculeAncestor(mol);
		Assert.assertNull("equals", e);
		e = CMLMolecule.getMoleculeAncestor(cml);
		Assert.assertNull("equals", e);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.addBond(CMLBond,
	 * boolean)'
	 * 
	 * @exception Exception
	 */
	@Test
	public void testAddBond() throws Exception {
		CMLMolecule molecule = (CMLMolecule) parseValidString("<molecule "
				+ CML_XMLNS + " id='m1'>" + "  <atomArray>"
				+ "    <atom id='a1'/>" + "    <atom id='a2'/>"
				+ "    <atom id='a3'/>" + "  </atomArray>" + "</molecule>");
		List<CMLAtom> atoms = molecule.getAtoms();
		CMLBond bond = new CMLBond(atoms.get(0), atoms.get(1));
		bond.setId("b1");
		molecule.addBond(bond);
		bond = new CMLBond(atoms.get(2), atoms.get(1));
		bond.setId("b2");
		molecule.addBond(bond);
		bond = new CMLBond(atoms.get(0), atoms.get(1));
		bond.setId("b1");
		try {
			molecule.addBond(bond);
		} catch (RuntimeException e) {
			Assert.assertEquals("check", "Bond id not unique: b1", e
					.getMessage());
		}
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.appendToIds(String)'
	 */
	@Test
	public void testAppendToIds() {
		makeMol1();
		CMLAtom atom = mol1.getAtom(0);
		String id = atom.getId();
		Assert.assertEquals("resetId", "a1", id);
		atom = mol1.getAtomById("a1");
		Assert.assertNotNull("atom 0 not null", atom);
		mol1.appendToIds("X");
		id = mol1.getAtom(0).getId();
		Assert.assertEquals("resetId", "a1X", id);
		atom = mol1.getAtomById("a1X");
		Assert.assertNotNull("atom 0 not null", atom);

		makeMol8();
		atom = mol8.getAtom(0);
		id = atom.getId();
		Assert.assertEquals("resetId", "a1", id);
		mol8.appendToIds("X");
		id = atom.getId();
		Assert.assertEquals("resetId", "a1X", id);
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.createCartesiansFromFractionals(CMLCrystal
	 * ) '
	 */
	@Test
	public void testCreateCartesiansFromFractionalsCMLCrystal() {
		makeMolCryst();
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getAtom(int)'
	 */
	@Test
	public void testGetAtom() {
		makeMol1();
		CMLAtom atom = mol1.getAtom(0);
		Assert.assertNotNull("atom 0", atom);
		Assert.assertEquals("atom 0", "a1", atom.getId());
		atom = mol1.getAtom(3);
		Assert.assertNull("atom 3", atom);
		makeMol8();
		atom = mol8.getAtom(0);
		Assert.assertNotNull("atom 0 not null", atom);
		Assert.assertEquals("atom 0", "a1", atom.getId());
		atom = mol8.getAtom(4);
		Assert.assertNotNull("atom 4 not null", atom);
		Assert.assertEquals("atom 4", "a2", atom.getId());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getAtomById(String)'
	 */
	@Test
	public void testGetAtomById() {
		makeMol1();
		CMLAtom atom = mol1.getAtomById("a1");
		Assert.assertNotNull("atom 0", atom);
		Assert.assertEquals("atom 0", "a1", atom.getId());
		atom = mol1.getAtomById("a4");
		Assert.assertNull("atom 4", atom);
		makeMol8();
		atom = mol8.getAtomById("a1");
		Assert.assertNull("atom 0 null", atom);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getBondById(String)'
	 */
	@Test
	public void testGetBondById() {
		makeMol5a();
		CMLBond bond = mol5a.getBondById("a1_a2");
		Assert.assertNotNull("not null bond", bond);
		String id = bond.getId();
		Assert.assertEquals("bond", "a1_a2", id);
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.getAtomsByIds(String[])'
	 */
	@Test
	public void testGetAtomListByIds() {
		makeMol1();
		List<CMLAtom> atomList = mol1.getAtomListByIds(new String[] { "a1",
				"a3" });
		Assert.assertNotNull("atomList", atomList);
		Assert.assertEquals("atomList", 2, atomList.size());
		Assert.assertEquals("atom", "a1", atomList.get(0).getId());
		atomList = mol1.getAtomsById("a4");
		Assert.assertNull("atom 4", atomList);

		makeMol8();
		atomList = mol8.getAtomListByIds(new String[] { "a1", "a3" });
		Assert.assertNotNull("atomList", atomList);
		Assert.assertEquals("atomList", 0, atomList.size());
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.getAtomByLabel(String)'
	 */
	@Test
	public void testGetAtomByLabel() {
		makeMol5();
		CMLAtom atom = mol5.getAtomByLabel("C1");
		Assert.assertNotNull("atom not null", atom);
		Assert.assertEquals("atom id", "a1", atom.getId());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getBond(CMLAtom,
	 * CMLAtom)'
	 */
	@Test
	public void testGetBond() {
		makeMol5a();
		CMLAtom a1 = mol5a.getAtomById("a1");
		CMLAtom a2 = mol5a.getAtomById("a2");
		CMLBond b = mol5a.getBond(a1, a2);
		Assert.assertNotNull("b not null", b);
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.calculateCentroid2D()'
	 */
	@Test
	public void testCalculateCentroid2D() {
		makeMol7();
		Real2 r2 = mol7.calculateCentroid2D();
		Assert.assertEquals("r2", 0.4, r2.getX(), 0.00001);
		Assert.assertEquals("r2", 3.5 / 3., r2.getY(), 0.00001);
		makeMol1();
		r2 = mol1.calculateCentroid2D();
		Assert.assertNull("centroid null", r2);
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getDoubleBonds()'
	 */
	@Test
	public void testGetDoubleBonds() {
		makeMol5a();
		List<CMLBond> bonds = mol5a.getBonds();
		Assert.assertNull("bond 1", bonds.get(0).getOrder());
		bonds.get(0).setOrder(CMLBond.SINGLE);
		bonds.get(1).setOrder(CMLBond.DOUBLE);
		bonds.get(3).setOrder(CMLBond.DOUBLE);
		Assert.assertEquals("bond 0", CMLBond.SINGLE, bonds.get(0).getOrder());
		Assert.assertEquals("bond 1", CMLBond.DOUBLE, bonds.get(1).getOrder());
		Assert.assertNull("bond 2", bonds.get(2).getOrder());
		List<CMLBond> bondList = mol5a.getDoubleBonds();
		Assert.assertEquals("bonds", 2, bondList.size());
	}

	protected CMLMolecule makeMol1a() {
		CMLMolecule mol1a = new CMLMolecule(mol1);
		mol1a.getAtom(0).resetId("a11");
		mol1a.getAtom(1).resetId("a12");
		mol1a.getAtom(2).resetId("a13");
		return mol1a;
	}

	// /**
	// * Test method for
	// * 'org.xmlcml.cml.element.CMLMolecule.getMolecule(CMLElement)'
	// */
	// @Test
	// public void testGetMolecule() {
	// makeMol1();
	// CMLElement element = (CMLElement) mol1.getChild(0);
	// mol1.getMolecule(element);
	// }

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.getDescendantsOrMolecule()'
	 */
	@Test
	public void testGetDescendantsOrMolecule() {
		makeMol1();
		// get single molecule
		List<CMLMolecule> molList = mol1.getDescendantsOrMolecule();
		Assert.assertNotNull("mol not null", molList);
		Assert.assertEquals("mols", 1, molList.size());
		makeMol8();
		// gets the 2 children
		molList = mol8.getDescendantsOrMolecule();
		Assert.assertNotNull("mol not null", molList);
		Assert.assertEquals("mols", 2, molList.size());
	}

	/**
	 * Test method for 'org.xmlcml.cml.element.CMLMolecule.getCoordinates2D()'
	 */
	@Test
	public void testGetCoordinates2D() {
		makeMol7();
		Real2Vector r2v = mol7.getCoordinates2D();
		Assert.assertEquals("coord2", 3, r2v.size());
		Real2 r2 = r2v.get(2);
		Assert.assertEquals("r2", 1.2, r2.getX(), 0.00001);
		Assert.assertEquals("r2", 2.2, r2.getY(), 0.00001);
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.hasCoordinates(CoordinateType)'
	 */
	@Test
	public void testHasCoordinates() {
		makeMol5a();
		Assert.assertTrue("coords", mol5a
				.hasCoordinates(CoordinateType.CARTESIAN));
		makeMol7();
		Assert.assertFalse("coords", mol7
				.hasCoordinates(CoordinateType.CARTESIAN));
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.isMoleculeContainer()'
	 */
	@Test
	public void testIsMoleculeContainer() {
		makeMol1();
		Assert.assertFalse("container", mol1.isMoleculeContainer());
		makeMol8();
		Assert.assertTrue("container", mol8.isMoleculeContainer());
	}

	// /**
	// * Test method for
	// * 'org.xmlcml.cml.element.CMLMolecule.mustEqual(CMLElement)'
	// */
	// @Test
	// public void testMustEqual() {
	// mol1.mustEqual(mol1);
	// }

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.setBondOrders(String)'
	 */
	@Test
	public void testSetBondOrders() {
		makeMol5a();
		List<CMLBond> bonds = mol5a.getBonds();
		Assert.assertNull("bond 1", bonds.get(0).getOrder());
		bonds.get(0).setOrder(CMLBond.SINGLE);
		bonds.get(1).setOrder(CMLBond.DOUBLE);
		Assert.assertEquals("bond 0", CMLBond.SINGLE, bonds.get(0).getOrder());
		Assert.assertEquals("bond 1", CMLBond.DOUBLE, bonds.get(1).getOrder());
		Assert.assertNull("bond 2", bonds.get(2).getOrder());
	}

	/**
	 * Test method for
	 * 'org.xmlcml.cml.element.CMLMolecule.setPreferredBondOrders()'
	 */
	@Test
	public void testSetNormalizedBondOrders() {
		String s = ""
				+ "  <molecule id='m5' "
				+ CML_XMLNS
				+ ">"
				+ "    <atomArray>"
				+ "      <atom id='a1' elementType='C' x3='0.0' y3='0.0' z3='0.0'/>"
				+ "      <atom id='a2' elementType='N' x3='0.0' y3='1.3' z3='0.0'/>"
				+ "      <atom id='a3' elementType='C' x3='1.2' y3='2.2' z3='0.0'/>"
				+ "      <atom id='a4' elementType='H' x3='0.95' y3='-0.54' z3='0.0'/>"
				+ "      <atom id='a5' elementType='H' x3='-0.95' y3='-0.54' z3='0.0'/>"
				+ "    </atomArray>" + "    <bondArray>"
				+ "      <bond id='a1_a2' atomRefs2='a1 a2' order='1'/>"
				+ "      <bond id='a1_a4' atomRefs2='a1 a4' order='S'/>"
				+ "      <bond id='a1_a5' atomRefs2='a1 a5' order='2'/>"
				+ "      <bond id='a2_a3' atomRefs2='a2 a3'/>"
				+ "    </bondArray>" + "  </molecule>" + "";
		CMLMolecule mol = (CMLMolecule) parseValidString(s);
		List<CMLBond> bondList = mol.getBonds();
		Assert
				.assertEquals("bond0", CMLBond.SINGLE, bondList.get(0)
						.getOrder());
		Assert
				.assertEquals("bond1", CMLBond.SINGLE, bondList.get(1)
						.getOrder());
		Assert
				.assertEquals("bond2", CMLBond.DOUBLE, bondList.get(2)
						.getOrder());
		mol.setNormalizedBondOrders();
		Assert
				.assertEquals("bond0", CMLBond.SINGLE, bondList.get(0)
						.getOrder());
		Assert
				.assertEquals("bond1", CMLBond.SINGLE, bondList.get(1)
						.getOrder());
		Assert
				.assertEquals("bond2", CMLBond.DOUBLE, bondList.get(2)
						.getOrder());
		Assert.assertNull("bond3", bondList.get(3).getOrder());
		bondList.get(0).setOrder(CMLBond.SINGLE);
		bondList.get(1).setOrder(CMLBond.SINGLE_S);
		bondList.get(2).setOrder(CMLBond.DOUBLE);
		Assert
				.assertEquals("bond0", CMLBond.SINGLE, bondList.get(0)
						.getOrder());
		Assert
				.assertEquals("bond1", CMLBond.SINGLE, bondList.get(1)
						.getOrder());
		Assert
				.assertEquals("bond2", CMLBond.DOUBLE, bondList.get(2)
						.getOrder());
		Assert.assertNull("bond3", bondList.get(3).getOrder());
	}

	// /**
	// * Test method for
	// * 'org.xmlcml.cml.element.CMLMolecule.transform(CMLSymmetry)'
	// */
	// @Test
	// public void testTransformCMLSymmetry() {
	// mol1.transform(new CMLSymmetry());
	// fail("Not yet implemented"); // TODO
	// }
	//
	/**
	 * tests addition and deletion through the tree. should be rerouted
	 * 
	 */
	@Test
	public void testAddAndDeleteAtoms() {
		CMLMolecule molecule = new CMLMolecule();
		CMLAtom atom0 = new CMLAtom("a0");
		molecule.addAtom(atom0);
		Assert.assertEquals("addAtom", 1, molecule.getAtomCount());
		Assert.assertEquals("addAtom", 0, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 0, atom0.getLigandBonds().size());

		CMLAtom atom1 = new CMLAtom("a1");
		molecule.appendChild(atom1);
		Assert.assertEquals("addAtom", 2, molecule.getAtomCount());
		Assert.assertEquals("addAtom", 0, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 0, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 0, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 0, atom1.getLigandBonds().size());

		CMLBond bond0 = new CMLBond(atom0, atom1);
		molecule.appendChild(bond0);
		Assert.assertEquals("addBond", 1, molecule.getBondCount());
		Assert.assertEquals("addAtom", 1, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom1.getLigandBonds().size());

		CMLAtom atom2 = new CMLAtom("a2");
		molecule.appendChild(atom2);
		Assert.assertEquals("addAtom", 3, molecule.getAtomCount());
		Assert.assertEquals("addAtom", 1, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom1.getLigandBonds().size());
		Assert.assertEquals("addAtom", 0, atom2.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 0, atom2.getLigandBonds().size());

		CMLBond bond1 = new CMLBond(atom1, atom2);
		molecule.appendChild(bond1);
		Assert.assertEquals("addBond", 2, molecule.getBondCount());
		Assert.assertEquals("addAtom", 1, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 2, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 2, atom1.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom2.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom2.getLigandBonds().size());

		CMLAtom atom3 = new CMLAtom("a3");
		molecule.appendChild(atom3);
		Assert.assertEquals("addAtom", 4, molecule.getAtomCount());
		Assert.assertEquals("addAtom", 1, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 2, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 2, atom1.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom2.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom2.getLigandBonds().size());
		Assert.assertEquals("addAtom", 0, atom3.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 0, atom3.getLigandBonds().size());

		CMLBond bond2 = new CMLBond(atom1, atom3);
		molecule.appendChild(bond2);
		Assert.assertEquals("addBond", 3, molecule.getBondCount());
		Assert.assertEquals("addAtom", 1, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 3, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 3, atom1.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom2.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom2.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom3.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom3.getLigandBonds().size());

		molecule.deleteAtom(atom2);
		Assert.assertEquals("addAtom", 1, atom0.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom0.getLigandBonds().size());
		Assert.assertEquals("addAtom", 2, atom1.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 2, atom1.getLigandBonds().size());
		Assert.assertEquals("addAtom", 0, atom2.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 0, atom2.getLigandBonds().size());
		Assert.assertEquals("addAtom", 1, atom3.getLigandAtoms().size());
		Assert.assertEquals("addAtom", 1, atom3.getLigandBonds().size());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#createMoleculeWithId(java.lang.String)}
	 * .
	 */
	@Test
	public final void testCreateMoleculeWithIdString() {
		CMLMolecule mol = CMLMolecule.createMoleculeWithId("m1");
		Assert.assertEquals("create mol", 0, mol.getAtomCount());
		Assert.assertEquals("create mol", 0, mol.getBondCount());
		Assert.assertEquals("create mol", "m1", mol.getId());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#deleteAtom(org.xmlcml.cml.element.lite.CMLAtom)}
	 * .
	 */
	@Test
	public final void testDeleteAtom() {
		makeMol1();
		Assert.assertEquals("orig", 3, mol1.getAtomCount());
		Assert.assertEquals("orig", 0, mol1.getBondCount());
		CMLAtom a1 = mol1.getAtomById("a1");
		mol1.deleteAtom(a1);
		Assert.assertEquals("orig", 2, mol1.getAtomCount());
		a1 = mol1.getAtomById("a1");
		Assert.assertNull("deleted should be null", a1);

		makeMol5a();
		Assert.assertEquals("orig", 5, mol5a.getAtomCount());
		Assert.assertEquals("orig", 4, mol5a.getBondCount());
		a1 = mol5a.getAtomById("a1");
		CMLAtom a2 = mol5a.getAtomById("a2");
		CMLAtom a3 = mol5a.getAtomById("a3");
		CMLAtom a4 = mol5a.getAtomById("a4");
		CMLBond bond = mol5a.getBond(a1, a2);
		mol5a.deleteAtom(a1);
		Assert.assertEquals("orig", 4, mol5a.getAtomCount());
		Assert.assertEquals("orig", 1, mol5a.getBondCount());
		bond = mol5a.getBond(a1, a2);
		Assert.assertNull("orig", bond);
		bond = mol5a.getBond(a1, a4);
		Assert.assertNull("orig", bond);
		bond = mol5a.getBond(a2, a3);
		Assert.assertNotNull("orig", bond);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#removeAtomArray()}.
	 */
	@Test
	public final void testRemoveAtomArray() {
		makeMol1();
		Assert.assertEquals("orig", 3, mol1.getAtomCount());
		mol1.removeAtomArray();
		Assert.assertEquals("orig", 0, mol1.getAtomCount());
		makeMol5a();
		Assert.assertEquals("orig", 5, mol5a.getAtomCount());
		Assert.assertEquals("orig", 4, mol5a.getBondCount());
		mol5a.removeAtomArray();
		Assert.assertEquals("orig", 0, mol5a.getAtomCount());
		Assert.assertEquals("orig", 0, mol5a.getBondCount());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#removeBondArray()}.
	 */
	@Test
	public final void testRemoveBondArray() {
		makeMol1();
		Assert.assertEquals("orig", 0, mol1.getBondCount());
		mol1.removeBondArray();
		Assert.assertEquals("orig", 0, mol1.getBondCount());
		makeMol5a();
		Assert.assertEquals("orig", 5, mol5a.getAtomCount());
		Assert.assertEquals("orig", 4, mol5a.getBondCount());
		mol5a.removeBondArray();
		Assert.assertEquals("orig", 5, mol5a.getAtomCount());
		Assert.assertEquals("orig", 0, mol5a.getBondCount());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#deleteBond(org.xmlcml.cml.element.lite.CMLBond)}
	 * .
	 */
	@Test
	public final void testDeleteBond() {
		makeMol5a();
		Assert.assertEquals("orig", 5, mol5a.getAtomCount());
		Assert.assertEquals("orig", 4, mol5a.getBondCount());
		CMLAtom a1 = mol5a.getAtomById("a1");
		CMLAtom a2 = mol5a.getAtomById("a2");
		CMLBond bond = mol5a.getBond(a1, a2);
		mol5a.deleteBond(bond);
		Assert.assertEquals("orig", 5, mol5a.getAtomCount());
		Assert.assertEquals("orig", 3, mol5a.getBondCount());
		bond = mol5a.getBond(a1, a2);
		Assert.assertNull("orig", bond);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#deleteMolecule(org.xmlcml.cml.element.lite.CMLMolecule)}
	 * .
	 */
	@Test
	public final void testDeleteMolecule() {
		makeMol8();
		Assert.assertEquals("orig", 2, mol8.getMoleculeCount());
		CMLMolecule mol81 = mol8.getMoleculeElements().get(0);
		// CMLMolecule mol82 = mol8.getMoleculeElements().get(1);
		mol8.deleteMolecule(mol81);
		Assert.assertEquals("after", 1, mol8.getMoleculeCount());
		mol8.normalizeSingleMoleculeChild();
		Assert.assertEquals("after normalization", 0, mol8.getMoleculeCount());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#normalizeSingleMoleculeChild()}
	 * .
	 */
	@Test
	public final void testNormalizeSingleMoleculeChild() {
		makeMol1();
		Assert.assertEquals("orig", 3, mol1.getAtomCount());
		Assert.assertEquals("orig", 0, mol1.getMoleculeCount());
		Assert.assertEquals("orig", "m1", mol1.getId());
		CMLMolecule top = CMLMolecule.createMoleculeWithId("top");
		top.appendChild(mol1);
		Assert.assertEquals("after", 3, top.getAtomCount());
		Assert.assertEquals("after", 1, top.getMoleculeCount());
		Assert.assertEquals("after", "top", top.getId());
		top.normalizeSingleMoleculeChild();
		Assert.assertEquals("after norm", 3, top.getAtomCount());
		Assert.assertEquals("after norm", 0, top.getMoleculeCount());
		Assert.assertEquals("after norm", "top", top.getId());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#appendChild(org.xmlcml.cml.element.lite.CMLMolecule)}
	 * .
	 */
	@Test
	public final void testAppendChildCMLMolecule() {
		// same as addMolecule
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#removeChild(org.xmlcml.cml.element.lite.CMLMolecule)}
	 * .
	 */
	@Test
	public final void testRemoveChildCMLMolecule() {
		// same as removeMolecule
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#appendChild(org.xmlcml.cml.element.lite.CMLAtom)}
	 * .
	 */
	@Test
	public final void testAppendChildCMLAtom() {
		// same as addAtom
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#removeChild(org.xmlcml.cml.element.lite.CMLAtom)}
	 * .
	 */
	@Test
	public final void testRemoveChildCMLAtom() {
		// same as removeAtom
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#appendChild(org.xmlcml.cml.element.lite.CMLBond)}
	 * .
	 */
	@Test
	public final void testAppendChildCMLBond() {
		// same as addBond
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#removeChild(org.xmlcml.cml.element.lite.CMLBond)}
	 * .
	 */
	@Test
	public final void testRemoveChildCMLBond() {
		// same as removeBond
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getAtomArray()}.
	 */
	@Test
	public final void testGetAtomArray() {
		makeMol1();
		CMLAtomArray atomArray = mol1.getAtomArray();
		Assert.assertNotNull("atomArray", atomArray);
		mol1 = CMLMolecule.createMoleculeWithId("m1");
		atomArray = mol1.getAtomArray();
		Assert.assertNull("atomArray", atomArray);
		CMLAtom atom = new CMLAtom("a1");
		mol1.addAtom(atom);
		atomArray = mol1.getAtomArray();
		Assert.assertNotNull("atomArray", atomArray);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getAtomMap()}.
	 */
	@Test
	public final void testGetAtomMap() {
		makeMol5a();
		Map<String, CMLAtom> map = mol5a.getAtomMap();
		Assert.assertEquals("map", 5, map.size());
		CMLAtom atom = map.get("a2");
		Assert.assertNotNull("atom not null", atom);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getBondMap()}.
	 */
	@Test
	public final void testGetBondMap() {
		makeMol5a();
		Map<String, CMLBond> map = mol5a.getBondMap();
		Assert.assertEquals("map", 4, map.size());
		// hash is ordered this way
		CMLBond bond = map.get("a2__a1");
		Assert.assertNotNull("bond not null", bond);
		Assert.assertEquals("bond", new String[] { "a1", "a2" }, bond
				.getAtomRefs2());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getBondIdMap()}.
	 */
	@Test
	public final void testGetBondIdMap() {
		makeMol5a();
		Map<String, CMLBond> bondMap = mol5a.getBondIdMap();
		Assert.assertEquals("map", 4, bondMap.size());
		CMLBond bond = bondMap.get("a1_a4");
		Assert.assertNotNull("bond not null", bond);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getAtomsById(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetAtomsById() {
		makeMol1();
		List<CMLAtom> atomList = mol1.getAtomsById("a1");
		Assert.assertNotNull("atomList", atomList);
		Assert.assertEquals("atomList", 1, atomList.size());
		Assert.assertEquals("atom", "a1", atomList.get(0).getId());
		atomList = mol1.getAtomsById("a4");
		Assert.assertNull("atom 4", atomList);

		makeMol8();
		atomList = mol8.getAtomsById("a1");
		Assert.assertNotNull("atomList", atomList);
		Assert.assertEquals("atomList", 2, atomList.size());
		Assert.assertEquals("atom", "a1", atomList.get(0).getId());
		atomList = mol8.getAtomsById("a33");
		Assert.assertEquals("atomList", 1, atomList.size());
		atomList = mol8.getAtomsById("a4");
		Assert.assertNull("atom 4", atomList);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getAtomCount()}.
	 */
	@Test
	public final void testGetAtomCount() {
		makeMol1();
		Assert.assertEquals("atom count", 3, mol1.getAtomCount());
		makeMol8();
		Assert.assertEquals("atom count", 6, mol8.getAtomCount());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getAtoms()}.
	 */
	@Test
	public final void testGetAtoms() {
		makeMol1();
		List<CMLAtom> atomList = mol1.getAtoms();
		Assert.assertEquals("atoms", 3, atomList.size());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getBondArray()}.
	 */
	@Test
	public final void testGetBondArray() {
		makeMol5a();
		CMLBondArray bondArray = mol5a.getBondArray();
		Assert.assertNotNull("bondArray", bondArray);
		makeMol1();
		bondArray = mol1.getBondArray();
		Assert.assertNull("bondArray", bondArray);
		CMLAtom a1 = mol1.getAtomById("a1");
		CMLAtom a2 = mol1.getAtomById("a2");
		CMLBond bond = new CMLBond(a1, a2);
		mol1.addBond(bond);
		bondArray = mol1.getBondArray();
		Assert.assertNotNull("bondArray", bondArray);
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getOrCreateAtomArray()}.
	 */
	@Test
	public final void testGetOrCreateAtomArray() {
		makeMol1();
		CMLAtomArray atomArray = mol1.getOrCreateAtomArray();
		Assert.assertNotNull("create atomArray", atomArray);
		Assert.assertEquals("atomArray", 3, atomArray.getChildCMLElements(
				CMLAtom.TAG).size());
		mol1 = CMLMolecule.createMoleculeWithId("m1");
		Elements atomArrayList = mol1.getChildCMLElements(CMLAtomArray.TAG);
		Assert.assertEquals("missing atomArray", 0, atomArrayList.size());
		atomArray = mol1.getOrCreateAtomArray();
		Assert.assertNotNull("create atomArray", atomArray);
		Assert.assertEquals("atomArray", 0, atomArray.getChildCMLElements(
				CMLAtom.TAG).size());
	}

	/**
	 * Test method for
	 * {@link org.xmlcml.cml.element.lite.CMLMolecule#getOrCreateBondArray()}.
	 */
	@Test
	public final void testGetOrCreateBondArray() {
		makeMol1();
		Elements bondArrayList = mol1.getChildCMLElements(CMLBondArray.TAG);
		Assert.assertEquals("missing bondArray", 0, bondArrayList.size());
		CMLBondArray bondArray = mol1.getOrCreateBondArray();
		Assert.assertNotNull("create bondArray", bondArray);
		Assert.assertEquals("bondArray", 0, bondArray.getChildCMLElements(
				CMLBond.TAG).size());
		mol1 = CMLMolecule.createMoleculeWithId("m1");
		bondArray = mol1.getOrCreateBondArray();
		Assert.assertNotNull("create bondArray", bondArray);
		Assert.assertEquals("bondArray", 0, bondArray.getChildCMLElements(
				CMLBond.TAG).size());
	}

	// /**
	// * Test method for {@link
	// org.xmlcml.cml.element.CMLMolecule#expandCountExpression()}.
	// */
	// @Test
	// public final void testExpandCountExpression() {
	// makeMol1();
	// mol1.setCountExpression("*(3)");
	// try {
	// mol1.expandCountExpression();
	// } catch (RuntimeException e) {
	// Assert.assertEquals(
	// "cannot clone", "Cannot clone under Document parent",
	// e.getMessage());
	// }
	// CMLMolecule top = CMLMolecule.createMoleculeWithId("top");
	// top.appendChild(mol1);
	// mol1.expandCountExpression();
	// Assert.assertEquals("after clone", 3, top.getMoleculeCount());
	// CMLMolecule mola = top.getMoleculeElements().get(0);
	// CMLMolecule molb = top.getMoleculeElements().get(1);
	// // reset ids just for comparison
	// mola.resetId("XX");
	// molb.resetId("XX");
	// CMLMoleculeTest.assertEqualsCanonically("cloned mols", mola, molb);
	// }
}
