package WildcardLibrary;

import org.dcm4che2.data.*;
import org.dcm4che2.io.DicomOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Pedro Matos
 *
 * This is the main core of the project. This class contains the functions
 * responsible for the creation of the DICOM SR.
 *
 */
public class DicomCreator {
    private DicomObject do_out, aux;
    private HashMap<String, TID> TIDs = new HashMap<>();
    private HashMap<String, CID> CIDs = new HashMap<>();
    private HashMap<String, String> mapping_values = new HashMap<>();
    private HashMap<String, LinkedList<String>> real_values = new HashMap<>();
    private String directory_name;
    private boolean used = false;
    //private String test_tree = "";

    public DicomCreator(HashMap<String, TID> TIDs, HashMap<String, CID> CIDs, HashMap<String,
            String> mapping_values, HashMap<String, LinkedList<String>> real_values, String directory_name) {
        this.TIDs = TIDs;
        this.CIDs = CIDs;
        this.mapping_values = mapping_values;
        this.real_values = real_values;
        this.do_out = new BasicDicomObject();
        this.aux = new BasicDicomObject();
        this.directory_name = directory_name;
    }

    /**
     * Name: CreateDicom
     * This is the unique public function in this class. The reason is pretty simple:
     * all the others functions must be called in order, to create a well structured DICOM SR.
     * For that, we "force" the user to call only this function in order to the program to work well.
     *
     * There is only one necessary argument: the number of lines of the csv with the values.
     * With that, we will create one different DICOM SR for each line of the csv.
     *
     */

    public void CreateDicom(int num){
        System.out.println("Writing Dicom Files...\n\n");

        //number of lines of the csv
        int lenght_csv = num;
        for(int i = 0; i < lenght_csv-1; i++){
            Write_Patient_Data(i);
            Write_TIDS(i);
            Write_to_File(i);

        }

    }


    /**
     * Name: Write_TIDS
     * The DICOM structure is supported by a tree-alike model.
     * This function starts on the first node of the tree-alike model
     */

    private void Write_TIDS(int index) {
        //The first TID is the TID4200
        TID T4200 = TIDs.get("T4200");

        /* It is necessary to find the number of childs and triplets this TID has
           so that we can process them all.
        */

        //test_tree = "";

        int ordem = T4200.getChilds().size() + T4200.getTriplet().size();


        /**
         * Problema em ter a estrutura em árvore e ter os campos em pdf. É necessário mais algumas modificações.
         *
         */

        DicomElement pdf_fields = aux.putSequence(Tag.ContentSequence);
        PDF_Fields(pdf_fields,index);
        do_out.add(pdf_fields);

        DicomElement de = aux.putSequence(Tag.ContentTemplateSequence);

        //the first tid is obligatory so we it will always be "true" for the 1 node.
        Writing_TID_Content(ordem,T4200,de,index,true, de, aux);
        do_out.add(de);

    }

    /**
     * Name: Writing_TID_Content
     * In this function we will visit all the nodes by order
     * and insert their information on the DICOM SR.
     * Each node can be a TID or CID, which means they can contain only information
     * for the DICOM or they can also contain references for next nodes: the child nodes.
     */

