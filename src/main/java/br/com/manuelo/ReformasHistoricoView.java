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
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "imoveis/reformas", layout = MainLayout.class)
@PageTitle("Histórico de reformas")
public class ReformasHistoricoView extends VerticalLayout implements BeforeEnterObserver {

    record Reforma(LocalDate data, String descricao, String proprietarioPeriodo, String valor) {}

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Grid<Reforma> grid = new Grid<>();
    private final Span subtitulo = new Span("");

    public ReformasHistoricoView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarTabela());
        atualizarReformas(new ArrayList<>());
    }

    // Lê ?endereco=... enviado pelo botão Reformas do modal
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String endereco = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("endereco", List.of()).stream().findFirst().orElse("-");
        subtitulo.setText("Proprietário atual: -; " + endereco);
        // futuramente: buscar as reformas desse imóvel no banco
    }

    private Component criarTopo() {
        H2 titulo = new H2("Visão geral");
        Button exportar = new Button("Exportar");
        Button novo = new Button("+ Novo Registro");
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
        grid.addColumn(Reforma::descricao).setHeader("Descrição");
        grid.addComponentColumn(r -> {
            Span s = new Span(r.proprietarioPeriodo());
            s.getStyle().set("color", "#4f46e5").set("font-size", "0.8rem");
            return s;
        }).setHeader("Proprietário do período");
        grid.addComponentColumn(r -> {
            Span v = new Span(r.valor());
            v.getStyle().set("color", "#e11d48").set("font-weight", "bold");
            return v;
        }).setHeader("Valor");
        grid.setEmptyStateText("Nenhuma reforma registrada");
        grid.setHeight("400px");

        H2 titulo = new H2("Histórico de reformas");
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
    public void atualizarReformas(List<Reforma> lista) {
        grid.setItems(lista);
    }
}