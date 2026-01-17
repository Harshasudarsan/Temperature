//package org.harsha;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Main {
//
//    static class Stats {
//        double min = Double.MAX_VALUE;
//        double max = -Double.MAX_VALUE;
//        double sum;
//        int count;
//
//        void add(double v) {
//            if (v < min) min = v;
//            if (v > max) max = v;
//            sum += v;
//            count++;
//        }
//
//        double mean() {
//            return sum / count;
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//
//        String filePath = "C:\\Users\\harsh\\Downloads\\measurements.txt";
//        Map<String, Stats> map = new HashMap<>(10_000);
//
//        Files.lines(Path.of(filePath)).forEach(line -> {
//            int sep = line.indexOf(';');
//            String station = line.substring(0, sep);
//            double temp = Double.parseDouble(line.substring(sep + 1));
//
//            map.computeIfAbsent(station, k -> new Stats()).add(temp);
//        });
//
//        StringBuilder sb = new StringBuilder("{");
//        map.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .forEach(e -> {
//                    Stats s = e.getValue();
//                    sb.append(e.getKey()).append("=")
//                            .append(round(s.min)).append("/")
//                            .append(round(s.mean())).append("/")
//                            .append(round(s.max)).append(", ");
//                });
//
//        if (sb.length() > 1) sb.setLength(sb.length() - 2);
//        sb.append("}");
//        System.out.println(sb);
//    }
//
//    private static double round(double v) {
//        return Math.round(v * 10.0) / 10.0;
//    }
//}
//
