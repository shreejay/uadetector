/*******************************************************************************
 * Copyright 2012 André Rouél
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sf.uadetector.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		// undeploy used parsers cleanly
		UADetectorServiceFactory.getOnlineUpdatingParser().shutdown();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);

		
		PrintWriter out = response.getWriter();

		// Get an UserAgentStringParser and analyze the requesting client
		UserAgentStringParser parser = UADetectorServiceFactory.getOnlineUpdatingParser();
		if(parser != null){
			//ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
			request.getQueryString();
			String userAgent = "Default String Values to prevent Quality Check from crapping out";
			userAgent = request.getParameter("user_agent");
			if(userAgent != null){
				ReadableUserAgent agent = parser.parse(userAgent);
							
				JSONObject obj = new JSONObject();
				obj.put("DeviceType", agent.getDeviceCategory().getCategory().getName());
				obj.put("OperatingSystem", agent.getOperatingSystem().getName());
				obj.put("Browser", agent.getFamily().getName());
				
				String browserVersion = "";
				if(agent.getVersionNumber().getGroups().size() > 0){
					browserVersion = concatStringsWSep(agent.getVersionNumber().getGroups(), ".");
				}
				obj.put("BrowserVersion",browserVersion);
				
				out.print(obj);
				out.flush();
			}
		}
	}
	
	public static String concatStringsWSep(Iterable<String> strings, String separator) {
	    StringBuilder sb = new StringBuilder();
	    String sep = "";
	    for(String s: strings) {
	        sb.append(sep).append(s);
	        sep = separator;
	    }
	    return sb.toString();                           
	}

}
