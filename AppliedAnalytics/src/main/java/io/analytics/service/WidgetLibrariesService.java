package io.analytics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.analytics.domain.WidgetLibrary;
import io.analytics.domain.WidgetLibraryType;
import io.analytics.repository.interfaces.IWidgetLibrariesRepository;
import io.analytics.service.interfaces.IWidgetLibrariesService;

@Service
public class WidgetLibrariesService implements IWidgetLibrariesService {
	@Autowired
	IWidgetLibrariesRepository WidgetLibrariesRepository;
	
	@Override
	public List<WidgetLibraryType> getTypesInLibrary(int widgetLibraryId) throws DataAccessException {
		return WidgetLibrariesRepository.getTypesInLibrary(widgetLibraryId);
	}

	@Override
	public List<WidgetLibrary> getWidgetLibraries() throws DataAccessException {
		return WidgetLibrariesRepository.getWidgetLibraries();
	}

	@Override
	public WidgetLibrary getWidgetLibrary(int widgetLibraryId) throws DataAccessException {
		return WidgetLibrariesRepository.getWidgetLibrary(widgetLibraryId);
	}

	@Override
	public boolean addWidgetTypeToLibrary(WidgetLibraryType widgetLibraryType, int widgetLibraryId)
			throws DataAccessException {
		return WidgetLibrariesRepository.addWidgetTypeToLibrary(widgetLibraryType, widgetLibraryId);
	}

	@Override
	public void addWidgetLibrary(WidgetLibrary widgetLibrary) throws DataAccessException {
		WidgetLibrariesRepository.addWidgetLibrary(widgetLibrary);
	}

}
