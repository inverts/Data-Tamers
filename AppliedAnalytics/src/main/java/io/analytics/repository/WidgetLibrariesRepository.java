package io.analytics.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.analytics.domain.WidgetLibrary;
import io.analytics.domain.WidgetLibraryType;
import io.analytics.repository.interfaces.IWidgetLibrariesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class WidgetLibrariesRepository implements IWidgetLibrariesRepository {

	private JdbcTemplate jdbc;
	private SimpleJdbcInsert jdbcInsertLibraries;
	private SimpleJdbcInsert jdbcInsertTypesInLibraries;
	
	@Autowired
	public WidgetLibrariesRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		jdbcInsertLibraries = new SimpleJdbcInsert(this.jdbc).withTableName(WIDGET_LIBRARIES_TABLE).usingGeneratedKeyColumns(WidgetLibrariesTable.LIBRARY_ID);
		jdbcInsertTypesInLibraries = new SimpleJdbcInsert(this.jdbc).withTableName(WIDGET_TYPES_IN_LIBRARIES_TABLE);
	}

	public static final String WIDGET_LIBRARIES_TABLE = "WidgetLibraries";
	private static final class WidgetLibrariesTable {
		public static final String LIBRARY_ID = "idWidgetLibraries";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
	}

	public static final String WIDGET_TYPES_IN_LIBRARIES_TABLE = "WidgetLibraries_has_WidgetTypes";
	private static final class WidgetTypesInLibrariesTable {
		public static final String LIBRARY_ID = "idWidgetLibraries";
		public static final String WIDGET_TYPE_ID = "idWidgetTypes";
		public static final String PRIORITY = "priority";
		public static final String IS_FEATURED = "isFeatured";
	}
	
	public static final String WIDGET_TYPES_TABLE = "WidgetTypes";
	private static final class WidgetTypesTable {
		public static final String WIDGET_TYPES_ID = "idWidgetTypes";
		public static final String WIDGET_NAME = "widgetName";
		public static final String WIDGET_DESCRIPTION = "widgetDescription";
	}

	private static final class WidgetLibraryTypesMapper implements RowMapper<WidgetLibraryType> {
		@Override
		public WidgetLibraryType mapRow(ResultSet rs, int row) throws SQLException {
			WidgetLibraryType w = new WidgetLibraryType(rs.getInt(WidgetTypesInLibrariesTable.WIDGET_TYPE_ID));
			w.setName(rs.getString(WidgetTypesTable.WIDGET_NAME));
			w.setDescription(rs.getString(WidgetTypesTable.WIDGET_DESCRIPTION));
			w.setPriority(rs.getInt(WidgetTypesInLibrariesTable.PRIORITY));
			w.setIsFeatured(rs.getInt(WidgetTypesInLibrariesTable.IS_FEATURED));
			return w;
		}
	}
	
	private static final class WidgetLibrariesMapper implements RowMapper<WidgetLibrary> {
		@Override
		public WidgetLibrary mapRow(ResultSet rs, int row) throws SQLException {
			WidgetLibrary w = new WidgetLibrary(rs.getInt(WidgetLibrariesTable.LIBRARY_ID));
			w.setName(rs.getString(WidgetLibrariesTable.NAME));
			w.setName(rs.getString(WidgetLibrariesTable.DESCRIPTION));
			return w;
		}
	}

	@Override
	public List<WidgetLibraryType> getTypesInLibrary(int widgetLibraryId) throws DataAccessException {
		Object[] args;
		int[] argTypes;
		String whereClause = String.format("WHERE `%s`.`%s`=`%s`.`%s`", 
				WIDGET_TYPES_IN_LIBRARIES_TABLE, WidgetTypesInLibrariesTable.WIDGET_TYPE_ID,
				WIDGET_TYPES_TABLE, WidgetTypesTable.WIDGET_TYPES_ID);
		
		String preStatement = String.format("SELECT * FROM `%s`, `%s` %s AND `%s`.`%s`=?;", 
				WIDGET_TYPES_IN_LIBRARIES_TABLE, WIDGET_TYPES_TABLE, 
				whereClause,
				WIDGET_TYPES_IN_LIBRARIES_TABLE, WidgetTypesInLibrariesTable.LIBRARY_ID);
		
		args = new Object[] { widgetLibraryId };
		argTypes = new int[] { Types.INTEGER };

		List<WidgetLibraryType> widgetLibraryTypes = jdbc.query(preStatement, args, argTypes, new WidgetLibraryTypesMapper());
		
		return widgetLibraryTypes; 
			
	}

	@Override
	public List<WidgetLibrary> getWidgetLibraries() throws DataAccessException {
		
		String preStatement = String.format("SELECT * FROM `%s`;", WIDGET_LIBRARIES_TABLE);
		List<WidgetLibrary> widgetLibraries = jdbc.query(preStatement, new WidgetLibrariesMapper());
		
		return widgetLibraries; 
	}

	@Override
	public WidgetLibrary getWidgetLibrary(int widgetLibraryId) throws DataAccessException {
		String preStatement = String.format("SELECT * FROM `%s` WHERE `%s`=?;", WIDGET_LIBRARIES_TABLE, WidgetLibrariesTable.LIBRARY_ID);
		Object[] args = new Object[] { widgetLibraryId };
		int[] argTypes = new int[] { Types.INTEGER };
		List<WidgetLibrary> widgetLibraries = jdbc.query(preStatement, args, argTypes, new WidgetLibrariesMapper());
		if (widgetLibraries == null || widgetLibraries.isEmpty())
			return null;
		return widgetLibraries.get(0); 
	}

	@Override
	public boolean addWidgetTypeToLibrary(WidgetLibraryType widgetLibraryType, int widgetLibraryId) throws DataAccessException {
		Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(WidgetTypesInLibrariesTable.LIBRARY_ID, widgetLibraryId);
        insertParams.put(WidgetTypesInLibrariesTable.WIDGET_TYPE_ID, widgetLibraryType.getId());
        insertParams.put(WidgetTypesInLibrariesTable.PRIORITY, widgetLibraryType.getPriority());
        insertParams.put(WidgetTypesInLibrariesTable.IS_FEATURED, widgetLibraryType.getIsFeatured());
        int rowsAffected = jdbcInsertLibraries.execute(insertParams);
        return rowsAffected == 1;
	}

	@Override
	public void addWidgetLibrary(WidgetLibrary widgetLibrary) throws DataAccessException {
		Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(WidgetLibrariesTable.LIBRARY_ID, widgetLibrary.getId());
        insertParams.put(WidgetLibrariesTable.NAME, widgetLibrary.getName());
        insertParams.put(WidgetLibrariesTable.DESCRIPTION, widgetLibrary.getDescription());
        int rowsAffected = jdbcInsertLibraries.execute(insertParams);
        
		//TODO: Not sure if rowsAffected will ever be 0 without throwing an exception with this query.
	}

	@Override
	public List<WidgetLibraryType> getAllWidgetLibraryTypes() throws DataAccessException {

		throw new UnsupportedOperationException();
	}

	@Override
	public WidgetLibraryType getWidgetLibraryType(int widgetLibraryTypeId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
