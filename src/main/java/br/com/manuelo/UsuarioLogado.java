package br.com.manuelo;

public class UsuarioLogado {

    private static final String NOME_PADRAO = "Usuário";
    private static final String CARGO_PADRAO = "Sem login";

    // Ponto de ligação: quando existir login, trocar por quem estiver autenticado
    public static String getNome() {
        return NOME_PADRAO;
    }

    public static String getCargo() {
        return CARGO_PADRAO;
    }
}