import java.io.*;
import java.util.*;
public class Saha_project2 {
    private static final String stopwords_file = "stopwords.txt";
    private static final String alice_file = "alice29.txt";
    // Task 1: Preprocessing
    public static List<String> preprocess(String filename, Set<String> stopwords) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " .,;:!?'\"[]{}()<>-_—=+\t\n\r\f", false);
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().toLowerCase();
                    if (!stopwords.contains(token)) {
                        words.add(token);} } } }
        return words; }
    public static Set<String> readStopwords(String filename) throws IOException {
        Set<String> stopwords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopwords.add(line.trim()); }}
        return stopwords; }
    // Task 2: Frequency count
    public static HashMap<String, Integer> frequencyCount(List<String> words) {
        HashMap<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1); }
        return wordFrequency;}
    // Task 3: Top n words and ratio
    public static void topNWordsAndRatio(HashMap<String, Integer> wordFrequency, int n) {
        TreeMap<Integer, List<String>> sortedByFrequency = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            int frequency = entry.getValue();
            String word = entry.getKey();
            List<String> wordList = sortedByFrequency.getOrDefault(frequency, new ArrayList<>());
            wordList.add(word);
            sortedByFrequency.put(frequency, wordList);  }
        System.out.println("Top " + n + " words and their frequency:");
        int count = 0;
        for (Map.Entry<Integer, List<String>> entry : sortedByFrequency.entrySet()) {
            if (count >= n) break;
            int frequency = entry.getKey();
            List<String> words = entry.getValue();
            for (String word : words) {
                System.out.println("Word: " + word + ", Frequency: " + frequency);
                count++;
                if (count >= n) break; }} }
    public static int sizeOfDataset(String filename) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " .,;:!?'\"[]{}()<>-_—=+\t\n\r\f", false);
                while (st.hasMoreTokens()) {
                    st.nextToken();
                    count++;} } }
        return count; }
    public static int numberOfWords(List<String> words) {
        return words.size(); }
    // Task 4: Dataset statistics
    public static int numberOfStopWords(String filename, Set<String> stopwords) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " .,;:!?'\"[]{}()<>-_—=+\t\n\r\f", false);
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().toLowerCase();
                    if (stopwords.contains(token)) {
                        count++; } } } }
        return count; }
    public static int numberOfPunctuation(String filename) throws IOException {
        int count = 0;
        String punctuations = ".,;:!?'\"[]{}()<>-_—=+";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (char ch : line.toCharArray()) {
                    if (punctuations.indexOf(ch) >= 0) {
                        count++; }} } }
        return count; }
    public static void main(String[] args) {
        try {
            Set<String> stopwords = readStopwords(stopwords_file);
            List<String> words = preprocess(alice_file, stopwords);
            HashMap<String, Integer> wordFrequency = frequencyCount(words);
            int datasetSize = sizeOfDataset(alice_file);
            int numberOfWords = numberOfWords(words);
            int stopWordsCount = numberOfStopWords(alice_file, stopwords);
            int punctuationCount = numberOfPunctuation(alice_file);
            System.out.println("Size of dataset (total tokens before preprocessing): " + datasetSize);
            System.out.println("Number of words (tokens after preprocessing): " + numberOfWords);
            System.out.println("Number of stopwords: " + stopWordsCount);
            System.out.println("Number of punctuations: " + punctuationCount);
            // Calculate ratio
            double ratio = (double) numberOfWords / datasetSize;
            System.out.println("Ratio: " + ratio);
            // Print top 50 words
            int topN = 50;
            topNWordsAndRatio(wordFrequency, topN); } catch (IOException e) {
            System.err.println("Error while reading the file: " + e.getMessage()); } }}
