# Relevance Ranker

This program ranks text documents based on keyword. The relevance score of a keyword i to document j is computed using the following formula:         
```
w(i, j) = {[1 + log(f(i,j))] * log(N/n(i)), if f(i,j) > 0; otherwise 0}      
```
where f(i,j) is the frequency of keyword i in document j, N is the total number of documents, and n(i) is the number of documents that cotain keyword i.

**Compile**      
compile: make or javac *.java

**Usage**     
First, run preproc on every text file individually, creating a counts file for each. The text file and its outputted counts file must be in the same directory as the java files.

Second, run the indexer, which looks for any counts files and creates an index as a result.

Finally, run the ranker with at least one, but no more than three keywords. It will return the files that are relevant and their score based on the equation above.

The requirements listed above are due to the structure of the assignment and its requirements.

Included are 6 sample text documents.