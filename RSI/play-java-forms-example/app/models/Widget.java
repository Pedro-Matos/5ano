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

    public Widget(String name, String patientName) {
        this.doctorName = name;
        this.patientName = patientName;
    }
}
