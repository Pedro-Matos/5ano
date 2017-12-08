package controllers;

import play.data.validation.Constraints;

import javax.validation.Constraint;

/**
 * A form processing DTO that maps to the widget form.
 *
 * Using a class specifically for form binding reduces the chances
 * of a parameter tampering attack and makes code clearer, because
 * you can define constraints against the class.
 */
public class WidgetData {

    @Constraints.Required
    private String doctorName;

    @Constraints.Required
    private String patientName;

    @Constraints.Min(0)
    private int patientid;

    @Constraints.Required
    private String physicalExaminationResults;

    @Constraints.Required
    private String comparisontopreviousexams;

    @Constraints.Required
    private String findings;

    @Constraints.Required
    private String recommendations;

    @Constraints.Required
    private String conclusions;

    @Constraints.Required
    private String pathologyResults;

    @Constraints.Required
    private String pathology;

    @Constraints.Required
    private String malignancyType;

    @Constraints.Required
    private String nippleinvolved;

    @Constraints.Min(0)
    private int numbernodesremoved;

    @Constraints.Min(0)
    private int numbernodespositive;

    @Constraints.Required
    public String distancefromnipple;
    @Constraints.Required
    public String distancefromskin;
    @Constraints.Required
    public String distancefromchestwall;



    public WidgetData() {
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhysicalExaminationResults() {
        return physicalExaminationResults;
    }

    public void setPhysicalExaminationResults(String physicalExaminationResults) {
        this.physicalExaminationResults = physicalExaminationResults;
    }

    public String getComparisontopreviousexams() {
        return comparisontopreviousexams;
    }

    public void setComparisontopreviousexams(String comparisontopreviousexams) {
        this.comparisontopreviousexams = comparisontopreviousexams;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getConclusions() {
        return conclusions;
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    public int getPatientid() {
        return patientid;
    }

    public void setPatientid(int patientid) {
        this.patientid = patientid;
    }

    public String getPathologyResults() {
        return pathologyResults;
    }

    public void setPathologyResults(String pathologyResults) {
        this.pathologyResults = pathologyResults;
    }

    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

    public String getMalignancyType() {
        return malignancyType;
    }

    public void setMalignancyType(String malignancyType) {
        this.malignancyType = malignancyType;
    }

    public String getNippleinvolved() {
        return nippleinvolved;
    }

    public void setNippleinvolved(String nippleinvolved) {
        this.nippleinvolved = nippleinvolved;
    }

    public int getNumbernodesremoved() {
        return numbernodesremoved;
    }

    public void setNumbernodesremoved(int numbernodesremoved) {
        this.numbernodesremoved = numbernodesremoved;
    }

    public int getNumbernodespositive() {
        return numbernodespositive;
    }

    public void setNumbernodespositive(int numbernodespositive) {
        this.numbernodespositive = numbernodespositive;
    }

    public String getDistancefromnipple() {
        return distancefromnipple;
    }

    public void setDistancefromnipple(String distancefromnipple) {
        this.distancefromnipple = distancefromnipple;
    }

    public String getDistancefromskin() {
        return distancefromskin;
    }

    public void setDistancefromskin(String distancefromskin) {
        this.distancefromskin = distancefromskin;
    }

    public String getDistancefromchestwall() {
        return distancefromchestwall;
    }

    public void setDistancefromchestwall(String distancefromchestwall) {
        this.distancefromchestwall = distancefromchestwall;
    }
}
