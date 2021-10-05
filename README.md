# CASnumber_lookup

Program to find chemical names from CAS numbers in an excel sheet

The program expects an excel sheet where CAS numbers are in one column. The user can define how many rows to skip, what column the CAS numbers are in, and what column the chemical names should be written in through a small GUI. The jar file for the program currently must be kept in the same directory as the excel file that functions as a database.

The program first checks the 'database' for the CAS number. If it is not found, then the NIST page is checked. If that is not found, then a search is run through another website. It is assumed that the CAS is found and a chemical name identified. The CAS and chemical name are added to the 'database'. The original excel sheet is updated to have the chemical names along with the CAS numbers. A message window lets the user new when the program is finished.

The excel 'database' must have two sheets. The first column of the first 50 rows contain the string 'none' as a quick and dirty work-around so that the row iterator can access them.

# Usage

```
mvn package
java -jar target/xlsxReader-0.0.3-SNAPSHOT-jar-with-dependencies.jar
```