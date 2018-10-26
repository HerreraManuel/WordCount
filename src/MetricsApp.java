import picocli.CommandLine;

public class MetricsApp {
    public static void main(String[] args){
        if (args.length < 1) CommandLine.usage(new Metrics(), System.out);
        else {
            try {
                Metrics m = CommandLine.populateCommand(new Metrics(), args);
                m.run(m.files);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
