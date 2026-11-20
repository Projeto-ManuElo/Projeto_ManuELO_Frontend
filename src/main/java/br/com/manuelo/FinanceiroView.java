package br.com.manuelo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;

import java.util.ArrayList;
import java.util.List;

@Route(value = "financeiro/:tipo?", layout = MainLayout.class)
@PageTitle("Movimentação Financeira")
public class FinanceiroView extends VerticalLayout implements BeforeEnterObserver {

    private final Tab abaEntrada = new Tab("Entrada");
    private final Tab abaSaida = new Tab("Saída");
    private final Tabs abas = new Tabs(abaEntrada, abaSaida);

    private final ComboBox<String> categoria = new ComboBox<>("Categoria");
    private final ComboBox<String> formaPagamento = new ComboBox<>("Forma de pagamento");
    private final BigDecimalField valor = new BigDecimalField("Valor");
    private final DatePicker data = new DatePicker("Data");
    private final TextField descricao = new TextField("Descrição");
    private final Button registrar = new Button("Registrar entrada");

    private final ExtratoGrid extrato = new ExtratoGrid();
    private final PesquisaLancamentoDialog pesquisa = new PesquisaLancamentoDialog();
    private final ConfirmarLancamentoDialog confirmacao = new ConfirmarLancamentoDialog(this::registrarLancamento);
    private final ConfirmarExportacaoDialog exportacao = new ConfirmarExportacaoDialog(this::exportarExtrato);

    private boolean entrada = true;
    private boolean atualizandoAba = false;

    public FinanceiroView() {
        setSizeFull();
        setPadding(true);
        getStyle().set("background", "#fff9e8").set("flex", "1").set("overflow", "auto");

        add(criarTopo(), criarFormulario(), criarExtrato());

        aplicarModo();
        atualizarExtrato(new ArrayList<>());
    }

    // Lê /financeiro/entrada ou /financeiro/saida
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String tipo = event.getRouteParameters().get("tipo").orElse("entrada");
        entrada = !tipo.equals("saida");
        atualizandoAba = true;
        abas.setSelectedTab(entrada ? abaEntrada : abaSaida);
        atualizandoAba = false;
        aplicarModo();
    }

    private Component criarTopo() {
        H2 titulo = new H2("Movimentação Financeira");
        Button exportar = new Button("Exportar extrato", e -> exportacao.open());
        Button pesquisar = new Button("Pesquisar Lançamentos", e -> pesquisa.open());
        pesquisar.getStyle().set("background", "#5b5bf0").set("color", "white");

        HorizontalLayout topo = new HorizontalLayout(titulo, new HorizontalLayout(exportar, pesquisar));
        topo.setWidthFull();
        topo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topo.setAlignItems(FlexComponent.Alignment.CENTER);
        return topo;
    }

    private Component criarFormulario() {
        abas.addSelectedChangeListener(e -> {
            if (atualizandoAba) {
                return;
            }
            String destino = e.getSelectedTab() == abaEntrada ? "financeiro/entrada" : "financeiro/saida";
            getUI().ifPresent(ui -> ui.navigate(destino));
        });

        categoria.setPlaceholder("Selecione uma categoria");
        categoria.setWidthFull();

        formaPagamento.setItems("Dinheiro", "PIX", "Transferência", "Boleto", "Cartão");
        formaPagamento.setPlaceholder("Selecione a forma de pagamento");
        formaPagamento.setWidthFull();

        HorizontalLayout linha1 = new HorizontalLayout(categoria, formaPagamento);
        linha1.setWidthFull();

        valor.setPlaceholder("R$ 0,00");
        valor.setWidthFull();
        data.setPlaceholder("dd/mm/aaaa");
        data.setWidthFull();

        HorizontalLayout linha2 = new HorizontalLayout(valor, data);
        linha2.setWidthFull();

        descricao.setPlaceholder("Ex.: reparo hidráulico - Ed. Aurora");
        descricao.setWidthFull();

        Span rotuloComprovante = new Span("Comprovante");
        rotuloComprovante.getStyle().set("font-size", "var(--lumo-font-size-s)");
        Upload upload = new Upload(UploadHandler.inMemory((metadata, bytes) -> {
            // futuramente: guardar o comprovante junto com o lançamento
        }));
        upload.setAcceptedMimeTypes("application/pdf", "image/png", "image/jpeg");
        upload.setAcceptedFileExtensions(".pdf", ".png", ".jpg", ".jpeg");
        upload.setMaxFileSize(10 * 1024 * 1024);
        upload.setDropLabel(new Span("Arraste o recibo aqui ou clique para selecionar"));

        registrar.getStyle().set("background", "#5b5bf0").set("color", "white");
        registrar.addClickListener(e -> pedirConfirmacao());
        Button cancelar = new Button("Cancelar", e -> limparFormulario());
        HorizontalLayout botoes = new HorizontalLayout(registrar, cancelar);

        VerticalLayout caixa = new VerticalLayout(
                abas, linha1, linha2, descricao, rotuloComprovante, upload, botoes);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    private Component criarExtrato() {
        H2 titulo = new H2("Extrato do período");
        titulo.getStyle().set("font-size", "1rem").set("margin", "0");

        VerticalLayout caixa = new VerticalLayout(titulo, extrato);
        caixa.setWidthFull();
        caixa.getStyle().set("background", "white").set("border-radius", "10px");
        return caixa;
    }

    // Troca categorias e texto do botão conforme a aba
    private void aplicarModo() {
        if (entrada) {
            categoria.setItems("Aluguel", "Caução", "Outros");
            registrar.setText("Registrar entrada");
        } else {
            categoria.setItems("Manutenção", "Tributos", "Outros");
            registrar.setText("Registrar saída");
        }
        categoria.clear();
    }

    private void limparFormulario() {
        categoria.clear();
        formaPagamento.clear();
        valor.clear();
        data.clear();
        descricao.clear();
    }

    // Valida os campos obrigatórios e abre o modal de confirmação
    private void pedirConfirmacao() {
        categoria.setInvalid(categoria.isEmpty());
        formaPagamento.setInvalid(formaPagamento.isEmpty());
        valor.setInvalid(valor.isEmpty());
        data.setInvalid(data.isEmpty());
        if (categoria.isEmpty() || formaPagamento.isEmpty() || valor.isEmpty() || data.isEmpty()) {
            return;
        }
        confirmacao.open();
    }

    // Ponto de ligação com o backend: chamado após o usuário confirmar
    private void registrarLancamento() {
        // futuramente: salvar o lançamento (entrada ou saída) no banco
        limparFormulario();
    }

    // Ponto de ligação com o backend: chamado após confirmar a exportação
    private void exportarExtrato() {
        // futuramente: gerar o PDF do extrato do período
    }

    public void atualizarExtrato(List<ExtratoGrid.Lancamento> lista) {
        extrato.setItems(lista);
    }
}