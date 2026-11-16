package br.com.manuelo;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ExtratoGrid extends Grid<ExtratoGrid.Lancamento> {

    public record Lancamento(LocalDate data, String tipo, String categoria, BigDecimal valor) {}

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    public ExtratoGrid() {
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        addColumn(l -> l.data().format(DATA)).setHeader("Data").setFlexGrow(0).setWidth("120px");
        addComponentColumn(l -> badge(l.tipo())).setHeader("Tipo").setFlexGrow(0).setWidth("120px");
        addColumn(Lancamento::categoria).setHeader("Categoria");
        addComponentColumn(l -> {
            Span v = new Span(MOEDA.format(l.valor()));
            v.getStyle().set("font-weight", "bold");
            return v;
        }).setHeader("Valor").setTextAlign(ColumnTextAlign.END);
        setEmptyStateText("Nenhum lançamento no período");
        setHeight("250px");
    }

    private Span badge(String tipo) {
        String cor = "Entrada".equals(tipo) ? "#00a884" : "#e11d48";
        Span b = new Span(tipo);
        b.getStyle()
                .set("color", cor)
                .set("border", "1px solid " + cor)
                .set("border-radius", "6px")
                .set("padding", "2px 8px")
                .set("font-size", "0.7rem");
        return b;
    }
}