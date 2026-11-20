package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.streams.UploadHandler;

import java.util.Map;
import java.util.function.Consumer;

public class InquilinoDialog extends Dialog {

    public record DadosInquilino(String nome, String cpf, String email) {}

    private final H3 titulo = new H3("Adicionar Inquilino");
    private final TextField nome = new TextField("Nome");
    private final TextField cpf = new TextField("CPF");
    private final EmailField email = new EmailField("Email");
    private final TextField nomeFiador = new TextField("Nome do fiador");
    private final TextField cpfFiador = new TextField("CPF do fiador");
    private final EmailField emailFiador = new EmailField("Email do fiador");
    private final TextField enderecoVinculado = new TextField("Endereço Vinculado");
    private final BigDecimalField valorCaucao = new BigDecimalField("Valor do calção");
    private final Button historico = new Button("Histórico");
    private final Consumer<DadosInquilino> aoSalvar;
    private boolean editando = false;

    public InquilinoDialog(Consumer<DadosInquilino> aoSalvar) {
        this.aoSalvar = aoSalvar;
        setWidth("700px");
        setCloseOnOutsideClick(false);

        titulo.getStyle().set("margin", "0");

        nome.setPlaceholder("Ex: João Miguel Oliveira de Souza");
        nome.setRequiredIndicatorVisible(true);
        cpf.setPlaceholder("ex: 123.xxx.xxx.123");
        cpf.setRequiredIndicatorVisible(true);
        email.setPlaceholder("ex: jteste@gmail.com");
        nomeFiador.setPlaceholder("Ex: João Miguel Oliveira de Souza");
        cpfFiador.setPlaceholder("ex: 123.xxx.xxx.123");
        emailFiador.setPlaceholder("ex: jteste@gmail.com");
        enderecoVinculado.setPlaceholder("Buscar por endereço");
        valorCaucao.setPlaceholder("R$ 0,00");

        nome.setWidthFull();
        nomeFiador.setWidthFull();
        enderecoVinculado.setWidthFull();
        valorCaucao.setWidthFull();

        Upload contrato = criarUpload("Arraste o contrato aqui ou clique para selecionar");
        Upload recibo = criarUpload("Arraste o contrato aqui ou clique para selecionar");

        // Histórico -> Movimentação de registros do inquilino
        historico.getStyle().set("background", "#5b5bf0").set("color", "white");
        historico.addClickListener(e -> {
            String n = nome.getValue();
            close();
            UI.getCurrent().navigate("inquilinos/historico",
                    QueryParameters.simple(Map.of("nome", n)));
        });

        Button cancelar = new Button("Cancelar", e -> close());
        Button salvar = new Button("Salvar", e -> salvar());
        salvar.getStyle().set("background", "#5b5bf0").set("color", "white");
        HorizontalLayout botoes = new HorizontalLayout(cancelar, historico, salvar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout conteudo = new VerticalLayout(
                titulo, nome, dupla(cpf, email), nomeFiador, dupla(cpfFiador, emailFiador),
                enderecoVinculado, rotulo("Contrato"), contrato,
                valorCaucao, rotulo("Recibo do calção"), recibo, botoes);
        conteudo.setPadding(false);
        add(conteudo);
    }

    private Upload criarUpload(String texto) {
        Upload upload = new Upload(UploadHandler.inMemory((metadata, bytes) -> {
            // futuramente: guardar o arquivo junto com o inquilino
        }));
        upload.setAcceptedMimeTypes("application/pdf", "image/png", "image/jpeg");
        upload.setAcceptedFileExtensions(".pdf", ".png", ".jpg", ".jpeg");
        upload.setMaxFileSize(10 * 1024 * 1024);
        upload.setDropLabel(new Span(texto));
        return upload;
    }

    private Span rotulo(String texto) {
        Span s = new Span(texto);
        s.getStyle().set("font-size", "var(--lumo-font-size-s)");
        return s;
    }

    private HorizontalLayout dupla(Component a, Component b) {
        if (a instanceof HasSize hs) {
            hs.setWidthFull();
        }
        if (b instanceof HasSize hs) {
            hs.setWidthFull();
        }
        HorizontalLayout l = new HorizontalLayout(a, b);
        l.setWidthFull();
        return l;
    }

    public void abrirNovo() {
        editando = false;
        titulo.setText("Adicionar Inquilino");
        limpar();
        historico.setVisible(false);
        open();
    }

    public void abrirEdicao(DadosInquilino d) {
        editando = true;
        titulo.setText("Editar Inquilino");
        limpar();
        nome.setValue(d.nome() == null ? "" : d.nome());
        cpf.setValue(d.cpf() == null ? "" : d.cpf());
        email.setValue(d.email() == null ? "" : d.email());
        historico.setVisible(true);
        open();
    }

    public boolean isEditando() {
        return editando;
    }

    private void limpar() {
        nome.clear();
        cpf.clear();
        email.clear();
        nomeFiador.clear();
        cpfFiador.clear();
        emailFiador.clear();
        enderecoVinculado.clear();
        valorCaucao.clear();
    }

    private void salvar() {
        nome.setInvalid(nome.isEmpty());
        cpf.setInvalid(cpf.isEmpty());
        if (nome.isEmpty() || cpf.isEmpty()) {
            return;
        }
        aoSalvar.accept(new DadosInquilino(nome.getValue(), cpf.getValue(), email.getValue()));
        close();
    }
}