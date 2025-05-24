package org.xtracat.dao;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.xtracat.logger.SingletonLogger;

import java.sql.SQLException;
import java.util.Properties;

public class SingletonDAO {
    private static DatabaseManager dao;
    private final static Logger logger = SingletonLogger.getLogger();

    public static DatabaseManager getDao(String url, String user, String pass){
        if(dao==null){
            try {
                dao = new DatabaseManager(url,user,pass);
                return dao;
            }catch (SQLException e){
                logger.error("Connection to databse gone wrong." ,e);
            }
        }
        return dao;
    }

    public static DatabaseManager getDao(){
        if(dao == null){
            logger.error("No existing database connection, create one. Null returned");
            return null;
        }
        return dao;
    }

    public static DatabaseManager getDao(String dbUrl, Properties info) {
        if(dao==null){
            try {
                dao = new DatabaseManager(dbUrl,info);
                return dao;

            }catch (SQLException e){
                logger.error("Connection to databse gone wrong." ,e);
            }
        }

        return dao;
    }
}
