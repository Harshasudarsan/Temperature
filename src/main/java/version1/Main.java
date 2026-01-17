package version1;

import java.io.*;
import java.nio.file.*;
import java.util.*;

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

        double mean() {
            return sum / count;
        }
    }

    public static void main(String[] args) throws Exception {

        long startTime = System.nanoTime();

        String filePath = "C:\\Users\\harsh\\Downloads\\measurements.txt";

        Map<String, Stats> map = new HashMap<>(50_000, 0.75f);

        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                int sep = line.indexOf(';');

                String station = line.substring(0, sep);
                 double temp = Double.parseDouble(line.substring(sep + 1));

                Stats s = map.get(station);
                if (s == null) {
                    s = new Stats();
                    map.put(station, s);
                }
                s.add(temp);
            }
        }


        TreeMap<String, Stats> sorted = new TreeMap<>(map);

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

        double timeMinutes = (endTime - startTime) / 1_000_000_000.0 / 60.0;
        System.out.println("Execution Time: " + timeMinutes + " minutes");
        System.out.println(sb);
    }

    private static double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}
