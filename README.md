# CS3219-Assignment-3

## API Documentation (Planned!!)

The various files provided are downloaded and parsed by the `DataParser` package. All these data will be saved in a seperate files
(XML for now). 

Each conference for each year is considered to be a table (that is everything in D12 will be one table stored in one
XML and everything in D13 will be considered a seperate table saved in another file).

The API aims to emulate the SQL query syntax to lend it more expressive power and extensibility.

### Assumptions

* All tables will have the same number of columns and each columns are the same.
* There will be no table joining

User will use `QueryBuilder` to build a query on a prepopulated table containing all the papers published in that conference and thus each table named after the file like D12, D13 etc) and the columns will be named after the attributes. 

A table is described in the code as a `Schema` like so:

```java
public class PaperPublishedInConference {
  /**
  An attribute of the paper detailing the author of the paper.
  The schema class will look into SerializedJournal for the attribute named "author"
  This attribute is a string.
  */
  public SchemaString authorName = new SchemaString("author");
  
  /**
  An attribute of the paper detailing the title of the paper.
  The schema class will look into SerializedJournal for the attribute named "title"
  This attribute is a string.
  */
  public SchemaString titleOfPaper = new SchemaString("title");
  
   /**
  An attribute of the paper detailing the title of the paper.
  The schema class will look into SerializedJournal for the attribute named "yearOfPublication"
  This attribute is a integer.
  */
  public SchemaInt yearPublished = new SchemaInt("yearOfPublication");
  
  // More code below...
}

```

Furthermore, all `Schema` classes allow for comparison like SQL such as:

```java
yearPublished.greaterOrEqualsTo(2012); // yearPublished >= 2012
yearPublished.lessThan(2011); yearPublished < 2011
yearPublished.equalsTo(2012).and(authorName.equalsTo("Bob")); // yearPublished = 2012 AND authorName = 'Bob'

```

The user can then call `QueryBuilder.build()` to build the query to return a `Query` object which can be executed using `execute()`.
The execute call will return the results in JSON format.

The planned API calls for each question are:

> Q1: How many documents are there in all datasets put together?
```java
PaperPublishedInConference conf = new PaperPublishedInConference();
Query query = QueryBuilder
  .select(new SchemaCount(conf.id))
  .from("D12", "D13", "D14", "D15", "K14", "W14", "Q14")
  .build();
  
query.execute();
```
