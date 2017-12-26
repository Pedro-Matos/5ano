package RSI.Structure;


/**
 * Created by Pedro Matos
 * Contains the information of every triplet.
 */
public class Triplet {
    private String CodeMeaning, CodeValue, CodingSchemeDesignator, CodingSchemeVersion, Mapping, Rel_With_Parent, VT, VM;
    private String NL, Req_Type, Condition, Constraint, TID_order;

    public String getCodeMeaning() {
        return CodeMeaning;
    }

    public void setCodeMeaning(String codeMeaning) {
        CodeMeaning = codeMeaning;
    }

    public String getCodeValue() {
        return CodeValue;
    }

    public void setCodeValue(String codeValue) {
        CodeValue = codeValue;
    }

    public String getCodingSchemeDesignator() {
        return CodingSchemeDesignator;
    }

    public void setCodingSchemeDesignator(String codingSchemeDesignator) {
        CodingSchemeDesignator = codingSchemeDesignator;
    }

    public String getCodingSchemeVersion() {
        return CodingSchemeVersion;
    }

    public void setCodingSchemeVersion(String codingSchemeVersion) {
        CodingSchemeVersion = codingSchemeVersion;
    }

    public String getMapping() {
        return Mapping;
    }

    public void setMapping(String mapping) {
        Mapping = mapping;
    }

    public String getRel_With_Parent() {
        return Rel_With_Parent;
    }

    public void setRel_With_Parent(String rel_With_Parent) {
        Rel_With_Parent = rel_With_Parent;
    }

    public String getVT() {
        return VT;
    }

    public void setVT(String VT) {
        this.VT = VT;
    }

    public String getVM() {
        return VM;
    }

    public void setVM(String VM) {
        this.VM = VM;
    }

    public String getNL() {
        return NL;
    }

    public void setNL(String NL) {
        this.NL = NL;
    }

    public String getReq_Type() {
        return Req_Type;
    }

    public void setReq_Type(String req_Type) {
        Req_Type = req_Type;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getConstraint() {
        return Constraint;
    }

    public void setConstraint(String constraint) {
        Constraint = constraint;
    }

    public String getTID_order() {
        return TID_order;
    }

    public void setTID_order(String TID_order) {
        this.TID_order = TID_order;
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "CodeMeaning='" + CodeMeaning + '\'' +
                ", CodeValue='" + CodeValue + '\'' +
                ", CodingSchemeDesignator='" + CodingSchemeDesignator + '\'' +
                ", CodingSchemeVersion='" + CodingSchemeVersion + '\'' +
                ", Mapping='" + Mapping + '\'' +
                ", Rel_With_Parent='" + Rel_With_Parent + '\'' +
                ", VT='" + VT + '\'' +
                ", VM='" + VM + '\'' +
                ", NL='" + NL + '\'' +
                ", Req_Type='" + Req_Type + '\'' +
                ", Condition='" + Condition + '\'' +
                ", Constraint='" + Constraint + '\'' +
                ", TID_order='" + TID_order + '\'' +
                '}';
    }
}
