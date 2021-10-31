# Agendamento de pedidos

A única função deste serviço é coletar os Agendamentos de Pedidos do Kafka e persistir no Oracle Coherence.

Um pedido é considerado igual, se for a mesma `quantidade`, do mesmo `ativo`, para o mesmo `cliente`. Se mais de um pedido com estes valores for enviado para persistência, o pedido será atualizado ao invés de inserido um novo registro, evitando assim a duplicação do registro.

Documentação do Oracle's Coherence: <https://docs.oracle.com/en/middleware/standalone/coherence/14.1.1.0/index.html>