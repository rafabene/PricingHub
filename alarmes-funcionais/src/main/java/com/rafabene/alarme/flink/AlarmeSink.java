package com.rafabene.alarme.flink;

import java.util.logging.Logger;

import com.rafabene.alarme.Alarme;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class AlarmeSink implements SinkFunction<Alarme> {

    @Override
    public void invoke(Alarme alarme, Context context) throws Exception {
        Logger logger = Logger.getLogger(this.getClass().toString());
        logger.info(alarme.getMensagem());
    }

}
