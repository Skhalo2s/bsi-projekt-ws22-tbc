/**
 * @outhor  Salah
 * @vision 1.0
 * @Zuletzt bearbeiret: 06.12.22 by Salah
 *
 */
package org.hbrs.project.wram.model.manager;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.hbrs.project.wram.control.anfrage.AnfrageService;
import org.hbrs.project.wram.control.entwickler.EntwicklerService;
import org.hbrs.project.wram.model.anfrage.Anfrage;
import org.hbrs.project.wram.util.Constants;
import org.hbrs.project.wram.views.common.layouts.AppView;
import org.hbrs.project.wram.views.routes.Notify;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Diese View dient der Manager ein Entwickler Anfregezugewissung  zu senden
 * Dabei wird die View innerhalb der AppView angezeigt.
 */

@PageTitle("Zuweisung Senden")
@CssImport("./styles/views/main/main-view.css")
@Route(value = Constants.Pages.ZUWEISUNGSENDEN, layout = AppView.class)
@Slf4j
public class ZuweisungSenden extends Div {
    private H2 header;

    private List<Anfrage> anfrage = new ArrayList<>();

    @Autowired
    private AnfrageService anfrageService;

    @Autowired
    private EntwicklerService entwicklerService;

    @PostConstruct
    public void init() {
        header = new H2("Alle Anfragen");
        UUID userID =(UUID) UI.getCurrent().getSession().getAttribute(Constants.CURRENT_USER);

        anfrage = anfrageService.findAll();

        VerticalLayout layout = new VerticalLayout();
        layout.add(header);
        add(layout, setUpGrid());
    }

    /**
     * Diese Methode dient dazu, eine Tabelle mit allen Anfragen zu befüllen.
     * Dabei werden Name und Skills eines Kundenprojektes angezeigt.
     * @return Component Grid
     */
    private Component setUpGrid() {
        Grid<Anfrage> grid = new Grid<>();

        // Befüllen der Tabelle mit den zuvor ausgelesenen Anfrage
        ListDataProvider<Anfrage> dataProvider = new ListDataProvider<>(anfrage);
        grid.setDataProvider(dataProvider);

        // Projekt name
        Grid.Column<Anfrage> KundenprojektColumn = grid.addColumn((Anfrage a)->{
            return a.getKundenprojekt().getProjektname();
        }).setHeader("Kundenprojekt").setWidth("225px");


        Grid.Column<Anfrage> ReviewerColumn = grid.addColumn((Anfrage a)->{
                    return  a.getReviewer().getFirstname()+ " " + a.getReviewer().getName();})
                .setHeader("Zuweisender Reviewer").setWidth("100px");

        Grid.Column<Anfrage> EntwicklerColumn = grid.addColumn((Anfrage a)->{
                    return a.getEntwicklerProfil().getFirstname() + " " + a.getEntwicklerProfil().getName();})
                .setHeader("Entwickler").setWidth("100px");

        //Reason
        Grid.Column<Anfrage> StatusColumn = grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setAutoWidth(true);
        Grid.Column<Anfrage> AnnhemenColumn = grid.addComponentColumn(anfrage -> {
            Button anfrageSendenButton= null;
            Icon lumoIcon = null;
            if (anfrage.isEntwicklerpublic()){

                lumoIcon= new Icon("lumo", "cross");
                lumoIcon.setColor("Red");
                anfrageSendenButton = new Button("Anfrage nicht senden",lumoIcon);

            }
            else {
                lumoIcon = new Icon("lumo", "checkmark");
                lumoIcon.setColor("Green");
                anfrageSendenButton = new Button("Anfrage senden", lumoIcon);
            }
            anfrageSendenButton.addClickListener(event ->{
                        senden(anfrage);
                        UI.getCurrent().navigate(Constants.Pages.PROJECTS_OVERVIEW);
                        UI.getCurrent().navigate(Constants.Pages.ZUWEISUNGSENDEN);
                    }

            );

            return anfrageSendenButton;
        }).setAutoWidth(true).setFlexGrow(0);

        // Projektdaten ausklappen
        grid.setItemDetailsRenderer(createProjektDetailsRenderer());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("1000px");

        return grid;
    }


    private static ComponentRenderer<ZuweisungSenden.ProjektDetailsFormLayout, Anfrage> createProjektDetailsRenderer() {
        return new ComponentRenderer<>(
                ZuweisungSenden.ProjektDetailsFormLayout::new,
                ZuweisungSenden.ProjektDetailsFormLayout::setProjekt);
    }

    private static class ProjektDetailsFormLayout extends FormLayout {
        private final TextField projektname = new TextField("Projektname");
        private final TextArea projektbeschreibung = new TextArea("Projektbeschreibung");
        private final TextArea skills = new TextArea("Benötigte Skills");

        private final TextArea reason = new TextArea("Ablehnungsbegründung");


        public ProjektDetailsFormLayout() {
            projektname.setReadOnly(true);
            projektbeschreibung.setReadOnly(true);
            skills.setReadOnly(true);
            reason.setReadOnly(true);

            setResponsiveSteps(new ResponsiveStep("0", 4));
            setColspan(projektname, 2);
            setColspan(reason, 4);
            setColspan(projektbeschreibung, 4);
            setColspan(skills, 4);
            add(projektname, projektbeschreibung, skills,reason);
        }

        public void setProjekt(Anfrage anfrage) {
            if(anfrage.getKundenprojekt()!=null){
                projektname.setValue(anfrage.getKundenprojekt().getProjektname());}
            else{projektname.setValue("-");}

            if(anfrage.getKundenprojekt()!=null){projektbeschreibung.setValue(anfrage.getKundenprojekt().getProjektbeschreibung());}
            else{projektbeschreibung.setValue("-");}

            if(anfrage.getKundenprojekt()!=null){skills.setValue(anfrage.getKundenprojekt().getSkills());}
            else{skills.setValue("-");}

            if(anfrage.getReason()!=null){reason.setValue(anfrage.getReason());}
            else{reason.setValue("-");}



        }

    }

    /**
     * Diese Methode dient dazu einen StatusComponentRenderer zur Anzeige des Status in der Grid zu erstellen
     * @return ComponentRenderer
     */
    private static ComponentRenderer<Span, Anfrage> createStatusComponentRenderer () {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    /**
     * Hilfmethode für das erstellen des ComponentRenderer
     */
    private static final SerializableBiConsumer<Span, Anfrage> statusComponentUpdater = (
            span, anfrage) -> {
        boolean isEntwicklerpublic = (anfrage.isEntwicklerpublic());
        String theme = String.format("badge %s", isEntwicklerpublic ? "success" : "error");
        span.getElement().setAttribute("theme", theme);

        if (isEntwicklerpublic) {
            span.setText("gesendet");
        } else {
            span.setText("Nicht gesendet");
        }
    };

    private void senden(Anfrage anfrage) {
        if(!(anfrage.isEntwicklerpublic())){
            Notify.notifyAfterUpdateWithOkay("Anfrage gesendet!");
            anfrage.setEntwicklerpublic(true);

        }else{
            Notify.notifyAfterUpdateWithOkay("Anfrage nicht gesendet!");
            anfrage.setEntwicklerpublic(false);
        }
        anfrageService.doCreatAnfrage(anfrage);
    }





}
