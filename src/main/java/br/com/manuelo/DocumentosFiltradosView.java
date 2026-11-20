package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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

@Route(value = "documentos/filtrados", layout = MainLayout.class)
@PageTitle("Documentos filtrados")
public class DocumentosFiltradosView extends VerticalLayout implements BeforeEnterObserver {

    private final DocumentoLista lista = new DocumentoLista();
    private final DocumentoDialog dialog = new DocumentoDialog(this::salvarDocumento);

    public DocumentosFiltradosView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarResultados());

        atualizarDocumentos(new ArrayList<>());
    }

    // Lê os filtros enviados pela tela de Documentos (nome, tipo, financeiro, nome2)
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // futuramente: buscar no banco usando os parâmetros da URL
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

    private Component criarResultados() {
        H2 titulo = new H2("Documentação recente");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        VerticalLayout caixa = new VerticalLayout(titulo, lista);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    // Ponto de ligação com o backend
    private void salvarDocumento(DocumentoDialog.DadosDocumento d) {
        // futuramente: salvar o documento no banco e recarregar a lista
    }

    public void atualizarDocumentos(List<DocumentoLista.Documento> documentos) {
        lista.setDocumentos(documentos);
    }
}