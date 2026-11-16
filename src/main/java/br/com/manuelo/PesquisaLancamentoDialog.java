package br.com.manuelo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class PesquisaLancamentoDialog extends Dialog {

    private final TextField recebedor = new TextField("Nome do recebedor");
    private final BigDecimalField valor = new BigDecimalField("Valor");
    private final ComboBox<String> tipo = new ComboBox<>("Tipo");
    private final ExtratoGrid extrato = new ExtratoGrid();

    public PesquisaLancamentoDialog() {
        setWidth("700px");
        setCloseOnOutsideClick(false);

        H3 titulo = new H3("Pesquisar Lançamento");
        titulo.getStyle().set("margin", "0");

        recebedor.setPlaceholder("Ex: João Miguel Oliveira de Souza");
        recebedor.setWidthFull();

        valor.setPlaceholder("R$ 0,00");
        valor.setWidthFull();

        tipo.setItems("Entrada", "Saída");
        tipo.setPlaceholder("Escolha um tipo");
        tipo.setWidthFull();

        HorizontalLayout linha = new HorizontalLayout(valor, tipo);
        linha.setWidthFull();

        Button cancelar = new Button("Cancelar", e -> {
            limpar();
            close();
        });
        Button filtrar = new Button("Filtrar", e -> filtrar());
        filtrar.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout botoes = new HorizontalLayout(cancelar, filtrar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        H2 tituloExtrato = new H2("Extrato do período");
        tituloExtrato.getStyle().set("font-size", "1rem").set("margin", "0");

        VerticalLayout conteudo = new VerticalLayout(
                titulo, recebedor, linha, botoes, tituloExtrato, extrato);
        conteudo.setPadding(false);
        add(conteudo);

        extrato.setItems(new ArrayList<>());
    }

    private void limpar() {
        recebedor.clear();
        valor.clear();
        tipo.clear();
    }

    // Ponto de ligação com o backend: buscar usando recebedor, valor e tipo
    private void filtrar() {
        // futuramente: consultar o banco e chamar atualizarResultados(lista)
    }

    public void atualizarResultados(List<ExtratoGrid.Lancamento> lista) {
        extrato.setItems(lista);
    }
}