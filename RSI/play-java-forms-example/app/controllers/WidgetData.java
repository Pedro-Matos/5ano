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
    public String birth_date;

    @Constraints.Required
    public String patient_sex;

    @Constraints.Required
    public String organization;

    @Constraints.Required
    public String language;



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
    private String distancefromnipple;
    @Constraints.Required
    private String distancefromskin;
    @Constraints.Required
    private String distancefromchestwall;

    @Constraints.Required
    private String quadrant_location;

    @Constraints.Required
    private String depth;

    @Constraints.Required
    private String lesion_density;

    @Constraints.Required
    private String margins;

    @Constraints.Required
    private String calcification_type;

    @Constraints.Required
    private String calcification_distribution;

    @Constraints.Required
    private String number_of_calc;

    @Constraints.Required
    private String recommended_followup;

    @Constraints.Required
    private String recommended_followup_interval;

    @Constraints.Min(0)
    @Constraints.Max(6)
    private int bi_rads;



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

    public String getQuadrant_location() {
        return quadrant_location;
    }

    public void setQuadrant_location(String quadrant_location) {
        this.quadrant_location = quadrant_location;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getLesion_density() {
        return lesion_density;
    }

    public void setLesion_density(String lesion_density) {
        this.lesion_density = lesion_density;
    }

    public String getMargins() {
        return margins;
    }

    public void setMargins(String margins) {
        this.margins = margins;
    }

    public String getCalcification_type() {
        return calcification_type;
    }

    public void setCalcification_type(String calcification_type) {
        this.calcification_type = calcification_type;
    }

    public String getCalcification_distribution() {
        return calcification_distribution;
    }

    public void setCalcification_distribution(String calcification_distribution) {
        this.calcification_distribution = calcification_distribution;
    }

    public String getNumber_of_calc() {
        return number_of_calc;
    }

    public void setNumber_of_calc(String number_of_calc) {
        this.number_of_calc = number_of_calc;
    }

    public String getRecommended_followup() {
        return recommended_followup;
    }

    public void setRecommended_followup(String recommended_followup) {
        this.recommended_followup = recommended_followup;
    }

    public String getRecommended_followup_interval() {
        return recommended_followup_interval;
    }

    public void setRecommended_followup_interval(String recommended_followup_interval) {
        this.recommended_followup_interval = recommended_followup_interval;
    }

    public int getBi_rads() {
        return bi_rads;
    }

    public void setBi_rads(int bi_rads) {
        this.bi_rads = bi_rads;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPatient_sex() {
        return patient_sex;
    }

    public void setPatient_sex(String patient_sex) {
        this.patient_sex = patient_sex;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
