package org.xmlcml.cml.interfacex;

import org.xmlcml.cml.base.CMLAttribute;

/**
 * attached to elements that can carry dictRef. 
 * examples are scalar, array, matrix, property
 * 
 * @author pmr
 * 
 */
public interface HasDictRef {

//    /**
//     * sets value on dictRef attribute. 
//     * example: setDictRef("myRef", "floop");
//     * 
//     * @param prefix
//     * @param idRef
//     * @param namespaceURI
//     */
//    void setDictRef(String prefix, String idRef, String namespaceURI);

    /** get dictRef attribute.
     * 
     * @return attribute (must be CMLAttribute to be compatible with autogenerated code)
     */
    CMLAttribute getDictRefAttribute();

    /** get dictRef value.
     * 
     * @return value
     */
    String getDictRef();


    /** set dictRef value.
     * 
     * @return value
     */
    void setDictRef(String dictRef);

}