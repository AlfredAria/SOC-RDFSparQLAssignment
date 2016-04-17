package rdf;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.riot.RDFDataMgr;

public class RDFDataset {

	private static RDFDataset singleton;

	private Model model;

	private RDFDataset () {
		model = RDFDataMgr.loadModel(PathUtils.outputPath);
	}

	public static RDFDataset getInstance() {
		synchronized (RDFDataset.class) {			
			if (singleton == null) {
				singleton = new RDFDataset();
			}
			return singleton;
		}
	}

	public Model getModel () {
		return model;
	}

	public List<Map<String, String>> getQ1(String authorName) {
		// Build Parameterized query
		Query query = QueryFactory.create ( 
				String.format ("PREFIX arc: <http://purl.org/dc/elements/1.1/>\n"
						+ "SELECT ?title \n"
						+ "WHERE {\n"
						+ "?x arc:title ?title .\n"
						+ "?x arc:creator \"%s\" .\n"
						+ "}\n", authorName )
				, Syntax.syntaxARQ );

		// Execute query
		QueryExecution qExec = QueryExecutionFactory.create(query, model);

		ResultSet rs = qExec.execSelect();

		List<Map<String, String>> list = new ArrayList<> ();

		while (rs.hasNext()) {
			Map<String, String> map = new HashMap <String, String> ();
			QuerySolution qs = rs.next();
			map.put("title", qs.get("title").toString());
			list.add(map);
		}
		qExec.close();
		return list;
	}

	public List<Map<String, String>> getQ2(String authorName, Integer startWithYear, Integer endWithYear) {
		// Build Parameterized query
		Query query = QueryFactory.create ( 
				String.format (
						"PREFIX arc: <http://purl.org/dc/elements/1.1/>\n" + 
								"SELECT ?title ?author ?date\n" + 
								" WHERE {" + 
								"	?x arc:title ?title ." + 
								"	?x arc:creator ?author ." + 
								"	FILTER regex(?author, \"%s\") ." + 
								"	?x arc:date ?date ." + 
								"		FILTER (?date >= %d && ?date <= %d)" +
								"}", authorName, startWithYear, endWithYear )
				, Syntax.syntaxARQ );

		// Execute query
		QueryExecution qExec = QueryExecutionFactory.create(query, model);

		ResultSet rs = qExec.execSelect();

		List<Map<String, String>> list = new ArrayList<> ();

		while (rs.hasNext()) {
			Map<String, String> map = new HashMap <String, String> ();
			QuerySolution qs = rs.next();
			map.put("title", qs.get("title").toString());
			map.put("author", qs.get("author").toString());
			map.put("year", qs.get("date").asLiteral().getInt() + "");
			list.add(map);
		}
		qExec.close();
		return list;
	}

	public List<Map<String, String>> getQ3(String authorName) {
		// Build Parameterized query
		Query query = QueryFactory.create ( 
				String.format (
						"PREFIX arc: <http://purl.org/dc/elements/1.1/>\n"
								+"SELECT DISTINCT ?author\n"
								+"WHERE {\n"
								+"	?x arc:creator \"%s\" .\n"
								+"	?x arc:creator ?author .\n"
								+"}"
								, authorName)
				, Syntax.syntaxARQ );

		// Execute query
		QueryExecution qExec = QueryExecutionFactory.create(query, model);

		ResultSet rs = qExec.execSelect();

		List<Map<String,String>> list = new ArrayList<> ();

		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			Map <String, String> map = new HashMap <> ();
			String coauthorName = qs.get("author").toString();
			if (!coauthorName.equals(authorName)) {
				map.put("coauthor", coauthorName);
				list.add(map);
			}
		}
		qExec.close();

		return list;
	}

	public List<Map<String, String>> getQ4(String paperTitle) {
		// Build Parameterized query
		Query query = QueryFactory.create ( 
				String.format(
						"PREFIX arc: <http://purl.org/dc/elements/1.1/>\n" +
								"SELECT ?title ?description ?author ?date\n" +
								"	WHERE {\n" +
								"		 ?x arc:title ?title .\n" +
								"		 FILTER regex(?title, \"^%s$\") .\n" +
								"		 ?x arc:description ?description .\n" +
								"		 ?x arc:creator ?author .\n" +
								"		 ?x arc:date ?date . \n" +
								"}", paperTitle)
				, Syntax.syntaxARQ );

		// Execute query
		QueryExecution qExec = QueryExecutionFactory.create(query, model);

		ResultSet rs = qExec.execSelect();

		List<Map<String, String>> list = new ArrayList<> ();

		while (rs.hasNext()) {
			Map<String, String> map = new HashMap <String, String> ();
			QuerySolution qs = rs.next();
			map.put("title", qs.get("title").toString());
			map.put("description", qs.get("description").toString());
			map.put("author", qs.get("author").toString());
			map.put("date", qs.get("date").asLiteral().getInt() + "");
			list.add(map);
		}
		qExec.close();
		return list;
	}

	public List<Map<String, String>> getQ5(String titleKeywords) {
		// Build Parameterized query
		Query query = QueryFactory.create ( 
				String.format(
						 "PREFIX arc: <http://purl.org/dc/elements/1.1/>"
								 +"SELECT ?title ?description ?author ?date"
								 +"	WHERE {"
								 +"		 ?x arc:title ?title ."
								 +"		 FILTER regex(?title, \"%s\") ."
								 +"		 ?x arc:description ?description ."
								 +"		 ?x arc:creator ?author ."
								 +"		 ?x arc:date ?date . "
								 +"	 }", titleKeywords)
				, Syntax.syntaxARQ );
		// Execute query
		QueryExecution qExec = QueryExecutionFactory.create(query, model);

		ResultSet rs = qExec.execSelect();

		List<Map<String, String>> list = new ArrayList<> ();

		while (rs.hasNext()) {
			Map<String, String> map = new HashMap <String, String> ();
			QuerySolution qs = rs.next();
			map.put("title", qs.get("title").toString());
			map.put("description", qs.get("description").toString());
			map.put("author", qs.get("author").toString());
			map.put("date", qs.get("date").asLiteral().getInt() + "");
			list.add(map);
		}
		qExec.close();
		return list;
	}



	public static void main (String[] args) {
		System.out.println(
				getInstance().getQ2("Brian Allen", 1930, 2015));
	}
}
