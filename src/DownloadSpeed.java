import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadSpeed {

    private long max_duration_in_ms = 10_000;
    private boolean max_duration_reached = false;
    private int max_download_bytes = 20_000_000;
    private int buffer_size = 1024;
    private String download_url = "http://cachefly.cachefly.net/100mb.test";

    // Handler to handle timeout of measurements
    private class TimeoutHandler extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(max_duration_in_ms);
            } catch (InterruptedException e) {
                return;
            }
            max_duration_reached = true;
        }
    }

    public void download_and_print_speed(boolean display_in_bytes) {
        byte[] buffer = new byte[buffer_size];
        long total_bytes_read = 0;

        // 0. Start timeout handler
        TimeoutHandler timeouthandler = new TimeoutHandler();
        timeouthandler.start();

        // 1. Start timer
        StopWatch stopwatch = StopWatch.createStarted();

        // 2. Download file and track bytes read
        try {
            URL url = new URL(download_url);
            URLConnection conn = url.openConnection();
            InputStream instream = conn.getInputStream();

            long bytes_read;
            while (!max_duration_reached && (bytes_read = instream.read(buffer)) != -1) {
                total_bytes_read += bytes_read;
                if (total_bytes_read >= max_download_bytes) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Stop timer to calculate time passed
        long time_passed_in_ns = stopwatch.getNanoTime();
        double time_passed_in_s = time_passed_in_ns / 1_000_000_000.0;

        // 4. Calculate download speed
        double download_bytes_per_second = total_bytes_read / time_passed_in_s;
        double download_MB_per_second = download_bytes_per_second / 1_000_000;
        double download_MBits_per_second = download_MB_per_second * 8;

        // 5. Print out download speed
        if (display_in_bytes) {
            System.out.println(String.format("DOWNLOAD SPEED: %.2f MB/s", download_MB_per_second));
        } else {
            System.out.println(String.format("DOWNLOAD SPEED: %.2f Mbit/s", download_MBits_per_second));
        }
    }
}
