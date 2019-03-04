import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;

public class Main {

  public static void main(String ... argv) throws NoSuchPortException, PortInUseException, IOException {
    Main main = new Main();
    RuntimeSettings runtimeSettings = new RuntimeSettings(argv);
    if (runtimeSettings.parse()) {
      if (runtimeSettings.getHelp()) {
        // print out the usage to sysout
        runtimeSettings.printUsage();
      } else {
        // run the app
        main.run(runtimeSettings);
        System.exit(0);
      }
    } else {
      // print the parameter error, show the usage, and bail
      System.err.println(runtimeSettings.getParseErrorMessage());
      runtimeSettings.printUsage();
      System.exit(1);
    }
  }

  public void run(RuntimeSettings runtimeSettings) throws NoSuchPortException, PortInUseException, IOException {
    HttpURLConnection httpConn = null;

    // Open up serial port
    CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(runtimeSettings.getCommPort());
    SerialPort serialPort = (SerialPort)commPortIdentifier.open(runtimeSettings.programName, 2 * 1000);
    // Get serial port writer
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));

    // Open up remote mjpeg over http reader
    URL url = new URL(runtimeSettings.getURL());
    URLConnection urlConn = url.openConnection();
    httpConn = (HttpURLConnection)urlConn;
    httpConn.setAllowUserInteraction(false);
    httpConn.setInstanceFollowRedirects(true);
    httpConn.setRequestMethod("GET");
    httpConn.setReadTimeout(1000);
    httpConn.setConnectTimeout(10 * 1000);
    httpConn.connect();
    BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
    reader.transferTo(writer);
  }
}