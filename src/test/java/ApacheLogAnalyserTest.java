package test.java;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import main.java.ApacheLogAnalyser;

public class ApacheLogAnalyserTest {

	public void original_file() {		
		final String filename = "src/test/resources/programming-task-example-data.log";
		ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();
		System.out.println(filename);
		printNumberOfUniqueIpOccurrences(apacheLogAnalyser.findUniqueOccurrences(filename, 1));
		printTopUrlOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 6, 3));
		printTopIpOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 1, 3));
	}
	
	public void there_are_only_top_2_url() {		
		final String filename = "src/test/resources/data_for_only_top_2.log";
		ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();
		System.out.println(filename);
		printNumberOfUniqueIpOccurrences(apacheLogAnalyser.findUniqueOccurrences(filename, 1));
		printTopUrlOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 6, 3));
		printTopIpOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 1, 3));
	}

	public void there_are_exactly_top_3_url() {		
		final String filename = "src/test/resources/data_for_exactly_top_3.log";
		ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();
		System.out.println(filename);
		printNumberOfUniqueIpOccurrences(apacheLogAnalyser.findUniqueOccurrences(filename, 1));
		printTopUrlOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 6, 3));
		printTopIpOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 1, 3));
	}
	
	public void there_are_more_than_top_3_url() {		
		final String filename = "src/test/resources/data_for_more_than_top_3.log";
		ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();
		System.out.println(filename);
		printNumberOfUniqueIpOccurrences(apacheLogAnalyser.findUniqueOccurrences(filename, 1));
		printTopUrlOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 6, 3));
		printTopIpOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 1, 3));
	}

	public void empty_file() {		
		final String filename = "src/test/resources/empty_file.log";
		ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();
		System.out.println(filename);
		printNumberOfUniqueIpOccurrences(apacheLogAnalyser.findUniqueOccurrences(filename, 1));
		printTopUrlOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 6, 3));
		printTopIpOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 1, 3));
	}

	public void there_are_url_same_n_occurrence() {		
		final String filename = "src/test/resources/same_n_occurrences.log";
		ApacheLogAnalyser apacheLogAnalyser = new ApacheLogAnalyser();
		System.out.println(filename);
		printNumberOfUniqueIpOccurrences(apacheLogAnalyser.findUniqueOccurrences(filename, 1));
		printTopUrlOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 6, 3));
		printTopIpOccurrences(apacheLogAnalyser.findTopOccurrences(filename, 1, 3));
	}


	private void printTopUrlOccurrences(List<List<Entry<String, Long>>> list) {
		System.out.println("-------------------------------------------------");
		System.out.println("Top 3 most visited URLs: ");			
		int i = 1;
		for (List<Entry<String, Long>> entryList: list) {
			System.out.println("\n"+i+") Occurrences = "+entryList.get(0).getValue());
			for (Entry<String, Long> entry : entryList) {
				System.out.println(entry.getKey());
			}
			i++;
		}
	}

	private void printTopIpOccurrences(List<List<Entry<String, Long>>> list) {
			System.out.println("-------------------------------------------------");
			System.out.println("Top 3 most active IP addresses: ");			
			int i = 1;
			for (List<Entry<String, Long>> entryList: list) {
				System.out.println("\n"+i+") Occurrences = "+entryList.get(0).getValue());
				for (Entry<String, Long> entry : entryList) {
					System.out.println(entry.getKey());
				}
				i++;
			}
	}

	private void printNumberOfUniqueIpOccurrences(List<String> list) {
		System.out.println("-------------------------------------------------");
			System.out.println("Number of unique IP addresses:");
			System.out.println(list.size());
	}

	public static void main(String[] args) {
		ApacheLogAnalyserTest test = new ApacheLogAnalyserTest();
		test.original_file();
		test.there_are_only_top_2_url();
		test.there_are_exactly_top_3_url();
		test.there_are_more_than_top_3_url();
		test.there_are_url_same_n_occurrence();
		test.empty_file();
	
	}
}
