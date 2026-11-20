package br.com.manuelo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmarExportacaoDialog extends Dialog {

    public ConfirmarExportacaoDialog(Runnable aoConfirmar) {
        setWidth("520px");
        setCloseOnOutsideClick(false);

        H2 titulo = new H2("Confirmar Exportação?");
        titulo.getStyle().set("margin", "0");

        Icon check = VaadinIcon.CHECK.create();
        check.setSize("60px");
        check.getStyle().set("color", "white");
        Span circulo = new Span(check);
        circulo.getStyle()
                .set("background", "#14b8a6")
                .set("border-radius", "50%")
                .set("width", "120px")
                .set("height", "120px")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center");

        Span pergunta = new Span("Você tem certeza de que o período requisitado está correto?");
        pergunta.getStyle().set("font-weight", "bold").set("text-align", "center");
        Span aviso = new Span("Após essa confirmação o PDF será gerado com base no período relatado.");
        aviso.getStyle().set("font-size", "0.75rem").set("color", "gray").set("text-align", "center");

        Button cancelar = new Button("Cancelar", e -> close());
        Button confirmar = new Button("Confirmar", e -> {
            aoConfirmar.run();
            close();
        });
        confirmar.getStyle().set("background", "#14b8a6").set("color", "white");

        HorizontalLayout botoes = new HorizontalLayout(cancelar, confirmar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout conteudo = new VerticalLayout(titulo, circulo, pergunta, aviso, botoes);
        conteudo.setAlignItems(FlexComponent.Alignment.CENTER);
        conteudo.setPadding(false);
        add(conteudo);
    }
}