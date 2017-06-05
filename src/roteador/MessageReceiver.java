package roteador;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReceiver implements Runnable{
    private TabelaRoteamento tabela;
    
    public MessageReceiver(TabelaRoteamento t){
        tabela = t;
    }
    
    @Override
    public void run() {
        DatagramSocket serverSocket = null;
        
        try {
            
            /* Inicializa o servidor para aguardar datagramas na porta 5000 */
            serverSocket = new DatagramSocket(5000);
        } catch (SocketException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        byte[] receiveData = new byte[1024];
        
        while(true){
            
            /* Cria um DatagramPacket */
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                /* Aguarda o recebimento de uma mensagem */
                serverSocket.receive(receivePacket);
            } catch (IOException ex) {
                Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /* Transforma a mensagem em string */
            String tabela_string = new String( receivePacket.getData());
            String oldTabela = tabela.get_tabela_string();
            /* Obtem o IP de origem da mensagem */
            InetAddress IPAddress = receivePacket.getAddress();
            //Sempre somar 1 metrica ao receber IP novo por outro roteador
            //Caso receber um IP conhecido porém com métrica superiro a atual, manter a métrica mais baixa
            tabela.update_tabela(tabela_string, IPAddress);
            
            if(!tabela.get_tabela_string().equals(oldTabela)){
                
            }
            
        }
    }
    
}
