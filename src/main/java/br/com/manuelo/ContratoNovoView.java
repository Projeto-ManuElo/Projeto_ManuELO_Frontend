package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contratos/novo", layout = MainLayout.class)
@PageTitle("Novo contrato")
public class ContratoNovoView extends VerticalLayout {

    private final TextField nome = new TextField("Nome");
    private final TextField endereco = new TextField("Endereço");
    private final TextField cpf = new TextField("CPF");
    private final EmailField email = new EmailField("Email");
    private final TextField acaoJudicial = new TextField("Tem ação judicial");
    private final BigDecimalField caucaoInicial = new BigDecimalField("Calção inicial");
    private final BigDecimalField caucaoAtual = new BigDecimalField("Calção atual");

    private final TextField fiadorNome = new TextField("Nome");
    private final EmailField fiadorEmail = new EmailField("Email");
    private final TextField fiadorNumero = new TextField("Numero");
    private final TextField fiadorCpf = new TextField("CPF");

    public ContratoNovoView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFormulario());
    }

    private Component criarTopo() {
        H2 titulo = new H2("Contratos");
        Button pagar = new Button("Pagar o aluguel",
                e -> getUI().ifPresent(ui -> ui.navigate("financeiro/entrada")));
        pagar.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, pagar);
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarFormulario() {
        H3 tituloInquilino = new H3("Informações do Inquilino");
        tituloInquilino.getStyle().set("margin", "0").set("font-size", "1rem");

        nome.setPlaceholder("João Miguel Oliveira dos Santos");
        endereco.setPlaceholder("Ap 302, Ed. Aurora");
        cpf.setPlaceholder("074.xxx.39-xy");
        email.setPlaceholder("joao@email.com");
        acaoJudicial.setPlaceholder("Se sim, qual?");
        caucaoInicial.setPlaceholder("R$ 800");
        caucaoAtual.setPlaceholder("R$ 400");

        nome.setWidthFull();
        HorizontalLayout linha1 = dupla(endereco, cpf);
        HorizontalLayout linha2 = dupla(email, acaoJudicial);
        HorizontalLayout linha3 = dupla(caucaoInicial, caucaoAtual);

        H3 tituloFiador = new H3("Fiador");
        tituloFiador.getStyle().set("margin", "0").set("font-size", "1rem");

        fiadorNome.setPlaceholder("Leocardi Daniel Oliveira de Oliveira");
        fiadorNome.setWidthFull();
        fiadorEmail.setPlaceholder("Leo@wmail.com");
        fiadorEmail.setWidthFull();
        fiadorNumero.setPlaceholder("090123-5467");
        fiadorCpf.setPlaceholder("876.xxx.59-xy");
        HorizontalLayout linha4 = dupla(fiadorNumero, fiadorCpf);

        Button criar = new Button("Criar contrato", e -> criarContrato());
        criar.getStyle().set("background", "#5b5bf0").set("color", "white");
        HorizontalLayout botoes = new HorizontalLayout(criar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout caixa = new VerticalLayout(
                tituloInquilino, nome, linha1, linha2, linha3,
                tituloFiador, fiadorNome, fiadorEmail, linha4, botoes);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private HorizontalLayout dupla(Component a, Component b) {
        if (a instanceof com.vaadin.flow.component.HasSize hs) {
            hs.setWidthFull();
        }
        if (b instanceof com.vaadin.flow.component.HasSize hs) {
            hs.setWidthFull();
        }
        HorizontalLayout l = new HorizontalLayout(a, b);
        l.setWidthFull();
        return l;
    }

    // Ponto de ligação com o backend
    private void criarContrato() {
        nome.setInvalid(nome.isEmpty());
        if (nome.isEmpty()) {
            return;
        }
        // futuramente: salvar o contrato no banco e voltar para a lista
        getUI().ifPresent(ui -> ui.navigate("contratos/pagar"));
    }
}