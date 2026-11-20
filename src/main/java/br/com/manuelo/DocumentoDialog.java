package br.com.manuelo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;

import java.util.function.Consumer;

public class DocumentoDialog extends Dialog {

    public record DadosDocumento(String titulo, String descricao, String tipo, String inquilino) {}

    private final TextField titulo = new TextField("Título");
    private final TextArea descricao = new TextArea("Descrição");
    private final ComboBox<String> tipo = new ComboBox<>("Tipo");
    private final TextField inquilino = new TextField("Inquilino vinculado");
    private final Consumer<DadosDocumento> aoSalvar;

    public DocumentoDialog(Consumer<DadosDocumento> aoSalvar) {
        this.aoSalvar = aoSalvar;
        setWidth("600px");
        setCloseOnOutsideClick(false);

        H3 cabecalho = new H3("Adicionar Documento");
        cabecalho.getStyle().set("margin", "0");

        titulo.setPlaceholder("Ex: Rua das Flores, 88");
        titulo.setWidthFull();
        titulo.setRequiredIndicatorVisible(true);

        descricao.setPlaceholder("Ex: Rua das Flores, 88");
        descricao.setWidthFull();
        descricao.setMinHeight("150px");

        tipo.setItems("Contrato", "Comprovante", "Cadastro", "Outros");
        tipo.setPlaceholder("Escolha um tipo");
        tipo.setWidthFull();
        tipo.setRequiredIndicatorVisible(true);

        inquilino.setPlaceholder("Buscar por nome ou documento");
        inquilino.setWidthFull();

        Span rotuloUpload = new Span("Documento escaneado");
        rotuloUpload.getStyle().set("font-size", "var(--lumo-font-size-s)");
        Upload upload = new Upload(UploadHandler.inMemory((metadata, bytes) -> {
            // futuramente: guardar o arquivo junto com o documento
        }));
        upload.setAcceptedMimeTypes("application/pdf", "image/png", "image/jpeg");
        upload.setAcceptedFileExtensions(".pdf", ".png", ".jpg", ".jpeg");
        upload.setMaxFileSize(10 * 1024 * 1024);
        upload.setDropLabel(new Span("Arraste o documento aqui ou clique para selecionar"));

        Button cancelar = new Button("Cancelar", e -> close());
        Button salvar = new Button("Salvar", e -> salvar());
        salvar.getStyle().set("background", "#5b5bf0").set("color", "white");
        HorizontalLayout botoes = new HorizontalLayout(cancelar, salvar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout conteudo = new VerticalLayout(
                cabecalho, titulo, descricao, tipo, inquilino, rotuloUpload, upload, botoes);
        conteudo.setPadding(false);
        add(conteudo);
    }

    public void abrirNovo() {
        titulo.clear();
        descricao.clear();
        tipo.clear();
        inquilino.clear();
        open();
    }

    private void salvar() {
        titulo.setInvalid(titulo.isEmpty());
        tipo.setInvalid(tipo.isEmpty());
        if (titulo.isEmpty() || tipo.isEmpty()) {
            return;
        }
        aoSalvar.accept(new DadosDocumento(
                titulo.getValue(), descricao.getValue(), tipo.getValue(), inquilino.getValue()));
        close();
    }
}