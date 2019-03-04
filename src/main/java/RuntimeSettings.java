import com.beust.jcommander.*;

/*
* RuntimeSettings class.
*
* <p>Package up command line processing into a separate class.  This abstracts out the logic
*    from the main class and makes this more testable.
*
* @author Chuck Benedict
*/
public class RuntimeSettings {
  // Command line args
  private String[] argv;
  // Program name that you want to pass to command line parsing...which will be put into usage info
  public static final String programName = "2019DSSoftJoystickDaemon";

  // command line args
  @Parameter(names={"--comm", "-c"}, required=true, description="Comm port of Teensy")
  private String commPort;
  @Parameter(names={"--url", "-u"}, required=true, 
      description="MJPEG over http streaming source")
  private String url = "";
  @Parameter(names = "--help", help = true)
  private boolean help = false;

  // Internal jCommander variable
  private JCommander jc;

  // Parse error message
  private String parseErrorMessage;

public RuntimeSettings(String ... argv) {
  if (argv == null)
  {
    throw new IllegalArgumentException("Arguments cannot be null.");
  }
    // Init the jcommander parser
    jc = JCommander.newBuilder()
      .programName(programName)
      .addObject(this)
      .build();
    this.argv = argv;
  }
  
  public boolean parse() {
    // parse command line args
    try {
      jc.parse(argv);
      return true;
    } catch (ParameterException pe) {
      // print the parameter error, show the usage, and bail
      parseErrorMessage = pe.getMessage();
      return false;
    }
  }

  public void printUsage() {
    jc.usage();
  }

  // Define getters
  public String getParseErrorMessage() {
    return parseErrorMessage;
  }

  public String getCommPort() {
    return commPort;
  }

  public String getURL() {
    return url;
  }

  public boolean getHelp() {
    return help;
  }
}