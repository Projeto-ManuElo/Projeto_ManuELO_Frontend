package br.com.manuelo;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class DocumentoLista extends VerticalLayout {

    public record Documento(String titulo, String detalhe, String etiqueta) {}

    public DocumentoLista() {
        setPadding(false);
    }

    public void setDocumentos(List<Documento> lista) {
        removeAll();
        if (lista == null || lista.isEmpty()) {
            Span vazio = new Span("Nenhum documento encontrado");
            vazio.getStyle().set("color", "gray");
            add(vazio);
            return;
        }
        lista.forEach(d -> add(linha(d)));
    }

    private HorizontalLayout linha(Documento d) {
        Icon icone = VaadinIcon.FILE_TEXT_O.create();
        icone.setSize("18px");
        Span caixa = new Span(icone);
        caixa.getStyle()
                .set("background", "#e5e7f0")
                .set("border-radius", "8px")
                .set("padding", "8px")
                .set("display", "flex");

        Span titulo = new Span(d.titulo());
        titulo.getStyle().set("font-weight", "bold").set("font-size", "0.9rem");
        Span detalhe = new Span(d.detalhe());
        detalhe.getStyle().set("font-size", "0.75rem").set("color", "gray");
        VerticalLayout textos = new VerticalLayout(titulo, detalhe);
        textos.setPadding(false);
        textos.setSpacing(false);

        HorizontalLayout esquerda = new HorizontalLayout(caixa, textos);
        esquerda.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout linha = new HorizontalLayout(esquerda, badge(d.etiqueta()));
        linha.setWidthFull();
        linha.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        linha.setAlignItems(FlexComponent.Alignment.CENTER);
        linha.getStyle()
                .set("background", "#f6f7fb")
                .set("border-radius", "8px")
                .set("padding", "10px");
        return linha;
    }

    private Span badge(String texto) {
        boolean pendente = "Pendente".equals(texto);
        Span b = new Span(texto);
        b.getStyle()
                .set("color", pendente ? "#b7791f" : "#00a884")
                .set("background", pendente ? "#fff3cd" : "#dcfce7")
                .set("border-radius", "10px")
                .set("padding", "2px 10px")
                .set("font-size", "0.7rem");
        return b;
    }
}