package org.wso2.esb.integration.common.utils.servers;/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class RabbitMQServer {
    private static final Log log = LogFactory.getLog(org.wso2.esb.integration.common.utils.servers.RabbitMQServer.class);
    private boolean started;
    //TODO : get from automation.xml
    private File rabbitMQHome;

    public RabbitMQServer() {
        rabbitMQHome = new File("/Users/maheeka/ESB_WORK/RABBITMQ/rabbitmq_server-3.5.0/sbin");
        log.info("Using the RabbitMQ server path : " + rabbitMQHome.getAbsolutePath());
    }

    public void start() {
        log.info("Starting RabbitMQ Broker");
        execute("sh rabbitmq-server -detached");
        started = true;
    }

    public void stop() {
        log.info("Stopping RabbitMQ Broker");
        execute("sh rabbitmqctl stop");
        started = false;
    }

    public void initialize() {
        log.info("Initializing RabbitMQ Broker");
        execute("sh rabbitmqctl stop_app");
        execute("sh rabbitmqctl reset");
        execute("sh rabbitmqctl start_app");
    }

    public boolean isStarted() {
        return started;
    }

    private void execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command, null, rabbitMQHome);
            InputStream instream = process.getInputStream();
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(instream));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            reader.close();
            instream.close();
            log.info(writer.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
