package controllers;

import models.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.file.FileVisitResult;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.nio.file.SimpleFileVisitor;

import java.nio.file.attribute.BasicFileAttributes;

import java.util.zip.ZipEntry;

import java.util.zip.ZipOutputStream;


import static play.libs.Scala.asScala;

/**
 * An example of form processing.
 *
 * https://playframework.com/documentation/latest/JavaForms
 */
@Singleton
public class WidgetController extends Controller {

    private final Form<WidgetData> form;
    private final List<Widget> widgets;

    @Inject
    public WidgetController(FormFactory formFactory) {
        this.form = formFactory.form(WidgetData.class);
        this.widgets = com.google.common.collect.Lists.newArrayList();
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result listWidgets() {
        return ok(views.html.listWidgets.render(asScala(widgets), form));
    }

    public Result createWidget() throws Exception {
        final Form<WidgetData> boundForm = form.bindFromRequest();

        if (boundForm.hasErrors()) {
            play.Logger.ALogger logger = play.Logger.of(getClass());
            logger.error("errors = {}", boundForm);
            return badRequest(views.html.listWidgets.render(asScala(widgets), boundForm));
        } else {
            WidgetData data = boundForm.get();
            widgets.add(new Widget(data.getDoctorName(), data.getPatientName(), data.getPatientid(), data.getBirth_date(), data.getPatient_sex(),
                    data.getOrganization(), data.getLanguage(), data.getPhysicalExaminationResults(),
                    data.getComparisontopreviousexams(), data.getFindings(), data.getRecommendations(), data.getConclusions(),
                    data.getPathologyResults(), data.getPathology(),data.getMalignancyType(),data.getNippleinvolved(),data.getNumbernodesremoved(),
                    data.getNumbernodespositive(), data.getDistancefromnipple(), data.getDistancefromskin(), data.getDistancefromchestwall(),
                    data.getQuadrant_location(), data.getDepth(), data.getLesion_density(), data.getMargins(),
                    data.getCalcification_type(),data.getCalcification_distribution(),data.getNumber_of_calc(),
                    data.getRecommended_followup(), data.getRecommended_followup_interval(), data.getBi_rads()));
            flash("info", "Form created!");
            String foldername;
            foldername = upload(data);
            System.out.println(foldername);
            String zipname= "";
            zipname = GenerateZip(foldername);
            //return redirect(routes.WidgetController.listWidgets());
            return ok(new File(zipname));
        }
    }

    public String upload(WidgetData data) throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        String[] data_tmp = dateFormat.format(date).split(" ");

        String[] data_tmp_1 = data_tmp[0].split("/");
        String data_final = "";
        for(String abc : data_tmp_1){
            data_final = data_final.concat(abc);
        }
        String[] hora_tmp = data_tmp[1].split(":");
        String hora_final = "";
        for(String abc : hora_tmp){
            hora_final = hora_final.concat(abc);
        }


        String type = "";
        String dir = "public/outputs/";
        boolean success = false;
        String folderName = "";
        do {
            //criar numero random para a pasta deste pedido
            Random random = new Random();
            int randomNumber = random.nextInt(Integer.MAX_VALUE - 1) + 1;

            //criar novo repositório
            success = (new File(dir + randomNumber)).mkdirs();
            if (success)
                folderName = "public/outputs/" + randomNumber;

        } while (!success);

        //writing the mapping file
        try {
            String blockFileName = "mapping.csv";
            PrintWriter pwt = new PrintWriter(new File(folderName, blockFileName));

            pwt.println("PhysicalExaminationResults,ComparisonToPreviousExams,Findings,Recommendations,Conclusions," +
                    "PathologyResults,Pathology,MalignancyType,NippleInvolved,NumberOfNodesRemoved,NumberOfNodesPositive," +
                    "DistanceFromNipple,DistanceFromSkin,DistanceFromChestWall," +
                    "QuadrantLocation,Depth,LesionDensity,Margins,CalcificationType,CalcificationDistribution,NumberOfCalcifications," +
                    "RecommendedFollow-up,RecommendedFollow-upInterval,BI-RADS," +
                    "DoctorName,PatientName,PatientID,PatientBirthDate,PatientSex,Organization,Language," +
                    "ContentDate,ContentTime");

            pwt.println("111423,111424,121070,121074,121076," +
                    "111468,111042,111388,111472,111473,111474," +
                    "121242,121243,121244," +
                    "111048,111020,111035,111037,111009,111008,111038," +
                    "111053,111055,BI-RADS," +
                    "ReferringPhysicianName,PatientName,PatientId,PatientBirthDate,PatientSex,ResponsibleOrganization,121049," +
                    "ContentDate,ContentTime");
            pwt.close();


            String blockFileName2 = "ideal.csv";
            PrintWriter printer = new PrintWriter(new File(folderName, blockFileName2));

            printer.println("PhysicalExaminationResults,ComparisonToPreviousExams,Findings,Recommendations,Conclusions," +
                    "PathologyResults,Pathology,MalignancyType,NippleInvolved,NumberOfNodesRemoved,NumberOfNodesPositive," +
                    "DistanceFromNipple,DistanceFromSkin,DistanceFromChestWall," +
                    "QuadrantLocation,Depth,LesionDensity,Margins,CalcificationType,CalcificationDistribution,NumberOfCalcifications," +
                    "RecommendedFollow-up,RecommendedFollow-upInterval,BI-RADS," +
                    "DoctorName,PatientName,PatientID,PatientBirthDate,PatientSex,Organization,Language," +
                    "ContentDate,ContentTime");

            printer.println(data.getPhysicalExaminationResults()+"/"+data.getComparisontopreviousexams()+"/"+data.getFindings()
                            +"/"+data.getRecommendations()+"/"+data.getConclusions()+"/"+data.getPathologyResults()
                            +"/"+data.getPathology()+"/"+data.getMalignancyType()+"/"+data.getNippleinvolved()
                            +"/"+Integer.toString(data.getNumbernodesremoved())+"/"+ Integer.toString(data.getNumbernodespositive())
                            +"/"+data.getDistancefromnipple()+"/"+data.getDistancefromskin()+"/"+data.getDistancefromchestwall()
                            +"/"+data.getQuadrant_location()+"/"+data.getDepth()+"/"+data.getLesion_density()+"/"+data.getMargins()
                            +"/"+data.getCalcification_type()+"/"+data.getCalcification_distribution()+"/"+data.getNumber_of_calc()
                            +"/"+data.getRecommended_followup()+"/"+data.getRecommended_followup_interval()
                            +"/"+Integer.toString(data.getBi_rads())
                            +"/"+data.getDoctorName()+"/"+data.getPatientName()+"/"+
                            Integer.toString(data.getPatientid())+"/"+data.getBirth_date()+"/"+data.getPatient_sex()
                    +"/"+data.getOrganization()+"/"+data.getLanguage()+
            "/" + data_final+"/" +hora_final);

            printer.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the content to a file", ex);
        }

        return folderName;
    }

    private String GenerateZip(String folderToZip) throws Exception {

        String zipName = folderToZip.concat(".zip");

        zipFolder(Paths.get(folderToZip), Paths.get(zipName));

        return zipName;
    }

    // Uses java.util.zip to create zip file

    private void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));

                Files.copy(file, zos);

                zos.closeEntry();

                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }

}
