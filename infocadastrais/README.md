# Informacoes Cadastrais

Plataforma online que funciona apenas em horário comercial.

- Informações cadastrais disponibilizada em arquivo delimitado.
- Informações cadastrais nunca passa de 2GB
- Fornece código de precificação que pode ser MB, B, N, C, MA.
- É necessário informar um JWT usando o algorítimo RS256.
- O arquvo com as informações cadastrais podem ser armazenados em outro caminho. A configuração `ARQUIVO` informará o caminho absoluto para o arquivo.
- Dado o possível tamanho do arquvio, a saída possui as seguintes características:
    - transfer-encoding: chunked
    - Content-Encoding: gzip

Mais informação sobre `Transfer-Encoding` em: <https://gist.github.com/CMCDragonkai/6bfade6431e9ffb7fe88>

Teste:

    curl -s -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwcm9jZXNzYWRvciIsImlhdCI6MTYzMzAzNTI4NiwiZXhwIjoxNjY0NTkyNzc4LCJhdWQiOiJpbmZvY2FkYXN0cmFpcyIsInN1YiI6InJhZmFiZW5lQGdtYWlsLmNvbSIsInVwbiI6IlByb2Nlc3NhZG9yIiwiZ3JvdXBzIjpbInNlcnZpY2UiLCJkb3dubG9hZCJdfQ.f42fA_rJ0RYVsctQAn6lt8zDSA8vfc9IGe4hc4Uhm0CC0sp8cBYG-_SmVM8pxO5n1JrxxkKZxPo_rusOTCxsAfPeZg-EycK6f8F-rwqtJph-SD08S7TBrfUSs2jHE_NxUPMH4vggvNBuc9qmCSsZu2adGAL2VpQhFCfcSHWSW_0mbCjqBAKrjUg6qtMedwR6xxZhFz0xw1UgKJByREUvrFYEN2RyMpY6y6K42kOn6tq0f_sbB8JUAeAHf75SfhEv74UfVLXGcXi19544a5gwxXhiayt9a_j1RRVjhD-6CEmORjcta8K-rQkaQNdsLBbFwHbNtcsSx4krkGZiu7cyAA' -H 'Accept-encoding: gzip' -v http://localhost:8080/ativos |gunzip -

