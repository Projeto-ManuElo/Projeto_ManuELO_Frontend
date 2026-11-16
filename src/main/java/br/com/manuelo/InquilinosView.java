package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "inquilinos", layout = MainLayout.class)
@PageTitle("Inquilinos")
public class InquilinosView extends VerticalLayout {

    record Inquilino(String nome, String cpf, String email) {}

    private final Grid<Inquilino> grid = new Grid<>();
    private final TextField nome = new TextField("Nome");
    private final TextField cpf = new TextField("CPF");
    private final TextField email = new TextField("Email");
    private final InquilinoDialog dialog = new InquilinoDialog(this::salvarInquilino);

    public InquilinosView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFiltros(), criarTabela());

        atualizarInquilinos(new ArrayList<>());
    }

    private Component criarTopo() {
        H2 titulo = new H2("Inquilinos");
        Button novo = new Button("Novo Inquilino", e -> dialog.abrirNovo());
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
        cpf.setPlaceholder("Ex: xxx.yyy.zzz.xz");
        cpf.setWidthFull();
        email.setPlaceholder("Ex: joao@wmail.com");
        email.setWidthFull();
        HorizontalLayout linha = new HorizontalLayout(cpf, email);
        linha.setWidthFull();

        Button cancelar = new Button("Cancelar", e -> limparFiltros());
        Button filtrar = new Button("Filtrar", e -> aplicarFiltros());
        filtrar.getStyle().set("background", "#5b5bf0").set("color", "white");
        HorizontalLayout botoes = new HorizontalLayout(cancelar, filtrar);
        botoes.setWidthFull();
        botoes.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout caixa = new VerticalLayout(titulo, nome, linha, botoes);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private Component criarTabela() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(Inquilino::nome).setHeader("Nome");
        grid.addColumn(Inquilino::cpf).setHeader("CPF");
        grid.addColumn(Inquilino::email).setHeader("Email");
        grid.setEmptyStateText("Nenhum inquilino cadastrado");
        grid.setHeight("300px");

        // Clicar na linha abre o modal de edição
        grid.addItemClickListener(e -> {
            Inquilino i = e.getItem();
            dialog.abrirEdicao(new InquilinoDialog.DadosInquilino(i.nome(), i.cpf(), i.email()));
        });

        H2 titulo = new H2("Inquilinos Cadastrados");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        VerticalLayout caixa = new VerticalLayout(titulo, grid);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private void limparFiltros() {
        nome.clear();
        cpf.clear();
        email.clear();
    }

    // Ponto de ligação com o backend
    private void aplicarFiltros() {
        // futuramente: buscar usando nome, cpf e email
    }

    private void salvarInquilino(InquilinoDialog.DadosInquilino d) {
        if (dialog.isEditando()) {
            // futuramente: atualizar o inquilino no banco
        } else {
            // futuramente: criar um novo inquilino no banco
        }
    }

    public void atualizarInquilinos(List<Inquilino> lista) {
        grid.setItems(lista);
    }
}