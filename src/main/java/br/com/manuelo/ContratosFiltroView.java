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

import java.util.ArrayList;
import java.util.List;

@Route(value = "home/filtro/:tipo", layout = MainLayout.class)
@PageTitle("Visão geral")
public class ContratosFiltroView extends VerticalLayout implements BeforeEnterObserver {

    record Movimentacao(String data, String descricao, String categoria, String valor) {}

    private final Grid<Movimentacao> grid = new Grid<>();
    private Grid.Column<Movimentacao> colunaCategoria;
    private final Span rotuloCard = new Span("Contratos próximos dos vencimentos");
    private final Span numero = new Span("0");
    private final Span detalhe = new Span("Aguardando resolução");

    public ContratosFiltroView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarCard(), criarTabela());
        atualizarMovimentacoes(new ArrayList<>());
    }

    // Lê :tipo (vencendo ou atrasados)
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String tipo = event.getRouteParameters().get("tipo").orElse("vencendo");
        if (tipo.equals("atrasados")) {
            rotuloCard.setText("Contratos atrasados");
            detalhe.setText("0 mais de uma semana sem pagar");
            detalhe.getStyle().set("color", "#e11d48");
            colunaCategoria.setVisible(true);
        } else {
            rotuloCard.setText("Contratos próximos dos vencimentos");
            detalhe.setText("Aguardando resolução");
            detalhe.getStyle().set("color", "#4f46e5");
            colunaCategoria.setVisible(false);
        }
        // futuramente: buscar no banco os dados desse tipo
    }

    private Component criarTopo() {
        H2 titulo = new H2("Visão geral");
        Button exportar = new Button("Exportar");
        Button novo = new Button("Novo Contrato", e -> getUI().ifPresent(ui -> ui.navigate("contratos/novo")));
        novo.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, new HorizontalLayout(exportar, novo));
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarCard() {
        rotuloCard.getStyle().set("font-size", "0.8rem").set("color", "#555");
        numero.getStyle().set("font-size", "2rem").set("font-weight", "bold");
        detalhe.getStyle().set("font-size", "0.75rem").set("color", "#4f46e5").set("font-weight", "bold");

        VerticalLayout card = new VerticalLayout(rotuloCard, numero, detalhe);
        card.setSpacing(false);
        card.setWidthFull();
        card.getStyle().set("background", "white").set("border-radius", "10px");
        return card;
    }

    private Component criarTabela() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(Movimentacao::data).setHeader("Data").setFlexGrow(0).setWidth("90px");
        grid.addColumn(Movimentacao::descricao).setHeader("Descrição");
        colunaCategoria = grid.addComponentColumn(m -> badge(m.categoria())).setHeader("Categoria");
        grid.addComponentColumn(m -> {
            Span v = new Span(m.valor());
            v.getStyle().set("color", "#e11d48").set("font-weight", "bold");
            return v;
        }).setHeader("Valor");
        grid.setEmptyStateText("Nenhuma movimentação registrada");
        grid.setHeight("300px");

        H2 titulo = new H2("Movimentação recentes");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");
        Span sub = new Span("Últimas movimentações registradas no sistema");
        sub.getStyle().set("font-size", "0.75rem").set("color", "gray");
        Button verTudo = new Button("Ver tudo");

        VerticalLayout textos = new VerticalLayout(titulo, sub);
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

    // Pontos de ligação com o backend
    public void atualizarResumo(int quantidade) {
        numero.setText(String.valueOf(quantidade));
    }

    public void atualizarMovimentacoes(List<Movimentacao> lista) {
        grid.setItems(lista);
    }

    private Span badge(String texto) {
        String cor = texto.startsWith("+") ? "#e11d48" : "#f59e0b";
        Span b = new Span(texto);
        b.getStyle()
                .set("color", cor)
                .set("border", "1px solid " + cor)
                .set("border-radius", "6px")
                .set("padding", "2px 8px")
                .set("font-size", "0.7rem");
        return b;
    }
}