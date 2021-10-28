package com.rafabene.alarme.flink.sink;

import java.util.logging.Logger;

import com.rafabene.alarme.Alarme;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class AlarmeSink implements SinkFunction<Alarme> {

    @Override
    public void invoke(Alarme alarme, Context context) throws Exception {
        NamedCache<String, Alarme> alarmes = CacheFactory.getCache("alarmes");
        Logger logger = Logger.getLogger(this.getClass().toString());
        logger.info(alarme.getMensagem());
        alarmes.put(alarme.getCliente(), alarme);
    }

}
