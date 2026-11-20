package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(value = "inquilinos/historico", layout = MainLayout.class)
@PageTitle("Histórico do inquilino")
public class InquilinoHistoricoView extends VerticalLayout implements BeforeEnterObserver {

    record Registro(LocalDate data, String descricao) {}

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Grid<Registro> grid = new Grid<>();
    private final Span subtitulo = new Span("");
    private String nomeInquilino = "";

    public InquilinoHistoricoView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarTabela());
        atualizarRegistros(new ArrayList<>());
    }

    // Lê ?nome=... enviado pelo botão Histórico do modal
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        nomeInquilino = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("nome", List.of()).stream().findFirst().orElse("");
        subtitulo.setText(nomeInquilino.isBlank() ? "" : "Inquilino: " + nomeInquilino);
        // futuramente: buscar os registros desse inquilino no banco
    }

    private Component criarTopo() {
        H2 titulo = new H2("Inquilinos");
        Button novo = new Button("+ Novo Inquilino", e -> getUI().ifPresent(ui -> ui.navigate("inquilinos")));
        novo.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, novo);
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarTabela() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(r -> r.data().format(DATA)).setHeader("Data").setFlexGrow(0).setWidth("120px");
        grid.addColumn(Registro::descricao).setHeader("Descrição");
        grid.setEmptyStateText("Nenhum registro");
        grid.setHeight("400px");

        // Clicar num registro abre a Ficha do inquilino
        grid.addItemClickListener(e -> getUI().ifPresent(ui -> ui.navigate("inquilinos/ficha",
                QueryParameters.simple(Map.of("nome", nomeInquilino)))));

        H2 titulo = new H2("Movimentação de registros");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");
        subtitulo.getStyle().set("font-size", "0.75rem").set("color", "gray");
        Button verTudo = new Button("Ver tudo");

        VerticalLayout textos = new VerticalLayout(titulo, subtitulo);
        textos.setPadding(false);
        textos.setSpacing(false);

        HorizontalLayout cabecalho = new HorizontalLayout(textos, verTudo);
        cabecalho.setWidthFull();
        cabecalho.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout caixa = new VerticalLayout(cabecalho, grid);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    // Ponto de ligação com o backend
    public void atualizarRegistros(List<Registro> lista) {
        grid.setItems(lista);
    }
}