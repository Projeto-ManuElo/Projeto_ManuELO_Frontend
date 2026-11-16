package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;

@Route(value = "contratos/editar", layout = MainLayout.class)
@PageTitle("Editar contrato")
public class ContratoEditarView extends VerticalLayout {

    private final TextField nome = new TextField("Nome");
    private final TextField endereco = new TextField("Endereço");
    private final TextField cpf = new TextField("CPF");
    private final EmailField email = new EmailField("Email");
    private final TextField acaoJudicial = new TextField("Tem ação judicial");
    private final BigDecimalField caucaoInicial = new BigDecimalField("Calção inicial");
    private final BigDecimalField caucaoAtual = new BigDecimalField("Calção atual");

    public ContratoEditarView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarInformacoes(), criarContratoReferente());
        // futuramente: carregar os dados do contrato escolhido
    }

    private Component criarTopo() {
        H2 titulo = new H2("Contratos");
        Button novo = new Button("Novo contrato", e -> getUI().ifPresent(ui -> ui.navigate("contratos/pagar")));
        novo.getStyle().set("background", "#5b5bf0").set("color", "white");
        Button pagar = new Button("Pagar o aluguel", e -> getUI().ifPresent(ui -> ui.navigate("financeiro/entrada")));
        pagar.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, new HorizontalLayout(novo, pagar));
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarInformacoes() {
        H3 titulo = new H3("Informações do Inquilino");
        titulo.getStyle().set("margin", "0").set("font-size", "1rem");

        nome.setWidthFull();
        VerticalLayout caixa = new VerticalLayout(titulo, nome,
                dupla(endereco, cpf), dupla(email, acaoJudicial), dupla(caucaoInicial, caucaoAtual));
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private Component criarContratoReferente() {
        H3 titulo = new H3("Contrato referente");
        titulo.getStyle().set("margin", "0").set("font-size", "1rem");

        Upload upload = new Upload(UploadHandler.inMemory((metadata, bytes) -> {
            // futuramente: guardar o contrato junto com o inquilino
        }));
        upload.setAcceptedMimeTypes("application/pdf", "image/png", "image/jpeg");
        upload.setAcceptedFileExtensions(".pdf", ".png", ".jpg", ".jpeg");
        upload.setMaxFileSize(10 * 1024 * 1024);
        upload.setDropLabel(new Span("Aqui vai ficar o contrato do inquilino"));

        VerticalLayout caixa = new VerticalLayout(titulo, upload);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
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
}