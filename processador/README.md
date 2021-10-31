# Processador

Responsável por:

- Coletar as Ordens de Compra no Kafka
- Fazer o Download do cadastro de ativos
    - Um JOB quartz realiza esta operação uma vez por dia e uma vez ao inicializar a operação
- Consultar o preço atual dos ativos
- Agendar os Pedidos em um tópico do Kafka
- Se houver qualquer problema com as Ordens, elas serão redirecionadas para outra fila para processamento.


