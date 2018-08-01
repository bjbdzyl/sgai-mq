package com.szx.ea.mq.support;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.szx.ea.mq.entity.QueueCfg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RabbitMqAgent {

    private static final Log logger = LogFactory.getLog(RabbitMqAgent.class);
    ConnectionFactory factory = new ConnectionFactory();

    List<MqConnection> connectionList = new ArrayList<>();

    private Map<String, Consumer> consumers = new ConcurrentHashMap();

    public boolean start(List<QueueCfg> mqConfiguration, Consumer mainConsumer) {
        for (QueueCfg cfg : mqConfiguration) {
            //System.out.println("pwd " + cfg.getPwd());
            //System.out.println("pwd enc " + cfg.enc("sgai001"));
            if (!alreadyConnect(cfg.getIp(), cfg.getPort())) {
                //System.out.println("pwd decrypt " + cfg.decrypt(cfg.getPwd()));
                MqConnection connecter = connect(cfg.getUserName(), cfg.decrypt(cfg.getPwd()), cfg.getIp(), cfg.getPort());
                if (connecter != null) {
                    for (String queuename : cfg.getQueueList().split("\\|")) {
                        connecter.startListenQueue(queuename, mainConsumer);
                    }
                }
            }
        }

        return true;
    }

    private boolean alreadyConnect(String ip, int port) {
        for (int i = 0; i < connectionList.size(); i++) {
            if (connectionList.get(i).isSameSrv(ip, port)) {
                return true;
            }
        }
        return false;
    }

    private MqConnection connect(String userName, String password, String hostName, int portNumber) {
        try {
            factory.setUsername(userName);
            factory.setPassword(password);
            factory.setHost(hostName);
            factory.setPort(portNumber);

            MqConnection conn = new MqConnection();
            conn.setConn(factory.newConnection());
            conn.setChannel(conn.getConn().createChannel());
            conn.setServerIp(hostName);
            conn.setPort(portNumber);
            connectionList.add(conn);
            return conn;
        } catch (Exception e) {
            logger.fatal("fail to connect rabbit mq");
            e.printStackTrace();
            return null;
        }
    }

    public boolean setQueueState(String ip, int port, String queueName, int state){
        for (int i = 0; i < connectionList.size(); i++) {
            MqConnection connection = connectionList.get(i);
            if (connection.isSameSrv(ip, port)){
                return connection.setQueueState(queueName, state);
            }
        }
        return false;
    }

    public boolean stop() {
        for (MqConnection conn :
                connectionList) {
            conn.stop();
        }
        return true;
    }


    class MqConnection {
        public Connection getConn() {
            return conn;
        }

        public void setConn(Connection conn) {
            this.conn = conn;
        }

        private Connection conn;

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        private boolean startListenQueue(String queueName, Consumer consumer) {
            try {
                channel.queueDeclare(queueName, true, false, false, null);
                channel.basicConsume(queueName, true, queueName, consumer); // 为了方便，consumerTag直接用queueName
                consumers.put(queueName, consumer);
            } catch (Exception e) {
                logger.fatal("fail to start listen queue " + queueName);
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean stop() {
            try {
                channel.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public boolean setQueueState(String queueName, int state) {
            try {
                if (state == 1) {
                    return startListenQueue(queueName, consumers.get(queueName));
                } else if (state == 0) {
                    channel.basicCancel(queueName);
                    return true;
                } else {
                    logger.error("set queue state get invalid param");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean isSameSrv(String ip, int port){
            return this.serverIp.compareTo(ip) == 0 && port == this.port;
        }

        private Channel channel;
        String serverIp;
        int port;
    }
}
