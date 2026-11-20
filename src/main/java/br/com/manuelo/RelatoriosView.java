package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Route(value = "relatorios/:tipo?", layout = MainLayout.class)
@PageTitle("Relatórios")
public class RelatoriosView extends VerticalLayout implements BeforeEnterObserver {

    private static final String CONTRATOS = "Contratos existentes";
    private static final String ENTRADA_SAIDA = "Entrada ou Saída";
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    private final ComboBox<String> trimestre = new ComboBox<>("Trimestre");
    private final ComboBox<String> formato = new ComboBox<>("Formato");
    private final ComboBox<String> tipoRelatorio = new ComboBox<>("Tipo de relatório");
    private final Span entradas = new Span();
    private final Span saidas = new Span();
    private final Span saldo = new Span();
    private final ExtratoGrid extrato = new ExtratoGrid();
    private final ConfirmarExportacaoDialog exportacao = new ConfirmarExportacaoDialog(this::gerarPdf);

    public RelatoriosView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFiltros(), criarResumo(), criarExtrato());

        atualizarResumo(BigDecimal.ZERO, BigDecimal.ZERO);
        atualizarExtrato(new ArrayList<>());
    }

    // Lê /relatorios (contratos) ou /relatorios/entrada-saida
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String tipo = event.getRouteParameters().get("tipo").orElse("contratos");
        tipoRelatorio.setValue(tipo.equals("entrada-saida") ? ENTRADA_SAIDA : CONTRATOS);
        // futuramente: buscar os dados do relatório escolhido
    }

    private Component criarTopo() {
        H2 titulo = new H2("Relatórios");
        Button gerar = new Button("Gerar PDF", e -> exportacao.open());
        gerar.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, gerar);
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarFiltros() {
        H3 titulo = new H3("Gerar relatório trimestral");
        titulo.getStyle().set("margin", "0").set("font-size", "1rem");

        trimestre.setItems("1º trimestre 2026 (jan-mar)", "2º trimestre 2026 (abr-jun)",
                "3º trimestre 2026 (jul-set)", "4º trimestre 2026 (out-dez)");
        trimestre.setValue("3º trimestre 2026 (jul-set)");

        formato.setItems("PDF", "Excel");
        formato.setValue("PDF");

        tipoRelatorio.setItems(CONTRATOS, ENTRADA_SAIDA);

        // Trocar o tipo abre a rota correspondente
        tipoRelatorio.addValueChangeListener(e -> {
            if (!e.isFromClient()) {
                return;
            }
            String destino = ENTRADA_SAIDA.equals(e.getValue()) ? "relatorios/entrada-saida" : "relatorios";
            getUI().ifPresent(ui -> ui.navigate(destino));
        });

        trimestre.setWidthFull();
        formato.setWidthFull();
        tipoRelatorio.setWidthFull();
        HorizontalLayout linha = new HorizontalLayout(trimestre, formato);
        linha.setWidthFull();

        VerticalLayout caixa = new VerticalLayout(titulo, linha, tipoRelatorio);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private Component criarResumo() {
        HorizontalLayout cards = new HorizontalLayout(
                card("Entradas", entradas, "#00a884"),
                card("Saídas", saidas, "#e11d48"),
                card("Saldo", saldo, "#1a1f44"));
        cards.setWidthFull();
        return cards;
    }

    private Component card(String rotulo, Span valor, String cor) {
        Span r = new Span(rotulo);
        r.getStyle().set("font-size", "0.8rem").set("color", "#555");
        valor.getStyle().set("font-size", "1.8rem").set("font-weight", "bold").set("color", cor);

        VerticalLayout c = new VerticalLayout(r, valor);
        c.setSpacing(false);
        c.getStyle().set("background", "white").set("border-radius", "10px").set("flex", "1");
        return c;
    }

    private Component criarExtrato() {
        H2 titulo = new H2("Extrato do período");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");
        extrato.setHeight("350px");

        VerticalLayout caixa = new VerticalLayout(titulo, extrato);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    // Ponto de ligação com o backend: chamado após confirmar a exportação
    private void gerarPdf() {
        // futuramente: gerar o relatório com o trimestre, formato e tipo escolhidos
    }

    // Pontos de ligação com o backend
    public void atualizarResumo(BigDecimal totalEntradas, BigDecimal totalSaidas) {
        entradas.setText(MOEDA.format(totalEntradas));
        saidas.setText(MOEDA.format(totalSaidas));
        saldo.setText(MOEDA.format(totalEntradas.subtract(totalSaidas)));
    }

    public void atualizarExtrato(List<ExtratoGrid.Lancamento> lista) {
        extrato.setItems(lista);
    }
}