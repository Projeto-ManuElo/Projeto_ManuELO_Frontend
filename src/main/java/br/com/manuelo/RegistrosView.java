package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "imoveis/registros", layout = MainLayout.class)
@PageTitle("Movimentação de registros")
public class RegistrosView extends VerticalLayout {

    record Registro(LocalDate data, String imovel, String proprietarioAtual) {}

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Grid<Registro> grid = new Grid<>();

    public RegistrosView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarTabela());
        atualizarRegistros(new ArrayList<>());
    }

    private Component criarTopo() {
        H2 titulo = new H2("Visão geral");
        Button exportar = new Button("Exportar");
        Button novo = new Button("+ Novo Imóvel", e -> getUI().ifPresent(ui -> ui.navigate("imoveis")));
        novo.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, new HorizontalLayout(exportar, novo));
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarTabela() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(r -> r.data().format(DATA)).setHeader("Data").setFlexGrow(0).setWidth("120px");
        grid.addColumn(r -> r.imovel() + "; Proprietário Atual: " + r.proprietarioAtual())
                .setHeader("Descrição");
        grid.setEmptyStateText("Nenhum registro");
        grid.setHeight("400px");

        H2 titulo = new H2("Movimentação de registros");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");
        Button verTudo = new Button("Ver tudo");

        HorizontalLayout cabecalho = new HorizontalLayout(titulo, verTudo);
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