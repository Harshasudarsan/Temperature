package version2;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    static class Stats {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        double sum;
        int count;

        void add(double v) {
            if (v < min) min = v;
            if (v > max) max = v;
            sum += v;
            count++;
        }

        void merge(Stats o) {
            if (o.min < min) min = o.min;
            if (o.max > max) max = o.max;
            sum += o.sum;
            count += o.count;
        }

        double mean() {
            return sum / count;
        }
    }

    static final int THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws Exception {

        long startTime = System.nanoTime();

        Path path = Path.of("C:\\Users\\harsh\\Downloads\\measurements.txt");

        long fileSize;
        try (FileChannel fc = FileChannel.open(path, StandardOpenOption.READ)) {
            fileSize = fc.size();
        }

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        List<Future<Map<String, Stats>>> futures = new ArrayList<>();

        long chunkSize = fileSize / THREADS;

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {

            for (int i = 0; i < THREADS; i++) {
                long start = i * chunkSize;
                long size = (i == THREADS - 1)
                        ? fileSize - start
                        : chunkSize;

                MappedByteBuffer buffer =
                        channel.map(FileChannel.MapMode.READ_ONLY, start, size);

                boolean skipFirstLine = (i != 0);

                futures.add(pool.submit(() -> parse(buffer, skipFirstLine)));
            }
        }

        pool.shutdown();

        Map<String, Stats> finalMap = new HashMap<>(50_000);

        for (Future<Map<String, Stats>> f : futures) {
            Map<String, Stats> local = f.get();
            for (var e : local.entrySet()) {
                finalMap.merge(e.getKey(), e.getValue(), (a, b) -> {
                    a.merge(b);
                    return a;
                });
            }
        }

        TreeMap<String, Stats> sorted = new TreeMap<>(finalMap);

        StringBuilder sb = new StringBuilder("{");
        for (var e : sorted.entrySet()) {
            Stats s = e.getValue();
            sb.append(e.getKey()).append("=")
                    .append(round(s.min)).append("/")
                    .append(round(s.mean())).append("/")
                    .append(round(s.max)).append(", ");
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");

        long endTime = System.nanoTime();

        System.out.printf("Execution Time: %.2f seconds%n",
                (endTime - startTime) / 1_000_000_000.0);

        System.out.println(sb);
    }

    private static Map<String, Stats> parse(ByteBuffer buf, boolean skipFirstLine) {

        Map<String, Stats> map = new HashMap<>(10_000);

        // Skip partial first line
        if (skipFirstLine) {
            while (buf.hasRemaining()) {
                if (buf.get() == '\n') break;
            }
        }

        StringBuilder station = new StringBuilder(64);
        StringBuilder temp = new StringBuilder(8);
        boolean readingTemp = false;

        while (buf.hasRemaining()) {
            char c = (char) buf.get();

            if (c == ';') {
                readingTemp = true;
                temp.setLength(0);
            }
            else if (c == '\n') {

                if (temp.length() > 0) { // ✅ safety check
                    double value = Double.parseDouble(temp.toString());
                    map.computeIfAbsent(station.toString(),
                            k -> new Stats()).add(value);
                }

                station.setLength(0);
                readingTemp = false;
            }
            else {
                if (readingTemp) temp.append(c);
                else station.append(c);
            }
        }
        return map;
    }

    private static double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
