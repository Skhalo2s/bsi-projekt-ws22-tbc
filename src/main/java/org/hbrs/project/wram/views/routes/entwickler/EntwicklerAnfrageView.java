/**
 * @outhor  Salah
 * @vision 1.0
 * @Zuletzt bearbeiret: 06.12.22 by Salah
 *
 */
package org.hbrs.project.wram.views.routes.entwickler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Diese View dient dazu einem als Entwickler zugewissene Anträge anzuschauen
 * Dabei wird die View innerhalb der AppView angezeigt.
 */

@PageTitle("Meine Anfrage")
@CssImport("./styles/views/main/main-view.css")
@Route(value = Constants.Pages.ENTWICKLERANFRAGEVIEW, layout = AppView.class)
@Slf4j
public class EntwicklerAnfrageView extends Div {
    private H2 header;



    private List<Anfrage>  anfrage = new ArrayList<>();
    private List<Anfrage> anfragePublic = new ArrayList<>();//isEntwicklerpublic

    @Autowired
    private AnfrageService anfrageService;

    @Autowired
    private EntwicklerService entwicklerService;

    @PostConstruct
    public void init() {
        header = new H2("Alle Anfragen");
        UUID userID =(UUID) UI.getCurrent().getSession().getAttribute(Constants.CURRENT_USER);

        anfrage = anfrageService.findAllByEntwicklerId(entwicklerService.getByUserId(userID).getId());

        //isEntwicklerpublic
        for (Anfrage a: anfrage) {
            if (a.isEntwicklerpublic())
            anfragePublic.add(a);
        }


        VerticalLayout layout = new VerticalLayout();
        layout.add(header);
        add(layout, setUpGrid());
    }

    /**
     * Diese Methode dient dazu, eine Tabelle mit allen Anfragen ein Entwickler.
     * Dabei werden Name und Skills eines Kundenprojektes angezeigt.
     * @return Component Grid
     */
    private Component setUpGrid() {
        Grid<Anfrage> grid = new Grid<>();

        // Befüllen der Tabelle mit den zuvor ausgelesenen Anfrage
        ListDataProvider<Anfrage> dataProvider = new ListDataProvider<>(anfragePublic);
        grid.setDataProvider(dataProvider);

        // Projekt name
        Grid.Column<Anfrage> KundenprojektColumn = grid.addColumn((Anfrage a)->{
            return a.getKundenprojekt().getProjektname();
        }).setHeader("Kundenprojekt").setWidth("225px");


        Grid.Column<Anfrage> ReviewerColumn = grid.addColumn((Anfrage a)->{
            return a.getReviewer().getName()+ ", " + a.getReviewer().getFirstname();})
                .setHeader("Zuweisender Reviewer").setWidth("100px");

        //Reason
        Grid.Column<Anfrage> StatusColumn = grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setAutoWidth(true);

        // Anfrage annehmen
        Grid.Column<Anfrage> AnnhemenColumn = grid.addComponentColumn(anfrage -> {
            Button annehmenButton= null;
            Icon lumoIcon =  new Icon("lumo", "checkmark");
                lumoIcon.setColor("Green");

                annehmenButton = new Button("Annehmen", lumoIcon);

            annehmenButton.addClickListener(event ->{
                if (anfrage.isAccepted())
                    Notify.notifyAfterUpdateWithOkay("Antrag wurde bereits angenommen!");
                else{
                    annehmen(anfrage);
                    UI.getCurrent().navigate(Constants.Pages.CREATEENTWICKLERPROFIL);}
                }

            );

            return annehmenButton;
        }).setAutoWidth(true).setFlexGrow(0);

        // Anfrage ablehnen
        Grid.Column<Anfrage> AblehnenColumn = grid.addComponentColumn(anfrage -> {
            Button ablehnenButton = null;
            Icon lumoIcon = new Icon("lumo", "cross");// Icon X
                lumoIcon.setColor("Red");// Icon Farbe

                ablehnenButton = new Button("Ablehnen",lumoIcon);

            ablehnenButton.addClickListener(event ->{
                ablehnen(anfrage);
                        UI.getCurrent().navigate(Constants.Pages.CREATEENTWICKLERPROFIL);
                    }

            );

            return ablehnenButton;
        }).setAutoWidth(true).setFlexGrow(0);

        // Projektdaten ausklappen
        grid.setItemDetailsRenderer(createProjektDetailsRenderer());
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("1000px");

        return grid;
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
        boolean isAccepted = (anfrage.isAccepted());
        String theme = String.format("badge %s", isAccepted ? "success" : "error");
        span.getElement().setAttribute("theme", theme);

        if (isAccepted) {
            span.setText("Angenommen");
        } else {
            span.setText("Nicht angenommen");
        }
    };

    /**
     * Hilfmethode um Antrag anzunehmen
     * dabei wird der User benachrichtigt
     */
    private void annehmen(Anfrage anfrage) {
        notifyAfterUpdateWithOkay("Anfrage wird angenommen!",anfrage);
        anfrage.setAccepted(true);
        anfrage.setBearbeitet(true);
        anfrageService.doCreatAnfrage(anfrage);
    }

    /**
     * Hilfmethode um Antrag abzulehnen
     * dabei wird der User benachrichtigt
     */
    private void ablehnen(Anfrage anfrage) {
        notifyAfterUpdateWithOkay("Anfrage wird abgelehnt!",anfrage);
        anfrage.setAccepted(false);
        anfrage.setBearbeitet(true);
        anfrageService.doCreatAnfrage(anfrage);
    }



    /**
     * User benachrichtigen
     */
    public  void notifyAfterUpdateWithOkay(String benachrichtigung, Anfrage anfrage) {

        Dialog dialog = new Dialog();
        // dialog grosse
        dialog.setHeight("calc(50vh - (2*var(--lumo-space-m)))");
        dialog.setWidth("calc(50vw - (4*var(--lumo-space-m)))");

        dialog.open();

        // Feld um bei Ablehnung eine Begründung einzugeben
        TextArea textArea = new TextArea("Bitte Ablehnungsbegründung eingeben!");



        VerticalLayout dialoglayout = new VerticalLayout();
        dialoglayout.setId("confirm-dialog-layout");

        if ((benachrichtigung.equals("Anfrage wird abgelehnt!"))){
            dialoglayout.add(textArea);
        }

        dialoglayout.add(    new Text(benachrichtigung),

                new Button("Speichern", e ->{
                    anfrage.setReason(textArea.getValue());
                    anfrageService.doCreatAnfrage(anfrage);

                    dialog.close();
                })/*,

                new Button("Abbrechen", e ->{
                    dialog.close();
                })*/

        );



        dialog.add(
                dialoglayout
        );

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
    }


    private static ComponentRenderer<EntwicklerAnfrageView.ProjektDetailsFormLayout, Anfrage> createProjektDetailsRenderer() {
        return new ComponentRenderer<>(
                EntwicklerAnfrageView.ProjektDetailsFormLayout::new,
                EntwicklerAnfrageView.ProjektDetailsFormLayout::setProjekt);
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


}
