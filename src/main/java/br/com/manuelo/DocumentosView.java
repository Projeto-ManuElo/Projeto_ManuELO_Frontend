package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "documentos", layout = MainLayout.class)
@PageTitle("Documentos")
public class DocumentosView extends VerticalLayout {

    private final TextField nome = new TextField("Nome");
    private final ComboBox<String> tipoDocumento = new ComboBox<>("Tipo de documento");
    private final ComboBox<String> tipoFinanceiro = new ComboBox<>("Tipo financeiro");
    private final TextField nome2 = new TextField("Nome");
    private final DocumentoLista lista = new DocumentoLista();
    private final DocumentoDialog dialog = new DocumentoDialog(this::salvarDocumento);

    public DocumentosView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFiltros(), criarRecentes());

        atualizarDocumentos(new ArrayList<>());
    }

    private Component criarTopo() {
        H2 titulo = new H2("Documentos");
        titulo.getStyle().set("margin", "0");
        Span sub = new Span("Envie, organize e acompanhe arquivos vinculados à operação");
        sub.getStyle().set("font-size", "0.8rem").set("color", "gray");
        VerticalLayout textos = new VerticalLayout(titulo, sub);
        textos.setPadding(false);
        textos.setSpacing(false);

        Button adicionar = new Button("Adicionar documento", e -> dialog.abrirNovo());
        adicionar.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(textos, adicionar);
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarFiltros() {
        H2 titulo = new H2("Filtros");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        nome.setPlaceholder("nome do inquilino, sobrenome");
        nome.setWidthFull();

        tipoDocumento.setItems("Contrato", "Comprovante", "Cadastro", "Outros");
        tipoDocumento.setPlaceholder("Todos");
        tipoDocumento.setWidthFull();
        tipoFinanceiro.setItems("Entrada", "Saída");
        tipoFinanceiro.setPlaceholder("Todos");
        tipoFinanceiro.setWidthFull();
        HorizontalLayout linha = new HorizontalLayout(tipoDocumento, tipoFinanceiro);
        linha.setWidthFull();

        nome2.setPlaceholder("nome do inquilino, sobrenome");
        nome2.setWidthFull();

        Button cancelar = new Button("Cancelar", e -> limparFiltros());
        Button filtrar = new Button("Filtrar", e -> filtrar());
        filtrar.getStyle().set("background", "#5b5bf0").set("color", "white");
        HorizontalLayout botoes = new HorizontalLayout(cancelar, filtrar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout caixa = new VerticalLayout(titulo, nome, linha, nome2, botoes);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private Component criarRecentes() {
        H2 titulo = new H2("Documento recentes");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        VerticalLayout caixa = new VerticalLayout(titulo, lista);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private void limparFiltros() {
        nome.clear();
        tipoDocumento.clear();
        tipoFinanceiro.clear();
        nome2.clear();
    }

    // Abre a tela de resultados com os filtros na URL
    private void filtrar() {
        Map<String, String> params = new HashMap<>();
        if (!nome.isEmpty()) {
            params.put("nome", nome.getValue());
        }
        if (!tipoDocumento.isEmpty()) {
            params.put("tipo", tipoDocumento.getValue());
        }
        if (!tipoFinanceiro.isEmpty()) {
            params.put("financeiro", tipoFinanceiro.getValue());
        }
        if (!nome2.isEmpty()) {
            params.put("nome2", nome2.getValue());
        }
        getUI().ifPresent(ui -> ui.navigate("documentos/filtrados", QueryParameters.simple(params)));
    }

    // Ponto de ligação com o backend
    private void salvarDocumento(DocumentoDialog.DadosDocumento d) {
        // futuramente: salvar o documento no banco e recarregar a lista
    }

    public void atualizarDocumentos(List<DocumentoLista.Documento> documentos) {
        lista.setDocumentos(documentos);
    }
}