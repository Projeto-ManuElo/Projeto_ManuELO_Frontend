package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "inquilinos/ficha", layout = MainLayout.class)
@PageTitle("Ficha do inquilino")
public class InquilinoFichaView extends VerticalLayout implements BeforeEnterObserver {

    private final TextField nome = new TextField("Nome");
    private final TextField cpf = new TextField("CPF");
    private final TextField status = new TextField("Status");
    private final TextField telefone = new TextField("Telefone");
    private final TextArea geral = new TextArea("Geral");
    private final TextArea contratos = new TextArea("1.Contratos");
    private final TextArea lancamentos = new TextArea("2.Lançamentos Financeiros");
    private final TextArea alugueisEmAberto = new TextArea("3.Aluguéis em aberto");
    private final TextArea documentos = new TextArea("4.Documentos do Inquilino");

    public InquilinoFichaView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFicha());
    }

    // Lê ?nome=... vindo do histórico
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String n = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("nome", List.of()).stream().findFirst().orElse("");
        nome.setValue(n);
        // futuramente: carregar a ficha completa do inquilino do banco
    }

    private Component criarTopo() {
        H2 titulo = new H2("Inquilinos");
        Button novo = new Button("+ Novo Inquilino", e -> getUI().ifPresent(ui -> ui.navigate("inquilinos")));
        novo.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, novo);
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarFicha() {
        H3 titulo = new H3("Ficha Inquilino");
        titulo.getStyle().set("margin", "0");

        nome.setWidthFull();
        cpf.setWidthFull();
        status.setWidthFull();
        telefone.setWidthFull();
        geral.setWidthFull();
        contratos.setWidthFull();
        lancamentos.setWidthFull();
        alugueisEmAberto.setWidthFull();
        documentos.setWidthFull();

        nome.setReadOnly(true);
        cpf.setReadOnly(true);
        status.setReadOnly(true);
        telefone.setReadOnly(true);
        geral.setReadOnly(true);
        contratos.setReadOnly(true);
        lancamentos.setReadOnly(true);
        alugueisEmAberto.setReadOnly(true);
        documentos.setReadOnly(true);

        geral.setMinHeight("80px");
        contratos.setMinHeight("120px");
        lancamentos.setMinHeight("120px");
        alugueisEmAberto.setMinHeight("120px");
        documentos.setMinHeight("120px");

        HorizontalLayout linha1 = new HorizontalLayout(nome, cpf);
        linha1.setWidthFull();
        HorizontalLayout linha2 = new HorizontalLayout(status, telefone);
        linha2.setWidthFull();
        HorizontalLayout linha3 = new HorizontalLayout(contratos, lancamentos);
        linha3.setWidthFull();
        HorizontalLayout linha4 = new HorizontalLayout(alugueisEmAberto, documentos);
        linha4.setWidthFull();

        VerticalLayout caixa = new VerticalLayout(titulo, linha1, linha2, geral, linha3, linha4);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "#ffffff").set("border-radius", "10px");
        return caixa;
    }

    // Ponto de ligação com o backend
    public void preencher(String cpfValor, String statusValor, String telefoneValor, String resumoGeral,
                          String contratosTexto, String lancamentosTexto,
                          String emAbertoTexto, String documentosTexto) {
        cpf.setValue(cpfValor);
        status.setValue(statusValor);
        telefone.setValue(telefoneValor);
        geral.setValue(resumoGeral);
        contratos.setValue(contratosTexto);
        lancamentos.setValue(lancamentosTexto);
        alugueisEmAberto.setValue(emAbertoTexto);
        documentos.setValue(documentosTexto);
    }
}