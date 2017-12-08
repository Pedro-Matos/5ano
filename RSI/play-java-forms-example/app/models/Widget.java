package models;

/**
 * Presentation object used for displaying data in a template.
 *
 * Note that it's a good practice to keep the presentation DTO,
 * which are used for reads, distinct from the form processing DTO,
 * which are used for writes.
 */
public class Widget {
    public String doctorName;
    public String patientName;
    public int patientid;


    public String physicalExaminationResults;
    public String comparisontopreviousexams;
    public String findings;
    public String recommendations;
    public String conclusions;

    public String pathologyResults;
    public String pathology;
    public String malignancyType;
    public String nippleinvolved;
    public int numbernodesremoved;
    public int numbernodespositive;

    public String distancefromnipple;
    public String distancefromskin;
    public String distancefromchestwall;


    public Widget(String name, String patientName,int patientid, String physicalExaminationResults, String comparisontopreviousexams,
                  String findings, String recommendations, String conclusions,
                  String pathologyResults, String pathology, String malignancyType, String nippleinvolved, int numbernodesremoved, int numbernodespositive,
                  String distancefromnipple, String distancefromskin, String distancefromchestwall) {
        this.doctorName = name;
        this.patientName = patientName;
        this.patientid = patientid;

        this.physicalExaminationResults = physicalExaminationResults;
        this.comparisontopreviousexams = comparisontopreviousexams;
        this.findings = findings;
        this.recommendations = recommendations;
        this.conclusions = conclusions;

        this.pathologyResults = pathologyResults;
        this.pathology = pathology;
        this.malignancyType = malignancyType;
        this.nippleinvolved = nippleinvolved;
        this.numbernodesremoved = numbernodesremoved;
        this.numbernodespositive = numbernodespositive;

        this.distancefromnipple = distancefromnipple;
        this.distancefromskin = distancefromskin;
        this.distancefromchestwall = distancefromchestwall;

    }
}
