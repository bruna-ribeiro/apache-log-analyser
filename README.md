
## Apache Log Analyser

This application parses an Apache access log file and reports the number of unique fields, as well as the top K occurrences of a field.
A field is any part of the log entry, like IP address, URL, user ID, status code, etc.

## Description

The application expects the log file name as an entry parameter.
An example of an Apache access log file is: 

    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326

The log entry is parsed using a regular expression, splitting the line into groups as per below:
- IP address: group 1
- identd: group 2
- userid: group 3
- request date and time: group 4
- HTTP method: group 5
- request resource (URL): group 6
- protocol: group 7
- status code: group 8
- size object returned: group 9

The aplication has 2 main functions:
- Search for a list of unique values of a field (for example, unique IP addresses). When calling the function, the user needs to pass the group number that corresponds to the field being searched. It can be also be used to report the number of unique values, by counting the size of the list returned.
- Search for the top K occurrences of a field (for example, top 3 URLs). When calling the function, the user needs to pass the group number that corresponds to the field being searched and the K value.

A simple explanation of the application workflow is shown below using the following log sample:

    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /newsletter/ HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /asset.js HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /faq/how-to/ HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /asset.js HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /newsletter/ HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /faq/how-to/ HTTP/1.0" 200 2326
    127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /newsletter/ HTTP/1.0" 200 2326

 1) Parse each line of the file using a regular expression that splits the text into groups (for example, let's search for the URL)
2) Create a list of all found values:
[/newsletter/, /apache_pb.gif, /asset.js, /faq/how-to/, /asset.js, /newsletter/, /faq/how-to/, /newsletter/]
3) To find unique values, we just need to remove the duplicates:
[/newsletter/, /apache_pb.gif, /asset.js, /faq/how-to/]
4) To find the top K occurrences, we create a map of a key-value pair, where the key is the URL, and the value is how many times the key occurs in the file:
{/asset.js=2, /apache_pb.gif=1, /newsletter/=3, /faq/how-to/=2}
5) Now we create a list of the unique number of occurrences:
[2, 1, 3]
6) We only want the top K (let's consider K = 2), so we need to reverse sort:
[3, 2]
7) We can now loop the map searching for the entries that match the values to the list on step 6:
All entries where value = 3: /newsletter/
All entries where value = 2: /asset.js, /faq/how-to/

Note: if is is guaranteed that there aren't values with the same number of occurrences, we can skip steps 5 and 6 and just reverse sort the map on 4.

## Instructions

There are no extra libraries. To run the application, pass the file name (relative path) as a parameter:

    java ApacheLogAnalyser /src/test/resources/access_data.log

The application also contains a class ApacheLogAnalyserTest with a few examples on how to test different files and print the results.

