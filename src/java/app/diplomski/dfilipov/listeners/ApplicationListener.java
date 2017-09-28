package app.diplomski.dfilipov.listeners;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

@WebListener()
public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		
		PoolProperties properties = new PoolProperties();
		properties.setUrl("jdbc:derby://localhost:1527/dfilipov_diplomski_db");
		properties.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
		properties.setUsername("dfilipov");
		properties.setPassword("diplomski");
		properties.setInitialSize(4);
		properties.setMaxIdle(24);
		properties.setMaxActive(24);
		
		DataSource dataSource = new DataSource();
		dataSource.setPoolProperties(properties);
		servletContext.setAttribute("DerbyDB", dataSource);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		servletContext.removeAttribute("DerbyDB");
	}
	
	private static ServletContext servletContext;
	
	public static synchronized ServletContext getServletContext() {
		return servletContext;
	}
}
