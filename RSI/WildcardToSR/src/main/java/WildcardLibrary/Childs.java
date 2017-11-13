package WildcardLibrary;

/**
 * Created by Pedro Matos
 *
 * This class contains all the information relationed with the node childs.
 * This nodes can be TIDs or CIDs, and because of that there is a field 'Type' that contains that description.
 * To know the name of the json file with the information of the child, one can acess the field 'File_Name
 *
 */
public class Childs {
    private String NL, Rel_With_Parent, VT, VM, Req_Type, Condition, Constraint, Name, Type , TID_order, File_Name;

    public String getNL() {
        return NL;
    }

    public void setNL(String NL) {
        this.NL = NL;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTID_order() {
        return TID_order;
    }

    public void setTID_order(String TID_order) {
        this.TID_order = TID_order;
    }

    public String getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(String file_Name) {
        File_Name = file_Name;
    }

    @Override
    public String toString() {
        return "Childs{" +
                "NL='" + NL + '\'' +
                ", Rel_With_Parent='" + Rel_With_Parent + '\'' +
                ", VT='" + VT + '\'' +
                ", VM='" + VM + '\'' +
                ", Req_Type='" + Req_Type + '\'' +
                ", Condition='" + Condition + '\'' +
                ", Constraint='" + Constraint + '\'' +
                ", Name='" + Name + '\'' +
                ", Type='" + Type + '\'' +
                ", TID_order='" + TID_order + '\'' +
                ", File_Name='" + File_Name + '\'' +
                '}';
    }
}
