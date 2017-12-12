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
    public String birth_date;
    public String patient_sex;
    public String organization;
    public String language;

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

    public String quadrant_location;
    public String depth;
    public String lesion_density;
    public String margins;
    public String calcification_type;
    public String calcification_distribution;
    public String number_of_calc;

    public String recommended_followup;
    public String recommended_followup_interval;
    public int bi_rads;



    public Widget(String name, String patientName,int patientid, String birth_date, String patient_sex, String organization, String language,
                  String physicalExaminationResults, String comparisontopreviousexams,
                  String findings, String recommendations, String conclusions,
                  String pathologyResults, String pathology, String malignancyType, String nippleinvolved, int numbernodesremoved, int numbernodespositive,
                  String distancefromnipple, String distancefromskin, String distancefromchestwall,
                  String quadrant_location, String depth, String lesion_density, String margins, String calcification_type,
                  String calcification_distribution, String number_of_calc,
                  String recommended_followup, String recommended_followup_interval, int bi_rads) {
        this.doctorName = name;
        this.patientName = patientName;
        this.patientid = patientid;
        this.birth_date = birth_date;
        this.patient_sex = patient_sex;
        this.organization = organization;
        this.language = language;

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

        this.quadrant_location = quadrant_location;
        this.depth = depth;
        this.lesion_density = lesion_density;
        this.margins = margins;
        this.calcification_type = calcification_type;
        this.calcification_distribution = calcification_distribution;
        this.number_of_calc = number_of_calc;

        this.recommended_followup = recommended_followup;
        this.recommended_followup_interval = recommended_followup_interval;
        this.bi_rads = bi_rads;
    }
}
