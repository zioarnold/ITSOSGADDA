package com.example.arnold.itsosgadda.utilities;

import android.os.Environment;

import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;


public class Log4jHelper {
    private final static LogConfigurator mLogConfigurator = new LogConfigurator();

    static {
        configureLog4j();
    }

    private static void configureLog4j() {
        String fileName = Environment.getExternalStorageDirectory() + "/" + "log4j.log";
        String filePattern = "%d - [%c] - %p : %m%n";
        int maxBackupSize = 10;
        long maxFileSize = 1024 * 1024;

        configure( fileName, filePattern, maxBackupSize, maxFileSize );
    }

    private static void configure( String fileName, String filePattern, int maxBackupSize, long maxFileSize ) {
        mLogConfigurator.setFileName( fileName );
        mLogConfigurator.setMaxFileSize( maxFileSize );
        mLogConfigurator.setFilePattern(filePattern);
        mLogConfigurator.setMaxBackupSize(maxBackupSize);
        mLogConfigurator.setUseLogCatAppender(true);
        mLogConfigurator.configure();


    }

    public static Logger getLogger( String name ) {
        return Logger.getLogger( name );
    }
}



