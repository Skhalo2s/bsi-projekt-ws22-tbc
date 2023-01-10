/**
 * @outhor Salah
 * @vision 1.0
 * @Zuletzt bearbeiret: 08.01.23 by Leon
 */
package org.hbrs.project.wram.views.routes;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.hbrs.project.wram.util.Constants;
import org.hbrs.project.wram.views.common.layouts.AppViewOutside;

import javax.annotation.PostConstruct;

@PageTitle("Hilfe")
@CssImport("./styles/views/main/main-view.css")
@Route(value = Constants.Pages.HilfeOUT, layout = AppViewOutside.class)
@Slf4j
public class Hilfe extends Div {

    private H2 title;

    @PostConstruct
    private void init() {
        add(createFormLayout());


    }

    /**
     * Diese Methode erzeugt das Formlayout Hife funktion darstellt.
     *
     * @return Instanz des Layouts
     */
    public VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();


        title = new H2("WAC FAQ-Hilfe:");
        Label question1 = new Label("Question1");
        Label answer1 = new Label("Answer1");
        Label question2 = new Label("Question2");
        Label answer2 = new Label("Answer2");
        Label question3 = new Label("Question3");
        Label answer3 = new Label("Answer3");
        Label question4 = new Label("Question4");
        Label answer4 = new Label("Answer4");

        question1.getElement().addEventListener("click", e -> {
            answer1.setVisible(!answer1.isVisible());
        });
        question2.getElement().addEventListener("click", e -> {
            answer2.setVisible(!answer2.isVisible());
        });
        question3.getElement().addEventListener("click", e -> {
            answer3.setVisible(!answer3.isVisible());
        });
        question4.getElement().addEventListener("click", e -> {
            answer4.setVisible(!answer4.isVisible());
        });

        formLayout.add(title, question1, answer1, question2, answer2, question3, answer3, question4, answer4);
        formLayout.setMaxWidth("900px");
        return formLayout;
    }
}