    private void Writing_TID_Content(int ordem, TID tid, DicomElement de, int index, boolean mandatory, DicomElement de_before, DicomObject do_before) {
        // flag to test if this Child is used or not!
        used = false;

        for(int i = 1; i <= ordem; i++){
            String key = Integer.toString(i);
            if(tid.getChilds().containsKey(key)) {
                //if is a child node, the function must be recursively called again.
                String child_name = tid.getChilds().get(key).getName();
                Childs tmp = tid.getChilds().get(key);
                //if its a TID
                if(tmp.getType().equalsIgnoreCase("TID")){
                    TID tid_child = TIDs.get(child_name);
                    if(tid_child == null){
                        System.out.println("\nFicheiro inixistente: "+child_name+"\n");
                        System.exit(0);
                    }
                    //calling the function recursively

                    /**
                     * STILL NOT WORKING PROPERLY
                     */

                    else{
                        DicomObject dcmobj = new BasicDicomObject();
                        dcmobj.putString(Tag.RelationshipType, VR.CS, tmp.getRel_With_Parent());
                        dcmobj.putString(Tag.ValueType, VR.CS, tmp.getVT());

                        DicomElement sq = dcmobj.putSequence(Tag.ContentSequence);
                        sq.addDicomObject(de.getDicomObject());

                        de.addDicomObject(dcmobj);

                        int ordem2 = tid_child.getChilds().size() + tid_child.getTriplet().size();


                        if(tmp.getReq_Type().equalsIgnoreCase("M"))
                            Writing_TID_Content(ordem2, tid_child, sq, index, true, de, dcmobj);
                        else
                            Writing_TID_Content(ordem2, tid_child, sq, index, false,de, dcmobj);
                    }

                }
                //if it's a CID we must call another function
                else if(tmp.getType().equalsIgnoreCase("CID")){
                    DicomObject d_o = new BasicDicomObject();
                    DicomElement de_child = d_o.putSequence(Tag.ContentSequence);
                    de.addDicomObject(d_o);

                    if(tmp.getReq_Type().equalsIgnoreCase("M"))
                        Wrintig_CID(child_name,de_child,index,true);
                    else
                        Wrintig_CID(child_name,de_child,index,false);
                }


            }
            //in this case its triplet information and it must be stored on the DICOM SR
            else {
                Triplet triplet_1 = tid.getTriplet().get(key);
                Dicom_Sequence_BlackBox(triplet_1, de, index);
            }

        }
        // if it has never been used, we can delete it!

        /**
         * NOT WORKING YET
         */

        /*if(!used && !mandatory){
            de_before.removeDicomObject(do_before);
        }*/
    }


    /**
     * Name: Writing_CID
     *
     * This function is responsible for passing the information in each triplet inside the CID
     * to the DICOM SR
     */

    private void Wrintig_CID(String child_name, DicomElement d_ele, int index, boolean mandatory){
        CID tid_child = CIDs.get(child_name);
        LinkedList<String> tmp_l = null;
        if(tid_child == null){
            System.out.println("\nFicheiro inixistente: "+child_name+"\n");
            System.exit(0);
        }
        else{
            int ordem = tid_child.getTriplet().size();
            for(int i = 1; i <= ordem; i++){
                String key = Integer.toString(i);
                if(tid_child.getTriplet().containsKey(key)) {
                    Triplet triplet_1 = tid_child.getTriplet().get(key);
                    if(mapping_values.containsKey(triplet_1.getCodeValue())){
                        DicomObject d_obj_tmp = new BasicDicomObject();
                        d_obj_tmp.putString(Tag.CodeValue, null, triplet_1.getCodeValue());
                        d_obj_tmp.putString(Tag.CodeMeaning, null, triplet_1.getCodeMeaning());
                        d_obj_tmp.putString(Tag.CodingSchemeDesignator, null, triplet_1.getCodingSchemeDesignator());

                        String key_tmp = mapping_values.get(triplet_1.getCodeValue());
                        tmp_l = real_values.get(key_tmp);
                        //d_obj_tmp.putString(Tag.ItemDelimitationItem, null, "0");
                        d_obj_tmp.putString(Tag.TextValue, null, tmp_l.get(index));
                        //d_obj_tmp.putString(Tag.ItemDelimitationItem, null, "0");
                        d_ele.addDicomObject(d_obj_tmp);
                        used = true;
                    }
                    else if(!mapping_values.containsKey(triplet_1.getCodeValue()) && mandatory ){
                        DicomObject d_obj_tmp = new BasicDicomObject();
                        d_obj_tmp.putString(Tag.CodeValue, null, triplet_1.getCodeValue());
                        d_obj_tmp.putString(Tag.CodeMeaning, null, triplet_1.getCodeMeaning());
                        d_obj_tmp.putString(Tag.CodingSchemeDesignator, null, triplet_1.getCodingSchemeDesignator());

                        String key_tmp = mapping_values.get(triplet_1.getCodeValue());
                        tmp_l = real_values.get(key_tmp);
                        //d_obj_tmp.putString(Tag.ItemDelimitationItem, null, "0");
                        d_obj_tmp.putString(Tag.TextValue, null, "No value was presented.");
                        //d_obj_tmp.putString(Tag.ItemDelimitationItem, null, "0");
                        d_ele.addDicomObject(d_obj_tmp);
                        used = true;
                    }
                }
            }
        }
    }

