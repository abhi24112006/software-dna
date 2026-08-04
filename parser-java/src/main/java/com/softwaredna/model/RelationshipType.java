package com.softwaredna.model;

public enum RelationshipType {

    EXTENDS,

    IMPLEMENTS,

    FIELD_DEPENDENCY,

    PARAMETER_DEPENDENCY,

    RETURN_DEPENDENCY,

    METHOD_CALL_INTERNAL,
    
    METHOD_CALL_EXTERNAL

}