package com.rafabene.alarme;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.CacheEvent;
import com.tangosol.util.Filter;
import com.tangosol.util.MapEvent;
import com.tangosol.util.listener.SimpleMapListener;

@ServerEndpoint("/alarme/{cliente}")
public class AlarmeWebsocket {

    private NamedCache<String, Alarme> alarmes = CacheFactory.getCache("alarmes");

    private Logger logger = Logger.getLogger(this.getClass().toString());

    /*
     * Armazena as sessões websocket para serem removidas ao fechar a sessão
     * evitando assim erros do cache listener notificando sessões já fechadas
     */
    private Map<String, AlarmeListener> sessoes = new HashMap<>();

    @OnOpen
    public void getAlarmes(@PathParam("cliente") String cliente, Session session) throws IOException {
        logger.info(String.format("Abrindo Sessão Websocket %s para o cliente %s", session.getId(), cliente));

        // Pega qualquer alarme existente se houver
        Alarme alarme = alarmes.get(cliente);
        if (alarme != null) {
            session.getBasicRemote().sendText(alarme.getMensagem());
        }

        AlarmeListener alarmeListener = new AlarmeListener(session);
        sessoes.put(session.getId(), alarmeListener);
        alarmes.addMapListener(alarmeListener, new FiltroPorCliente(cliente), true);
    }

    @OnClose
    public void removeAlarme(@PathParam("cliente") String cliente, Session session) {
        logger.info("Fechando Sessão Websocket:" + session.getId());
        alarmes.removeMapListener(sessoes.get(session.getId()), new FiltroPorCliente(cliente));
    }

    class AlarmeListener extends SimpleMapListener<String, Alarme> {

        private Session sessaoWebSocket;

        public AlarmeListener(Session sessaoWebSocket) {
            this.sessaoWebSocket = sessaoWebSocket;
        }

        @Override
        public void entryInserted(MapEvent<String, Alarme> evento) {
            try {
                sessaoWebSocket.getBasicRemote().sendText(evento.getNewValue().getMensagem());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class FiltroPorCliente implements Filter<CacheEvent<String, Alarme>> {

        private String cliente;

        public FiltroPorCliente(String cliente) {
            this.cliente = cliente;
        }

        @Override
        public boolean evaluate(CacheEvent<String, Alarme> evento) {
            return evento.getKey().equals(cliente);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FiltroPorCliente other = (FiltroPorCliente) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (cliente == null) {
                if (other.cliente != null)
                    return false;
            } else if (!cliente.equals(other.cliente))
                return false;
            return true;
        }

        private AlarmeWebsocket getEnclosingInstance() {
            return AlarmeWebsocket.this;
        }

    }

}