    /**
     * Name: Write_Patient_Data
     *
     * This function is responsible for passing the information related to the patient
     * to the DICOM SR
     */

    private void Write_Patient_Data(int index){
        System.out.println("Writing Patient Data");
        LinkedList<String> tmp_l;

        do_out.putString(Tag.FileMetaInformationGroupLength,VR.UL, "200");
        //do_out.putString(Tag.FileMetaInformationVersion, VR.OB, );
        do_out.putString(Tag.MediaStorageSOPClassUID, VR.UI,"1.2.840.10008.5.1.4.1.1.88.11");
        do_out.putString(Tag.MediaStorageSOPInstanceUID,VR.UI, "1.2.276.0.7230010.3.1.4.1787205428.166.1117461927.20");
        do_out.putString(Tag.TransferSyntaxUID, VR.UI, "1.2.840.10008.1.2.1");
        do_out.putString(Tag.ImplementationClassUID, VR.UI, "1.2.276.0.7230010.3.0.3.5.3");
        do_out.putString(Tag.ImplementationVersionName, VR.SH, "OFFIS_DCMTK_353");
        do_out.putString(Tag.InstanceCreationDate, VR.DA, "20050530");
        do_out.putString(Tag.InstanceCreationTime, VR.TM, "160527.000000");
        do_out.putString(Tag.InstanceCreatorUID, VR.UI, "1.2.276.0.7230010.3.0.3.5.3");
        do_out.putString(Tag.SOPClassUID, VR.UI, "1.2.840.10008.5.1.4.1.1.88.11");
        do_out.putString(Tag.SOPInstanceUID, VR.UI, "1.2.276.0.7230010.3.1.4.1787205428.166.1117461927.20");

        try{
            tmp_l = real_values.get(mapping_values.get("StudyDate"));
            do_out.putString(Tag.StudyDate, VR.DA,tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.StudyDate, VR.DA, "");
        }

        try{
            tmp_l = real_values.get(mapping_values.get("ContentDate"));
            do_out.putString(Tag.ContentDate, VR.DA, tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.ContentDate, VR.DA, "");
        }

        try{
            tmp_l = real_values.get(mapping_values.get("StudyTime"));
            do_out.putString(Tag.StudyTime, VR.TM,tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.StudyTime, VR.TM, "");
        }

        try{
            tmp_l = real_values.get(mapping_values.get("ContentTime"));
            do_out.putString(Tag.ContentTime, VR.TM, tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.ContentTime, VR.TM, "");
        }

        do_out.putString(Tag.AccessionNumber, VR.SH, "");
        do_out.putString(Tag.Modality, VR.CS, "SR");
        do_out.putString(Tag.Manufacturer, VR.LO, "" );


        try{
            tmp_l = real_values.get(mapping_values.get("ReferringPhysicianName"));
            do_out.putString(Tag.ReferringPhysicianName, VR.PN, tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.ReferringPhysicianName, VR.PN, "The Physician Name was not referred.");
        }


        DicomElement de_tmp = do_out.putSequence(Tag.CodingSchemeIdentificationSequence);
        DicomObject do_tmp = new BasicDicomObject();
        do_tmp.putString(Tag.CodingSchemeDesignator, VR.SH, "99_OFFIS_DCMTK");
        do_tmp.putString(Tag.CodingSchemeUID, VR.UI, "1.2.276.0.7230010.3.0.0.1");
        do_tmp.putString(Tag.CodingSchemeName, VR.ST, "OFFIS DCMTK Coding Scheme");

        try{
            tmp_l = real_values.get(mapping_values.get("ResponsibleOrganization"));
            do_tmp.putString(Tag.CodingSchemeResponsibleOrganization, VR.ST, tmp_l.get(index));
        }
        catch (Exception e){
            do_tmp.putString(Tag.CodingSchemeResponsibleOrganization, VR.ST, "The Responsible Organization was not specified.");
        }


        de_tmp.addDicomObject(do_tmp);
        do_out.add(de_tmp);



        do_out.putString(Tag.StudyDescription, VR.LO, "OFFIS Structured Reporting Samples");
        do_out.putString(Tag.SeriesDescription, VR.LO, "Basic Text Report");
        DicomElement de2 = do_out.putSequence(Tag.ReferencedPerformedProcedureStepSequence);

        try{
            tmp_l = real_values.get(mapping_values.get("PatientName"));
            do_out.putString(Tag.PatientName, VR.PN, tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.PatientName, VR.PN, "The PatientName was not specified.");
        }


        try{
            tmp_l = real_values.get(mapping_values.get("PatientId"));
            do_out.putString(Tag.PatientID, VR.LO, tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.PatientID, VR.LO,"The PatientID was not specified.");
        }

        try{
            tmp_l = real_values.get(mapping_values.get("PatientBirthDate"));
            do_out.putString(Tag.PatientBirthDate, VR.DA, tmp_l.get(index));
        }
        catch (Exception e){

        }

        try{
            tmp_l = real_values.get(mapping_values.get("PatientSex"));
            do_out.putString(Tag.PatientSex, VR.CS, tmp_l.get(index));
        }
        catch (Exception e){
            do_out.putString(Tag.PatientSex, VR.CS, "The PatientSex was not defined.");
        }




        do_out.putString(Tag.StudyInstanceUID, VR.UI, "1.2.276.0.7230010.3.1.2.1787205428.166.1117461927.21");
        do_out.putString(Tag.SeriesInstanceUID, VR.UI, "1.2.276.0.7230010.3.1.3.1787205428.166.1117461927.22");
        do_out.putString(Tag.StudyID, VR.SH, "");
        do_out.putString(Tag.SeriesNumber, VR.IS, "1");
        do_out.putString(Tag.InstanceNumber, VR.IS, "1");
        do_out.putString(Tag.ValueType, VR.CS, "CONTAINER");



        DicomElement de_tmp2 = do_out.putSequence(Tag.ConceptNameCodeSequence);
        DicomObject do_tmp2 = new BasicDicomObject();
        do_tmp2.putString(Tag.CodingSchemeDesignator, VR.SH, "99_OFFIS_DCMTK");
        do_tmp2.putString(Tag.CodeValue, VR.SH, "DT.06");
        do_tmp2.putString(Tag.CodeMeaning, VR.LO, "Consultation Report");
        de_tmp2.addDicomObject(do_tmp2);
        do_out.add(de_tmp2);



        do_out.putString(Tag.ContinuityOfContent, VR.CS, "SEPARATE");
        DicomElement de3 = do_out.putSequence(Tag.PerformedProcedureCodeSequence);
        do_out.putString(Tag.CompletionFlag, VR.CS, "PARTIAL");
        do_out.putString(Tag.VerificationFlag, VR.CS, "UNVERIFIED");
    }

