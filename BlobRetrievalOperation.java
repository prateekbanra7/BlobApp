package in.abc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import in.abc.jdbcUtil.JdbcUtil;

public class BlobRetrievalOperation {

	public static void main(String[] args) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		FileOutputStream fos = null;

		try {
			// Getting the database connection using utility code
			connection = JdbcUtil.getJdbcConnection();

			String sqlSelectQuery = "select name,image from person where name = ?";
			String name = "03";

			if (connection != null)
				pstmt = connection.prepareStatement(sqlSelectQuery);

			if (pstmt != null) {

				// Setting the first index to String
				pstmt.setString(1, name);

				// execute the Query
				resultSet = pstmt.executeQuery();
			}
			if (resultSet != null) {

				if (resultSet.next()) {
					// fetching the name
					String username = resultSet.getString(1);

					// fetching the image and keeping it in hard disk(just like downloading)
					InputStream is = resultSet.getBinaryStream(2);
										
					String fileName = "03_download.jpg";
					File file = new File(fileName);
					fos = new FileOutputStream(file);
					
					//copy from inputStream to outputStream
					IOUtils.copy(is, fos);
					
					System.out.println(username);
					System.out.println("File is saved to the location::"+file.getAbsolutePath());

				} else {
					System.out.println("record not available for the given :: " + name);
				}
			}

		} catch (SQLException e) {
			// handling logic of exception related to SQLException
			e.printStackTrace();
		} catch (Exception e) {
			// handling logic of exception related to common problem
			e.printStackTrace();
		} finally {
			// closing the resource
			try {
				JdbcUtil.closeConnection(null, pstmt, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
