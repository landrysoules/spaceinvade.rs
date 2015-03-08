---
layout: post.html
title: CouchDB/Lucene integration
tags: [CouchDB]
---

![CouchDB logo](https://ef65e426d0abf9418992e271986035a1945d7865.googledrive.com/host/0Byy3K2j5Zp_TeXByVnlqTS10UUU/couchdb-logo1.png)

CouchDB is an awesome database, however you can face situations where the map/reduce views won't be able to solve efficiently your problem.  
Let's take a simple client document:  

~~~ json
"name": "John Doe",
"gender": "M",
"age": 39,
"city": "Springfield",
"lastEmployer": "Moe's Tavern"
~~~

Using complex key, you will be able to search all the men from Springfield, but if you want to perform complex queries, you will have to create multiple views:  

+ a view indexed by gender and age
+ another by age and gender
+ another by city and age
+ another by age and city, and so on ...

As you can see, the number of views can grow very fast ! Instead, the power of map/reduce views combined with the flexibility of fulltext search will make you forget about sql databases for ever !  
Currently, fulltext search feature can be added to your CouchDB via Lucene or ElasticSearch. The following instructions will guide you through the install process of [CouchDB-Lucene](https://github.com/rnewson/couchdb-lucene).  
I assume you already have CouchDB installed. If not, please refer to the [official documentation](http://docs.couchdb.org/en/latest/install/index.html).

## CouchDB-Lucene installation ##

Again, you shouldn't have any problem following the [official installation instructions](https://github.com/rnewson/couchdb-lucene#build-and-run-couchdb-lucene).  
Once done, you will have to edit your CouchDB configuration file (/etc/couchdb/local.ini), adding the following options:

~~~ apache
timeout = 60000
[httpd_global_handlers]
_fti = {couch_httpd_proxy, handle_proxy_req, <<"http://localhost:5985">>}
[couchdb]
timeout = 60000
~~~

Here, localhost:5985 is the URL of your CouchDB-Lucene service. Of course, you can modify the port lucene is listening to in the couchdb-lucene.ini configuration file.  

## Create your first fulltext search ##

Create a new design document in CouchDB, let's call it _design/lucene. Here is how it looks like (as always when creating design documents, be careful to escape your javascript functions):  

~~~ json
{
   "_id": "_design/lucene",
   "_rev": "23-b7e715e927d362bc2de8d7716a29947f",
   "fulltext": {
       "people": {
           "index": "function(doc) {\n\tvar ret = new Document();
		   \n\tret.add(doc.gender,{'field':'gender', 'store':'yes'});
		   \n\tret.add(doc.age,{'field':'age', 'store':'yes'});
		   \n\tret.add(doc.name,{'field':'name', 'store':'yes'});
		   \n\tret.add(doc.city,{'field':'city', 'store':'yes'});
		   \n\tret.add(doc.lastEmployer,{'field':'lastEmployer', 'store':'yes'});
		   \n\treturn ret;\n}"
       }
   }
}
~~~

And it's all that is required to enable fulltext search on your documents ! The only tricky part is now to send requests to your newly created Lucene index.

## Fulltext searches ##

For a complete syntax reference, you can read the [official documentation](http://lucene.apache.org/core/2_9_4/queryparsersyntax.html). 

The URL is: ```http://127.0.0.1:5984/_fti/local/dbname/_design/lucene/people?q=gender:F 
AND age:[20 TO 40] AND city:s*&force_json=true&include_docs=true&sort=age```
Here, "people" is the name of your index, as defined in the design document.  
'force_json', 'include_docs' and 'sort' are Lucene parameters, explanations [here](https://github.com/rnewson/couchdb-lucene#search-parameters).
You are now ready to enjoy the new power Lucene brought to your database !