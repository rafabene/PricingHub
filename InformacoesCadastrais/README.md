# Informacoes Cadastrais

Plataforma online que funciona apenas em horário comercial.

- Informações cadastrais disponibilizada em arquivo delimitado.
- Informações cadastrais nunca passa de 2GB
- Fornece código de precificação que pode ser MB, B, N, C, MA.

Teste:

    curl -sH 'Accept-encoding: gzip' -v http://localhost:8080/ativos |gunzip -

More info about `Transfer-Encoding` at: <https://gist.github.com/CMCDragonkai/6bfade6431e9ffb7fe88>