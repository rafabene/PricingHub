package com.rafabene.recebepedidos.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdemCompraSocket extends Thread{

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Socket socket;

    public OrdemCompraSocket(Socket socket){
        this.socket = socket;
    }
     
    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os), true);
    
            String inputLine, outputLine;
            ProtocoloOrdemCompra protocolo = new ProtocoloOrdemCompra();
            while ((inputLine = br.readLine()) != null) {
                outputLine = protocolo.processaEntrada(inputLine);
                pw.println(outputLine);
                if (inputLine.equals("GOODBYE")) {
                    break;
                }
            }
            socket.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    
}
