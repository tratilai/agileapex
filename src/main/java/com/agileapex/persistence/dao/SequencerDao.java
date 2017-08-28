package com.agileapex.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class SequencerDao {
    private static final Logger logger = LoggerFactory.getLogger(SequencerDao.class);

    public long create(String sequenceKey, long value) {
        logger.debug("About to create an sequence key: {} with value: {}", sequenceKey, value);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("sequence_key", sequenceKey);
        parameters.addValue("last_used_sequence_value", value);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("sequencer").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        logger.debug("Sequencer created. uniqueId: {}", uniqueId);
        return uniqueId.longValue();
    }

    public void update(String sequenceKey, long newValue) {
        logger.debug("About to update sequence. key: {} new value: {}", sequenceKey, newValue);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("sequence_key", sequenceKey);
        parameters.addValue("last_used_sequence_value", newValue);
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".sequencer SET last_used_sequence_value=:last_used_sequence_value ";
        sql += "WHERE sequence_key=:sequence_key";
        template.update(sql, parameters);
    }

    public List<Long> getNextSequence(String sequenceKey) {
        logger.debug("About to get possible list of sequences. key: {}", sequenceKey);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        Object[] parameters = new Object[] { sequenceKey };
        String sql = "SELECT last_used_sequence_value FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".sequencer WHERE sequence_key = ?";
        return template.getJdbcOperations().query(sql, parameters, new SequencerRowMapper());
    }

    private class SequencerRowMapper implements RowMapper<Long> {
        @Override
        public Long mapRow(ResultSet resultSet, int row) throws SQLException {
            return new Long(resultSet.getLong("last_used_sequence_value"));
        }
    }
}
