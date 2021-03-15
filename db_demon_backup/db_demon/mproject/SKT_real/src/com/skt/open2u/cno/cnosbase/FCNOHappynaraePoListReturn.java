/**
 * FCNOHappynaraePoListReturn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.skt.open2u.cno.cnosbase;

public class FCNOHappynaraePoListReturn  implements java.io.Serializable {
    private com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord[] RESULT_LIST;

    private common.types.ResultMessage resultMessage;

    public FCNOHappynaraePoListReturn() {
    }

    public FCNOHappynaraePoListReturn(
           com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord[] RESULT_LIST,
           common.types.ResultMessage resultMessage) {
           this.RESULT_LIST = RESULT_LIST;
           this.resultMessage = resultMessage;
    }


    /**
     * Gets the RESULT_LIST value for this FCNOHappynaraePoListReturn.
     * 
     * @return RESULT_LIST
     */
    public com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord[] getRESULT_LIST() {
        return RESULT_LIST;
    }


    /**
     * Sets the RESULT_LIST value for this FCNOHappynaraePoListReturn.
     * 
     * @param RESULT_LIST
     */
    public void setRESULT_LIST(com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord[] RESULT_LIST) {
        this.RESULT_LIST = RESULT_LIST;
    }

    public com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord getRESULT_LIST(int i) {
        return this.RESULT_LIST[i];
    }

    public void setRESULT_LIST(int i, com.skt.open2u.cno.cnosbase.FCNOHappynaraePoListReturnRESULT_LISTRecord _value) {
        this.RESULT_LIST[i] = _value;
    }


    /**
     * Gets the resultMessage value for this FCNOHappynaraePoListReturn.
     * 
     * @return resultMessage
     */
    public common.types.ResultMessage getResultMessage() {
        return resultMessage;
    }


    /**
     * Sets the resultMessage value for this FCNOHappynaraePoListReturn.
     * 
     * @param resultMessage
     */
    public void setResultMessage(common.types.ResultMessage resultMessage) {
        this.resultMessage = resultMessage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FCNOHappynaraePoListReturn)) return false;
        FCNOHappynaraePoListReturn other = (FCNOHappynaraePoListReturn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.RESULT_LIST==null && other.getRESULT_LIST()==null) || 
             (this.RESULT_LIST!=null &&
              java.util.Arrays.equals(this.RESULT_LIST, other.getRESULT_LIST()))) &&
            ((this.resultMessage==null && other.getResultMessage()==null) || 
             (this.resultMessage!=null &&
              this.resultMessage.equals(other.getResultMessage())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRESULT_LIST() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRESULT_LIST());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRESULT_LIST(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResultMessage() != null) {
            _hashCode += getResultMessage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FCNOHappynaraePoListReturn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("componentNs:cnosbase.cno.open2u.skt.com/cno.CNOSBase", "fCNOHappynaraePoListReturn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RESULT_LIST");
        elemField.setXmlName(new javax.xml.namespace.QName("componentNs:cnosbase.cno.open2u.skt.com/cno.CNOSBase", "RESULT_LIST"));
        elemField.setXmlType(new javax.xml.namespace.QName("componentNs:cnosbase.cno.open2u.skt.com/cno.CNOSBase", "fCNOHappynaraePoListReturnRESULT_LISTRecord"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("componentNs:cnosbase.cno.open2u.skt.com/cno.CNOSBase", "resultMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:types.common", "ResultMessage"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
