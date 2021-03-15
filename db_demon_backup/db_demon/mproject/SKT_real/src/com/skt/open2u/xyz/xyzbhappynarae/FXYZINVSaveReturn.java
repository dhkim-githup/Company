/**
 * FXYZINVSaveReturn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.skt.open2u.xyz.xyzbhappynarae;

public class FXYZINVSaveReturn  implements java.io.Serializable {
    private java.lang.String RESULT;

    private java.lang.String MSG;

    private java.lang.String INFKEY;

    private common.types.ResultMessage resultMessage;

    public FXYZINVSaveReturn() {
    }

    public FXYZINVSaveReturn(
           java.lang.String RESULT,
           java.lang.String MSG,
           java.lang.String INFKEY,
           common.types.ResultMessage resultMessage) {
           this.RESULT = RESULT;
           this.MSG = MSG;
           this.INFKEY = INFKEY;
           this.resultMessage = resultMessage;
    }


    /**
     * Gets the RESULT value for this FXYZINVSaveReturn.
     * 
     * @return RESULT
     */
    public java.lang.String getRESULT() {
        return RESULT;
    }


    /**
     * Sets the RESULT value for this FXYZINVSaveReturn.
     * 
     * @param RESULT
     */
    public void setRESULT(java.lang.String RESULT) {
        this.RESULT = RESULT;
    }


    /**
     * Gets the MSG value for this FXYZINVSaveReturn.
     * 
     * @return MSG
     */
    public java.lang.String getMSG() {
        return MSG;
    }


    /**
     * Sets the MSG value for this FXYZINVSaveReturn.
     * 
     * @param MSG
     */
    public void setMSG(java.lang.String MSG) {
        this.MSG = MSG;
    }


    /**
     * Gets the INFKEY value for this FXYZINVSaveReturn.
     * 
     * @return INFKEY
     */
    public java.lang.String getINFKEY() {
        return INFKEY;
    }


    /**
     * Sets the INFKEY value for this FXYZINVSaveReturn.
     * 
     * @param INFKEY
     */
    public void setINFKEY(java.lang.String INFKEY) {
        this.INFKEY = INFKEY;
    }


    /**
     * Gets the resultMessage value for this FXYZINVSaveReturn.
     * 
     * @return resultMessage
     */
    public common.types.ResultMessage getResultMessage() {
        return resultMessage;
    }


    /**
     * Sets the resultMessage value for this FXYZINVSaveReturn.
     * 
     * @param resultMessage
     */
    public void setResultMessage(common.types.ResultMessage resultMessage) {
        this.resultMessage = resultMessage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FXYZINVSaveReturn)) return false;
        FXYZINVSaveReturn other = (FXYZINVSaveReturn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.RESULT==null && other.getRESULT()==null) || 
             (this.RESULT!=null &&
              this.RESULT.equals(other.getRESULT()))) &&
            ((this.MSG==null && other.getMSG()==null) || 
             (this.MSG!=null &&
              this.MSG.equals(other.getMSG()))) &&
            ((this.INFKEY==null && other.getINFKEY()==null) || 
             (this.INFKEY!=null &&
              this.INFKEY.equals(other.getINFKEY()))) &&
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
        if (getRESULT() != null) {
            _hashCode += getRESULT().hashCode();
        }
        if (getMSG() != null) {
            _hashCode += getMSG().hashCode();
        }
        if (getINFKEY() != null) {
            _hashCode += getINFKEY().hashCode();
        }
        if (getResultMessage() != null) {
            _hashCode += getResultMessage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FXYZINVSaveReturn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("componentNs:xyzbhappynarae.xyz.open2u.skt.com/xyz.XYZBHappyNarae", "fXYZINVSaveReturn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RESULT");
        elemField.setXmlName(new javax.xml.namespace.QName("componentNs:xyzbhappynarae.xyz.open2u.skt.com/xyz.XYZBHappyNarae", "RESULT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSG");
        elemField.setXmlName(new javax.xml.namespace.QName("componentNs:xyzbhappynarae.xyz.open2u.skt.com/xyz.XYZBHappyNarae", "MSG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("INFKEY");
        elemField.setXmlName(new javax.xml.namespace.QName("componentNs:xyzbhappynarae.xyz.open2u.skt.com/xyz.XYZBHappyNarae", "INFKEY"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("componentNs:xyzbhappynarae.xyz.open2u.skt.com/xyz.XYZBHappyNarae", "resultMessage"));
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
