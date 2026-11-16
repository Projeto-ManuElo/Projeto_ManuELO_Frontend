package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;

@Route(value = "contratos/pagar", layout = MainLayout.class)
@PageTitle("Pagar aluguel")
public class ContratoPagarView extends VerticalLayout {

    private final BigDecimalField valorAluguel = new BigDecimalField("Valor do aluguel");
    private final ComboBox<String> formaPagamento = new ComboBox<>("Forma de pagamento");
    private final BigDecimalField caucaoInicial = new BigDecimalField("Calção inicial");
    private final BigDecimalField caucaoAtual = new BigDecimalField("Calção atual");

    public ContratoPagarView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(new H2("Contratos"), criarInformacoes(), criarContratoReferente());
        // futuramente: carregar os valores do contrato do inquilino
    }

    private Component criarInformacoes() {
        H3 titulo = new H3("Informações do aluguel");
        titulo.getStyle().set("margin", "0").set("font-size", "1rem");

        valorAluguel.setPlaceholder("R$ 350");
        formaPagamento.setItems("PIX", "Boleto", "Dinheiro", "Transferência", "Cartão");
        formaPagamento.setPlaceholder("Ex.:pix,boleto e etc...");
        caucaoInicial.setPlaceholder("R$ 800");
        caucaoAtual.setPlaceholder("R$ 400");

        Button pagar = new Button("Pagar", e -> pagar(false));
        pagar.getStyle().set("background", "#5b5bf0").set("color", "white");
        Button pagarCaucao = new Button("Pagar com calção", e -> pagar(true));
        pagarCaucao.getStyle().set("background", "#fff3cd").set("color", "#b7791f");
        HorizontalLayout botoes = new HorizontalLayout(pagar, pagarCaucao);

        VerticalLayout caixa = new VerticalLayout(titulo,
                dupla(valorAluguel, formaPagamento), dupla(caucaoInicial, caucaoAtual), botoes);
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

    // Ponto de ligação com o backend
    private void pagar(boolean comCaucao) {
        // futuramente: registrar o pagamento (usando o calção se comCaucao for true)
    }
}