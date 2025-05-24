package org.xtracat.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SingletonLogger {
    static Logger logger;

    public SingletonLogger(){}


    public static Logger getLogger() {
        if(logger == null){
            logger = LoggerFactory.getLogger(SingletonLogger.class);
        }

        return logger;
    }
}
