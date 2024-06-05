import java.io.Serializable;
public class Incident implements Serializable {
    public int id;
    public String problem;
    public Incident(){
        this.problem="";
    }
    public Incident(int id,String opisProblema){
        this.id=id;
        this.problem=opisProblema;
    }
    public String getProblem() {
        return problem;
    }
    public void setProblem(String opisProblema) {
        this.problem=opisProblema;
    }
    @Override
    public String toString(){
        return id+problem;
    }
    public void setId(int id) { this.id = id; }
    public int getId() { return id;}
}
