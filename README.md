Problem Statement:
How fast we can process a file containing 1 billion lines of data. The input is a UTF-8 file where each line consists of a station name and a temperature. 

Input:
A text file (measurements.txt) containing exactly 1,000,000,000 rows (one billion lines), where each row follows this format:

Tokyo;12.8
Marseille;28.1
Philadelphia;10.1
Tokyo;14.8
...
Philadelphia;-4.1


The goal is to write an application that:

Read the file.
Calculate the min, mean, and max temperatures per station.
Write the results on stdout (sorted by station name) in a specific format.

Output:
For each unique weather station name formatted as
{Tokyo=-23.0/18.0/59.2, Philadelphia=-16.2/26.0/67.3, ...}



Measurement.txt:
Download it from below link
https://drive.google.com/file/d/1mHYNhpVtvBiF8uBoYn2GvUXv4Cpnl-o1/view?usp=sharing

Key Constraints and Details
Aspect
Specification
File size
~13-14 GB
Number of rows
Exactly 1,000,000,000
Unique stations
Up to 10,000 distinct names
Station name length
1-100 bytes (UTF-8)
Temperature range
-99.9 to 99.9
Temperature precision
Exactly one decimal place
Separator
Semicolon (;)

