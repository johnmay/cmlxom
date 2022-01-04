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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.xmlcml.cml.attribute.DelimiterAttribute;
import org.xmlcml.cml.attribute.DelimiterAttribute.Action;
import org.xmlcml.cml.attribute.NamespaceRefAttribute;
import org.xmlcml.cml.base.CMLAttribute;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLType;
import org.xmlcml.cml.interfacex.HasArraySize;
import org.xmlcml.cml.interfacex.HasDataType;
import org.xmlcml.cml.interfacex.HasDelimiter;
import org.xmlcml.cml.interfacex.HasDictRef;
import org.xmlcml.cml.interfacex.HasUnits;
import org.xmlcml.euclid.EuclidRuntimeException;
import org.xmlcml.euclid.IntArray;
import org.xmlcml.euclid.JodaDate;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.euclid.Util;

/**
 * user-modifiable class supporting array. * autogenerated from schema use as a
 * shell which can be edited
 * 
 */
public class CMLArray extends AbstractArray implements HasUnits, HasArraySize,
		HasDataType, HasDictRef, HasDelimiter {

	static Logger logger = Logger.getLogger(CMLArray.class);
	/** namespaced element name. */
	public final static String NS = C_E + TAG;
	private DelimiterAttribute delimiterAttribute = null;

	/**
	 * constructor.
	 */
	public CMLArray() {
		init();
	}

	private void init() {
		// ensureDelimiterAttribute(Action.RESET);
	}

	/**
	 * constructor.
	 * 
	 * @param old
	 */
	public CMLArray(CMLArray old) {
		super((AbstractArray) old);
		init();
	}

	/**
	 * copy node .
	 * 
	 * @return Node
	 */
	@Override
	public Element copy() {
		return new CMLArray(this);
	}

	/**
	 * create new instance in context of parent, overridable by subclasses.
	 * 
	 * @param parent
	 *            parent of element to be constructed (ignored by default)
	 * @return CMLArray
	 */
	@Override
	public CMLElement makeElementInContext(Element parent) {
		return new CMLArray();

	}

	/**
	 * check array is OK.
	 * 
	 * @param parent
	 *            element
	 */
	@Override
	public void finishMakingElement(Element parent) {
		delimiterAttribute = null;
		delimiterAttribute = (DelimiterAttribute) this.getDelimiterAttribute();
		int ll = -1;
		int size = -1;
		if (this.getSizeAttribute() != null) {
			String aa = super.getAttributeValue("size");
			size = Integer.parseInt(aa);
		}
		String dataType = this.getDataType();
		if (dataType.equals(XSD_STRING)) {
			String[] ss = this.getStrings();
			ll = ss.length;
		} else if (XSD_DOUBLE.equals(CMLType.getNormalizedValue(dataType))) {
			try {
				double[] ss = this.getDoubles();
				ll = ss.length;
			} catch (RuntimeException e) {
				throw new RuntimeException("Fault in XML: "
						+ this.getXMLContent(), e);
			}
		} else if (dataType.equals(XSD_INTEGER)) {
			try {
				int[] ss = this.getInts();
				ll = ss.length;
			} catch (RuntimeException e) {
				throw new RuntimeException("cannot parse as ints: "
						+ this.getXMLContent());
			}
		} else if (dataType.equals(CML_DATATYPETYPE)) {
			String[] ss = this.getStrings();
			ll = ss.length;
		} else if (dataType.equals(CML_NAMESPACEREFTYPE)) {
			String[] ss = this.getStrings();
			ll = ss.length;
		} else if (dataType.equals(CML_UNITSTYPE)) {
			String[] ss = this.getStrings();
			ll = ss.length;
		} else {
			throw new RuntimeException("array does  not support dataType: "
					+ dataType);
		}
		if (size != -1 && ll != size) {
			throw new RuntimeException("Size attribute: " + size
					+ " incompatible with content: " + ll);
		}
		if (size != -1) {
			this.resetSize(ll);
		} else {
			this.removeAttribute("size");
		}
	}

	public void ensureDelimiterAttribute(Action action) {
		if (action.equals(Action.RESET)) {
			delimiterAttribute = null;
		}
		delimiterAttribute = (DelimiterAttribute) this.getDelimiterAttribute();
		if (delimiterAttribute == null) {
			delimiterAttribute = new DelimiterAttribute(S_SPACE);
			super.setDelimiter(S_SPACE);
		}
	}

	/**
	 * get delimiter attribute if the delimiter is whitespace this should return
	 * null
	 * 
	 * @return attribute
	 */
	@Override
	public CMLAttribute getDelimiterAttribute() {
		delimiterAttribute = (DelimiterAttribute) super.getDelimiterAttribute();
		return delimiterAttribute;
	}

	// =========================== additional constructors
	// ========================

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:string cannot use default delimiter (S_SPACE) if strings
	 * contain whitespace, so choose another delimiter
	 * 
	 * @param array
	 * @throws RuntimeException
	 *             strings must not contain whitespace
	 */
	public CMLArray(String[] array) throws RuntimeException {
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:string cannot use delimiter if strings contain it, so
	 * choose another delimiter
	 * 
	 * @param array
	 * @param delimiter
	 * @throws RuntimeException
	 *             strings must not contain delimiter
	 */
	public CMLArray(String[] array, String delimiter) throws RuntimeException {
		setDelimiter(delimiter);
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:double
	 * 
	 * @param array
	 */
	public CMLArray(RealArray array) {
		this.setArray(array.getArray());
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:double
	 * 
	 * @param array
	 */
	public CMLArray(double[] array) {
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:double
	 * 
	 * @param array
	 */
	public CMLArray(boolean[] array) {
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:double cannot use delimiter if strings contain it, so
	 * choose another delimiter
	 * 
	 * @param array
	 * @param delimiter
	 * @throws RuntimeException
	 *             strings must not contain delimiter
	 */
	public CMLArray(double[] array, String delimiter) throws RuntimeException {
		setDelimiter(delimiter);
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:integer
	 * 
	 * @param array
	 */
	public CMLArray(DateTime[] array) {
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:integer
	 * 
	 * @param array
	 */
	public CMLArray(int[] array) {
		this.setArray(array);
	}

	/**
	 * formed from components. size is extracted from array dimensions sets
	 * dataType to xsd:integer cannot use delimiter if strings contain it, so
	 * choose another delimiter
	 * 
	 * @param array
	 * @param delimiter
	 * @throws RuntimeException
	 *             doubles must not contain delimiter
	 */
	public CMLArray(int[] array, String delimiter) {
		setDelimiter(delimiter);
		this.setArray(array);
	}

	/**
	 * creates array of given dataType
	 * 
	 * @param dataType
	 */
	public CMLArray(String dataType) {
		this();
		this.resetDataType(dataType);
	}

	public static CMLArray createArray(List<CMLScalar> scalarList) {
		CMLArray array = null;
		if (scalarList != null && scalarList.size() > 0) {
			CMLScalar scalar = scalarList.get(0);
			if (scalar == null) {
				throw new RuntimeException("Null scalar in list: "
						+ scalarList.size());
			}
			array = createArrayWithAttributes(scalar);
		}
		for (CMLScalar scalar : scalarList) {
			array.append(scalar);
		}
		array.removeWhitespaceDelimiterAttribute();
		return array;
	}

	public static CMLArray createArrayWithAttributes(HasDataType hasDataType) {
		CMLArray array;
		String dataType = hasDataType.getDataType();
		if (dataType == null) {
			throw new RuntimeException("Null data type");
		}
		array = new CMLArray(dataType);
		String dictRef = hasDataType.getDictRef();
		if (dictRef != null) {
			array.setDictRef(dictRef);
		}
		String units = ((HasUnits) hasDataType).getUnits();
		if (units != null) {
			array.setUnits(units);
		}
		array.removeWhitespaceDelimiterAttribute();
		return array;
	}

	/**
	 * creates array of type comptatible with scalar can be used to add
	 * subsequently scalar contents to array does NOT add scalar contents
	 * typical use List&lt;CMLScalar&gt; scalars; CMLArray array =
	 * CMLArray.createEmptyArray(scalars.get(0)); for (CMLScalar scalar :
	 * scalars) { array.append(scalar); }
	 * 
	 * @param scalar
	 * @return
	 */
	public CMLArray createEmptyArray(CMLScalar scalar) {
		CMLArray array = null;
		if (scalar != null && scalar.getDataType() != null) {
			array = new CMLArray(scalar.getDataType());
		}
		array.removeWhitespaceDelimiterAttribute();
		return array;
	}
	
	/**
	 * creates a new array formed from a subset of the current array
	 * @param start inclusive start
	 * @param end inclusive end
	 * @return new array of correct dataType and dictRef; null if fails
	 * @throws IllegalArgumentException if indices are out of bounds
	 */
	public CMLArray createSubArray(int start, int end) 
	    throws IllegalArgumentException {
		int size = this.getSize();
		if (start < 0 || end >= size || end < start) {
			throw new IllegalArgumentException("bad array slice indexes "+start+"/"+end+" in "+size);
		}
		String dataType = this.getDataType();
		CMLArray subArray = null;
		if (dataType == null || dataType.equals(CMLConstants.XSD_STRING)) {
			String[] sout = new String[end-start+1];
			String[] ss = this.getStrings();
			for (int i = start; i <= end; i++) {
				sout[i-start] = ss[i];
			}
			String delimiter = this.getDelimiter();
			subArray = (delimiter == null) ? new CMLArray(sout) : new CMLArray(sout, delimiter);
		} else if (dataType.equals(CMLConstants.XSD_DOUBLE)) {
			RealArray realArray = new RealArray(this.getDoubles());
			subArray = new CMLArray(realArray.getSubArray(start, end));
		} else if (dataType.equals(CMLConstants.XSD_INTEGER)) {
			IntArray intArray = new IntArray(this.getInts());
			subArray = new CMLArray(intArray.getSubArray(start, end).getArray());
		}
		String dictRef = this.getDictRef();
		if (dictRef != null) {
			subArray.setDictRef(dictRef);
		}
		return subArray;
	}

	// ====================== housekeeping methods =====================

	/**
	 * get size of array.
	 * 
	 * @return size
	 */
	public int getArraySize() {
		return this.getSize();
	}

	/**
	 * returns array of primitive types based on dataType.
	 * 
	 * @return double[], int[], String[] or null
	 */
	public Object getPrimitiveArray() {
		Object primitiveArray = null;
		if (XSD_DOUBLE.equals(this.getDataType())) {
			primitiveArray = this.getDoubles();
		} else if (XSD_BOOLEAN.equals(this.getDataType())) {
			primitiveArray = this.getBooleans();
		} else if (XSD_DATE.equals(this.getDataType())) {
			primitiveArray = this.getDates();
		} else if (XSD_INTEGER.equals(this.getDataType())) {
			primitiveArray = this.getInts();
		} else if (XSD_STRING.equals(this.getDataType())) {
			primitiveArray = this.getStrings();
		} else {
			primitiveArray = this.getStrings();
		}
		return primitiveArray;
	}

	/**
	 * get strings.
	 * 
	 * @return strings
	 */
	public String[] getStrings() {
		String[] ss = null;
		if (this.getDataType().equals(XSD_STRING)) {
			ss = getSplitContent();
		}
		return ss;
	}

	/**
	 * splits content into tokens. if delimiter is whitespace, trims content and
	 * splits at whitespace (however long) else assume starts and ends with
	 * delimiter
	 * 
	 * @return the tokens
	 * @throws RuntimeException
	 *             if size attribute is inconsistent
	 */
	private String[] getSplitContent() throws RuntimeException {
		String[] ss = new String[0];
		String content = this.getXMLContent();
		if (content != null) {
			content = content.trim();
			ensureDelimiterAttribute(Action.PRESERVE);
			ss = delimiterAttribute.getSplitContent(content);
			int size = -1;
			if (this.getSizeAttribute() == null) {
				//size = ss.length;
				//setSize(size);
			} else {
				size = super.getSize();
				if (ss.length != size) {
					// FIXME this is not yet working
					// throw new CMLRuntime("Bad array length: "+size+"
					// incompatible
					// with elements: "+ss.length);
				}
			}
		}
		this.removeWhitespaceDelimiterAttribute();
		return ss;
	}

	/**
	 * get doubles.
	 * 
	 * @return doubles
	 * @throws RuntimeException
	 */
	public boolean[] getBooleans() throws RuntimeException {
		boolean[] dd = null;
		String dataType = this.getDataType();
		if (dataType != null
				&& XSD_BOOLEAN.equals(CMLType.getNormalizedValue(dataType))) {
			String[] ss = getSplitContent();
			dd = new boolean[ss.length];
			for (int i = 0; i < dd.length; i++) {
				dd[i] = new Boolean(ss[i]);
			}
		}
		return dd;
	}

	/**
	 * get doubles.
	 * 
	 * @return doubles
	 * @throws RuntimeException
	 */
	public double[] getDoubles() throws RuntimeException {
		double[] dd = null;
		String dataType = this.getDataType();
		if (dataType != null
				&& XSD_DOUBLE.equals(CMLType.getNormalizedValue(dataType))) {
			String[] ss = getSplitContent();
			dd = new double[ss.length];
			for (int i = 0; i < dd.length; i++) {
				try {
					dd[i] = Util.parseFlexibleDouble(ss[i]);
				} catch (NumberFormatException nfe) {
					throw new RuntimeException("Bad double :" + ss[i]
							+ " at position: " + i, nfe);
				} catch (ParseException e) {
					throw new RuntimeException("Bad double : " + ss[i]
							+ "at position " + i, e);
				}
			}
		}
		return dd;
	}

	/** convenience method returns a list of doubles for either xsd:integer or xsd:double
	 * 
	 * @return
	 */
	public double[] getNumbersAsDoubles() {
		double[] doubles = getDoubles();
		if (doubles == null) {
			int[] integers = getInts();
			if (integers != null) {
				RealArray realArray = RealArray.createRealArray(integers);
				doubles = (realArray == null) ? null : realArray.getArray();
			}
		}
		return doubles;
	}
	

	/**
	 * get dates
	 * 
	 * @return dates
	 * @throws RuntimeException
	 */
	public DateTime[] getDates() throws RuntimeException {
		DateTime[] dd = null;
		String dataType = this.getDataType();
		if (dataType != null
				&& XSD_DATE.equals(CMLType.getNormalizedValue(dataType))) {
			String[] ss = getSplitContent();
			dd = new DateTime[ss.length];
			for (int i = 0; i < dd.length; i++) {
				dd[i] = JodaDate.parseDate(ss[i]);
			}
		}
		return dd;
	}

	/**
	 * get ints.
	 * 
	 * @return ints
	 * @throws RuntimeException
	 */
	public int[] getInts() throws RuntimeException {
		int[] ii = null;
		String dataType = this.getDataType();
		if (XSD_INTEGER.equals(dataType)) {
			String[] ss = getSplitContent();
			ii = new int[ss.length];
			for (int i = 0; i < ii.length; i++) {
				try {
					ii[i] = new Integer(ss[i]).intValue();
				} catch (NumberFormatException nfe) {
					throw new RuntimeException("Bad int (" + ss[i]
							+ ") at position: " + i);
				}
			}
		}
		return ii;
	}

	public CMLScalar getElementAt(int i) {
		CMLScalar scalar = null;
		if (i >= 0 && i < getSize()) {
			String dataType = this.getDataType();
			if (dataType == null) {
				dataType = XSD_STRING;
			}

			if (dataType.equals(XSD_STRING)) {
				String s = getStrings()[i];
				scalar = new CMLScalar(s);
			} else if (dataType.equals(XSD_BOOLEAN)) {
				Boolean b = getBooleans()[i];
				scalar = new CMLScalar(b);
			} else if (dataType.equals(XSD_DATE)) {
				DateTime d = getDates()[i];
				scalar = new CMLScalar(d);
			} else if (dataType.equals(XSD_DOUBLE)) {
				Double d = getDoubles()[i];
				scalar = new CMLScalar(d);
			} else if (dataType.equals(XSD_INTEGER)) {
				Integer ii = getInts()[i];
				scalar = new CMLScalar(ii);
			}
			CMLArray.copyAttributesFromTo(this, scalar);
		}
		return scalar;
	}

	public static void copyAttributesFromTo(Element from, Element to) {
		copyAttributeTo(from, to, CMLAttribute.CONSTANT_TO_SI);
		copyAttributeTo(from, to, CMLAttribute.CONVENTION);
		copyAttributeTo(from, to, CMLAttribute.DICTREF);
		copyAttributeTo(from, to, CMLAttribute.ID);
		copyAttributeTo(from, to, CMLAttribute.MULTIPLIER_TO_SI);
		copyAttributeTo(from, to, CMLAttribute.TITLE);
		copyAttributeTo(from, to, CMLAttribute.UNITS);
	}

	private static void copyAttributeTo(Element from, Element to, String attName) {
		String attVal = from.getAttributeValue(attName);
		if (attVal != null) {
			to.addAttribute(new Attribute(attName, attVal));
		}
	}

	/**
	 * returns the String value of the array. convenience method to avoid
	 * repeated accesses i.e. converts int and double to string
	 * 
	 * @return strings
	 */
	public List<String> getStringValues() {
		List<String> values = new ArrayList<String>();
		String dataType = this.getDataType();
		if (dataType == null || dataType.equals(XSD_STRING)) {
			String[] strings = this.getStrings();
			for (String s : strings) {
				values.add(s);
			}
		} else if (XSD_INTEGER.equals(dataType)) {
			int[] ints = this.getInts();
			for (int i : ints) {
				values.add(S_EMPTY + i);
			}
		} else if (XSD_DOUBLE.equals(dataType)) {

			double[] doubles = this.getDoubles();
			for (double d : doubles) {
				values.add(S_EMPTY + d);
			}
		}
		return values;
	}

	// ====================== subsidiary accessors =====================

	/**
	 * sets components.
	 * 
	 * @param array
	 * @throws RuntimeException
	 */
	public void setArray(String[] array) throws RuntimeException {
		resetDataType(XSD_STRING);
		ensureDelimiterAttribute(Action.PRESERVE);
		for (String s : array) {
			delimiterAttribute.checkDelimiter(s);
		}
		setXMLContent(delimiterAttribute.getDelimitedXMLContent(array));
		resetSize(array.length);
		this.removeWhitespaceDelimiterAttribute();
	}

	private void resetDataType(String type) {
		Attribute a = (this.getAttribute("dataType"));
		if (a != null) {
			this.removeAttribute(a);
		}
		super.setDataType(type);
	}

	private void resetSize(int size) {
		Attribute a = (this.getAttribute("size"));
		if (a != null) {
			this.removeAttribute(a);
		}
		super.setSize(size);
	}

	/**
	 * sets components. NOT IMPLEMENTED
	 * 
	 * @param array
	 */
	public void setArray(DateTime[] array) {
		resetDataType(XSD_DATE);
		ensureDelimiterAttribute(Action.PRESERVE);
		// setXMLContent(delimiterAttribute.getDelimitedXMLContent(array));
		resetSize(array.length);
		throw new RuntimeException("dates in array not fully implemented");
		// this.removeWhitespaceDelimiterAttribute();
	}

	/**
	 * sets components.
	 * 
	 * @param array
	 */
	public void setArray(boolean[] array) {
		resetDataType(XSD_BOOLEAN);
		ensureDelimiterAttribute(Action.PRESERVE);
		setXMLContent(delimiterAttribute.getDelimitedXMLContent(array));
		resetSize(array.length);
		this.removeWhitespaceDelimiterAttribute();
	}

	/**
	 * sets components.
	 * 
	 * @param array
	 */
	public void setArray(double[] array) {
		resetDataType(XSD_DOUBLE);
		ensureDelimiterAttribute(Action.PRESERVE);
		setXMLContent(delimiterAttribute.getDelimitedXMLContent(array));
		resetSize(array.length);
		this.removeWhitespaceDelimiterAttribute();
	}

	/**
	 * sets components.
	 * 
	 * @param array
	 */
	public void setArray(int[] array) {
		resetDataType(XSD_INTEGER);
		ensureDelimiterAttribute(Action.PRESERVE);
		setXMLContent(delimiterAttribute.getDelimitedXMLContent(array));
		resetSize(array.length);
		this.removeWhitespaceDelimiterAttribute();
	}

	/**
	 * gets size of array.
	 * 
	 * @return int size of array
	 */
	public int getSize() {
		int size = -1;
		if (this.getSizeAttribute() != null) {
			size = super.getSize();
		} else {
			String[] array = this.getSplitContent();
			size = array.length;
		}
		return size;
	}

	/**
	 * reset null to whitespace, etc.
	 * 
	 * @return String
	 */
	public String getDelimiter() {
		String delimiter = super.getDelimiter();
		if (delimiter == null) {
			ensureDelimiterAttribute(Action.RESET);
			delimiter = delimiterAttribute.getValue();
		}
		this.removeWhitespaceDelimiterAttribute();
		return delimiter;
	}

	/**
	 * set delimiter.
	 * 
	 * @param value
	 */
	public void setDelimiter(String value) {
		String[] old = this.getSplitContent();
		ensureDelimiterAttribute(Action.RESET);
		super.setDelimiter(value);
		delimiterAttribute = (DelimiterAttribute) this.getDelimiterAttribute();
		if (old.length>0) {
			for (String s : old) {
				delimiterAttribute.checkDelimiter(s);
			}
			setXMLContent(delimiterAttribute.getDelimitedXMLContent(old));
		}
		removeWhitespaceDelimiterAttribute();
	}

	/**
	 * get dataType. if attribute not set, reset to String.
	 * 
	 * @return dataType (default XSD_STRING)
	 */
	public String getDataType() {
		String dataType = super.getDataType();
		if (dataType == null) {
			dataType = XSD_STRING;
			super.setDataType(dataType);
		}
		return CMLType.getNormalizedValue(dataType);
	}

	/**
	 * set dataType.
	 * 
	 * sets dataType. Cannot reset after array is populated
	 * 
	 * @param dType
	 *            (default XSD_STRING)
	 * @throws RuntimeException
	 *             attempt to reset datatype
	 */
	public void setDataType(String dType) {
		if (this.getDataTypeAttribute() != null) {
			throw new RuntimeException("Cannot reset dataType");
		}
		super.setDataType(dType);
	}

	/**
	 * set size.
	 * 
	 * @deprecated not user-accesible - throws CMLRuntime sets delimiter. Cannot
	 *             reset after array is populated if delimiter is whitespace,
	 *             removes the attribute
	 * @param s
	 *            the size
	 * @throws RuntimeException
	 *             attempt to reset datatype
	 */
	public void setSize(int s) {
		if (this.getSizeAttribute() != null) {
			throw new RuntimeException("user cannot reset size");
		}
		super.setSize(s);
	}

	// ====================== functionality =====================

	/**
	 * can two arrays be used for arithmetic. checks that both arrays are
	 * numeric and of same dataType and of same size
	 * 
	 * @param array
	 *            the array to test; can have different owner
	 * @throws RuntimeException
	 *             if not of same numeric data type and size
	 */
	public void checkNumericConformability(CMLArray array) {
		String thisDataType = this.getDataType();
		String arrayDataType = array.getDataType();
		if (thisDataType.equals(XSD_STRING)
				|| !thisDataType.equals(arrayDataType)
				|| this.getSize() != array.getSize()) {
			throw new RuntimeException(
					"Unsuitable dataTypes for numeric operations / "
							+ this.getDataType() + CMLConstants.S_SLASH
							+ this.getSize() + CMLConstants.S_SLASH
							+ array.getDataType() + CMLConstants.S_SLASH
							+ array.getSize());
		}
	}

	/**
	 * subtract an array from this..
	 * 
	 * result = this - array, owner document = this does not alter this only
	 * works if both arrays are numeric and of same dataType
	 * 
	 * @param array
	 *            the array to subtract; can have different owner
	 * @throws RuntimeException
	 *             inappropriate dataTypes, unequal arrays
	 * @return new array
	 */
	public CMLArray subtract(CMLArray array) {
		checkNumericConformability(array);
		CMLArray resultArray = null;
		try {
			if (this.getDataType().equals(XSD_DOUBLE)) {
				RealArray result = new RealArray(array.getDoubles())
						.subtract(new RealArray(this.getDoubles()));
				resultArray = new CMLArray(result.getArray());
			} else if (this.getDataType().equals(XSD_INTEGER)) {
				IntArray result = new IntArray(array.getInts())
						.subtract(new IntArray(this.getInts()));
				resultArray = new CMLArray(result.getArray());
			}
		} catch (EuclidRuntimeException je) {
			throw new RuntimeException(S_EMPTY + je);
		}
		return resultArray;
	}

	/**
	 * add an array to this..
	 * 
	 * result is this + array, owner document = this does not alter this
	 * 
	 * only works if both arrays are numeric and of same dataType
	 * 
	 * @param array
	 *            the array to add; can have different owner
	 * @throws RuntimeException
	 *             inappropriate dataTypes, unequal arrays
	 * 
	 * @return the new array
	 */
	public CMLArray plus(CMLArray array) {
		checkNumericConformability(array);
		CMLArray resultArray = null;
		try {
			if (this.getDataType().equals(XSD_DOUBLE)) {
				RealArray result = new RealArray(this.getDoubles())
						.plus(new RealArray(array.getDoubles()));
				resultArray = new CMLArray(result.getArray());
			} else if (this.getDataType().equals(XSD_INTEGER)) {
				IntArray result = new IntArray(this.getInts())
						.plus(new IntArray(array.getInts()));
				resultArray = new CMLArray(result.getArray());
			}
		} catch (EuclidRuntimeException je) {
			throw new RuntimeException(S_EMPTY + je);
		}
		return resultArray;
	}

	/**
	 * add a string.
	 * 
	 * datatype must be unset or have been set to XSD_STRING
	 * 
	 * @param s
	 *            String to add
	 * 
	 * @throws RuntimeException
	 *             dataType not XSD_STRING
	 */
	public void append(String s) throws RuntimeException {
		String dataType = this.getDataType();
		if (!XSD_STRING.equals(dataType)) {
			throw new RuntimeException("Cannot add string (" + s
					+ ") to array of: " + dataType);
		}
		appendXML(s, 1);
	}

	/**
	 * add a double. datatype must have been set to XSD_DOUBLE
	 * 
	 * @param b
	 *            boolean to add
	 * @throws RuntimeException
	 *             dataType not XSD_BOOLEAN
	 */
	public void append(boolean b) throws RuntimeException {
		String dataType = this.getDataType();
		if (!XSD_BOOLEAN.equals(dataType)) {
			throw new RuntimeException("Cannot add boolean to array of: "
					+ dataType);
		}
		appendXML(S_EMPTY + b, 1);
	}

	/**
	 * add a double. datatype must have been set to XSD_DOUBLE
	 * 
	 * @param d
	 *            double to add
	 * @throws RuntimeException
	 *             dataType not XSD_DOUBLE
	 */
	public void append(double d) throws RuntimeException {
		String dataType = this.getDataType();
		if (!XSD_DOUBLE.equals(dataType)) {
			throw new RuntimeException("Cannot add double to array of: "
					+ dataType);
		}
		appendXML(Double.toString(d), 1);
	}

	/**
	 * add an integer. datatype must have been set to XSD_INTEGER
	 * 
	 * @param i
	 *            integer to add
	 * @throws RuntimeException
	 *             dataType not XSD_INTEGER
	 */
	public void append(int i) throws RuntimeException {
		String dataType = this.getDataType();
		if (!XSD_INTEGER.equals(dataType)) {
			throw new RuntimeException("Cannot add int to array of: "
					+ dataType);
		}
		appendXML(S_EMPTY + i, 1);
	}

	public void append(CMLArray array) {
		if (!this.getDataType().equals(array.getDataType())) {
			throw new RuntimeException(
					"Cannot append array of different type: "
							+ this.getDataType() + " != " + array.getDataType());
		}
		if (!this.getDelimiter().equals(array.getDelimiter())) {
			throw new RuntimeException(
					"Cannot append array with different delimiter: "
							+ this.getDelimiter() + " != "
							+ array.getDelimiter());
		}
		if (this.getUnits() != null
				&& !this.getUnits().equals(array.getUnits())) {
			throw new RuntimeException(
					"Cannot append array with different units: "
							+ this.getDelimiter() + " != " + array.getUnits());
		}
		String arrayString = array.getXMLContent();
		String delimiter = this.getDelimiter().trim();
		if (delimiter.length() > 0) {
			arrayString = arrayString.substring(1, arrayString.length() - 1);
		}
		appendXML(arrayString, array.getSize());
	}

	private void appendXML(String s, int toAdd) {
		int size = (this.getSizeAttribute() == null) ? 0 : this.getSize();
		ensureDelimiterAttribute(Action.PRESERVE);
		if (toAdd <= 1) {
			delimiterAttribute.checkDelimiter(s);
		}
		String xmlContent = this.getXMLContent();
		String delimitedContent = delimiterAttribute.appendXMLContent(
				xmlContent, s);
		this.setXMLContent(delimitedContent);
		resetSize(size + toAdd);
		this.removeWhitespaceDelimiterAttribute();
	}

	public void append(CMLScalar scalar) {
		if (scalar != null) {
			String dataType = this.getDataType();
			if (!dataType.equals(scalar.getDataType())) {
				throw new RuntimeException(
						"Cannot append scalar of different type: " + dataType
								+ " != " + scalar.getDataType());
			}
			if (this.getUnits() != null
					&& !this.getUnits().equals(scalar.getUnits())) {
				throw new RuntimeException(
						"Cannot append scalar with different units: "
								+ this.getDelimiter() + " != "
								+ scalar.getUnits());
			}
			append(scalar.getXMLContent(), dataType);
			this.removeWhitespaceDelimiterAttribute();
		}
	}

	private void append(String content, String dataType) {
		if (XSD_STRING.equals(dataType)) {
			this.append(content);
		} else if (XSD_BOOLEAN.equals(dataType)) {
			this.append(new Boolean(content).booleanValue());
		} else if (XSD_DATE.equals(dataType)) {
			this.append(JodaDate.parseDate(content).toString());
		} else if (XSD_DOUBLE.equals(dataType)) {
			this.append(new Double(content).doubleValue());
		} else if (XSD_INTEGER.equals(dataType)) {
			this.append(new Integer(content).intValue());
		}
	}

	public void append(HasDictRef hasDictRef) {
		if (hasDictRef instanceof CMLScalar) {
			this.append((CMLScalar) hasDictRef);
		} else if (hasDictRef instanceof CMLArray) {
			this.append((CMLArray) hasDictRef);
		} else {
			throw new RuntimeException("Cannot add HasDictRef: "
					+ ((CMLElement) hasDictRef).getLocalName());
		}
		this.removeWhitespaceDelimiterAttribute();
		// this.debug("APPPEND");
	}

	/**
	 * sets units attribute. requires namespace for unit to be in scope.
	 * 
	 * @param prefix
	 *            for namespace
	 * @param id
	 *            for unit
	 * @param namespaceURI
	 *            sets units namespace if not present already
	 */
	public void setUnits(String prefix, String id, String namespaceURI) {
		NamespaceRefAttribute.setUnits((HasUnits) this, prefix, id,
				namespaceURI);
	}

	/**
	 * add an integer. datatype must have been set to XSD_DATE
	 * 
	 * @param d
	 *            date to add
	 */
	public void append(DateTime d) throws RuntimeException {
		String dataType = this.getDataType();
		if (!XSD_DATE.equals(dataType)) {
			throw new RuntimeException("Cannot add date to array of: "
					+ dataType);
		}
		appendXML(d.toString(), 1);
	}

	/**
	 * removes attributes of the form delimiter="" or delimiter=" "
	 */
	public void removeWhitespaceDelimiterAttribute() {
		CMLArray.removeWhitespaceDelimiterAttribute(this);
	}

	public static void removeWhitespaceDelimiterAttribute(
			HasDelimiter hasDelimiter) {
		Attribute delimiter = hasDelimiter.getDelimiterAttribute();
		if (delimiter != null && delimiter.getValue().trim().length() == 0) {
			delimiter.detach();
		}
	}

	/** makes a list of CMLArrays
	 * 
	 * @param elements
	 * @return List&lt;CMLArray&gt;
	 */
	public static List<CMLArray> extractArrays(List<Element> elements) {
		List<CMLArray> arrayList = new ArrayList<CMLArray>();
		for (Element element : elements) {
			if (element instanceof CMLArray) {
				arrayList.add((CMLArray) element);
			}
		}
		return arrayList;
	}
	
}