    /**
     * Name: Dicom_Sequence_BlackBox
     *
     * This function is responsible to deal with passing the information to the DICOM SR in each
     * triplet of the TIDs
     */

    private void Dicom_Sequence_BlackBox(Triplet t1, DicomElement de, int index){
        if(mapping_values.containsKey(t1.getCodeValue())) {
            LinkedList<String> tmp_l = null;
            DicomObject d_obj = new BasicDicomObject();
            d_obj.putString(Tag.CodeValue, null, t1.getCodeValue());
            d_obj.putString(Tag.CodeMeaning, null, t1.getCodeMeaning());
            d_obj.putString(Tag.CodingSchemeDesignator, null, t1.getCodingSchemeDesignator());


            String key_tmp = mapping_values.get(t1.getCodeValue());
            tmp_l = real_values.get(key_tmp);
            //d_obj.putString(Tag.ItemDelimitationItem, VR.SQ, null);

            if(t1.getVT().equalsIgnoreCase("NUM"))
                d_obj.putString(Tag.NumericValue, null, tmp_l.get(index));
            else
               d_obj.putString(Tag.TextValue, null, tmp_l.get(index));

            //d_obj.putString(Tag.ItemDelimitationItem, VR.SQ, null);

            de.addDicomObject(d_obj);
            used = true;
        }
        else if (!mapping_values.containsKey(t1.getCodeValue()) && t1.getReq_Type().equalsIgnoreCase("M")){
            LinkedList<String> tmp_l = null;
            DicomObject d_obj = new BasicDicomObject();
            d_obj.putString(Tag.CodeValue, null, t1.getCodeValue());
            d_obj.putString(Tag.CodeMeaning, null, t1.getCodeMeaning());
            d_obj.putString(Tag.CodingSchemeDesignator, null, t1.getCodingSchemeDesignator());


            String key_tmp = mapping_values.get(t1.getCodeValue());
            tmp_l = real_values.get(key_tmp);
            //d_obj.putString(Tag.ItemDelimitationItem, VR.SQ, null);

            if(t1.getVT().equalsIgnoreCase("NUM"))
                d_obj.putString(Tag.NumericValue, null, "0");
            else
                d_obj.putString(Tag.TextValue, null, "Default");

            //d_obj.putString(Tag.ItemDelimitationItem, VR.SQ, null);

            de.addDicomObject(d_obj);
            used = true;
        }
    }

