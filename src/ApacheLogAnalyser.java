import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class ApacheLogAnalyser {

	final String defaultFileName = "src/test/resources/programming-task-example-data.log";
	final String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] \"(.+?) (.+?) (.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"(.+?)\"";
	List<String> ipAddressList = new ArrayList<>();
	List<String> urlList = new ArrayList<>();

	public ApacheLogAnalyser() {
		new ApacheLogAnalyser(defaultFileName);
	}

	public ApacheLogAnalyser(String filename) {

		try {
			Path filepath = Paths.get(filename);
			Stream<String> lines = Files.lines(filepath);
			Pattern p = Pattern.compile(regex);

			lines.forEach(s -> {
				Matcher matcher = p.matcher(s);
				if (matcher.find()) {
					ipAddressList.add(matcher.group(1));
					urlList.add(matcher.group(6));
				}
			});
			lines.close();

			System.out.println("-------------------------------------------------");
			System.out.println("Number of unique IP addresses: \n"+ipAddressList.stream().distinct().collect(Collectors.toList()).size());
			
			System.out.println("-------------------------------------------------");
			System.out.println("Top 3 most visited URLs: ");
			printTopOccurrences(urlList, 3);

			System.out.println("-------------------------------------------------");
			System.out.println("Top 3 most active IP addresses: ");
			printTopOccurrences(ipAddressList, 3);

			System.out.println("-------------------------------------------------");
			
			ipAddressList.sort(Comparator.naturalOrder());
			urlList.sort(Comparator.naturalOrder());
			System.out.println(ipAddressList);
			System.out.println(urlList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}	
	
	private void printTopOccurrences(List<String> list, int n) {
		// find how many times each string occurs		 
		Map<String, Long> stringOccurrenceMap = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		// create a list only with the number of occurrences and reverse sort
		List<Long> occurrenceList = new ArrayList<>(stringOccurrenceMap.values().stream().distinct().collect(Collectors.toList()));
		occurrenceList.sort(Comparator.reverseOrder());			

		// iterate the map to find the entries that match the 1st most occurred (position 0 of occurrenceList)
		// then the 2nd most occurred (position 1 of occurrenceList), etc
		if (occurrenceList.size() < n) n = occurrenceList.size();
		for (int i=0; i<n; i++) {
			System.out.println("\n"+(i+1)+") Occurrences = "+occurrenceList.get(i));
			for (Map.Entry<String, Long> entry: stringOccurrenceMap.entrySet()) {
				if (entry.getValue().equals(occurrenceList.get(i))) {
					System.out.println(entry.getKey());
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 0)
			new ApacheLogAnalyser(args[0]);
		else
			new ApacheLogAnalyser();

	}
}
