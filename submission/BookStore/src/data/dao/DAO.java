package data.dao;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;


import javax.xml.transform.stream.StreamResult;

import data.beans.Bean;
import data.query.Query;

public interface DAO{
	public abstract Query newQueryRequest();	
	public abstract DataUpdate newUpdateRequest();	
}