    private void PDF_Fields(DicomElement pdf_fields, int index){
        LinkedList<String> tmp_l;




        DicomObject d1 = new BasicDicomObject();

        d1.putString(Tag.RelationshipType, VR.CS, "HAS OBS CONTEXT");
        d1.putString(Tag.ValueType, VR.CS, "PNAME");

        DicomElement d1_e = d1.putSequence(Tag.ConceptNameCodeSequence);
        DicomObject d1_o = new BasicDicomObject();


        d1_o.putString(Tag.CodeValue, VR.SH, "IHE.04");
        d1_o.putString(Tag.CodeMeaning, VR.LO, "Observer Name");
        d1_o.putString(Tag.CodingSchemeDesignator,VR.SH , "99_OFFIS_DCMTK");

        d1_e.addDicomObject(d1_o);

        try{
            tmp_l = real_values.get(mapping_values.get("ObserverName"));
            d1.putString(Tag.PersonName, VR.PN, tmp_l.get(index));
        }
        catch (Exception e){
            d1.putString(Tag.PersonName, VR.PN, "Not referred.");
        }

        pdf_fields.addDicomObject(d1);




        DicomObject d2 = new BasicDicomObject();

        d2.putString(Tag.RelationshipType, VR.CS, "HAS OBS CONTEXT");
        d2.putString(Tag.ValueType, VR.CS, "TEXT");

        DicomElement d2_e = d2.putSequence(Tag.ConceptNameCodeSequence);
        DicomObject d2_o = new BasicDicomObject();

        d2_o.putString(Tag.CodeValue, VR.SH, "IHE.05");
        d2_o.putString(Tag.CodeMeaning, VR.LO, "Observer Organization Name");
        d2_o.putString(Tag.CodingSchemeDesignator,VR.SH , "99_OFFIS_DCMTK");

        d2_e.addDicomObject(d2_o);

        try{
            tmp_l = real_values.get(mapping_values.get("ObserverOrganizationName"));
            d2.putString(Tag.TextValue, VR.UT, tmp_l.get(index));
        }
        catch (Exception e){
            d2.putString(Tag.TextValue, VR.UT, "Not referred.");
        }

        pdf_fields.addDicomObject(d2);




        DicomObject d3 = new BasicDicomObject();

        d3.putString(Tag.RelationshipType, VR.CS, "CONTAINS");
        d3.putString(Tag.ValueType, VR.CS, "TEXT");

        DicomElement d3_e = d3.putSequence(Tag.ConceptNameCodeSequence);
        DicomObject d3_o = new BasicDicomObject();

        d3_o.putString(Tag.CodeValue, VR.SH, "CODE_01");
        d3_o.putString(Tag.CodeMeaning, VR.LO, "Description");
        d3_o.putString(Tag.CodingSchemeDesignator,VR.SH , "99_OFFIS_DCMTK");

        d3_e.addDicomObject(d3_o);

        try{
            tmp_l = real_values.get(mapping_values.get("Description"));
            d3.putString(Tag.TextValue, VR.UT, tmp_l.get(index));
        }
        catch (Exception e){
            d3.putString(Tag.TextValue, VR.UT, "The Description was not presented.");
        }

        pdf_fields.addDicomObject(d3);




        DicomObject d4 = new BasicDicomObject();

        d4.putString(Tag.RelationshipType, VR.CS, "CONTAINS");
        d4.putString(Tag.ValueType, VR.CS, "TEXT");

        DicomElement d4_e = d4.putSequence(Tag.ConceptNameCodeSequence);
        DicomObject d4_o = new BasicDicomObject();

        d4_o.putString(Tag.CodeValue, VR.SH, "CODE_02");
        d4_o.putString(Tag.CodeMeaning, VR.LO, "Diagnosis");
        d4_o.putString(Tag.CodingSchemeDesignator,VR.SH , "99_OFFIS_DCMTK");

        d4_e.addDicomObject(d4_o);

        try{
            tmp_l = real_values.get(mapping_values.get("Diagnosis"));
            d4.putString(Tag.TextValue, VR.UT, tmp_l.get(index));
        }
        catch (Exception e){
            d4.putString(Tag.TextValue, VR.UT, "The Diagnosis was not presented.");
        }


        pdf_fields.addDicomObject(d4);




        DicomObject d5 = new BasicDicomObject();

        d5.putString(Tag.RelationshipType, VR.CS, "CONTAINS");
        d5.putString(Tag.ValueType, VR.CS, "TEXT");

        DicomElement d5_e = d5.putSequence(Tag.ConceptNameCodeSequence);
        DicomObject d5_o = new BasicDicomObject();

        d5_o.putString(Tag.CodeValue, VR.SH, "CODE_03");
        d5_o.putString(Tag.CodeMeaning, VR.LO, "Treatment");
        d5_o.putString(Tag.CodingSchemeDesignator,VR.SH , "99_OFFIS_DCMTK");

        d5_e.addDicomObject(d5_o);

        try{
            tmp_l = real_values.get(mapping_values.get("Treatment"));
            d5.putString(Tag.TextValue, VR.UT, tmp_l.get(index));
        }
        catch (Exception e){
            d5.putString(Tag.TextValue, VR.UT, "The Treatment was not presented.");
        }

        pdf_fields.addDicomObject(d5);
    }



    /**
     * Name: Write_to_File
     *
     * This function is responsible to create the physical DICOM SR.
     * One for each line in the csv.
     */
    private void Write_to_File(int index) {
        System.out.println("Writing Dicom to File:");

        File f = new File(directory_name+"/"+"mamo_sr"+index+".dcm");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DicomOutputStream dos = new DicomOutputStream(bos);
        try {
            dos.writeDicomFile(do_out);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        finally {
            try {
                dos.close();
            }
            catch (IOException ignore) {
            }
        }

        System.out.println("Dicom File Created.\n\n");
        //System.out.println(test_tree+"\n\n");
    }

}
