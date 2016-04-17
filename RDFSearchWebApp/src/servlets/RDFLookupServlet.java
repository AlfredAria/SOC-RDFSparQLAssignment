package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import rdf.RDFDataset;

/**
 * Servlet implementation class RDFLookupServlet
 */
@WebServlet("/RDFLookupServlet")
public class RDFLookupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RDFLookupServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("RDFLookupServlet: doGet");
		try {
			request.setCharacterEncoding("utf8");
			Integer taskNum = Integer.valueOf(request.getParameter("task")); 
			String ans = "{}";
			switch (taskNum) {
			case 1:
				String author = request.getParameter("author");
				ans =
						new Gson().toJson(
								RDFDataset.getInstance().getQ1(author));
				break;
			case 2:
				author = request.getParameter("author");
				Integer startWith = Integer.valueOf(request.getParameter("startWith"));
				Integer endWith = Integer.valueOf(request.getParameter("endWith"));
				ans =
						new Gson().toJson(
								RDFDataset.getInstance().getQ2(author, startWith, endWith));
				break;
			case 3:
				author = request.getParameter("author");
				ans =
						new Gson().toJson(
								RDFDataset.getInstance().getQ3(author));
				break;
			case 4:
				String title = request.getParameter("title");
				ans =
						new Gson().toJson(
								RDFDataset.getInstance().getQ4(title));
				break;
			case 5:
				title = request.getParameter("title");
				ans =
						new Gson().toJson(
								RDFDataset.getInstance().getQ5(title));				
				break;			
			}
			response.setCharacterEncoding("utf8");
			response.setContentType("application/json");
			response.getWriter().write(ans);
		} catch (Exception e) {
			response.getWriter().write(e.getMessage());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
