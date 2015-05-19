package au.com.billingbuddy.connection.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.interfaces.IMySQLConnection;
import au.com.billingbuddy.dao.objects.DAO;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;

public class MySQLErrorLogConnection extends DAO implements IMySQLConnection {
	
	protected Connection connection = null;
	
	public MySQLErrorLogConnection() throws MySQLConnectionException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					ConfigurationSystem.getKey("urlConnection"),
					ConfigurationSystem.getKey("userDB"),
					ConfigurationSystem.getKey("passwordDB"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new MySQLConnectionException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MySQLConnectionException(e);
		}
	}
	
	public MySQLErrorLogConnection(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		try {
			connection = mySQLTransaction.getConection();
		} catch (MySQLTransactionException e) {
			e.printStackTrace();
		}
	}
	
	protected void Ps(PreparedStatement ps, Connection con) {
        try {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            
            if (con != null && con.getAutoCommit()) {
            	con.close();
            	con = null;
            }
            
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

	protected void Rs(ResultSet rs, Connection con) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            
            if (con != null && con.getAutoCommit()) {
            	con.close();
            	con = null;
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

	protected void Cs(CallableStatement cs, Connection con) {
        try {
            if (cs != null) {
            	
            	//Esta linea guarda de manera temporal el string que se ejecuto cuando se realizo el CALL al SP, 
            	//este elemento puede ser utilidado para registrar esta informacion en el log de los errores para futuras revisiones.
            	setCallString(cs.toString());
                
            	cs.close();
                cs = null;
            }
            if (con != null && con.getAutoCommit()) {
            	con.close();
            	con = null;
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
    
	protected void Cs(CallableStatement cs) {
        try {
            if (cs != null) {
                cs.close();
                cs = null;
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
    
	protected void Cs(CallableStatement cstmt, ResultSet rs, Connection con) {
		try {
			
		    if (cstmt != null){
				cstmt.close();
			    cstmt = null;
			}
		    if (rs != null){
				rs.close();
			    rs = null;
			}
		    if (con != null && con.getAutoCommit()) {
            	con.close();
            	con = null;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	protected void PsRs(PreparedStatement ps, ResultSet rs, Connection con) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (con != null && con.getAutoCommit()) {
            	con.close();
            	con = null;
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
    
	protected void Conn(Connection con) {
        try {
            if (con != null && con.getAutoCommit()) {
            	con.close();
            	con = null;
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
