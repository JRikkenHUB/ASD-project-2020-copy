package nl.ritogames;

import com.github.blindpirate.extensions.CaptureSystemOutput.OutputCapture;

import java.util.ArrayList;

public class LogChecker {

    public static boolean checkLogs(ArrayList<String> logs, OutputCapture outputCapture) {
        String logString = "";
        for (String logText : logs) {
            logString += logText;
           if(!outputCapture.toString().contains(logText)){
               return false;
           }
        }
        
        logString = logString.replaceAll("\\s+","");
        String outputString = outputCapture.toString().replaceAll("\\s+","");
        return logString.length() == outputString.length();
    }
}
