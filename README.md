# LuceneSearchEngine
A simple implementation of Lucene Search Engine for the Cranfield Collection

### Install Maven
```
sudo apt install maven
```

### Building project
```
1. mvn package
2. mvn compile
3. mvn exec:java
```

After following the mentioned steps, you should get a directory which contians the different top ranked documents. 


### Evaluating the search engine

Unzip trec_eval
```
unzip trec_eval.zip
```

QRels is the referential file ie the groud truth. TREC_EVAL takes in the following format:

```
trec_eval -q referential_file system_file
```

More in depth tutorial on TREC_EVAL:http://curric.dlib.vt.edu/modDev/package_modules/MidtermModuleTeam5-TRECevalFinal.pdf
