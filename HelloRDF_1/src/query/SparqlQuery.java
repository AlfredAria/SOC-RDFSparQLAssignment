package query;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.engine.QueryEngineFactory;

import conversion.PathUtils;

public class SparqlQuery {
	public static void main (String[] args) {
		
		// Build Parameterized query
		Query query = QueryFactory.create ( 
				String.format ("PREFIX arc: <http://purl.org/dc/elements/1.1/>\n"
				+ "SELECT ?title \n"
				+ "WHERE {\n"
				+ "?x arc:title ?title .\n"
				+ "?x arc:creator \"%s\" .\n"
				+ "}\n", "Brian Allen")
				, Syntax.syntaxARQ );

//		QuerySolutionMap querySolutionMap = new QuerySolutionMap();
//		querySolutionMap.add("creator", 
//				new ResourceImpl("Brian Allen"));
		
//		ParameterizedSparqlString parameterizedSparqlString
//			= new ParameterizedSparqlString(query.toString(), querySolutionMap);
		
		// Load RDF data
		Model model = RDFDataMgr.loadModel(PathUtils.outputPath);
		// Execute query
		QueryExecution qExec = QueryExecutionFactory.create(
				query,
//				parameterizedSparqlString.asQuery(),
				model);
		
		ResultSet resultSet = qExec.execSelect();
		System.out.println(
		ResultSetFormatter.asText(resultSet));
		
		qExec.close();
	}
}
