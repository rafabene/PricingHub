# Alarmes funcionais.

Este serviço faz com que o [Apache Flink](https://ci.apache.org/projects/flink/flink-docs-release-1.13/) escute todas as inserções de novos pedidos no cache do Oracle Coherence.

Caso o valor total do Pedido seja maior que o valor informado na configurção `limiteValorAlarme` dentro da janela de tempo configurado via `janelaTempoSegundos`, um Alarme será emitido.

Esta serviço disponibiliza os seguintes endpoints e suas respectivas funções:

- Lista todos os alarmes previamente emitidos

        Method: GET
        URL: 8080/alarmes

- Lista o valor movimentado pelo cliente para todas as ordens

        Method: GET
        URL: 8080/volume/{cliente}

- Escuta todos os alarmes emitidos near "real time"

        Method: Websocket
        URL: ws://HOST:8080/alarmes/{cliente}

O Websocket pode ser monitado usando a ferramenta [websocat](https://github.com/vi/websocat):

        websocat ws://myminikube/alarme/Cliente2