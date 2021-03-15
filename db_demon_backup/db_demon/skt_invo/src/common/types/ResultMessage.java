/**
 * ResultMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package common.types;

public class ResultMessage  implements java.io.Serializable {
    private common.types.ResultMessageResult result;

    private java.lang.String messageId;

    private java.lang.String messageName;

    private java.lang.String messageReason;

    private java.lang.String messageRemark;

    private java.lang.String recordSetId;

    private java.lang.String recordId;

    private java.lang.String fieldId;

    private java.math.BigInteger affectedRowCount;

    private java.lang.String exceptionStackTrace;

    public ResultMessage() {
    }

    public ResultMessage(
           common.types.ResultMessageResult result,
           java.lang.String messageId,
           java.lang.String messageName,
           java.lang.String messageReason,
           java.lang.String messageRemark,
           java.lang.String recordSetId,
           java.lang.String recordId,
           java.lang.String fieldId,
           java.math.BigInteger affectedRowCount,
           java.lang.String exceptionStackTrace) {
           this.result = result;
           this.messageId = messageId;
           this.messageName = messageName;
           this.messageReason = messageReason;
           this.messageRemark = messageRemark;
           this.recordSetId = recordSetId;
           this.recordId = recordId;
           this.fieldId = fieldId;
           this.affectedRowCount = affectedRowCount;
           this.exceptionStackTrace = exceptionStackTrace;
    }


    /**
     * Gets the result value for this ResultMessage.
     * 
     * @return result
     */
    public common.types.ResultMessageResult getResult() {
        return result;
    }


    /**
     * Sets the result value for this ResultMessage.
     * 
     * @param result
     */
    public void setResult(common.types.ResultMessageResult result) {
        this.result = result;
    }


    /**
     * Gets the messageId value for this ResultMessage.
     * 
     * @return messageId
     */
    public java.lang.String getMessageId() {
        return messageId;
    }


    /**
     * Sets the messageId value for this ResultMessage.
     * 
     * @param messageId
     */
    public void setMessageId(java.lang.String messageId) {
        this.messageId = messageId;
    }


    /**
     * Gets the messageName value for this ResultMessage.
     * 
     * @return messageName
     */
    public java.lang.String getMessageName() {
        return messageName;
    }


    /**
     * Sets the messageName value for this ResultMessage.
     * 
     * @param messageName
     */
    public void setMessageName(java.lang.String messageName) {
        this.messageName = messageName;
    }


    /**
     * Gets the messageReason value for this ResultMessage.
     * 
     * @return messageReason
     */
    public java.lang.String getMessageReason() {
        return messageReason;
    }


    /**
     * Sets the messageReason value for this ResultMessage.
     * 
     * @param messageReason
     */
    public void setMessageReason(java.lang.String messageReason) {
        this.messageReason = messageReason;
    }


    /**
     * Gets the messageRemark value for this ResultMessage.
     * 
     * @return messageRemark
     */
    public java.lang.String getMessageRemark() {
        return messageRemark;
    }


    /**
     * Sets the messageRemark value for this ResultMessage.
     * 
     * @param messageRemark
     */
    public void setMessageRemark(java.lang.String messageRemark) {
        this.messageRemark = messageRemark;
    }


    /**
     * Gets the recordSetId value for this ResultMessage.
     * 
     * @return recordSetId
     */
    public java.lang.String getRecordSetId() {
        return recordSetId;
    }


    /**
     * Sets the recordSetId value for this ResultMessage.
     * 
     * @param recordSetId
     */
    public void setRecordSetId(java.lang.String recordSetId) {
        this.recordSetId = recordSetId;
    }


    /**
     * Gets the recordId value for this ResultMessage.
     * 
     * @return recordId
     */
    public java.lang.String getRecordId() {
        return recordId;
    }


    /**
     * Sets the recordId value for this ResultMessage.
     * 
     * @param recordId
     */
    public void setRecordId(java.lang.String recordId) {
        this.recordId = recordId;
    }


    /**
     * Gets the fieldId value for this ResultMessage.
     * 
     * @return fieldId
     */
    public java.lang.String getFieldId() {
        return fieldId;
    }


    /**
     * Sets the fieldId value for this ResultMessage.
     * 
     * @param fieldId
     */
    public void setFieldId(java.lang.String fieldId) {
        this.fieldId = fieldId;
    }


    /**
     * Gets the affectedRowCount value for this ResultMessage.
     * 
     * @return affectedRowCount
     */
    public java.math.BigInteger getAffectedRowCount() {
        return affectedRowCount;
    }


    /**
     * Sets the affectedRowCount value for this ResultMessage.
     * 
     * @param affectedRowCount
     */
    public void setAffectedRowCount(java.math.BigInteger affectedRowCount) {
        this.affectedRowCount = affectedRowCount;
    }


    /**
     * Gets the exceptionStackTrace value for this ResultMessage.
     * 
     * @return exceptionStackTrace
     */
    public java.lang.String getExceptionStackTrace() {
        return exceptionStackTrace;
    }


    /**
     * Sets the exceptionStackTrace value for this ResultMessage.
     * 
     * @param exceptionStackTrace
     */
    public void setExceptionStackTrace(java.lang.String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultMessage)) return false;
        ResultMessage other = (ResultMessage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.result==null && other.getResult()==null) || 
             (this.result!=null &&
              this.result.equals(other.getResult()))) &&
            ((this.messageId==null && other.getMessageId()==null) || 
             (this.messageId!=null &&
              this.messageId.equals(other.getMessageId()))) &&
            ((this.messageName==null && other.getMessageName()==null) || 
             (this.messageName!=null &&
              this.messageName.equals(other.getMessageName()))) &&
            ((this.messageReason==null && other.getMessageReason()==null) || 
             (this.messageReason!=null &&
              this.messageReason.equals(other.getMessageReason()))) &&
            ((this.messageRemark==null && other.getMessageRemark()==null) || 
             (this.messageRemark!=null &&
              this.messageRemark.equals(other.getMessageRemark()))) &&
            ((this.recordSetId==null && other.getRecordSetId()==null) || 
             (this.recordSetId!=null &&
              this.recordSetId.equals(other.getRecordSetId()))) &&
            ((this.recordId==null && other.getRecordId()==null) || 
             (this.recordId!=null &&
              this.recordId.equals(other.getRecordId()))) &&
            ((this.fieldId==null && other.getFieldId()==null) || 
             (this.fieldId!=null &&
              this.fieldId.equals(other.getFieldId()))) &&
            ((this.affectedRowCount==null && other.getAffectedRowCount()==null) || 
             (this.affectedRowCount!=null &&
              this.affectedRowCount.equals(other.getAffectedRowCount()))) &&
            ((this.exceptionStackTrace==null && other.getExceptionStackTrace()==null) || 
             (this.exceptionStackTrace!=null &&
              this.exceptionStackTrace.equals(other.getExceptionStackTrace())));
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
        if (getResult() != null) {
            _hashCode += getResult().hashCode();
        }
        if (getMessageId() != null) {
            _hashCode += getMessageId().hashCode();
        }
        if (getMessageName() != null) {
            _hashCode += getMessageName().hashCode();
        }
        if (getMessageReason() != null) {
            _hashCode += getMessageReason().hashCode();
        }
        if (getMessageRemark() != null) {
            _hashCode += getMessageRemark().hashCode();
        }
        if (getRecordSetId() != null) {
            _hashCode += getRecordSetId().hashCode();
        }
        if (getRecordId() != null) {
            _hashCode += getRecordId().hashCode();
        }
        if (getFieldId() != null) {
            _hashCode += getFieldId().hashCode();
        }
        if (getAffectedRowCount() != null) {
            _hashCode += getAffectedRowCount().hashCode();
        }
        if (getExceptionStackTrace() != null) {
            _hashCode += getExceptionStackTrace().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultMessage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:types.common", "ResultMessage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:types.common", ">ResultMessage>result"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "messageId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "messageName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageReason");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "messageReason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageRemark");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "messageRemark"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordSetId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "recordSetId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "recordId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "fieldId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("affectedRowCount");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "affectedRowCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionStackTrace");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:types.common", "exceptionStackTrace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
