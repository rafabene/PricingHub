# Recebe Pedidos

Microserviço responsável para receber os pedidos via REST e via Socket.

## Ordens via REST

O serviço REST é executado na porta `8080`.

O formato para envio da ordem de compra é;

```
{
	"versao": "V1",
	"ordemCompra": {
		"nomeAtivo": "Teste",
		"quantidade": 1,
		"tokenCliente": "token"
	}
}
```

## Ordens via Socket

O serviço via Socket é executado na porta `5000`.

O formato para cada linha é 

```
    Nome do Ativo, 1, token
```

O commando `BYE` encerra a conexão via Socket.