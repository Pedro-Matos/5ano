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
import java.util.List;
import java.util.Random;

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

    public Result createWidget() throws IOException {
        final Form<WidgetData> boundForm = form.bindFromRequest();

        if (boundForm.hasErrors()) {
            play.Logger.ALogger logger = play.Logger.of(getClass());
            logger.error("errors = {}", boundForm);
            return badRequest(views.html.listWidgets.render(asScala(widgets), boundForm));
        } else {
            WidgetData data = boundForm.get();
            widgets.add(new Widget(data.getDoctorName(), data.getPatientName(), data.getPatientid() ,data.getPhysicalExaminationResults(),
                    data.getComparisontopreviousexams(), data.getFindings(), data.getRecommendations(), data.getConclusions(),
                    data.getPathologyResults(), data.getPathology(),data.getMalignancyType(),data.getNippleinvolved(),data.getNumbernodesremoved(),
                    data.getNumbernodespositive(), data.getDistancefromnipple(), data.getDistancefromskin(), data.getDistancefromchestwall()));
            flash("info", "Form created!");
            upload();
            return redirect(routes.WidgetController.listWidgets());
        }
    }

    public void upload() throws IOException {
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

        try {
            String blockFileName = "scores.txt";
            PrintWriter pwt = new PrintWriter(new File(folderName, blockFileName));

            pwt.println("teste");
            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }
    }
}
