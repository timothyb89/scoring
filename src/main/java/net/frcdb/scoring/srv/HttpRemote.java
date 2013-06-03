package net.frcdb.scoring.srv;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.game.GameLoader;
import net.frcdb.scoring.game.Property;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchDatabase;
import net.frcdb.scoring.match.MatchManager;
import net.frcdb.scoring.schedule.AllianceEntry;
import net.frcdb.scoring.schedule.MatchSchedule;
import net.frcdb.scoring.schedule.ScheduleDatabase;
import net.frcdb.scoring.schedule.ScheduledMatch;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author tim
 */
public class HttpRemote {

	private Server server;
	
	private JsonFactory factory;
	private ObjectMapper mapper;
	
	public HttpRemote() {
		factory = new JsonFactory();
		mapper = new ObjectMapper();
		
		try {
			server = new Server(8182);
			
			ContextHandlerCollection contexts = new ContextHandlerCollection();
			
			ResourceHandler files = new ResourceHandler();
			files.setDirectoriesListed(true);
			files.setWelcomeFiles(new String[] {"index.html"});
			files.setResourceBase("./web/");
			
			ContextHandler filesContext = new ContextHandler("/");
			filesContext.setHandler(files);
			contexts.addHandler(filesContext);
			
			ContextHandler teamsContext = new ContextHandler("/teams");
			teamsContext.setHandler(teamsHandler);
			contexts.addHandler(teamsContext);
			
			ContextHandler alliancesContext = new ContextHandler("/alliances");
			alliancesContext.setHandler(alliancesHandler);
			contexts.addHandler(alliancesContext);
			
			ContextHandler propertiesContext = new ContextHandler("/properties");
			propertiesContext.setHandler(propertiesHandler);
			contexts.addHandler(propertiesContext);
			
			ContextHandler scheduleContext = new ContextHandler("/schedule");
			scheduleContext.setHandler(scheduleHandler);
			contexts.addHandler(scheduleContext);
			
			ServletContextHandler submitContext = new ServletContextHandler();
			submitContext.setContextPath("/submit");
			submitContext.addServlet(new ServletHolder(submitServlet), "/*");
			contexts.addHandler(submitContext);
			
			server.setHandler(contexts);
			
			server.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private AbstractHandler teamsHandler = new AbstractHandler() {

		public void handle(
				String target, Request baseRequest, 
				HttpServletRequest request, HttpServletResponse response) 
				throws IOException, ServletException {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);

			try {
				PrintWriter body = response.getWriter();
				JsonGenerator g = factory.createJsonGenerator(body);
				
				g.writeStartArray();
			
				for (Team team : TeamDatabase.get().getTeams()) {
					g.writeStartObject();

					g.writeNumberField("number", team.getNumber());
					g.writeStringField("name", team.getName());
					g.writeNumberField("wins", team.getWins());
					g.writeNumberField("losses", team.getLosses());
					g.writeNumberField("ties", team.getTies());
					g.writeNumberField("totalPoints", team.getTotalPoints());

					g.writeEndObject();
				}

				g.writeEndArray();
				
				g.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	};
	
	private AbstractHandler alliancesHandler = new AbstractHandler() {

		public void handle(
				String target, Request baseRequest, 
				HttpServletRequest request, HttpServletResponse response) 
				throws IOException, ServletException {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);

			try {
				PrintWriter body = response.getWriter();
				JsonGenerator g = factory.createJsonGenerator(body);
				
				g.writeStartArray();
			
				for (Alliance a : GameLoader.get().getGame().getAlliances()) {
					g.writeString(a.getName());
				}

				g.writeEndArray();
				
				g.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	};
	
	private AbstractHandler propertiesHandler = new AbstractHandler() {

		public void handle(
				String target, Request baseRequest, 
				HttpServletRequest request, HttpServletResponse response) 
				throws IOException, ServletException {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);

			try {
				PrintWriter body = response.getWriter();
				JsonGenerator g = factory.createJsonGenerator(body);
				
				g.writeStartArray();
			
				for (Property p: GameLoader.get().getGame().getProperties()) {
					g.writeString(p.getName());
				}

				g.writeEndArray();
				
				g.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	};
	
	private AbstractHandler scheduleHandler = new AbstractHandler() {

		public void handle(
				String target, Request baseRequest, 
				HttpServletRequest request, HttpServletResponse response) 
				throws IOException, ServletException {
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);

			try {
				PrintWriter body = response.getWriter();
				JsonGenerator g = factory.createJsonGenerator(body);
				
				MatchSchedule sched = ScheduleDatabase.get().getSchedule();

				g.writeStartArray();
				for (ScheduledMatch match : sched.getMatches()) {
					g.writeStartObject();
					g.writeNumberField("number", match.getNumber());

					g.writeArrayFieldStart("alliances");
					for (AllianceEntry e : match.getAlliances()) {
						g.writeStartObject();
						g.writeStringField("name", e.getAllianceName());

						g.writeArrayFieldStart("teams");
						for (int team : e.getTeams()) {
							g.writeNumber(team);
						}
						g.writeEndArray();
						g.writeEndObject();
					}
					g.writeEndArray();
					g.writeEndObject();
				}
				g.writeEndArray();
				
				g.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	};
	
	private HttpServlet submitServlet = new HttpServlet() {

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
				throws ServletException, IOException {
			resp.setContentType("application/json;charset=utf-8");
			resp.setStatus(HttpServletResponse.SC_OK);
			
			try {
				PrintWriter body = resp.getWriter();
				JsonGenerator g = factory.createJsonGenerator(body);

				String text = "";
				
				String input;
				while ((input = req.getReader().readLine()) != null) {
					text += input + "\n";
				}
				
				JsonNode root = mapper.readTree(text);
				handleSubmit(root, g);
				
				g.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		
	};
	
	private void handleSubmit(JsonNode root, JsonGenerator g) throws IOException {
		JsonNode actionNode = root.get("action");
		if (actionNode == null) {
			error("No 'action' field provided", g);
			return;
		}
		String action = actionNode.asText().toLowerCase();
		
		try {
			if (action.equals("team-add")) {
				handleAddTeam(root, g);
			} else if (action.equals("team-delete")) {
				handleRemoveTeam(root, g);
			} else if (action.equals("team-modify")) {
				handleModifyTeam(root, g);
			} else if (action.equals("match-init")) {
				handleMatchInitialize(root, g);
			} else if (action.equals("match-start")) {
				handleMatchStart(root, g);
			} else if (action.equals("match-stop")) {
				handleMatchStop(root, g);
			} else if (action.equals("match-pause")) {
				handleMatchPause(root, g);
			} else if (action.equals("match-resume")) {
				handleMatchResume(root, g);
			} else if (action.equals("match-finish")) {
				handleMatchFinish(root, g);
			} else if (action.equals("match-cancel")) {
				handleMatchCancel(root, g);
			} else if (action.equals("point-add")) {
				handlePointAdd(root, g);
			} else if (action.equals("point-subtract")) {
				handlePointSubtract(root, g);
			} else if (action.equals("get-point")) {
				handleGetPoint(root, g);
			} else if (action.equals("get-points")) {
				handleGetPoints(root, g);
			} else if (action.equals("get-status")) {
				handleGetStatus(root, g);
			} else {
				error("Action '" + action + "' not supported.", g);
			}
		} catch (Exception ex) {
			error(ex.getMessage(), g);
		}
	}
	
	private void handleAddTeam(JsonNode root, JsonGenerator g) throws IOException {
		JsonNode numberNode = root.get("number");
		if (numberNode == null || !numberNode.isNumber()) {
			error("Invalid value for 'number' field or none provided.", g);
			return;
		}
		int teamNumber = numberNode.asInt();
		
		JsonNode nameNode = root.get("name");
		if (nameNode == null || !nameNode.isTextual()) {
			error("Invalid value for 'name' field or none provided.", g);
			return;
		}
		String name = nameNode.asText();

		Team team = new Team(teamNumber, name);
		TeamDatabase.get().addTeam(team);
		TeamDatabase.get().save();

		success("Added team #" + teamNumber, g);
	}
	
	private void handleRemoveTeam(JsonNode root, JsonGenerator g) throws IOException {
		JsonNode teamNode = root.get("number");
		if (teamNode == null || !teamNode.isNumber()) {
			error("Invalid value for 'team' field or none provided.", g);
			return;
		}
		int teamNumber = teamNode.asInt();
		
		Team team = TeamDatabase.get().getTeam(teamNumber);
		if (team == null) {
			error("Team does not exist: " + teamNumber, g);
			return;
		}

		TeamDatabase.get().removeTeam(team);
		TeamDatabase.get().save();
		
		success("Removed team #" + teamNumber, g);

		// potentially dangerous if team is used in any matches
	}
	
	private void handleModifyTeam(JsonNode root, JsonGenerator g) throws IOException {
		JsonNode teamNode = root.get("number");
		if (teamNode == null || !teamNode.isNumber()) {
			error("Invalid value for 'team' field or none provided.", g);
			return;
		}
		int teamNumber = teamNode.asInt();
		
		Team team = TeamDatabase.get().getTeam(teamNumber);
		if (team == null) {
			error("Team does not exist: " + teamNumber, g);
			return;
		}

		JsonNode nameNode = root.get("name");
		if (nameNode == null || !nameNode.isTextual()) {
			error("Invalid value for 'name' field or none provided.", g);
			return;
		}
		String name = nameNode.asText();

		team.setName(name);
		TeamDatabase.get().save();
	}
	
	private void handleMatchInitialize(JsonNode root, JsonGenerator g) throws IOException {
		try {
			List<MatchAlliance> alliances = new ArrayList<MatchAlliance>();

			for (JsonNode allianceNode : root.get("alliances")) {

				JsonNode nameNode = allianceNode.get("name");
				String name = nameNode.asText();

				List<Team> teams = new ArrayList<Team>();

				for (JsonNode team : allianceNode.get("teams")) {
					Team t = TeamDatabase.get().getTeam(team.asInt());
					teams.add(t);
				}

				MatchAlliance ma = new MatchAlliance(name, teams);
				alliances.add(ma);
			}

			MatchManager.get().initializeMatch(alliances);

			success("The match was initialized successfully.", g);
		} catch (Exception ex) {
			error("Malformed JSON query.", g);
		}
	}
	
	private void handleMatchStart(JsonNode root, JsonGenerator g)throws IOException {
		MatchManager.get().startMatch();
		success("Match started.", g);
	}
	
	private void handleMatchStop(JsonNode root, JsonGenerator g) throws IOException {
		MatchManager.get().stopMatch();
		success("Match stopped.", g);
	}
	
	private void handleMatchPause(JsonNode root, JsonGenerator g) throws IOException {
		MatchManager.get().pauseMatch();
		success("Match paused.", g);
	}
	
	private void handleMatchResume(JsonNode root, JsonGenerator g) throws IOException {
		MatchManager.get().resumeMatch();
		success("Match resumed.", g);
	}
	
	private void handleMatchFinish(JsonNode root, JsonGenerator g) throws IOException {
		MatchManager.get().finishMatch(true);
		success("Match saved.", g);
	}
	
	private void handleMatchCancel(JsonNode root, JsonGenerator g) throws IOException {
		MatchManager.get().finishMatch(false);
		success("Match cancelled.", g);
	}
	
	private void handlePointAdd(JsonNode root, JsonGenerator g) throws IOException {
		if (MatchManager.get().getCurrentMatch() == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		JsonNode allianceNode = root.get("alliance");
		String alliance = allianceNode.asText();
		
		JsonNode propertyNode = root.get("property");
		String property = propertyNode.asText();
		
		System.out.println("adding point to " + property + " on " + alliance);
		
		Property prop = GameLoader.sGetGame().getProperty(property);
		if (prop == null) {
			error("Property not found.", g);
			return;
		}
		
		for (MatchAlliance ma : MatchManager.get().getCurrentMatch().getAlliances()) {
			if (ma.getAlliance().equals(alliance)) {
				PropertyValue v = ma.getPoint(property);
				if (v == null) {
					v = new PropertyValue(property, 0); // first setting
					ma.updatePoint(v);
				}
				
				int newValue = v.getValue() + 1;
				if (newValue <= prop.getConstraints().getMaximum()) {
					v.setValue(newValue);
					MatchManager.get().notifyPropertyChanged(ma, v);
					
					System.out.println("updated.");
					
					success("Updated property value.", g);
					return;
				} else {
					error("Can't set property higher than " 
							+ prop.getConstraints().getMaximum() + ".", g);
					return;
				}
			}
		}
		
		error("Invalid alliance.", g);
	}
	
	private void handlePointSubtract(JsonNode root, JsonGenerator g) throws IOException {
		if (MatchManager.get().getCurrentMatch() == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		JsonNode allianceNode = root.get("alliance");
		String alliance = allianceNode.asText();
		
		JsonNode propertyNode = root.get("property");
		String property = propertyNode.asText();
		
		Property prop = GameLoader.sGetGame().getProperty(property);
		
		for (MatchAlliance ma : MatchManager.get().getCurrentMatch().getAlliances()) {
			if (ma.getAlliance().equals(alliance)) {
				PropertyValue v = ma.getPoint(property);
				if (v == null) {
					v = new PropertyValue(property, 0); // first setting
					ma.updatePoint(v);
				}
				
				int newValue = v.getValue() - 1;
				if (newValue >= prop.getConstraints().getMinimum()) {
					v.setValue(newValue);
					MatchManager.get().notifyPropertyChanged(ma, v);
					
					success("Updated property value.", g);
					return;
				} else {
					error("Can't set property lower than " 
							+ prop.getConstraints().getMinimum() + ".", g);
					return;
				}
			}
		}
	}
	
	private void handleGetPoint(JsonNode root, JsonGenerator g) throws IOException {
		if (MatchManager.get().getCurrentMatch() == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		JsonNode allianceNode = root.get("alliance");
		String alliance = allianceNode.asText();
		
		JsonNode propertyNode = root.get("property");
		String property = propertyNode.asText();
		
		Property prop = GameLoader.sGetGame().getProperty(property);
		if (prop == null) {
			error("Property not found.", g);
			return;
		}
		
		for (MatchAlliance ma : MatchManager.get().getCurrentMatch().getAlliances()) {
			if (ma.getAlliance().equals(alliance)) {
				PropertyValue v = ma.getPoint(property);
				if (v == null) {
					v = new PropertyValue(property, 0); // first setting
					ma.updatePoint(v);
				}
				
				g.writeStartObject();
				g.writeStringField("status", "success");
				g.writeStringField("name", v.getName());
				g.writeNumberField("value", v.getValue());
				g.writeEndObject();
				
				return;
			}
		}
		
		error("Alliance '" + alliance + "' not found." ,g);
	}
  	
	private void handleGetPoints(JsonNode root, JsonGenerator g) throws IOException {
		if (MatchManager.get().getCurrentMatch() == null) {
			throw new IllegalStateException("There is no current match.");
		}
		
		JsonNode allianceNode = root.get("alliance");
		String alliance = allianceNode.asText();
		
		for (MatchAlliance ma : MatchManager.get().getCurrentMatch().getAlliances()) {
			if (ma.getAlliance().equals(alliance)) {
				g.writeStartObject();
				g.writeStringField("status", "success");
				g.writeArrayFieldStart("points");
				
				for (PropertyValue p : ma.getPoints()) {
					g.writeStartObject();
					g.writeStringField("name", p.getName());
					g.writeNumberField("value", p.getValue());
					g.writeEndObject();
				}
				
				g.writeEndArray();
				g.writeEndObject();
				
				return;
			}
		}
		
		error("Alliance '" + alliance + "' not found.", g);
	}
	
	private void handleGetStatus(JsonNode root, JsonGenerator g) throws IOException {
		String statusText;
		int timeLeft = -1;
		if (MatchManager.get().getCurrentMatch() == null) {
			statusText = "uninitialized";
		} else {
			if (MatchManager.get().isMatchInProgress()) {
				timeLeft = MatchManager.get().getTimeLeft();
				
				if (MatchManager.get().isFinished()) {
					statusText = "finished";
				} else if (MatchManager.get().isPaused()) {
					statusText = "paused";
				} else {
					statusText = "running";
				}
			} else {
				statusText = "initialized";
			}
		}
		
		g.writeStartObject();
		g.writeStringField("status", "success");
		g.writeStringField("statusText", statusText);
		if (timeLeft >= 0) {
			g.writeNumberField("timeLeft", timeLeft);
		}
		g.writeNumberField("latestMatch", MatchDatabase.get().getMatches().size());
		g.writeEndObject();
	}
	
	private void status(String status, String message, JsonGenerator g) throws IOException {
		g.writeStartObject();
		g.writeStringField("status", status);
		g.writeStringField("message", message);
		g.writeEndObject();
	}
	
	private void success(String message, JsonGenerator g) throws IOException {
		status("success", message, g);
	}
	
	private void error(String message, JsonGenerator g) throws IOException {
		status("error", message, g);
	}
	
	public static void main(String[] args) {
		new HttpRemote();
	}
	
}
