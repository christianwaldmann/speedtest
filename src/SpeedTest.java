import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.util.concurrent.Callable;


@Command(name="speedtest", mixinStandardHelpOptions=true, version="speedtest 0.0.1", description="Measures the internet speed.")
class SpeedTest implements Callable<Integer> {

    @Option(names={"-b", "--bytes"}, description="Display values in bytes instead of bits")
    private boolean bytes = false;

    @Override
    public Integer call() throws Exception {
        new DownloadSpeed().download_and_print_speed(bytes);
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new SpeedTest()).execute(args);
        System.exit(exitCode);
    }
}
