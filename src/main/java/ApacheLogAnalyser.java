package main.java;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class ApacheLogAnalyser {

	
	final String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] \"(.+?) (.+?) (.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"(.+?)\"";
	List<String> ipAddressList = new ArrayList<>();
	List<String> urlList = new ArrayList<>();

	public ApacheLogAnalyser() {
			}

	

	/*
	 * Parses an Apache access log file and returns a list containing
	 * the values found on each line that match the specified group
	 * 
	 * @param filename relative path to the log file
	 * @param groupNumber index of the group in the regex pattern (ipaddress = 1, url = 6, etc)
	 * @return list of values that match the group number
	 */
	private List<String> parseLogFile(String filename, int groupNumber) {		
		Stream<String> lines = null;
		try {
			Path filepath = Paths.get(filename);
			lines = Files.lines(filepath);
			Pattern p = Pattern.compile(regex);
			List<String> resultList = new ArrayList<>();

			lines.forEach(s -> {
				Matcher matcher = p.matcher(s);
				if (matcher.find()) {
					resultList.add(matcher.group(groupNumber));
				}
			});
			return resultList;			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		} finally {
			if (lines != null) {				
				lines.close();
		  }
		}		
	}	

	/*
	 * Finds all unique occurrences of a field in an Apache access log file
	 * 
	 * @param filename relative path to the log file
	 * @param groupNumber index of the group in the regex pattern (ipaddress = 1, url = 6, etc)
	 * @return list of unique values that match the group number
	 */
	public List<String> findUniqueOccurrences(String filename, int groupNumber) {
		List<String> allOccurrences = parseLogFile(filename, groupNumber);
		return allOccurrences.stream().distinct().collect(Collectors.toList());
	}

	/*
	 * Finds top K occurrences of a field in an Apache access log file. 	
	 * Returns a list of size K ordered from the most frequent to the less frequent. 
	 * Each position is a list of a key-value pair, where the key is the field being 
	 * searched (for example, ipaddress) and the value is the number of occurrences.
	 * 
	 * @param filename relative path to the log file
	 * @param groupNumber index of the group in the regex pattern (ipaddress = 1, url = 6, etc)
	 * @param k number of ranking positions
	 * @return list of top k occurrences 
	 */
	public List<List<Entry<String, Long>>> findTopOccurrences(String filename, int groupNumber, int k) {
		List<List<Entry<String, Long>>> result = new ArrayList<>();
		List<String> allOccurrences = parseLogFile(filename, groupNumber);

		// find how many times each string occurs		 
		Map<String, Long> numberOfOccurrencesPerString = allOccurrences.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		// create a list only with the unique number of occurrences and reverse sort
		List<Long> numberOfOccurrencesOrdered = new ArrayList<>(numberOfOccurrencesPerString.values().stream().distinct().collect(Collectors.toList()));
		numberOfOccurrencesOrdered.sort(Comparator.reverseOrder());			

		// iterate the map to find the entries that match the 1st most occurred (position 0 of numberOfOccurrencesOrdered)
		// then the 2nd most occurred (position 1 of numberOfOccurrencesOrdered), etc
		if (numberOfOccurrencesOrdered.size() < k) k = numberOfOccurrencesOrdered.size();
		for (int i=0; i<k; i++) {
			Long nOfOcc = numberOfOccurrencesOrdered.get(i);
			List<Entry<String, Long>> sameNOfOccurrence = new ArrayList<>();
			for (Map.Entry<String, Long> entry: numberOfOccurrencesPerString.entrySet()) {
				if (entry.getValue().equals(nOfOcc)) {
					sameNOfOccurrence.add(entry);
				}
			}
			result.add(sameNOfOccurrence);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {		

		if (args.length > 0) {
			ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();

			System.out.println("-------------------------------------------------");
			System.out.println("Number of unique IP addresses:");
			System.out.println(apacheLogAnalyser.findUniqueOccurrences(args[0], 1).size());

			System.out.println("-------------------------------------------------");
			System.out.println("Top 3 most visited URLs: ");
			List<List<Entry<String, Long>>> topList = apacheLogAnalyser.findTopOccurrences(args[0], 6, 3);
			int i = 1;
			for (List<Entry<String, Long>> entryList: topList) {
				System.out.println("\n"+i+") Occurrences = "+entryList.get(0).getValue());
				for (Entry<String, Long> entry : entryList) {
					System.out.println(entry.getKey());
				}
				i++;
			}

			System.out.println("-------------------------------------------------");
			System.out.println("Top 3 most active IP addresses: ");
			topList = apacheLogAnalyser.findTopOccurrences(args[0], 1, 3);
			i = 1;
			for (List<Entry<String, Long>> entryList: topList) {
				System.out.println("\n"+i+") Occurrences = "+entryList.get(0).getValue());
				for (Entry<String, Long> entry : entryList) {
					System.out.println(entry.getKey());
				}
				i++;
			}
	}
	else
		System.out.println("usage: java ApacheLogAnalyser <filename>");

	}
}
