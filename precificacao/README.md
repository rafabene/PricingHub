# Precificação

A precificação é armazenada em um cluster Datagrid do Oracle Coherence.

Documentação do Oracle's Coherence: <https://docs.oracle.com/en/middleware/standalone/coherence/14.1.1.0/index.html>

Este serviço fornece dois endpoints:

- Atualiação de preço:

        Http Method: PUT 
        URL: 8080/preco/{tipoPrecificacao}

- Consulta de preço:

        Http Method: GET
        Headers: token = <Qualquer valor> 
        URL: 8080/preco/{tipoPrecificacao}

Este serviço carrega automáticamente preços para os ativos `MB`, `B`, `N` e `C` caso a variável `carregaPrecoPadrao` esteja configurada como `true`.