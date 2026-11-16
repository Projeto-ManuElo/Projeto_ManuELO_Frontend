package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Route(value = "contratos", layout = MainLayout.class)
@PageTitle("Contratos")
public class ContratosView extends VerticalLayout {

    record Contrato(String nome, LocalDate validade, String endereco, String cpf, String email) {}

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Grid<Contrato> grid = new Grid<>();
    private final TextField nome = new TextField("Nome");
    private final TextField cpf = new TextField("CPF");
    private final TextField email = new TextField("Email");
    private final DatePicker validade = new DatePicker("Validade do contrato");
    private final ComboBox<String> situacao = new ComboBox<>("Situação");

    public ContratosView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFiltros(), criarTabela());

        atualizarContratos(new ArrayList<>());
    }

    private Component criarTopo() {
        H2 titulo = new H2("Contratos");
        Button novo = new Button("Novo Contrato", e -> getUI().ifPresent(ui -> ui.navigate("contratos/novo")));
        novo.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, novo);
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarFiltros() {
        H2 titulo = new H2("Filtros");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        nome.setPlaceholder("Ex: João Miguel");
        nome.setWidthFull();

        cpf.setPlaceholder("Ex: xxx.yyy.zzz-xz");
        cpf.setWidthFull();
        email.setPlaceholder("Ex: joao@wmail.com");
        email.setWidthFull();
        HorizontalLayout linha1 = new HorizontalLayout(cpf, email);
        linha1.setWidthFull();

        validade.setPlaceholder("Ex: xx/xx/xx26");
        validade.setWidthFull();
        situacao.setItems("Em dia", "Vencendo", "Atrasado");
        situacao.setPlaceholder("Ex: Atrasado ou não");
        situacao.setWidthFull();
        HorizontalLayout linha2 = new HorizontalLayout(validade, situacao);
        linha2.setWidthFull();

        Button cancelar = new Button("Cancelar", e -> limparFiltros());
        Button filtrar = new Button("Filtrar", e -> aplicarFiltros());
        filtrar.getStyle().set("background", "#5b5bf0").set("color", "white");
        HorizontalLayout botoes = new HorizontalLayout(cancelar, filtrar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout caixa = new VerticalLayout(titulo, nome, linha1, linha2, botoes);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private Component criarTabela() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(Contrato::nome).setHeader("Nome");
        grid.addColumn(c -> c.validade() == null ? "-" : c.validade().format(DATA) + " " + prazo(c.validade()))
                .setHeader("Validade");
        grid.addColumn(Contrato::endereco).setHeader("Endereço");
        grid.addColumn(Contrato::cpf).setHeader("CPF");
        grid.addColumn(Contrato::email).setHeader("Email");
        grid.setEmptyStateText("Nenhum contrato cadastrado");
        grid.setHeight("300px");

        // Clicar na linha abre a edição do contrato
        grid.addItemClickListener(e -> getUI().ifPresent(ui -> ui.navigate("contratos/editar")));

        H2 titulo = new H2("Contratos recentes");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        VerticalLayout caixa = new VerticalLayout(titulo, grid);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private String prazo(LocalDate validade) {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), validade);
        return dias >= 0 ? "Faltam " + dias + " dias" : "Vencido há " + (-dias) + " dias";
    }

    private void limparFiltros() {
        nome.clear();
        cpf.clear();
        email.clear();
        validade.clear();
        situacao.clear();
    }

    // Ponto de ligação com o backend
    private void aplicarFiltros() {
        // futuramente: buscar usando nome, cpf, email, validade e situação
    }

    public void atualizarContratos(List<Contrato> lista) {
        grid.setItems(lista);
    }
}